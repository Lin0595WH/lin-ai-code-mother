package com.lin.linaicodemother.ai;

import com.lin.linaicodemother.ai.model.RoutingResult;
import dev.langchain4j.service.SystemMessage;

/**
 * AI代码生成类型智能路由服务
 * 使用结构化输出直接返回枚举类型
 *
 * @author yupi
 */
public interface AiCodeGenTypeRoutingService {

    /**
     * 根据用户需求智能生成应用描述
     *
     * @param userPrompt 用户输入的需求描述
     * @return 描述
     */
    @SystemMessage(fromResource = "prompt/codegen-routing-system-prompt.txt")
    RoutingResult routing(String userPrompt);
}
