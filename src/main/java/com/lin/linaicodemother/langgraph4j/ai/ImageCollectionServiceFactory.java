package com.lin.linaicodemother.langgraph4j.ai;


import com.lin.linaicodemother.langgraph4j.tools.ImageSearchTool;
import com.lin.linaicodemother.langgraph4j.tools.LogoGeneratorTool;
import com.lin.linaicodemother.langgraph4j.tools.MermaidDiagramTool;
import com.lin.linaicodemother.langgraph4j.tools.UndrawIllustrationTool;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.service.AiServices;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 图片收集服务工厂
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class ImageCollectionServiceFactory {

    private final ChatModel chatModel;

    private final ImageSearchTool imageSearchTool;

    private final UndrawIllustrationTool undrawIllustrationTool;

    private final MermaidDiagramTool mermaidDiagramTool;

    private final LogoGeneratorTool logoGeneratorTool;

    /**
     * 创建图片收集 AI 服务
     */
    @Bean
    public ImageCollectionService createImageCollectionService() {
        return AiServices.builder(ImageCollectionService.class)
                .chatModel(chatModel)
                .tools(
                        imageSearchTool,
                        undrawIllustrationTool,
                        mermaidDiagramTool,
                        logoGeneratorTool
                )
                .build();
    }
}