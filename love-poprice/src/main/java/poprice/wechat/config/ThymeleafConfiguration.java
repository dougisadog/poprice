package poprice.wechat.config;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.web.servlet.ViewResolver;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.extras.springsecurity4.dialect.SpringSecurityDialect;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ITemplateResolver;


/**
 * http://www.thymeleaf.org/doc/articles/thymeleaf3migration.html
 * https://gist.github.com/seanhinkley/6eab2130ceea857c160b
 * https://gist.github.com/seanhinkley/6eab2130ceea857c160b
 *
 * https://github.com/spring-projects/spring-boot/issues/6500
 * http://www.coderli.com/thymeleaf-3-migration-guide-by-onecoder/
 *
 * https://github.com/thymeleaf/thymeleaf-extras-java8time
 */
@Configuration
public class ThymeleafConfiguration {

    private final Logger log = LoggerFactory.getLogger(ThymeleafConfiguration.class);

    @Inject
    private ApplicationContext applicationContext;

    @Bean
    public ViewResolver viewResolver() {
        final ThymeleafViewResolver resolver = new ThymeleafViewResolver();
        resolver.setTemplateEngine(templateEngine());
        resolver.setCharacterEncoding("UTF-8");
        return resolver;
    }

    @Bean
    public TemplateEngine templateEngine() {
        final SpringTemplateEngine engine = new SpringTemplateEngine();
        engine.setEnableSpringELCompiler(true);
        engine.setTemplateResolver(templateResolver());
        return engine;
    }

    private ITemplateResolver templateResolver() {
        final SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
        resolver.setApplicationContext(applicationContext);
        resolver.setPrefix("/WEB-INF/templates/");
        resolver.setTemplateMode(TemplateMode.HTML);
        resolver.setOrder(0);
        return resolver;
    }

	/*
	 * @Bean
	 *
	 * @Description("Thymeleaf template resolver serving HTML 5 emails") public ClassLoaderTemplateResolver
	 * emailTemplateResolver() { ClassLoaderTemplateResolver emailTemplateResolver = new ClassLoaderTemplateResolver();
	 * emailTemplateResolver.setPrefix("mails/"); emailTemplateResolver.setSuffix(".html");
	 * emailTemplateResolver.setTemplateMode("HTML5"); emailTemplateResolver.setCharacterEncoding(CharEncoding.UTF_8);
	 * emailTemplateResolver.setOrder(2); return emailTemplateResolver; }
	 *
	 * @Bean
	 *
	 * @Description("Spring mail message resolver") public MessageSource emailMessageSource() { log.info(
	 * "loading non-reloadable mail messages resources"); ReloadableResourceBundleMessageSource messageSource = new
	 * ReloadableResourceBundleMessageSource(); messageSource.setBasename("classpath:/mails/messages/messages");
	 * messageSource.setDefaultEncoding(CharEncoding.UTF_8); return messageSource; } //
	 */
    /**
     * http://stackoverflow.com/questions/23531580/how-do-i-add-a-thymeleaf-dialect-to-spring-boot#
     * comment46937329_23531581 https://github.com/spring-projects/spring-boot/issues/661
     * https://github.com/thymeleaf/thymeleaf-extras-springsecurity LayoutDialect DataAttributeDialect
     * SpringSecurityDialect ConditionalCommentsDialect
     *
     * @return
     */
    @Bean
    @Description("必须手动加入才可以")
    public SpringSecurityDialect springSecurityDialect() {

        return new SpringSecurityDialect();
    }
}
