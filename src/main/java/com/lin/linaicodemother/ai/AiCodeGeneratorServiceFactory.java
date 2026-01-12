package com.lin.linaicodemother.ai;


import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.service.AiServices;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author Lin
 * @Date 2026/1/7 21:18
 * @Descriptions AI 服务创建工厂
 */
@Configuration
@RequiredArgsConstructor
public class AiCodeGeneratorServiceFactory {

    private final ChatModel chatModel;

    private final StreamingChatModel streamingChatModel;

    /**
     * 创建 AI 代码生成器服务
     */
    @Bean
    public AiCodeGeneratorService aiCodeGeneratorService() {
        return AiServices.builder(AiCodeGeneratorService.class)
                .chatModel(chatModel)
                .streamingChatModel(streamingChatModel)
                .build();
    }
}
