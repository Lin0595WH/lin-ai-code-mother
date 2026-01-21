package com.lin.linaicodemother.service.impl;

import com.lin.linaicodemother.mapper.ChatHistoryMapper;
import com.lin.linaicodemother.model.entity.ChatHistory;
import com.lin.linaicodemother.service.ChatHistoryService;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 对话历史 服务层实现。
 *
 * @author Lin
 */
@Service
public class ChatHistoryServiceImpl extends ServiceImpl<ChatHistoryMapper, ChatHistory>  implements ChatHistoryService{

}
