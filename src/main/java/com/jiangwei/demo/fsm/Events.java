package com.jiangwei.demo.fsm;

/**
 * @author jiangwei
 */

public enum Events {

    FLOW_CREATED(11, "支付流水生成事件"),
    EVENT1(1, "状态变更、发送ES"),
    EVENT2(2, "支付成功发送通知"),
    EVENT3(3, "支付失败发送通知"),
    EVENT4(4, "取消支付"),
    EVENT5(5, "待发送直连");
    int code;
    String desc;

    Events(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}