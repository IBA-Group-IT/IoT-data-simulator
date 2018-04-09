package com.iba.iot.datasimulator.common.util;

import com.iba.iot.datasimulator.common.service.bean.NullAwareBeanUtilsBean;
import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashSet;

public class PojoUtil {

    /** **/
    private static final Logger logger = LoggerFactory.getLogger(PojoUtil.class);

    /** **/
    private static NullAwareBeanUtilsBean beanUtilsBean = new NullAwareBeanUtilsBean();

    /** **/
    private static NullAwareBeanUtilsBean targetSystemsBeanUtilsBean =
            new NullAwareBeanUtilsBean(new HashSet<>(Arrays.asList("security", "messageSerializer", "headers")));

    /**
     *
     * @param source
     * @param target
     */
    public static void copyProperties(Object source, Object target) {
        copyProperties(source, target, beanUtilsBean);
    }

    /**
     *
     */
    public static void copyTargetSystemProperties(Object source, Object target) {
        copyProperties(source, target, targetSystemsBeanUtilsBean);
    }

    /**
     *
     * @param source
     * @param target
     */
    public static void copyProperties(Object source, Object target, NullAwareBeanUtilsBean beanUtilsBean) {

        try {

            beanUtilsBean.copyProperties(target, source);

        } catch (Exception exception) {
            logger.error(">>> An error occurred during beans properties copy operation: {}", ExceptionUtil.getErrorMessage(exception));
            throw new RuntimeException("Beans property copy operation error.", exception);
        }
    }

    /**
     *
     * @param propertyName
     * @param source
     * @return
     */
    public static <T> T getProperty(String propertyName, Object source) {

        try {
            return (T) PropertyUtils.getProperty(source, propertyName);
        } catch (NoSuchMethodException noSuchMethodException) {
            return null;
        } catch (Exception exception) {

            logger.error(">>> Cannot get property {} from bean {} due to error {}",
                    propertyName, source, ExceptionUtil.getErrorMessage(exception));
            throw new RuntimeException("Placeholder property reading error.", exception);
        }
    }

    /**
     *
     * @param propertyName
     * @param propertyValue
     * @param target
     * @param <T>
     */
    public static <T> void setProperty(String propertyName, T propertyValue, Object target) {

        try {
            PropertyUtils.setProperty(target, propertyName, propertyValue);
        } catch (Exception exception) {

            logger.error(">>> Cannot set property {} with value {} to bean {} due to error: {}", propertyName,
                    propertyValue, target, ExceptionUtil.getErrorMessage(exception));
            throw new RuntimeException("Bean property setting error", exception);
        }
    }
}


