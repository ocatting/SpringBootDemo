package com.nio.demo.asyn;

/**
 * @Description:
 * @Author Yan XinYu
 **/
public interface MessageState {

    /** 待执行 */
    int INITIAL = 0;

    /** 正在执行 */
    int RUNNING = 1;

    /** 异常退出 */
    int EXCEPTION = 2;

    /** 被取消 */
    int CANCEL = 3;

    /** 已完成 */
    int COMPLETE = 4;
}
