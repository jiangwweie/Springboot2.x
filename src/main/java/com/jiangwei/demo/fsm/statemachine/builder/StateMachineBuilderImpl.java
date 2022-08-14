package com.jiangwei.demo.fsm.statemachine.builder;

import com.jiangwei.demo.fsm.statemachine.State;
import com.jiangwei.demo.fsm.statemachine.StateMachine;
import com.jiangwei.demo.fsm.statemachine.StateMachineFactory;
import com.jiangwei.demo.fsm.statemachine.builder.ext.ChooseExternalTransitionBuilder;
import com.jiangwei.demo.fsm.statemachine.builder.ext.ChooseTransitionBuilderImpl;
import com.jiangwei.demo.fsm.statemachine.impl.StateMachineImpl;
import com.jiangwei.demo.fsm.statemachine.impl.TransitionType;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * StateMachineBuilderImpl
 *
 * @author Frank Zhang
 * @date 2020-02-07 9:40 PM
 */
public class StateMachineBuilderImpl<S, E, C> implements StateMachineBuilder<S, E, C> {
    /**
     * StateMap is the same with stateMachine, as the core of state machine is holding reference to states.
     * 状态枚举持有对状态机的引用
     */
    private final Map<S, State< S, E, C>> stateMap = new ConcurrentHashMap<>();

    private final StateMachineImpl<S, E, C> stateMachine = new StateMachineImpl<>(stateMap);

    @Override
    public ExternalTransitionBuilder<S, E, C> externalTransition() {
        return new TransitionBuilderImpl<>(stateMap, TransitionType.EXTERNAL);
    }

    @Override
    public ExternalTransitionsBuilder<S, E, C> externalTransitions() {
        return new TransitionsBuilderImpl<>(stateMap, TransitionType.EXTERNAL);
    }

    @Override
    public InternalTransitionBuilder<S, E, C> internalTransition() {
        return new TransitionBuilderImpl<>(stateMap, TransitionType.INTERNAL);
    }

    @Override
    public StateMachine<S, E, C> build(String machineId) {
        stateMachine.setMachineId(machineId);
        stateMachine.setReady(true);
        StateMachineFactory.register(stateMachine);
        return stateMachine;
    }

    @Override
    public ChooseExternalTransitionBuilder<S, E, C> chooseExternalTransitions() {
        return new ChooseTransitionBuilderImpl<>(stateMap,TransitionType.CHOOSE);
    }

}
