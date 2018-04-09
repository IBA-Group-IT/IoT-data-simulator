/*
 * *****************************************************************
 *   Licensed Materials - Property of IBM                           * 
 *   Copyright IBM Corp. 2016 All rights reserved.                  * 
 *                                                                  * 
 *   US Government Users Restricted Rights - Use, duplication or    * 
 *   disclosure restricted by GSA ADP Schedule Contract with        * 
 *   IBM Corp.                                                      * 
 *                                                                  *
 * ****************************************************************
 */
package com.iba.iot.datasimulator.common.model.function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Predicate;

/**
 *
 * @param <T>
 */
public interface ThrowingPredicate<T> extends Predicate<T> {

    /** **/
    Logger logger = LoggerFactory.getLogger(ThrowingPredicate.class);

    @Override
    default boolean test(final T elem) {
        try {
            return testThrows(elem);
        } catch (final Exception exception) {
            logger.error(exception.getMessage(), exception);
            throw new RuntimeException(exception);
        }
    }

    /**
     *
     * @param elem
     * @throws Exception
     */
    boolean testThrows(T elem) throws Exception;

}
