package com.google.code.activetemplates.view;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.View;

import com.google.code.activetemplates.TemplateModel;
import com.google.code.activetemplates.Template;
import com.google.code.activetemplates.TemplateCompiler;

public class TemplateView implements View {

    private Template template;
    private TemplateCompiler tc;
    
    public TemplateView(Template template, TemplateCompiler tc) {
        this.template = template;
        this.tc = tc;
    }
    
    @Override
    public String getContentType() {
        return null;
    }

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        tc.compile(template, new TemplateModel(model), response.getOutputStream());
    }

}
