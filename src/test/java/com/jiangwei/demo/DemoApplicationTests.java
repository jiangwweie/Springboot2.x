package com.jiangwei.demo;


import cn.hutool.json.JSONUtil;
import com.jiangwei.demo.fsm.PayFlow;
import com.jiangwei.demo.fsm.Events;
import com.jiangwei.demo.fsm.Status;
import com.jiangwei.demo.fsm.statemachine.Action;
import com.jiangwei.demo.fsm.statemachine.Condition;
import com.jiangwei.demo.fsm.statemachine.StateMachine;
import com.jiangwei.demo.fsm.statemachine.builder.StateMachineBuilder;
import com.jiangwei.demo.fsm.statemachine.builder.StateMachineBuilderFactory;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

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


        //region------------------------审批通过事件已验证---------------------------
        builder.chooseExternalTransitions()
                .from(Status.初始状态)
                .on(Events.审批通过事件)
                .caseThen(dtcPay(), Status.待发送直连, doAction())
                .caseThen(otherPay(), Status.成功, doAction())
                .end(Status.成功,doAction());


        ////endregion------------------------审批通过事件---------------------------

        //region------------------------指令发送事件已验证---------------------------
        builder.chooseExternalTransitions()
                .from(Status.待发送直连)
                .on(Events.支付指令发送事件)
                .caseThen(publishSuccess(true), Status.已发送直连, doAction())
                .caseThen(publishFail(false), Status.失败, doAction())
                .end(Status.成功,doAction());

        //endregion------------------------指令发送事件---------------------------

        //region------------------------指令回调事件---------------------------

        //region已提交直连-》银行已受理、未受理、可疑、待人工确认、支付成功、失败
        builder.chooseExternalTransitions()
                .from(Status.已发送直连)
                .on(Events.支付指令回调事件)
                .caseThen(checkStatus(Status.银行已受理), Status.银行已受理, doAction())
                .caseThen(checkStatus(Status.银行未受理), Status.银行未受理, doAction())
                .caseThen(checkStatus(Status.可疑), Status.可疑, doAction())
                .caseThen(checkStatus(Status.成功), Status.成功, doAction())
                .caseThen(checkStatus(Status.失败), Status.失败, doAction())
                .caseThen(checkStatus(Status.待人工确认), Status.待人工确认, doAction())
                .end(Status.已发送直连, doAction());


        //endregion已提交直连-》银行已受理、未受理、可疑、待人工确认、支付成功、失败

        builder.chooseExternalTransitions()
                .from(Status.银行未受理)
                .on(Events.支付指令回调事件)
                .caseThen(checkStatus(Status.银行已受理), Status.银行已受理, doAction())
                .caseThen(checkStatus(Status.可疑), Status.可疑, doAction())
                .caseThen(checkStatus(Status.成功), Status.成功, doAction())
                .caseThen(checkStatus(Status.失败), Status.失败, doAction())
                .caseThen(checkStatus(Status.待人工确认), Status.待人工确认, doAction())
                .end(Status.银行未受理, doAction());


        builder.chooseExternalTransitions()
                .from(Status.银行已受理)
                .on(Events.支付指令回调事件)
                .caseThen(checkStatus(Status.可疑), Status.可疑, doAction())
                .caseThen(checkStatus(Status.成功), Status.成功, doAction())
                .caseThen(checkStatus(Status.失败), Status.失败, doAction())
                .caseThen(checkStatus(Status.待人工确认), Status.待人工确认, doAction())
                .end(Status.银行已受理, doAction());

        builder.chooseExternalTransitions()
                .from(Status.可疑)
                .on(Events.支付指令回调事件)
                .caseThen(checkStatus(Status.成功), Status.成功, doAction())
                .caseThen(checkStatus(Status.失败), Status.失败, doAction())
                .caseThen(checkStatus(Status.待人工确认), Status.待人工确认, doAction())
                .end(Status.可疑, doAction());

        builder.chooseExternalTransitions()
                .from(Status.待人工确认)
                .on(Events.支付指令回调事件)
                .caseThen(checkStatus(Status.成功), Status.成功, doAction())
                .caseThen(checkStatus(Status.失败), Status.失败, doAction())
                .end(Status.待人工确认, doAction());


        //支付成功-》退票 已验证
        builder.externalTransition()
                .from(Status.成功)
                .to(Status.退票)
                .on(Events.支付指令回调事件)
                .when(checkCondition())
                .perform(doAction());


        //endregion------------------------指令回调事件---------------------------

        //region------------------------支付取消事件已验证---------------------------
        builder.externalTransition()
                .from(Status.待发送直连)
                .to(Status.取消支付)
                .on(Events.支付取消事件)
                .when(checkCondition())
                .perform(doAction());
        //endregion------------------------支付取消事件---------------------------

        StateMachine<Status, Events, PayFlow> stateMachine = builder.build(MACHINE_ID);

        PayFlow payFlow = new PayFlow();
        payFlow.setFlowNum("PF001");
        payFlow.setChannel(-1);
        payFlow.setCreator("0001");
        payFlow.setState(Status.退票.getCode());

        stateMachine.showStateMachine();
        //Status target = stateMachine.fireEvent(Status.初始状态, Events.审批通过事件, payFlow);
        //Status target = stateMachine.fireEvent(Status.待发送直连, Events.支付指令发送事件, payFlow);
        //Status target = stateMachine.fireEvent(Status.待发送直连, Events.支付取消事件, payFlow);
        //Status target = stateMachine.fireEvent(Status.成功, Events.支付指令回调事件, payFlow);
        Status target = stateMachine.fireEvent(Status.成功, Events.支付指令回调事件, payFlow);
        System.out.println("target: " + target);
    }

    private Condition<PayFlow> publishSuccess(boolean flag) {
        System.out.println("判断指令是否发送成功");
        return (ctx) -> ctx.channel > 0;
    }
    private Condition<PayFlow> publishFail(boolean flag) {
        System.out.println("判断指令是否发送失败");
        return (ctx) -> ctx.channel < 0;
    }


    private Condition<PayFlow> checkCondition() {
        return (ctx) -> true;
    }

    private Condition<PayFlow> checkCondition(boolean flag) {
        return (ctx) -> flag;
    }

    private Action<Status, Events, PayFlow> doAction() {
        return (from, to, event, ctx) -> {
            System.out.println("=======================>" + JSONUtil.toJsonStr(ctx) + "  from=" + from + "  to=" + to + "  event=" + event);
        };
    }

    private Condition<PayFlow> otherPay() {
        return (ctx) -> ctx.channel == 2;
    }

    private Condition<PayFlow> dtcPay() {
        return (ctx) -> ctx.channel == 1;
    }

    private Condition<PayFlow> checkStatus(Status status) {
        return (ctx) -> {
            Optional<Status> optional = Status.fromCode(ctx.state);
            if (optional.isPresent()) {
                return optional.get() == status;
            }
            return false;
        };
    }


}
