package com.lin.linaicodemother.ai;

import com.lin.linaicodemother.ai.model.RoutingResult;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
class AiCodeGenTypeRoutingServiceTest {

    @Resource
    private AiCodeGenTypeRoutingService aiCodeGenTypeRoutingService;

    @Test
    void routeCodeGenType() {
        String userPrompt = "做一个简单的个人介绍页面";
        RoutingResult result = aiCodeGenTypeRoutingService.routing(userPrompt);
        log.info("用户需求: {} ——》 决定生成类型：{}；应用名称：{}", userPrompt, result.getCodeGenTypeEnum().getValue(), result.getAppName());

        userPrompt = "做一个公司官网，需要首页、关于我们、联系我们三个页面";
        result = aiCodeGenTypeRoutingService.routing(userPrompt);
        log.info("用户需求: {} ——》 决定生成类型：{}；应用名称：{}", userPrompt, result.getCodeGenTypeEnum().getValue(), result.getAppName());

        userPrompt = "做一个电商管理系统，包含用户管理、商品管理、订单管理，需要路由和状态管理";
        result = aiCodeGenTypeRoutingService.routing(userPrompt);
        log.info("用户需求: {} ——》 决定生成类型：{}；应用名称：{}", userPrompt, result.getCodeGenTypeEnum().getValue(), result.getAppName());

    }


}