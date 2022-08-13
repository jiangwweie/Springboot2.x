package com.jiangwei.demo.fsm;

/**
 * @author jiangwei
 * @date 2022/8/12
 */
public enum Status {


    /**
     * 待发送直连
     */
    待发送直连(1, "待发送直连"),
    /**
     * 已发送直连
     */
    已发送直连(2, "已发送直连"),

    /**
     * 已受理
     */
    银行已受理(3, "银行已受理"),
    /**
     * 未受理
     */
    银行未受理(4, "银行未受理"),
    /**
     * 支付成功
     */
    成功(5, "成功"),
    /**
     * 支付失败
     */
    失败(6, "失败"),

    /**
     * 退票
     */
    退票(7, "退票"),
    /**
     * 可疑
     */
    可疑(8, "可疑"),

    /**
     * 待人工确认
     */
    待人工确认(9, "待人工确认"),

    /**
     * 取消支付
     */
    取消支付(10, "取消支付"),
    ;
    int code;
    String desc;

    Status(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
