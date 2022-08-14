package com.jiangwei.demo.fsm.statemachine;

/**
 * Condition
 *
 * @author Frank Zhang
 * @date 2020-02-07 2:50 PM
 */
public interface Condition<C> {

    /**
     * 状态机上下文条件判断语句
     * 获取状态机上下文
     * @param context context object
     * @return whether the context satisfied current condition
     */
    boolean isSatisfied(C context);

    default String name(){
        return this.getClass().getSimpleName();
    }
}
