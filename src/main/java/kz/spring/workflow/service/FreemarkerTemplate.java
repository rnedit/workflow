package kz.spring.workflow.service;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.IOException;
import java.util.Map;


@Service
public class FreemarkerTemplate {

    private final Configuration config;

    @Autowired
    public FreemarkerTemplate(Configuration config) {
        this.config = config;
    }

    @SneakyThrows
    public String mapTemplateFreemarker(String nameTemplate, Map<String, Object> model) {
        Template t = config.getTemplate(nameTemplate);
        String html = FreeMarkerTemplateUtils.processTemplateIntoString(t, model);
        return html;
    }


}