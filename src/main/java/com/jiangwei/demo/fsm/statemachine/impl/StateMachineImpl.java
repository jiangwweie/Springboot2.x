package com.jiangwei.demo.fsm.statemachine.impl;

import com.jiangwei.demo.fsm.statemachine.State;
import com.jiangwei.demo.fsm.statemachine.StateMachine;
import com.jiangwei.demo.fsm.statemachine.Transition;
import com.jiangwei.demo.fsm.statemachine.Visitor;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * For performance consideration,
 * The state machine is made "stateless" on purpose.
 * Once it's built, it can be shared by multi-thread
 * <p>
 * One side effect is since the state machine is stateless, we can not get current state from State Machine.
 *
 * @author Frank Zhang
 * @date 2020-02-07 5:40 PM
 */
public class StateMachineImpl<S, E, C> implements StateMachine<S, E, C> {

    private String machineId;

    private final Map<S, State<S, E, C>> stateMap;

    private boolean ready;

    public StateMachineImpl(Map<S, State<S, E, C>> stateMap) {
        this.stateMap = stateMap;
    }

    @Override
    public S fireEvent(S sourceStateId, E event, C ctx) {
        isReady();
        State sourceState = getState(sourceStateId);
        return doChooseTransition(sourceState, event, ctx).getId();
    }

    private State<S, E, C> doChooseTransition(State sourceState, E event, C ctx) {
        TransitionType typeByEvent = sourceState.getTypeByEvent(event);
        switch (typeByEvent) {
            case LOCAL:
            case INTERNAL:
            case EXTERNAL:
                Optional<Transition<S, E, C>> transition = sourceState.getTransition(event);
                if (transition.isPresent()) {
                    return transition.get().transit(ctx);
                }
                break;
            case CHOOSE:
                List<Transition<S, E, C>> transitionByChoose = sourceState.getTransitionByChoose(event);
                Optional<Transition<S, E, C>> optional = transitionByChoose.stream().filter(d -> d.getCondition().isSatisfied(ctx)).findFirst();
                if (optional.isPresent()) {
                    Transition<S, E, C> secTransition = optional.get();
                    secTransition.getAction().execute(secTransition.getSource().getId(), secTransition.getTarget().getId(), secTransition.getEvent(), ctx);
                    return secTransition.getTarget();
                }else{
                    throw new StateMachineException(String.format("choose类型状态机无触发条件: 当前状态(%s)绑定的事件是(%s）事件", sourceState.getId(), event));
                }
            default:
                throw new StateMachineException(String.format("状态机无效的类型: 当前状态(%s)绑定的事件是(%s）事件，当前类型=%s", sourceState.getId(), event,typeByEvent));
        }
        throw new StateMachineException(String.format("状态类型错误: 当前状态(%s)下没有绑定（%s）事件", sourceState.getId(), event));
    }

    @Override
    public State getState(S currentStateId) {
        if (!stateMap.containsKey(currentStateId)) {
            showStateMachine();
            throw new StateMachineException(currentStateId + " is not found, please check state machine");
        }
        return StateHelper.getState(stateMap, currentStateId);
    }

    private void isReady() {
        if (!ready) {
            throw new StateMachineException("State machine is not built yet, can not work");
        }
    }

    @Override
    public String accept(Visitor visitor) {
        StringBuilder sb = new StringBuilder();
        sb.append(visitor.visitOnEntry(this));
        for (State state : stateMap.values()) {
            sb.append(state.accept(visitor));
        }
        sb.append(visitor.visitOnExit(this));
        return sb.toString();
    }

    @Override
    public void showStateMachine() {
        SysOutVisitor sysOutVisitor = new SysOutVisitor();
        accept(sysOutVisitor);
    }

    @Override
    public String generatePlantUML() {
        PlantUMLVisitor plantUMLVisitor = new PlantUMLVisitor();
        return accept(plantUMLVisitor);
    }

    @Override
    public String getMachineId() {
        return machineId;
    }

    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }
}
