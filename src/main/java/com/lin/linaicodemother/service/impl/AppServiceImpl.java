package com.lin.linaicodemother.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.CharSequenceUtil;
import com.github.houbb.sensitive.word.core.SensitiveWordHelper;
import com.lin.linaicodemother.core.AiCodeGeneratorFacade;
import com.lin.linaicodemother.exception.BusinessException;
import com.lin.linaicodemother.exception.ErrorCode;
import com.lin.linaicodemother.exception.ThrowUtils;
import com.lin.linaicodemother.mapper.AppMapper;
import com.lin.linaicodemother.mapstruct.AppModuleMapper;
import com.lin.linaicodemother.model.dto.app.AppQueryRequest;
import com.lin.linaicodemother.model.entity.App;
import com.lin.linaicodemother.model.entity.User;
import com.lin.linaicodemother.model.enums.CodeGenTypeEnum;
import com.lin.linaicodemother.model.vo.AppVO;
import com.lin.linaicodemother.model.vo.UserVO;
import com.lin.linaicodemother.service.AppService;
import com.lin.linaicodemother.service.UserService;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 应用 服务层实现。
 *
 * @author Lin
 */
@Service
@RequiredArgsConstructor
public class AppServiceImpl extends ServiceImpl<AppMapper, App> implements AppService {

    private final UserService userService;

    private final AppModuleMapper appModuleMapper;

    private final AiCodeGeneratorFacade aiCodeGeneratorFacade;

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
        // 5. 调用 AI 生成代码
        return aiCodeGeneratorFacade.generateAndSaveCodeStream(message, codeGenTypeEnum, appId);
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
}
