package poprice.wechat.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.web.bind.support.ConfigurableWebBindingInitializer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import poprice.wechat.domain.util.JSR310DateConverters;

import javax.annotation.PostConstruct;

/**
 * 20161126用来测试解决日期转换问题，暂时保留，未来(比如你发现今天是2018年 xxxx)如果没有类似问题了，可以移除。
 *
 */
@Configuration
public class ConverterConfiguration {
//	public class ConverterConfiguration implements EnvironmentAware {

//    private final Logger log = LoggerFactory.getLogger(ConverterConfiguration.class);
//
//    private RelaxedPropertyResolver propertyResolver;
//
//    @Override
//    public void setEnvironment(Environment environment) {
//        this.propertyResolver = new RelaxedPropertyResolver(environment);
//    }
//
//    @Bean
//    public ConversionService getConversionService() {
//        ConversionServiceFactoryBean bean = new ConversionServiceFactoryBean();
//        Set<Converter> converters = Sets.newHashSet();
//        converters.add(JSR310DateConverters.StringToZonedDateTimeConverter.INSTANCE);
//        converters.add(JSR310DateConverters.StringToLocalDateTimeConverter.INSTANCE);
//        bean.setConverters(converters); //add converters
//        bean.afterPropertiesSet();
//        return bean.getObject();
//    }

	@Autowired
    private RequestMappingHandlerAdapter handlerAdapter;

    /**
     * 增加字符串转日期的功能
     */
    @PostConstruct
    public void initEditableValidation() {
        ConfigurableWebBindingInitializer initializer = (ConfigurableWebBindingInitializer) handlerAdapter
            .getWebBindingInitializer();
        if (initializer.getConversionService() != null) {
            GenericConversionService genericConversionService = (GenericConversionService) initializer.getConversionService();
            genericConversionService.addConverter(JSR310DateConverters.StringToZonedDateTimeConverter.INSTANCE);
            genericConversionService.addConverter(JSR310DateConverters.StringToLocalDateTimeConverter.INSTANCE);
        }

    }

}
