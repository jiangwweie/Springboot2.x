package com.jiangwei.demo.fsm.statemachine;

/**
 * StateMachine
 *
 * @author Frank Zhang
 *
 * @param <S> the type of state
 * @param <E> the type of event
 * @param <C> the user defined context
 * @date 2020-02-07 2:13 PM
 */
public interface StateMachine<S, E, C> extends Visitable{

    /**
     * Send an event {@code E} to the state machine.
     *
     * @param sourceState the source state
     * @param event the event to send
     * @param ctx the user defined context
     * @return the target state
     */
     S fireEvent(S sourceState, E event, C ctx);


    /**
     * MachineId is the identifier for a State Machine
     * @return
     */
    String getMachineId();

    /**
     * Use visitor pattern to display the structure of the state machine
     */
    void showStateMachine();

    String generatePlantUML();

    /**
     * 通过状态ID获取当前状态实体对象
     * @param currentStateId id
     * @return State
     */
    State getState(S currentStateId);
}
