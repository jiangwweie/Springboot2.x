package com.jiangwei.demo.fsm.statemachine.builder.ext;


import com.jiangwei.demo.fsm.statemachine.Action;
import com.jiangwei.demo.fsm.statemachine.Condition;

/**
 * @author wuxi
 * @date 2021-02-09 11:34 上午
 */
public interface ChooseOn<S, E, C> {

    /**
     * 选择判断
     *
     * @param condition 判断条件
     * @param stateId   目标状态
     * @param action    执行事件
     * @return ChooseOn
     */
    ChooseOn<S, E, C> caseThen(Condition<C> condition, S stateId, Action<S, E, C> action);


    /**
     * 结束语句
     *
     * @param stateId 目标状态
     * @param action  执行事件
     */
    void end(S stateId, Action<S, E, C> action);
}
