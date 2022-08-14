package com.jiangwei.demo.fsm.statemachine.impl;

import com.jiangwei.demo.fsm.statemachine.State;
import com.jiangwei.demo.fsm.statemachine.Transition;
import com.jiangwei.demo.fsm.statemachine.Visitor;

import java.util.*;

/**
 * StateImpl
 *
 * @author Frank Zhang
 * @date 2020-02-07 11:19 PM
 */
public class StateImpl<S, E, C> implements State<S, E, C> {
    protected final S stateId;
    private HashMap<E, Transition<S, E, C>> transitions = new HashMap<>();

    /**
     * 选择Transitions
     */
    private HashMap<E, List<Transition<S, E, C>>> chooseTransitions = new HashMap<>();

    /**
     * 事件类型选择器
     */
    private HashMap<E,TransitionType> eventHashMap = new HashMap<>();

    public StateImpl(S stateId) {
        this.stateId = stateId;
    }

    @Override
    public Transition<S, E, C> addTransition(E event, State<S, E, C> target, TransitionType transitionType) {
        Transition<S, E, C> newTransition = new TransitionImpl<>();
        newTransition.setSource(this);
        newTransition.setTarget(target);
        newTransition.setEvent(event);
        newTransition.setType(transitionType);
        Debugger.debug("Begin to add new transition: " + newTransition);
        verify(event, newTransition);
        eventHashMap.put(event,transitionType);
        transitions.put(event, newTransition);
        return newTransition;
    }

    @Override
    public Transition<S, E, C> addTransitionByChoose(E event, State<S, E, C> target, TransitionType transitionType) {
        verifyChoose(event);
        Transition<S, E, C> newTransition = new TransitionImpl<>();
        newTransition.setSource(this);
        newTransition.setTarget(target);
        newTransition.setEvent(event);
        newTransition.setType(transitionType);
        Debugger.debug("Begin to add Choose Transition: " + newTransition);
        List<Transition<S, E, C>> transitions;
        if (chooseTransitions.containsKey(event)) {
            transitions = chooseTransitions.get(event);
        } else {
            transitions = new ArrayList<>();
        }
        transitions.add(newTransition);
        eventHashMap.put(event,transitionType);
        chooseTransitions.put(event, transitions);
        return newTransition;
    }

    /**
     * 多选择事件处理器
     *
     * @param event
     * @return
     */
    @Override
    public List<Transition<S, E, C>> getTransitionByChoose(E event) {
        if (!chooseTransitions.containsKey(event)) {
            throw new StateMachineException(String.format("状态类型错误: 当前状态(%s)下没有绑定（%s）事件", stateId, event));
        }
        return chooseTransitions.get(event);
    }

    @Override
    public TransitionType getTypeByEvent(E event) {
        TransitionType transitionType = eventHashMap.get(event);
        if(Objects.isNull(transitionType)){
            throw new StateMachineException(String.format("状态类型错误: 当前状态(%s)下没有绑定（%s）事件", stateId, event));
        }
        return transitionType;
    }

    /**
     * Per one source and target state, there is only one transition is allowed
     *
     * @param event
     */
    private void verifyChoose(E event) {
        Transition existingTransition = transitions.get(event);
        if (existingTransition != null) {
            throw new StateMachineException(String.format("状态类型错误: 当前状态（%s）已经绑定了事件（%s）已经使用了 %s 类型，不能在选用TransitionType.Choose 类型",
                    stateId, event, existingTransition.getType()));
        }
    }

    /**
     * Per one source and target state, there is only one transition is allowed
     *
     * @param event
     * @param newTransition
     */
    private void verify(E event, Transition<S, E, C> newTransition) {
        if (chooseTransitions.containsKey(event)) {
            throw new StateMachineException(String.format("状态类型错误:当前状态（%s）已经绑定了事件（%s）已经使用了 TransitionType.Choose  类型，不能在选用其他类型",stateId, event));
        }
        Transition existingTransition = transitions.get(event);
        if (existingTransition != null) {
            if (existingTransition.equals(newTransition)) {
                throw new StateMachineException(existingTransition + " already Exist, you can not add another one");
            }
        }
    }

    @Override
    public Optional<Transition<S, E, C>> getTransition(E event) {
        return Optional.ofNullable(transitions.get(event));
    }

    @Override
    public Collection<Transition<S, E, C>> getTransitions() {
        return transitions.values();
    }

    @Override
    public S getId() {
        return stateId;
    }

    @Override
    public String accept(Visitor visitor) {
        String entry = visitor.visitOnEntry(this);
        String exit = visitor.visitOnExit(this);
        return entry + exit;
    }

    @Override
    public boolean equals(Object anObject) {
        if (anObject instanceof State) {
            State other = (State) anObject;
            if (this.stateId.equals(other.getId()))
                return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return stateId.toString();
    }
}
