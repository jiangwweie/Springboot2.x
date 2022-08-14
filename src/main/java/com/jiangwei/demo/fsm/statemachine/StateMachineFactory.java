package com.jiangwei.demo.fsm.statemachine;

import com.jiangwei.demo.fsm.statemachine.impl.StateMachineException;
import com.jiangwei.demo.fsm.statemachine.impl.TransitionType;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * StateMachineFactory
 *
 * @author Frank Zhang
 * @date 2020-02-08 10:21 PM
 */
public class StateMachineFactory {
    /**
     * 状态机工厂，来储存所有的状态，key：状态机唯一标识，value状态机
     */
    static Map<String /* machineId */, StateMachine> stateMachineMap = new ConcurrentHashMap<>();

    /**
     * 注册状态机
     *
     * @param stateMachine
     * @param <S>
     * @param <E>
     * @param <C>
     */
    public static <S, E, C> void register(StateMachine<S, E, C> stateMachine) {
        String machineId = stateMachine.getMachineId();
        if (stateMachineMap.get(machineId) != null) {
            throw new StateMachineException("The state machine with id [" + machineId + "] is already built, no need to build again");
        }
        stateMachineMap.put(stateMachine.getMachineId(), stateMachine);
    }

    /**
     * 通过状态机Id获取状态机
     *
     * @param machineId 状态机Id
     * @param <S>
     * @param <E>
     * @param <C>
     * @return
     */
    public static <S, E, C> StateMachine<S, E, C> get(String machineId) {
        StateMachine stateMachine = stateMachineMap.get(machineId);
        if (stateMachine == null) {
            throw new StateMachineException("There is no stateMachine instance for " + machineId + ", please build it first");
        }
        return stateMachine;
    }

    /**
     * 通过machineId,事件和起始状态获取Action类
     *
     * @param s
     * @param e
     * @param <S>
     * @param <E>
     * @param <C>
     * @return
     */
    public static <S, E, C> Class<? extends Action> getAction(String machineId, S s, E e, C context) {
        StateMachine stateMachine = stateMachineMap.get(machineId);
        if (Objects.isNull(stateMachine)) {
            throw new StateMachineException("无效的machineId，machineId=" + machineId);
        }
        State state = stateMachine.getState(s);
        if (Objects.isNull(state)) {
            throw new StateMachineException(String.format("当前状态机Id（%s）下,没有此状态state=%s", machineId, s));
        }
        TransitionType typeByEvent = state.getTypeByEvent(e);
        switch (typeByEvent) {
            case LOCAL:
            case EXTERNAL:
            case INTERNAL:
                Optional<Transition<S, E, C>> optional = state.getTransition(e);
                if (optional.isPresent()) {
                    return optional.get().getAction().getClass();
                }
                break;
            case CHOOSE:
                List<Transition<S, E, C>> transitionByChoose = state.getTransitionByChoose(e);
                Optional<Transition<S, E, C>> optionalTransition =
                        transitionByChoose.stream().filter(d -> d.getCondition().isSatisfied(context)).findFirst();
                if (optionalTransition.isPresent()) {
                    return optionalTransition.get().getAction().getClass();
                }
                break;
            default:
                throw new StateMachineException(String.format("状态机无效的类型: 当前状态(%s)绑定的事件是(%s）事件，当前类型=%s", state.getId(), e, typeByEvent));
        }
        throw new StateMachineException(String.format("当前状态机Id（%s）下,状态为state=%s，没找到对应的事件event=%s", machineId, s, e));
    }

}
