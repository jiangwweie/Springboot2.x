package com.jiangwei.demo;


import com.alibaba.cola.statemachine.Action;
import com.alibaba.cola.statemachine.Condition;
import com.alibaba.cola.statemachine.StateMachine;
import com.alibaba.cola.statemachine.StateMachineFactory;
import com.alibaba.cola.statemachine.builder.StateMachineBuilder;
import com.alibaba.cola.statemachine.builder.StateMachineBuilderFactory;
import com.jiangwei.demo.fsm.PayFlow;
import com.jiangwei.demo.fsm.Events;
import com.jiangwei.demo.fsm.Status;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * springboot2.5.4依赖的Junit5，不在需要@RunWith了
 */
@SpringBootTest
@RunWith(SpringRunner.class)
class DemoApplicationTests {


    private static final String MACHINE_ID = "TEST_MACHINE_ID";


    /**
     * 简单流转
     */
    @Test
    public void testExternalNormal() {
        StateMachineBuilder<Status, Events, PayFlow> builder = StateMachineBuilderFactory.create();
        builder.externalTransition()
                .from(Status.待发送直连)
                .to(Status.已发送直连)
                .on(Events.EVENT1)
                .when(checkCondition())
                .perform(doAction());


        builder.externalTransition()
                .from(Status.待发送直连)
                .to(Status.取消支付)
                .on(Events.EVENT4)
                .when(checkCondition())
                .perform(doAction());


        StateMachine<Status, Events, PayFlow> stateMachine = builder.build(MACHINE_ID);

        PayFlow payFlow = new PayFlow();
        payFlow.setFlowNum("PF001");
        payFlow.setChannel(1);
        payFlow.setCreator("0001");


        Status target = stateMachine.fireEvent(Status.待发送直连, Events.FLOW_CREATED, payFlow);

        Assert.assertEquals(Status.已发送直连, target);
    }


    @Test
    public void test2(){
        StateMachineBuilder<Status, Events, PayFlow> builder = StateMachineBuilderFactory.create();
        //external transition
        builder.externalTransition()
                .from(Status.待发送直连)
                .to(Status.已发送直连)
                .on(Events.EVENT1)
                .when(checkCondition())
                .perform(doAction());



        //internal transition
        builder.internalTransition()
                .within(Status.银行已受理)
                .on(Events.EVENT1)
                .when(checkCondition())
                .perform(doAction());



        //external transitions
        builder.externalTransitions()
                .fromAmong(Status.已发送直连, Status.银行已受理, Status.银行未受理)
                .to(Status.成功)
                .on(Events.EVENT2)
                .when(checkCondition())
                .perform(doAction());



        builder.build(MACHINE_ID);

        StateMachine<Status, Events, PayFlow> stateMachine = StateMachineFactory.get(MACHINE_ID);
        stateMachine.showStateMachine();

    }



    private Condition<PayFlow> checkCondition() {
        return (ctx) -> ctx.channel == 1;
    }

    private Action<Status, Events, PayFlow> doAction() {
        return (from, to, event, ctx) -> {
            System.out.println(ctx.creator + " is operating " + ctx.flowNum + " from:" + from + " to:" + to + " on:" + event);
        };
    }


}
