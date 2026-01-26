package com.lin.linaicodemother.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.RandomUtil;
import com.github.houbb.sensitive.word.core.SensitiveWordHelper;
import com.lin.linaicodemother.constant.AppConstant;
import com.lin.linaicodemother.core.AiCodeGeneratorFacade;
import com.lin.linaicodemother.exception.BusinessException;
import com.lin.linaicodemother.exception.ErrorCode;
import com.lin.linaicodemother.exception.ThrowUtils;
import com.lin.linaicodemother.mapper.AppMapper;
import com.lin.linaicodemother.mapstruct.AppModuleMapper;
import com.lin.linaicodemother.model.dto.app.AppQueryRequest;
import com.lin.linaicodemother.model.entity.App;
import com.lin.linaicodemother.model.entity.User;
import com.lin.linaicodemother.model.enums.ChatHistoryMessageTypeEnum;
import com.lin.linaicodemother.model.enums.CodeGenTypeEnum;
import com.lin.linaicodemother.model.vo.AppVO;
import com.lin.linaicodemother.model.vo.UserVO;
import com.lin.linaicodemother.service.AppService;
import com.lin.linaicodemother.service.ChatHistoryService;
import com.lin.linaicodemother.service.UserService;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.io.File;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 应用 服务层实现。
 *
 * @author Lin
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class AppServiceImpl extends ServiceImpl<AppMapper, App> implements AppService {

    private final UserService userService;

    private final AppModuleMapper appModuleMapper;

    private final AiCodeGeneratorFacade aiCodeGeneratorFacade;

    private final ChatHistoryService chatHistoryService;

    /**
     * 通过对话生成应用代码
     *
     * @param appId     应用 ID
     * @param message   提示词
     * @param loginUser 登录用户
     * @return
     */
    @Override
    public Flux<String> chatToGenCode(Long appId, String message, User loginUser) {
        // 1. 参数校验
        ThrowUtils.throwIf(appId == null || appId <= 0, ErrorCode.PARAMS_ERROR, "应用 ID 错误");
        ThrowUtils.throwIf(CharSequenceUtil.isBlank(message), ErrorCode.PARAMS_ERROR, "提示词不能为空");
        List<String> sensitive = checkSensitive(message);
        ThrowUtils.throwIf(CollUtil.isNotEmpty(sensitive), ErrorCode.PARAMS_ERROR, "提示词中包含敏感词汇:" + sensitive);
        // 2. 查询应用信息
        App app = this.getById(appId);
        ThrowUtils.throwIf(app == null, ErrorCode.NOT_FOUND_ERROR, "应用不存在");
        // 3. 权限校验，仅本人可以和自己的应用对话
        if (!app.getUserId().equals(loginUser.getId())) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "无权限访问该应用");
        }
        // 4. 获取应用的代码生成类型
        String codeGenType = app.getCodeGenType();
        CodeGenTypeEnum codeGenTypeEnum = CodeGenTypeEnum.getEnumByValue(codeGenType);
        if (codeGenTypeEnum == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "应用代码生成类型错误");
        }
        // 5. 调用 AI 生成代码 先保存用户消息到数据库中
        chatHistoryService.addChatMessage(appId, message, ChatHistoryMessageTypeEnum.USER.getValue(), loginUser.getId());
        // 6. 调用 AI 生成代码（流式）
        Flux<String> contentFlux = aiCodeGeneratorFacade.generateAndSaveCodeStream(message, codeGenTypeEnum, appId);
        // 7. 收集 AI 响应的内容，并且在完成后保存记录到对话历史
        StringBuilder aiResponseBuilder = new StringBuilder();
        return contentFlux.map(chunk -> {
            // 实时收集 AI 响应的内容
            aiResponseBuilder.append(chunk);
            return chunk;
        }).doOnComplete(() -> {
            // 流式返回完成后，保存 AI 消息到对话历史中
            String aiResponse = aiResponseBuilder.toString();
            chatHistoryService.addChatMessage(appId, aiResponse, ChatHistoryMessageTypeEnum.AI.getValue(), loginUser.getId());
        }).doOnError(error -> {
            // 如果 AI 回复失败，也需要保存记录到数据库中
            String errorMessage = "AI 回复失败：" + error.getMessage();
            chatHistoryService.addChatMessage(appId, errorMessage, ChatHistoryMessageTypeEnum.AI.getValue(), loginUser.getId());
        });
    }

    /**
     * 部署应用
     *
     * @param appId     应用 ID
     * @param loginUser 登录用户
     * @return 可访问的部署url地址
     */
    @Override
    public String deployApp(Long appId, User loginUser) {
        // 1.校验
        ThrowUtils.throwIf(appId == null || appId <= 0, ErrorCode.PARAMS_ERROR, "应用 ID 错误");
        ThrowUtils.throwIf(loginUser == null, ErrorCode.NOT_LOGIN_ERROR, "用户未登录");
        // 2.校验应用信息
        App app = this.getById(appId);
        ThrowUtils.throwIf(app == null, ErrorCode.NOT_FOUND_ERROR, "应用不存在");
        // 3.权限校验:仅本人可以部署应用
        ThrowUtils.throwIf(!app.getUserId().equals(loginUser.getId()), ErrorCode.NO_AUTH_ERROR, "无操作权限");
        // 4.检查是否已有部署key
        // 没有就生成部署key(6位，字母+数字)
        String deployKey = app.getDeployKey();
        if (CharSequenceUtil.isBlank(deployKey)) {
            deployKey = RandomUtil.randomString(6);
        }
        // 5.获取代码生成类型，获取文件生成路径
        String codeGenType = app.getCodeGenType();
        String sourceDirName = codeGenType + "_" + appId;
        // 6.判断路径是否真实存在
        String sourceDirPath = AppConstant.CODE_OUTPUT_ROOT_DIR + File.separator + sourceDirName;
        if (!FileUtil.isDirectory(sourceDirPath)) {
            log.error("应用部署失败，应用代码路径{}不存在，请先生成应用:", sourceDirPath);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "应用部署失败，应用代码路径不存在，请先生成应用");
        }
        // 7.复制文件到部署路径
        String deployDirPath = AppConstant.CODE_DEPLOY_ROOT_DIR + File.separator + deployKey;
        try {
            FileUtil.copyContent(new File(sourceDirPath), new File(deployDirPath), true);
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "应用部署失败：" + e.getMessage());
        }
        // 8.更新该应用的deployKey,deployedTime,editTime
        App updateApp = new App();
        updateApp.setId(appId);
        updateApp.setDeployKey(deployKey);
        LocalDateTime now = LocalDateTime.now();
        updateApp.setDeployedTime(now);
        updateApp.setEditTime(now);
        boolean updateResult = this.updateById(updateApp);
        ThrowUtils.throwIf(!updateResult, ErrorCode.OPERATION_ERROR, "更新应用部署信息失败");
        // 9.生成可访问的url地址
        return CharSequenceUtil.format("{}/{}/", AppConstant.CODE_DEPLOY_HOST, deployKey);
    }

    /**
     * 判断传入内容是否含有敏感词
     *
     * @param content 文本内容
     * @return List<String> 敏感词列表
     */
    @Override
    public List<String> checkSensitive(String content) {
        if (CharSequenceUtil.isBlank(content)) {
            return List.of();
        }
        return SensitiveWordHelper.findAll(content);
    }

    /**
     * 获取应用封装类
     *
     * @param app
     * @return
     */
    @Override
    public AppVO getAppVO(App app) {
        if (app == null) {
            return null;
        }
        // 使用MapStruct转换基础字段
        AppVO appVO = appModuleMapper.appToAppVO(app);

        // 设置用户信息
        if (app.getUserId() != null) {
            User user = userService.getById(app.getUserId());
            if (user != null) {
                appVO.setUser(userService.getUserVO(user));
            }
        }
        return appVO;
    }

    /**
     * 获取应用封装类列表
     *
     * @param appList
     * @return
     */
    @Override
    public List<AppVO> getAppVOList(List<App> appList) {
        if (appList == null || appList.isEmpty()) {
            return List.of();
        }
        // 批量转换App基础字段
        List<AppVO> appVOList = appModuleMapper.appListToAppVOList(appList);
        // 批量获取用户ID
        List<Long> userIds = appList.stream()
                .map(App::getUserId)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
        if (!userIds.isEmpty()) {
            // 批量查询用户信息
            List<User> users = userService.listByIds(userIds);
            Map<Long, UserVO> userVOMap = users.stream()
                    .collect(Collectors.toMap(User::getId, userService::getUserVO));
            // 设置用户信息到AppVO
            for (AppVO appVO : appVOList) {
                if (appVO.getUserId() != null) {
                    appVO.setUser(userVOMap.get(appVO.getUserId()));
                }
            }
        }
        return appVOList;
    }

    @Override
    public QueryWrapper getQueryWrapper(AppQueryRequest appQueryRequest) {
        if (appQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        Long id = appQueryRequest.getId();
        String appName = appQueryRequest.getAppName();
        String cover = appQueryRequest.getCover();
        String initPrompt = appQueryRequest.getInitPrompt();
        String codeGenType = appQueryRequest.getCodeGenType();
        String deployKey = appQueryRequest.getDeployKey();
        Integer priority = appQueryRequest.getPriority();
        Long userId = appQueryRequest.getUserId();
        String sortField = appQueryRequest.getSortField();
        String sortOrder = appQueryRequest.getSortOrder();
        return QueryWrapper.create()
                .eq("id", id)
                .like("app_name", appName)
                .like("cover", cover)
                .like("init_prompt", initPrompt)
                .eq("code_gen_type", codeGenType)
                .eq("deploy_key", deployKey)
                .eq("priority", priority)
                .eq("user_id", userId)
                .orderBy(sortField, "ascend".equals(sortOrder));
    }

    /**
     * 删除应用时，关联删除对话历史
     *
     * @param id
     * @return
     */
    @Override
    public boolean removeById(Serializable id) {
        if (id == null) {
            return false;
        }
        long appId = Long.parseLong(id.toString());
        if (appId <= 0) {
            return false;
        }
        // 先删除关联的对话历史
        try {
            chatHistoryService.deleteByAppId(appId);
        } catch (Exception e) {
            log.error("删除应用关联的对话历史失败：{}", e.getMessage());
        }
        // 删除应用
        return super.removeById(id);
    }
}
