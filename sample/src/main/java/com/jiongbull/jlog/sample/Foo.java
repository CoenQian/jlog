package com.jiongbull.jlog.sample;

import com.jiongbull.jlog.Logger;
import com.jiongbull.jlog.util.TimeUtils;

/**
 * 测试Proguard.
 */
@SuppressWarnings({"WeakerAccess"})
public class Foo {

    /**
     * 打印当前日期.
     */
    public void now(Logger logger) {
        logger.e(TimeUtils.getCurDate(TimeUtils.ZoneOffset.N0800));
    }
}