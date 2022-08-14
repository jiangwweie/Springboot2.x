package com.jiangwei.demo.fsm.statemachine.builder;

import com.jiangwei.demo.fsm.statemachine.Action;
import com.jiangwei.demo.fsm.statemachine.Condition;

/**
 * On
 *
 * @author Frank Zhang
 * @date 2020-02-07 6:14 PM
 */
public interface On<S, E, C> extends When<S, E, C> {
    /**
     * Add condition for the transition
     *
     * @param condition transition condition
     * @return When clause builder
     */
    When<S, E, C> when(Condition<C> condition);

    /**
     * 无判断条件
     *
     * @param action performed action
     */
    void isTruePerform(Action<S, E, C> action);
}
