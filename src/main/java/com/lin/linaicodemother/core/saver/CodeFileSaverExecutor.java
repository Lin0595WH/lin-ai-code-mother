package com.lin.linaicodemother.core.saver;


import com.lin.linaicodemother.ai.model.HtmlCodeResult;
import com.lin.linaicodemother.ai.model.MultiFileCodeResult;
import com.lin.linaicodemother.model.enums.CodeGenTypeEnum;

import java.io.File;

/**
 * @Author Lin
 * @Date 2026/1/12 21:05
 * @Descriptions 代码文件保存执行器 根据代码生成类型执行相应的保存逻辑
 * 代替原本的CodeFileSaver类
 */
public class CodeFileSaverExecutor {

    private static final HtmlCodeFileSaverTemplate htmlCodeFileSaver = new HtmlCodeFileSaverTemplate();

    private static final MultiFileCodeFileSaverTemplate multiFileCodeFileSaver = new MultiFileCodeFileSaverTemplate();

    /**
     * 执行代码保存
     *
     * @param codeResult  代码结果对象
     * @param codeGenType 代码生成类型
     * @return 保存的目录
     */
    public static File executeSaver(Object codeResult, CodeGenTypeEnum codeGenType) {
        return switch (codeGenType) {
            case HTML -> htmlCodeFileSaver.saveCode((HtmlCodeResult) codeResult);
            case MULTI_FILE -> multiFileCodeFileSaver.saveCode((MultiFileCodeResult) codeResult);
        };
    }
}
