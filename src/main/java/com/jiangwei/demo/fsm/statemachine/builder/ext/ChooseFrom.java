package com.jiangwei.demo.fsm.statemachine.builder.ext;

/**
 * @author wuxi
 * @date 2021-02-09 11:21 上午
 */
public interface ChooseFrom<S, E, C> {

    /**
     * Build transition event
     * @param event transition event
     * @return On clause builder
     */
    ChooseOn<S, E, C> on(E event);
}
