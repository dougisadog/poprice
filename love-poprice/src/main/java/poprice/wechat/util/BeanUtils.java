package poprice.wechat.util;


import java.lang.reflect.Field;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import org.springframework.beans.BeansException;

import poprice.wechat.domain.AbstractAuditingEntity;
import poprice.wechat.service.ServiceException;

/**
 * http://stackoverflow.com/questions/5079458/copy-specific-fields-by-using-beanutils-copyproperties
 */
public class BeanUtils {
    public static void copyProperties(Object source, Object target) {
        org.springframework.beans.BeanUtils.copyProperties(source, target);
    }

    public static void copyPropertiesWithExclude(Object source, Object target, String... exludedProperties) {
        org.springframework.beans.BeanUtils.copyProperties(source, target, exludedProperties);
    }

    public static void copyPropertiesWithInclude(Object source, Object target, String... includedProperties) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        final BeanWrapper trg = new BeanWrapperImpl(target);

        for (final String propertyName : includedProperties) {
            trg.setPropertyValue(propertyName, src.getPropertyValue(propertyName));
        }
    }

    /**
     * 给web用的，默认排除id和AbstractAuditingEntity的内容
     * @param source
     * @param target
     */
    public static void copyPropertiesByDefault(Object source, Object target) {
        if (target instanceof AbstractAuditingEntity) {
            copyPropertiesWithExclude(source, target, "id", "createdBy", "createdDate", "lastModifiedBy", "lastModifiedDate");
        } else {
            copyPropertiesWithExclude(source, target, "id");
        }
    }

    public static Object getProperty(Object bean, String name) {
        final BeanWrapper wrapper = new BeanWrapperImpl(bean);
        try {
            return wrapper.getPropertyValue(name);
        } catch (BeansException e) {
            return null;
        }

    }

    /**
     * 实际上expression里面有getJavaType(), 该方法然并卵了！
     * @param clazz
     * @param prop
     * @return
     */
    public static Class getPropertyType(Class clazz, String prop) {
        Field field = null;
        try {
            field = clazz.getDeclaredField(prop);
        } catch (NoSuchFieldException e) {
            throw new ServiceException(e);
        }
        return field.getType();
    }


}
