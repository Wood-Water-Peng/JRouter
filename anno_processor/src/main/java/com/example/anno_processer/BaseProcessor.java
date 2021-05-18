package com.example.anno_processer;

import org.apache.commons.collections4.MapUtils;

import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

import static com.example.anno_processer.Constants.KEY_MODULE_NAME;

/**
 * @Author jacky.peng
 * @Date 2021/5/18 2:23 PM
 * @Version 1.0
 */
public abstract class BaseProcessor extends AbstractProcessor {
    Elements elementUtils;
    /**
     * 每个组件都需要配置一下 module 的名字 ⬇⬇⬇⬇⬇⬇⬇⬇⬇⬇
     * <p>
     * javaCompileOptions {
     * annotationProcessorOptions {
     * arguments = [BROUTER_MODULE_NAME: project.getName()]
     * }
     * }
     */
    protected String moduleName;
    Filer mFiler;
    Logger logger;
    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        //获取各module在gradle中配置的参数
        Map<String, String> options = processingEnv.getOptions();
        if (MapUtils.isNotEmpty(options)) {
            moduleName = options.get(KEY_MODULE_NAME);
        }
        elementUtils = processingEnvironment.getElementUtils();
        mFiler=processingEnvironment.getFiler();
        logger = new Logger(processingEnv.getMessager());
    }
}
