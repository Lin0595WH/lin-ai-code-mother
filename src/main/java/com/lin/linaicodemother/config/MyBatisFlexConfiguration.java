package com.lin.linaicodemother.config;


import com.mybatisflex.core.audit.AuditManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

/**
 * @Author Lin
 * @Date 2026/1/4 21:54
 * @Descriptions MyBatisFlex配置类：开启日志打印
 */
@Slf4j
@Configuration
public class MyBatisFlexConfiguration {

    public MyBatisFlexConfiguration() {
        //开启审计功能
        AuditManager.setAuditEnable(true);

        //设置 SQL 审计收集器
        AuditManager.setMessageCollector(auditMessage ->
                log.info("{},{}ms", auditMessage.getFullSql()
                        , auditMessage.getElapsedTime())
        );
    }
}
