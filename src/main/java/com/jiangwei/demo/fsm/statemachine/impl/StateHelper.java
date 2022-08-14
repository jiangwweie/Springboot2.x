package com.jiangwei.demo.fsm.statemachine.impl;

import com.jiangwei.demo.fsm.statemachine.State;

import java.util.Map;

/**
 * StateHelper
 *
 * @author Frank Zhang
 * @date 2020-02-08 4:23 PM
 */
public class StateHelper {
    public static <S, E, C> State<S, E, C> getState(Map<S, State<S, E, C>> stateMap, S stateId){
        State<S, E, C> state = stateMap.get(stateId);
        if (state == null) {
            //初始化state对象
            state = new StateImpl<>(stateId);
            stateMap.put(stateId, state);
        }
        return state;
    }
}
