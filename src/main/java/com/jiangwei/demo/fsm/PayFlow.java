package com.jiangwei.demo.fsm;

import lombok.Data;

/**
 * @author jiangwei
 * @date 2022/8/12
 */
@Data
public class PayFlow {

    public String creator;

    public String flowNum;

    public int state;

    public int originState;

    /**
     * 渠道：1直连
     * 2非直连
     */
    public int channel;

}