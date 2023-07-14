package com.chen.Bi.common;

/**
 * 枚举类
 *
 * @author CSY
 */
public enum StateEnum {
    //    wait,running,succeed,failed
    WAIT("wait"),
    RUNNING("running"),
    SUCCEED("succeed"),
    FAILED("failed");
//    public static final String WAIT_NAME = "wait";
//    public static final String RUNNING_NAME = "running";
//    public static final String SUCCEED_NAME = "succeed";
//    public static final String FAILED_NAME = "failed";

    /**
     * 信息
     */
    private final String state;

    StateEnum(String state) {
        this.state = state;
    }


    public String getValue() {
        return state;
    }

}
