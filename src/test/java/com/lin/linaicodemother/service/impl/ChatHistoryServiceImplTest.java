package com.lin.linaicodemother.service.impl;

import com.lin.linaicodemother.model.dto.chathistory.ChatHistoryQueryRequest;
import com.lin.linaicodemother.model.enums.ChatHistoryMessageTypeEnum;
import com.lin.linaicodemother.service.ChatHistoryService;
import com.mybatisflex.core.query.QueryWrapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 对话历史服务测试类
 *
 * @author Lin
 */
@Slf4j
@SpringBootTest
class ChatHistoryServiceImplTest {

    @Resource
    private ChatHistoryService chatHistoryService;

    /**
     * 测试空请求参数
     */
    @Test
    void testGetQueryWrapperWithNullRequest() {
        QueryWrapper queryWrapper = chatHistoryService.getQueryWrapper(null);
        assertNotNull(queryWrapper);
        String sql = queryWrapper.toSQL();
        log.info("空请求SQL: {}", sql);
    }

    /**
     * 测试默认排序（无排序字段）
     */
    @Test
    void testGetQueryWrapperWithDefaultSort() {
        ChatHistoryQueryRequest request = new ChatHistoryQueryRequest();
        request.setAppId(1L);

        QueryWrapper queryWrapper = chatHistoryService.getQueryWrapper(request);
        assertNotNull(queryWrapper);
        String sql = queryWrapper.toSQL();
        log.info("默认排序SQL: {}", sql);
        assertTrue(sql.contains("ORDER BY"), "SQL应包含ORDER BY");
        assertTrue(sql.contains("create_time"), "默认排序应为create_time");
        assertTrue(sql.contains("DESC"), "默认排序应为降序");
    }

    /**
     * 测试按ID升序排序
     */
    @Test
    void testGetQueryWrapperWithIdAscendSort() {
        ChatHistoryQueryRequest request = new ChatHistoryQueryRequest();
        request.setSortField("id");
        request.setSortOrder("ascend");

        QueryWrapper queryWrapper = chatHistoryService.getQueryWrapper(request);
        assertNotNull(queryWrapper);
        String sql = queryWrapper.toSQL();
        log.info("ID升序排序SQL: {}", sql);
        assertTrue(sql.contains("ORDER BY"), "SQL应包含ORDER BY");
        assertTrue(sql.contains("id"), "排序字段应为id");
        assertTrue(sql.contains("ASC"), "排序应为升序");
    }

    /**
     * 测试按ID降序排序
     */
    @Test
    void testGetQueryWrapperWithIdDescendSort() {
        ChatHistoryQueryRequest request = new ChatHistoryQueryRequest();
        request.setSortField("id");
        request.setSortOrder("descend");

        QueryWrapper queryWrapper = chatHistoryService.getQueryWrapper(request);
        assertNotNull(queryWrapper);
        String sql = queryWrapper.toSQL();
        log.info("ID降序排序SQL: {}", sql);
        assertTrue(sql.contains("ORDER BY"), "SQL应包含ORDER BY");
        assertTrue(sql.contains("id"), "排序字段应为id");
        assertTrue(sql.contains("DESC"), "排序应为降序");
    }

    /**
     * 测试按message升序排序
     */
    @Test
    void testGetQueryWrapperWithMessageAscendSort() {
        ChatHistoryQueryRequest request = new ChatHistoryQueryRequest();
        request.setSortField("message");
        request.setSortOrder("ascend");

        QueryWrapper queryWrapper = chatHistoryService.getQueryWrapper(request);
        assertNotNull(queryWrapper);
        String sql = queryWrapper.toSQL();
        log.info("message升序排序SQL: {}", sql);
        assertTrue(sql.contains("ORDER BY"), "SQL应包含ORDER BY");
        assertTrue(sql.contains("message"), "排序字段应为message");
        assertTrue(sql.contains("ASC"), "排序应为升序");
    }

    /**
     * 测试按messageType降序排序
     */
    @Test
    void testGetQueryWrapperWithMessageTypeDescendSort() {
        ChatHistoryQueryRequest request = new ChatHistoryQueryRequest();
        request.setSortField("messageType");
        request.setSortOrder("descend");

        QueryWrapper queryWrapper = chatHistoryService.getQueryWrapper(request);
        assertNotNull(queryWrapper);
        String sql = queryWrapper.toSQL();
        log.info("messageType降序排序SQL: {}", sql);
        assertTrue(sql.contains("ORDER BY"), "SQL应包含ORDER BY");
        assertTrue(sql.contains("message_type"), "排序字段应为message_type");
        assertTrue(sql.contains("DESC"), "排序应为降序");
    }

    /**
     * 测试按appId升序排序
     */
    @Test
    void testGetQueryWrapperWithAppIdAscendSort() {
        ChatHistoryQueryRequest request = new ChatHistoryQueryRequest();
        request.setSortField("appId");
        request.setSortOrder("ascend");

        QueryWrapper queryWrapper = chatHistoryService.getQueryWrapper(request);
        assertNotNull(queryWrapper);
        String sql = queryWrapper.toSQL();
        log.info("appId升序排序SQL: {}", sql);
        assertTrue(sql.contains("ORDER BY"), "SQL应包含ORDER BY");
        assertTrue(sql.contains("app_id"), "排序字段应为app_id");
        assertTrue(sql.contains("ASC"), "排序应为升序");
    }

    /**
     * 测试按userId降序排序
     */
    @Test
    void testGetQueryWrapperWithUserIdDescendSort() {
        ChatHistoryQueryRequest request = new ChatHistoryQueryRequest();
        request.setSortField("userId");
        request.setSortOrder("descend");

        QueryWrapper queryWrapper = chatHistoryService.getQueryWrapper(request);
        assertNotNull(queryWrapper);
        String sql = queryWrapper.toSQL();
        log.info("userId降序排序SQL: {}", sql);
        assertTrue(sql.contains("ORDER BY"), "SQL应包含ORDER BY");
        assertTrue(sql.contains("user_id"), "排序字段应为user_id");
        assertTrue(sql.contains("DESC"), "排序应为降序");
    }

    /**
     * 测试按createTime升序排序
     */
    @Test
    void testGetQueryWrapperWithCreateTimeAscendSort() {
        ChatHistoryQueryRequest request = new ChatHistoryQueryRequest();
        request.setSortField("createTime");
        request.setSortOrder("ascend");

        QueryWrapper queryWrapper = chatHistoryService.getQueryWrapper(request);
        assertNotNull(queryWrapper);
        String sql = queryWrapper.toSQL();
        log.info("createTime升序排序SQL: {}", sql);
        assertTrue(sql.contains("ORDER BY"), "SQL应包含ORDER BY");
        assertTrue(sql.contains("create_time"), "排序字段应为create_time");
        assertTrue(sql.contains("ASC"), "排序应为升序");
    }

    /**
     * 测试按updateTime降序排序
     */
    @Test
    void testGetQueryWrapperWithUpdateTimeDescendSort() {
        ChatHistoryQueryRequest request = new ChatHistoryQueryRequest();
        request.setSortField("updateTime");
        request.setSortOrder("descend");

        QueryWrapper queryWrapper = chatHistoryService.getQueryWrapper(request);
        assertNotNull(queryWrapper);
        String sql = queryWrapper.toSQL();
        log.info("updateTime降序排序SQL: {}", sql);
        assertTrue(sql.contains("ORDER BY"), "SQL应包含ORDER BY");
        assertTrue(sql.contains("update_time"), "排序字段应为update_time");
        assertTrue(sql.contains("DESC"), "排序应为降序");
    }

    /**
     * 测试无效排序字段（应使用默认排序）
     */
    @Test
    void testGetQueryWrapperWithInvalidSortField() {
        ChatHistoryQueryRequest request = new ChatHistoryQueryRequest();
        request.setSortField("invalidField");
        request.setSortOrder("ascend");

        QueryWrapper queryWrapper = chatHistoryService.getQueryWrapper(request);
        assertNotNull(queryWrapper);
        String sql = queryWrapper.toSQL();
        log.info("无效字段排序SQL: {}", sql);
        assertTrue(sql.contains("ORDER BY"), "SQL应包含ORDER BY");
        assertTrue(sql.contains("create_time"), "无效字段应使用默认排序create_time");
        assertTrue(sql.contains("DESC"), "无效字段应使用默认降序排序");
    }

    /**
     * 测试空排序字段（应使用默认排序）
     */
    @Test
    void testGetQueryWrapperWithEmptySortField() {
        ChatHistoryQueryRequest request = new ChatHistoryQueryRequest();
        request.setSortField("");
        request.setSortOrder("ascend");

        QueryWrapper queryWrapper = chatHistoryService.getQueryWrapper(request);
        assertNotNull(queryWrapper);
        String sql = queryWrapper.toSQL();
        log.info("空排序字段SQL: {}", sql);
        assertTrue(sql.contains("ORDER BY"), "SQL应包含ORDER BY");
        assertTrue(sql.contains("create_time"), "空字段应使用默认排序create_time");
        assertTrue(sql.contains("DESC"), "空字段应使用默认降序排序");
    }

    /**
     * 测试综合查询条件
     */
    @Test
    void testGetQueryWrapperWithFullConditions() {
        ChatHistoryQueryRequest request = new ChatHistoryQueryRequest();
        //request.setId(1L);
        request.setMessage("测试消息");
        request.setMessageType(ChatHistoryMessageTypeEnum.USER.getValue());
        request.setAppId(1L);
        request.setUserId(1L);
        request.setLastCreateTime(LocalDateTime.now().minusDays(1));
        request.setSortField("createTime");
        request.setSortOrder("ascend");

        QueryWrapper queryWrapper = chatHistoryService.getQueryWrapper(request);
        assertNotNull(queryWrapper);
        String sql = queryWrapper.toSQL();
        log.info("综合查询SQL: {}", sql);
        assertTrue(sql.contains("WHERE"), "SQL应包含WHERE条件");
        assertTrue(sql.contains("create_time"), "SQL应包含create_time字段");
        assertTrue(sql.contains("ORDER BY"), "SQL应包含ORDER BY");
        assertTrue(sql.contains("ASC"), "排序应为升序");
    }

    /**
     * 测试游标查询（lastCreateTime）
     */
    @Test
    void testGetQueryWrapperWithCursor() {
        ChatHistoryQueryRequest request = new ChatHistoryQueryRequest();
        request.setAppId(1L);
        request.setLastCreateTime(LocalDateTime.now());

        QueryWrapper queryWrapper = chatHistoryService.getQueryWrapper(request);
        assertNotNull(queryWrapper);
        String sql = queryWrapper.toSQL();
        log.info("游标查询SQL: {}", sql);
        assertTrue(sql.contains("create_time"), "SQL应包含create_time字段");
    }
}
