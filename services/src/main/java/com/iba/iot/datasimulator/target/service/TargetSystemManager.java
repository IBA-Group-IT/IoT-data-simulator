package com.iba.iot.datasimulator.target.service;

import com.iba.iot.datasimulator.target.model.TargetSystem;

import java.util.Collection;

/**
 *
 */
public interface TargetSystemManager {

    /**
     *
     * @param targetSystem
     * @return
     */
    TargetSystem create(TargetSystem targetSystem);

    /**
     *
     * @param targetSystemId
     * @param targetSystem
     * @return
     */
    TargetSystem update(String targetSystemId, TargetSystem targetSystem);

    /**
     *
     * @return
     */
    Collection<TargetSystem> get();

    /**
     *
     * @param targetSystemId
     * @return
     */
    TargetSystem get(String targetSystemId);

    /**
     *
     * @param targetSystemId
     */
    void remove(String targetSystemId);
}
