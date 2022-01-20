package com.jiangwei.demo.dto;

import lombok.Builder;
import lombok.Data;

/**
 * @author jiangwei
 * @date 2022/1/13
 */
@Data
@Builder
public class BaseResp<T> {

    private Integer code;

    private String msg;

    private T data;

    public static BaseResp error(String msg) {
        return BaseResp.builder().code(0).msg(msg).build();
    }

    public static <T extends Object> BaseResp success(T data) {
        return BaseResp.builder().code(0).data(data).build();
    }

    public static BaseResp success() {
        return BaseResp.builder().code(0).build();
    }

}
