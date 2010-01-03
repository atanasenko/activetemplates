package com.google.code.activetemplates.view;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.AbstractCachingViewResolver;

import com.google.code.activetemplates.Template;
import com.google.code.activetemplates.TemplateBuilder;
import com.google.code.activetemplates.TemplateCompiler;

public class TemplateViewResolver extends AbstractCachingViewResolver {

    private TemplateCompiler templateCompiler;
    private TemplateBuilder templateBuilder;
    
    private volatile Map<String, Template> templates;
    
    public TemplateCompiler getTemplateCompiler() {
        return templateCompiler;
    }
    
    @Autowired
    public void setTemplateCompiler(TemplateCompiler templateCompiler) {
        this.templateCompiler = templateCompiler;
    }
    
    public TemplateBuilder getTemplateBuilder() {
        return templateBuilder;
    }
    
    @Autowired
    public void setTemplateBuilder(TemplateBuilder templateBuilder) {
        this.templateBuilder = templateBuilder;
    }

    @Override
    protected View loadView(String viewName, Locale locale) throws Exception {
        ensureTemplatesBuilt();
        Template t = templates.get(viewName);
        if(t == null) return null;
        return new TemplateView(t, templateCompiler);
    }

    private void ensureTemplatesBuilt() {
        if(templates == null) {
            synchronized(TemplateViewResolver.class) {
                if(templates == null) {
                    templates = new HashMap<String, Template>();
                    for(Template t: templateBuilder.build()) {
                        templates.put(t.getName(), t);
                    }
                }
            }
        }
    }

}
