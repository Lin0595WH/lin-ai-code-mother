package com.lin.linaicodemother.config;


import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.model.openai.OpenAiChatRequestParameters;
import dev.langchain4j.model.openai.OpenAiStreamingChatModel;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * @Author Lin
 * @Date 2026/2/3 20:55
 * @Descriptions 推理流式模型配置类
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "langchain4j.open-ai.chat-model")
public class ReasoningStreamingChatModelConfig {

    private String baseUrl;

    private String apiKey;

    private String modelName;

    private int maxTokens;

    /**
     * 推理流式模型（用于 Vue 项目生成，带工具调用）
     */
    @Bean
    public StreamingChatModel reasoningStreamingChatModel() {
        OpenAiChatRequestParameters parameters = OpenAiChatRequestParameters.builder()
                .modelName(modelName)
                .maxCompletionTokens(maxTokens)
                .customParameters(Map.of("enable_thinking", "true"))
                .build();
        return OpenAiStreamingChatModel.builder()
                .apiKey(apiKey)
                .baseUrl(baseUrl)
                .modelName(modelName)
                .maxTokens(maxTokens)
                .logRequests(true)
                .logResponses(true)
                .returnThinking(true)
                .defaultRequestParameters(parameters)
                .build();
    }
}
