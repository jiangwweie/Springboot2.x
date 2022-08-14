package com.jiangwei.demo.fsm.statemachine;

import com.jiangwei.demo.fsm.statemachine.impl.TransitionType;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * State
 *
 * @param <S> the type of state
 * @param <E> the type of event
 * @author Frank Zhang
 * @date 2020-02-07 2:12 PM
 */
public interface State<S, E, C> extends Visitable {

    /**
     * Gets the state identifier.
     *
     * @return the state identifiers
     */
    S getId();

    /**
     * Add transition to the state
     *
     * @param event  the event of the Transition
     * @param target the target of the transition
     * @return
     */
    Transition<S, E, C> addTransition(E event, State<S, E, C> target, TransitionType transitionType);

    Optional<Transition<S, E, C>> getTransition(E event);

    Collection<Transition<S, E, C>> getTransitions();

    /**
     * 可选择事件
     *
     * @param event
     * @param target
     * @param transitionType
     * @return
     */
    Transition<S, E, C> addTransitionByChoose(E event, State<S, E, C> target, TransitionType transitionType);

    List<Transition<S, E, C>> getTransitionByChoose(E event);

    /**
     * 通过event获取类型
     *
     * @param event 事件
     * @return TransitionType
     */
    TransitionType getTypeByEvent(E event);
}
