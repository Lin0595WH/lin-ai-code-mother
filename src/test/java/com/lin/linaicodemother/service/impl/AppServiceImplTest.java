package com.lin.linaicodemother.service.impl;

import com.lin.linaicodemother.model.entity.App;
import com.lin.linaicodemother.model.vo.AppVO;
import com.lin.linaicodemother.service.AppService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;

import java.util.List;

@Slf4j
@SpringBootTest
@RequiredArgsConstructor
// 核心配置：让 Spring 自动解析构造方法参数并注入 Bean
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class AppServiceImplTest {

    public  final AppService appService;

    @Test
    void getAppVO() {
        App app = appService.getById(1L);
        AppVO appVO = appService.getAppVO(app);
        log.info("app: {}", app);
        log.info("appVO: {}", appVO);
    }

    @Test
    void getAppVOList() {
        List<App> appList = appService.list();
        List<AppVO> appVOList = appService.getAppVOList(appList);
        log.info("appList: {}", appList);
        log.info("appVOList: {}", appVOList);
    }
}