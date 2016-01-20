package com.jiongbull.sample;

import com.jiongbull.jlog.JLog;
import com.jiongbull.jlog.util.TimeUtils;

/**
 * 测试Proguard.
 */
public class Foo {

    /**
     * 打印当前日期.
     */
    public void now() {
        JLog.e(TimeUtils.getCurDate());
    }
}