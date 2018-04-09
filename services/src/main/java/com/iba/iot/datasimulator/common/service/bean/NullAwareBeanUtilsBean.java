package com.iba.iot.datasimulator.common.service.bean;

import org.apache.commons.beanutils.BeanUtilsBean;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 */
public class NullAwareBeanUtilsBean extends BeanUtilsBean {

    /** **/
    private Set<String> propsToIgnore = new HashSet<>();

    /**
     *
     */
    public NullAwareBeanUtilsBean() {}

    /**
     *
     * @param propsToIgnore
     */
    public NullAwareBeanUtilsBean(Set<String> propsToIgnore) {
        this.propsToIgnore = propsToIgnore;
    }

    @Override
    public void copyProperty(Object dest, String name, Object value) throws IllegalAccessException, InvocationTargetException {

        boolean isString = value instanceof String;
        if(value != null && !propsToIgnore.contains(name) && (!isString || isNotEmptyString(value))) {
            super.copyProperty(dest, name, value);
        }
    }

    /**
     *
     * @param value
     * @return
     */
    boolean isNotEmptyString(Object value) {

        String stringValue = (String) value;
        return stringValue.length() > 0;
    }
}
