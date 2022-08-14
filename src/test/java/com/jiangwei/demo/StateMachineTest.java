package com.jiangwei.demo;

import cn.hutool.json.JSONUtil;
import com.jiangwei.demo.fsm.statemachine.Action;
import com.jiangwei.demo.fsm.statemachine.Condition;
import com.jiangwei.demo.fsm.statemachine.StateMachine;
import com.jiangwei.demo.fsm.statemachine.StateMachineFactory;
import com.jiangwei.demo.fsm.statemachine.builder.StateMachineBuilder;
import com.jiangwei.demo.fsm.statemachine.builder.StateMachineBuilderFactory;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * StateMachineTest
 *
 * @author Frank Zhang
 * @date 2020-02-08 12:19 PM
 */
public class StateMachineTest {

    static String MACHINE_ID = "TestStateMachine";

    static enum States {
        STATE1, STATE2, STATE3, STATE4
    }

    static enum Events {
        EVENT1, EVENT2, EVENT3, EVENT4, INTERNAL_EVENT
    }

    static class Context {
        public Integer id;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Context() {
        }

        public Context(Integer id) {
            this.id = id;
        }
    }

    @Test
    public void testChooseExternalNormal() {
        StateMachineBuilder<States, Events, Context> builder = StateMachineBuilderFactory.create();
        builder.chooseExternalTransitions()
                .from(States.STATE1)
                .on(Events.EVENT1)
                .caseThen(checkCondition(false), States.STATE1, doAction())
                .caseThen(checkCondition(true), States.STATE3, doAction())
                .end(States.STATE4, doAction());

        builder.externalTransition()
                .from(States.STATE2)
                .to(States.STATE2)
                .on(Events.EVENT2)
                .isTruePerform(doAction());
        StateMachine<States, Events, Context> stateMachine = builder.build(MACHINE_ID);
//        stateMachine.fireEvent(States.STATE1, Events.EVENT1, new Context(1));
        //from=STATE1  to=STATE3  event=EVENT1
        ThreadPoolExecutor pool = new ThreadPoolExecutor(5, 5, 10, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
        for (int i = 0; i < 10; i++) {
            final int index = i;
            pool.submit(() -> {
                stateMachine.fireEvent(States.STATE1, Events.EVENT1, new Context(index));
                stateMachine.fireEvent(States.STATE2, Events.EVENT2, new Context(10000 + index));
            });
        }
        pool.shutdown();
        try {
            // awaitTermination返回false即超时会继续循环，返回true即线程池中的线程执行完成主线程跳出循环往下执行，每隔10秒循环一次
            while (!pool.awaitTermination(10, TimeUnit.SECONDS)) ;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

//        Assert.assertEquals(States.STATE2, target);
    }

    @Test
    public void testExternalNormal() {
        StateMachineBuilder<States, Events, Context> builder = StateMachineBuilderFactory.create();
//        builder.externalTransition()
//                .from(States.STATE1)
//                .to(States.STATE2)
//                .on(Events.EVENT1)
//                .when(checkCondition())
//                .perform(doAction());


        builder.externalTransition()
                .from(States.STATE1)
                .to(States.STATE2)
                .on(Events.EVENT1)
                .isTruePerform(doAction());

        StateMachine<States, Events, Context> stateMachine = builder.build(MACHINE_ID);
        States target = stateMachine.fireEvent(States.STATE1, Events.EVENT1, new Context());
        Assert.assertEquals(States.STATE2, target);
    }

    @Test
    public void testExternalTransitionsNormal() {
        StateMachineBuilder<States, Events, Context> builder = StateMachineBuilderFactory.create();
        builder.externalTransitions()
                .fromAmong(States.STATE1, States.STATE2, States.STATE3)
                .to(States.STATE4)
                .on(Events.EVENT1)
                .when(checkCondition())
                .perform(doAction());

        StateMachine<States, Events, Context> stateMachine = builder.build(MACHINE_ID + "1");
        States target = stateMachine.fireEvent(States.STATE2, Events.EVENT1, new Context());
        Assert.assertEquals(States.STATE4, target);
    }

    @Test
    public void testInternalNormal() {
        StateMachineBuilder<States, Events, Context> builder = StateMachineBuilderFactory.create();
        builder.internalTransition()
                .within(States.STATE1)
                .on(Events.EVENT1)
                .when(checkCondition())
                .perform(doAction());
        StateMachine<States, Events, Context> stateMachine = builder.build(MACHINE_ID + "2");
        States target = stateMachine.fireEvent(States.STATE1, Events.EVENT1, new Context());
        Assert.assertEquals(States.STATE1, target);
    }

    @Test
    public void testExternalInternalNormal() {
        StateMachine<States, Events, Context> stateMachine = buildStateMachine("testExternalInternalNormal");

        Context context = new Context();
        States target = stateMachine.fireEvent(States.STATE1, Events.EVENT1, context);
        Assert.assertEquals(States.STATE2, target);
        target = stateMachine.fireEvent(States.STATE2, Events.INTERNAL_EVENT, context);
        Assert.assertEquals(States.STATE2, target);
        target = stateMachine.fireEvent(States.STATE2, Events.EVENT2, context);
        Assert.assertEquals(States.STATE1, target);
        target = stateMachine.fireEvent(States.STATE1, Events.EVENT3, context);
        Assert.assertEquals(States.STATE3, target);
    }

    private StateMachine<States, Events, Context> buildStateMachine(String machineId) {
        StateMachineBuilder<States, Events, Context> builder = StateMachineBuilderFactory.create();
        builder.externalTransition()
                .from(States.STATE1)
                .to(States.STATE2)
                .on(Events.EVENT1)
                .when(checkCondition())
                .perform(doAction());

        builder.internalTransition()
                .within(States.STATE2)
                .on(Events.INTERNAL_EVENT)
                .when(checkCondition())
                .perform(doAction());

        builder.externalTransition()
                .from(States.STATE2)
                .to(States.STATE1)
                .on(Events.EVENT2)
                .when(checkCondition())
                .perform(doAction());

        builder.externalTransition()
                .from(States.STATE1)
                .to(States.STATE3)
                .on(Events.EVENT3)
                .when(checkCondition())
                .perform(doAction());

        builder.externalTransitions()
                .fromAmong(States.STATE1, States.STATE2, States.STATE3)
                .to(States.STATE4)
                .on(Events.EVENT4)
                .when(checkCondition())
                .perform(doAction());

        builder.build(machineId);

        StateMachine<States, Events, Context> stateMachine = StateMachineFactory.get(machineId);
        stateMachine.showStateMachine();
        return stateMachine;
    }

    @Test
    public void testMultiThread() {
        buildStateMachine("testMultiThread");
        ThreadPoolExecutor pool = new ThreadPoolExecutor(5, 5, 10, TimeUnit.SECONDS, new LinkedBlockingQueue<>());

        for (int i = 0; i < 10; i++) {
            final int index = i;
            pool.submit(() -> {
                StateMachine<States, Events, Context> stateMachine = StateMachineFactory.get("testMultiThread");
                States target = stateMachine.fireEvent(States.STATE1, Events.EVENT1, new Context(index));
                Assert.assertEquals(States.STATE2, target);
            });
        }


        for (int i = 0; i < 10; i++) {
            final int index = i;
            pool.submit(() -> {
                StateMachine<States, Events, Context> stateMachine = StateMachineFactory.get("testMultiThread");
                States target = stateMachine.fireEvent(States.STATE1, Events.EVENT4, new Context(index));
                Assert.assertEquals(States.STATE4, target);
            });
        }

        for (int i = 0; i < 10; i++) {
            final int index = i;
            pool.submit(() -> {
                StateMachine<States, Events, Context> stateMachine = StateMachineFactory.get("testMultiThread");
                States target = stateMachine.fireEvent(States.STATE1, Events.EVENT3, new Context(index));
                Assert.assertEquals(States.STATE3, target);
            });
        }
        pool.shutdown();
        try {
            // awaitTermination返回false即超时会继续循环，返回true即线程池中的线程执行完成主线程跳出循环往下执行，每隔10秒循环一次
            while (!pool.awaitTermination(10, TimeUnit.SECONDS)) ;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    private Condition<Context> checkCondition() {
        return (ctx) -> {
            return true;
        };
    }

    private Action<States, Events, Context> doAction() {
        return (from, to, event, ctx) -> {
            System.out.println("=======================>" + JSONUtil.toJsonStr(ctx) + "  from=" + from + "  to=" + to + "  event=" + event);
        };
    }

    private Condition<Context> checkCondition(boolean check) {
        return (ctx) -> {
            return check;
        };
    }


}
