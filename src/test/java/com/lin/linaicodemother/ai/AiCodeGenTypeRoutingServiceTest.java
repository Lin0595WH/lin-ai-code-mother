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

    @Test
    void routeCodeGenType2() {
        String userPrompt = "制作一个精美的作品展示网站，适合设计师、摄影师、艺术家等创作者。包含作品画廊、项目详情页、个人简历、联系方式等模块。采用瀑布流或网格布局展示作品，支持图片放大预览和作品分类筛选。";
        RoutingResult result = aiCodeGenTypeRoutingService.routing(userPrompt);
        log.info("用户需求: {} ——》 决定生成类型：{}；应用名称：{}", userPrompt, result.getCodeGenTypeEnum().getValue(), result.getAppName());
    }

}