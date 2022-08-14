package com.jiangwei.demo.fsm.statemachine.builder;

import com.jiangwei.demo.fsm.statemachine.Action;
import com.jiangwei.demo.fsm.statemachine.Condition;
import com.jiangwei.demo.fsm.statemachine.State;
import com.jiangwei.demo.fsm.statemachine.Transition;
import com.jiangwei.demo.fsm.statemachine.impl.StateHelper;
import com.jiangwei.demo.fsm.statemachine.impl.TransitionType;

import java.util.Map;

/**
 * TransitionBuilderImpl
 *
 * @author Frank Zhang
 * @date 2020-02-07 10:20 PM
 * <p>
 * 这里可以看出DSL规范的实现：From<S,E,C>, On<S,E,C>, To<S,E,C>，ExternalTransitionBuilder<S,E,C>
 */
class TransitionBuilderImpl<S, E, C> implements ExternalTransitionBuilder<S, E, C>, InternalTransitionBuilder<S, E, C>, From<S, E, C>, On<S, E, C>, To<S, E, C> {
    /**
     * 状态机集合
     */
    final Map<S, State<S, E, C>> stateMap;
    /**
     * 源状态
     */
    private State<S, E, C> source;
    /**
     * 目标状态
     */
    protected State<S, E, C> target;
    /**
     * 状态扭转实体
     */
    private Transition<S, E, C> transition;

    /**
     * 状态扭转类型
     */
    final TransitionType transitionType;

    public TransitionBuilderImpl(Map<S, State<S, E, C>> stateMap, TransitionType transitionType) {
        this.stateMap = stateMap;
        this.transitionType = transitionType;
    }

    @Override
    public From<S, E, C> from(S stateId) {
        source = StateHelper.getState(stateMap, stateId);
        return this;
    }

    @Override
    public To<S, E, C> to(S stateId) {
        target = StateHelper.getState(stateMap, stateId);
        return this;
    }

    @Override
    public To<S, E, C> within(S stateId) {
        source = target = StateHelper.getState(stateMap, stateId);
        return this;
    }

    @Override
    public When<S, E, C> when(Condition<C> condition) {
        transition.setCondition(condition);
        return this;
    }

    @Override
    public void isTruePerform(Action<S, E, C> action) {
        transition.setCondition(context -> true);
        transition.setAction(action);
    }

    @Override
    public On<S, E, C> on(E event) {
        transition = source.addTransition(event, target, transitionType);
        return this;
    }

    @Override
    public void perform(Action<S, E, C> action) {
        transition.setAction(action);
    }


}
