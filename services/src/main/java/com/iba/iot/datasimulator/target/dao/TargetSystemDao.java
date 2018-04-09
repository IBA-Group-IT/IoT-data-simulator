package com.iba.iot.datasimulator.target.dao;

import com.iba.iot.datasimulator.session.model.Session;
import com.iba.iot.datasimulator.target.model.TargetSystem;

import java.util.Collection;
import java.util.List;

/**
 *
 */
public interface TargetSystemDao {

    /**
     *
     * @param target
     */
    TargetSystem save(TargetSystem target);

    /**
     *
     * @return
     */
    Collection<TargetSystem> get();

    /**
     *
     * @param targetId
     * @return
     */
    TargetSystem get(String targetId);

    /**
     *
     * @param target
     * @return
     */
    TargetSystem update(TargetSystem target);

    /**
     *
     * @param targetId
     */
    void remove(String targetId);

    /**
     *
     * @param targetId
     * @return
     */
    List<Session> getLinkedSessions(String targetId);
}
