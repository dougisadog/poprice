package poprice.wechat.config;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.context.embedded.*;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.filter.CharacterEncodingFilter;

import poprice.wechat.Constants;
import poprice.wechat.web.filter.CachingHttpHeadersFilter;
import poprice.wechat.web.filter.StaticResourcesProductionFilter;
import poprice.wechat.web.filter.gzip.GZipServletFilter;
import poprice.wechat.web.mvc.filter.WxSecurityFilter;

import javax.inject.Inject;
import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
/**
 * Configuration of web application with Servlet 3.0 APIs.
 */
@Configuration
@AutoConfigureAfter(CacheConfiguration.class)
public class WebConfigurer implements ServletContextInitializer, EmbeddedServletContainerCustomizer {

    private final Logger log = LoggerFactory.getLogger(WebConfigurer.class);

    @Inject
    private Environment env;

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        log.info("Web application configuration, using profiles: {}", Arrays.toString(env.getActiveProfiles()));
        EnumSet<DispatcherType> disps = EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD, DispatcherType.ASYNC);

        //initCharacterEncodingFilter(servletContext, disps);

        if (env.acceptsProfiles(Constants.SPRING_PROFILE_PRODUCTION)) {
            initCachingHttpHeadersFilter(servletContext, disps);
            //initStaticResourcesProductionFilter(servletContext, disps);
            //initGzipFilter(servletContext, disps);
        }

        initWxSecurityFilter(servletContext, disps);

        //String contextPath = env.getProperty("server.contextPath");
        //servletContext.getSessionCookieConfig().setPath(contextPath);

        log.info("Web application fully configured");
    }


    /**
     * Set up Mime types.
     */
    @Override
    public void customize(ConfigurableEmbeddedServletContainer container) {
        MimeMappings mappings = new MimeMappings(MimeMappings.DEFAULT);
        // IE issue, see https://github.com/jhipster/generator-jhipster/pull/711
        mappings.add("html", "text/html;charset=utf-8");
        // CloudFoundry issue, see https://github.com/cloudfoundry/gorouter/issues/64
        mappings.add("json", "text/html;charset=utf-8");
        container.setMimeMappings(mappings);
    }

    /**
     * Initializes the static resources production Filter.
     */
    private void initCharacterEncodingFilter(ServletContext servletContext, EnumSet<DispatcherType> disps) {

        log.debug("Registering springmvc CharacterEncoding Filter");
        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setEncoding("UTF-8");
        characterEncodingFilter.setForceEncoding(true);

        FilterRegistration.Dynamic encodingFilter =
                servletContext.addFilter("characterEncodingFilter", characterEncodingFilter);

        encodingFilter.addMappingForUrlPatterns(disps, true, "/*");

        //characterEncodingFilter.setAsyncSupported(true);
    }

    /**
     * Initializes the GZip filter.
     */
    private void initGzipFilter(ServletContext servletContext, EnumSet<DispatcherType> disps) {
        log.debug("Registering GZip Filter");
        FilterRegistration.Dynamic compressingFilter = servletContext.addFilter("gzipFilter", new GZipServletFilter());
        Map<String, String> parameters = new HashMap<>();
        compressingFilter.setInitParameters(parameters);
        compressingFilter.addMappingForUrlPatterns(disps, true, "*.css");
        compressingFilter.addMappingForUrlPatterns(disps, true, "*.json");
        compressingFilter.addMappingForUrlPatterns(disps, true, "*.html");
        compressingFilter.addMappingForUrlPatterns(disps, true, "*.js");
        compressingFilter.addMappingForUrlPatterns(disps, true, "*.svg");
        compressingFilter.addMappingForUrlPatterns(disps, true, "*.ttf");
        compressingFilter.addMappingForUrlPatterns(disps, true, "/api/*");
        compressingFilter.addMappingForUrlPatterns(disps, true, "/metrics/*");
        compressingFilter.setAsyncSupported(true);
    }

    /**
     * Initializes the static resources production Filter.
     */
    private void initStaticResourcesProductionFilter(ServletContext servletContext,
                                                     EnumSet<DispatcherType> disps) {

        log.debug("Registering static resources production Filter");
        FilterRegistration.Dynamic staticResourcesProductionFilter =
                servletContext.addFilter("staticResourcesProductionFilter",
                        new StaticResourcesProductionFilter());

        staticResourcesProductionFilter.addMappingForUrlPatterns(disps, true, "/");
        staticResourcesProductionFilter.addMappingForUrlPatterns(disps, true, "/index.html");
        staticResourcesProductionFilter.addMappingForUrlPatterns(disps, true, "/*.txt");
        staticResourcesProductionFilter.addMappingForUrlPatterns(disps, true, "/introduction.html");
        staticResourcesProductionFilter.addMappingForUrlPatterns(disps, true, "/assets/*");
        staticResourcesProductionFilter.addMappingForUrlPatterns(disps, true, "/scripts/*");
        staticResourcesProductionFilter.setAsyncSupported(true);
    }

    /**
     * Initializes the cachig HTTP Headers Filter.
     */
    private void initCachingHttpHeadersFilter(ServletContext servletContext,
                                              EnumSet<DispatcherType> disps) {
        log.debug("Registering Caching HTTP Headers Filter");
        FilterRegistration.Dynamic cachingHttpHeadersFilter =
                servletContext.addFilter("cachingHttpHeadersFilter",
                        new CachingHttpHeadersFilter(env));

        cachingHttpHeadersFilter.addMappingForUrlPatterns(disps, true, "/assets/*");
        cachingHttpHeadersFilter.addMappingForUrlPatterns(disps, true, "/scripts/*");
        cachingHttpHeadersFilter.setAsyncSupported(true);
    }



    /**
     * http://stackoverflow.com/questions/20863489/characterencodingfilter-dont-work-together-with-spring-security-3-2-0
     * 几个方法：直接添加肯定不好用，因为security的csrf在前面，会导致无效。所以：1.要么在security里面添加，2.要么独立把这个filter调到最前面。
     * 这里采用的是方法2.
     * @return
     */
    @Bean
    public FilterRegistrationBean characterEncodingFilterRegistrationBean() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        registrationBean.setFilter(characterEncodingFilter);
        characterEncodingFilter.setEncoding("UTF-8");
        characterEncodingFilter.setForceEncoding(true);
        registrationBean.setOrder(Integer.MIN_VALUE);
        registrationBean.addUrlPatterns("/*");
        return registrationBean;
    }

    /*
    //this is from previous project: ytrmc, and is for mobile client authorization, not useful anymore
    @Bean
    public FilterRegistrationBean apiSecurityFilterRegistrationBean() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        ApiSecurityFilter apiSecurityFilter = new ApiSecurityFilter();
        registrationBean.setFilter(apiSecurityFilter);
        registrationBean.setOrder(Integer.MIN_VALUE + 1);
        registrationBean.addUrlPatterns("/api/*");
        return registrationBean;
    }
    //*/

    /**
     * Initializes the caching HTTP Headers Filter.
     */
    private void initWxSecurityFilter(ServletContext servletContext,
                                      EnumSet<DispatcherType> disps) {
        log.debug("Registering WxSecurity Filter");
        FilterRegistration.Dynamic filter =
                servletContext.addFilter("wxSecurityFilter", new WxSecurityFilter());
        filter.addMappingForUrlPatterns(disps, true, "/*");
        filter.setAsyncSupported(true);
    }

}
