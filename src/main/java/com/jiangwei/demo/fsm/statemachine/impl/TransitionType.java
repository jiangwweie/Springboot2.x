package com.jiangwei.demo.fsm.statemachine.impl;

/**
 * TransitionType
 *
 * @author Frank Zhang
 * @date 2020-02-07 10:23 PM
 */
public enum TransitionType {
    /**
     * Implies that the Transition, if triggered, occurs without exiting or entering the source State
     * (i.e., it does not cause a state change). This means that the entry or exit condition of the source
     * State will not be invoked. An internal Transition can be taken even if the SateMachine is in one or
     * more Regions nested within the associated State.
     * 内部方式
     */
    INTERNAL,
    /**
     * Implies that the Transition, if triggered, will not exit the composite (source) State, but it
     * will exit and re-enter any state within the composite State that is in the current state configuration.
     * 本地方式
     */
    LOCAL,
    /**
     * Implies that the Transition, if triggered, will exit the composite (source) State.
     * 外部方式
     */
    EXTERNAL,
    /**
     * 多条件方式
     */
    CHOOSE,
}
