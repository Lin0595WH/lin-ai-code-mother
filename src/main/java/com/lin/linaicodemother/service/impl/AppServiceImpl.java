package com.lin.linaicodemother.service.impl;

import com.lin.linaicodemother.exception.BusinessException;
import com.lin.linaicodemother.exception.ErrorCode;
import com.lin.linaicodemother.mapper.AppMapper;
import com.lin.linaicodemother.mapstruct.AppModuleMapper;
import com.lin.linaicodemother.model.dto.app.AppQueryRequest;
import com.lin.linaicodemother.model.entity.App;
import com.lin.linaicodemother.model.entity.User;
import com.lin.linaicodemother.model.vo.AppVO;
import com.lin.linaicodemother.model.vo.UserVO;
import com.lin.linaicodemother.service.AppService;
import com.lin.linaicodemother.service.UserService;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
