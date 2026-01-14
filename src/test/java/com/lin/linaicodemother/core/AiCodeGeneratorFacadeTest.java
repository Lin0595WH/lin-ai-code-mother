package com.lin.linaicodemother.core;

import com.lin.linaicodemother.model.enums.CodeGenTypeEnum;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;

import java.io.File;
import java.util.List;

@SpringBootTest
class AiCodeGeneratorFacadeTest {

    @Resource
    private AiCodeGeneratorFacade aiCodeGeneratorFacade;

    @Test
    void generateAndSaveCode() {
        File file = aiCodeGeneratorFacade.generateAndSaveCode("生成一个程序员的HTML页面，代码不超过20行", CodeGenTypeEnum.HTML, 1L);
        Assertions.assertNotNull(file);
    }

    @Test
    void generateAndSaveMultiFileCode() {
        File file = aiCodeGeneratorFacade.generateAndSaveCode("生成一个登录的HTML页面，代码不超过50行", CodeGenTypeEnum.MULTI_FILE, 1L);
        Assertions.assertNotNull(file);
    }


    @Test
    void generateAndSaveCodeStream() {
        Flux<String> codeStream = aiCodeGeneratorFacade.generateAndSaveCodeStream("生成一个HTML页面，代码不超过20行", CodeGenTypeEnum.HTML, 1L);
        // 阻塞等待所有数据收集完成
        List<String> result = codeStream.collectList().block();
        // 验证结果
        Assertions.assertNotNull(result);
        // 拼接字符串，得到完整内容
        String completeContent = String.join("", result);
        Assertions.assertNotNull(completeContent);
    }

    @Test
    void generateAndSaveMultiFileCodeStream() {
        Flux<String> result = aiCodeGeneratorFacade.generateAndSaveCodeStream("生成一个暗黑风的用户登录HTML页面，代码不超过50行", CodeGenTypeEnum.MULTI_FILE, 1L);
        // 订阅一个异步数据流，并指定数据的消费逻辑 —— 简单说就是 “等数据流产生结果后，把结果打印到控制台”
        result.subscribe(System.out::println);
    }
}