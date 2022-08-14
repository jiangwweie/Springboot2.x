package com.jiangwei.demo.fsm.statemachine.builder.ext;

import com.jiangwei.demo.fsm.statemachine.Action;
import com.jiangwei.demo.fsm.statemachine.Condition;
import com.jiangwei.demo.fsm.statemachine.State;
import com.jiangwei.demo.fsm.statemachine.Transition;
import com.jiangwei.demo.fsm.statemachine.impl.StateHelper;
import com.jiangwei.demo.fsm.statemachine.impl.TransitionType;

import java.util.Map;

/**
 * @author wuxi
 * @date 2021-02-09 11:16 上午
 */
public class ChooseTransitionBuilderImpl<S, E, C> implements ChooseExternalTransitionBuilder<S, E, C>, ChooseFrom<S, E, C>, ChooseOn<S, E, C> {
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

    private E event;

    public ChooseTransitionBuilderImpl(Map<S, State<S, E, C>> stateMap, TransitionType transitionType) {
        this.stateMap = stateMap;
        this.transitionType = transitionType;
    }


    @Override
    public ChooseFrom<S, E, C> from(S stateId) {
        this.source = StateHelper.getState(stateMap, stateId);
        return this;
    }

    @Override
    public ChooseOn<S, E, C> on(E event) {
        this.event = event;
        return this;
    }

    @Override
    public ChooseOn<S, E, C> caseThen(Condition<C> condition, S stateId, Action<S, E, C> action) {
        target = StateHelper.getState(stateMap, stateId);
        transition = source.addTransitionByChoose(event, target, TransitionType.CHOOSE);
        transition.setAction(action);
        transition.setCondition(condition);
        return this;
    }

    @Override
    public void end(S stateId, Action<S, E, C> action) {
        target = StateHelper.getState(stateMap, stateId);
        transition = source.addTransitionByChoose(event, target, TransitionType.CHOOSE);
        transition.setAction(action);
        transition.setCondition(context -> true);
    }
}
