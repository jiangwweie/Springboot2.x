package com.jiangwei.demo.fsm.statemachine;



/**
 * Generic strategy interface used by a state machine to respond
 * events by executing an {@code Action} with a {@link StateContext}.
 *
 * @author Frank Zhang
 * @date 2020-02-07 2:51 PM
 */
public interface Action<S, E,C> {

//    /**
//     * Execute action with a {@link StateContext}.
//     *
//     * @param context the state context
//     */
//    void execute(StateContext<S, E> context);

    /**
     * 通用版本的状态机执行器
     *
     * @param from    来源状态
     * @param to      目标状态
     * @param event   触发事件
     * @param context 上下文
     */
    void execute(S from, S to, E event, C context);
}
