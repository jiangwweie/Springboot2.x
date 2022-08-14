package com.jiangwei.demo.fsm.statemachine.builder.ext;

/**
 * @author wuxi
 * @date 2021-02-09 11:15 上午
 */
public interface ChooseExternalTransitionBuilder<S, E, C> {
    /**
     * Build transition source state.
     * @param stateId id of state
     * @return from clause builder
     */
    ChooseFrom<S, E, C> from(S stateId);
}
