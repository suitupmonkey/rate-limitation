package com.example.config;

import org.springframework.context.annotation.Configuration;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

@Configuration
public class TemplateEngineConfig implements ServletContextListener {

    //servlet context, for templcate engine.
    private ServletContext servletContext;

    //template engine
    private TemplateEngine templateEngine;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        servletContext = sce.getServletContext();
        config();
    }

    //initial work for template engine, so we could add prefix and suffix
    //for html template
    public void config(){
        ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(servletContext);

        // HTML is the default mode, but we set it anyway for better understanding of code
        templateResolver.setTemplateMode(TemplateMode.HTML);
        // This will convert "home" to "/WEB-INF/templates/home.html"
        templateResolver.setPrefix("classpath:/templates/");
        templateResolver.setSuffix(".html");
        // Template cache TTL=1h. If not set, entries would be cached until expelled
        templateResolver.setCacheTTLMs(Long.valueOf(3600000L));

        // Cache is set to true by default. Set to false if you want templates to
        // be automatically updated when modified.
        templateResolver.setCacheable(true);

        this.templateEngine = new TemplateEngine();
        this.templateEngine.setTemplateResolver(templateResolver);
    }

}
