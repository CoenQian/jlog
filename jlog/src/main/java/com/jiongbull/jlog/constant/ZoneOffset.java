/*
 * Copyright JiongBull 2016
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jiongbull.jlog.constant;

/**
 * 时区偏移，N表示负，P表示正，数字表示时区，例如东八区使用P0800表示.
 */
public enum ZoneOffset {

    N1200(-43200000L),
    N1100(-39600000L),
    N1000(-36000000L),
    N0900(-32400000L),
    N0800(-28800000L),
    N0700(-25200000L),
    N0600(-21600000L),
    N0500(-18000000L),
    N0430(-16200000L),
    N0400(-14400000L),
    N0330(-12600000L),
    N0300(-10800000L),
    N0200(-7200000L),
    N0100(-3600000L),
    N0000(0L),
    P0100(3600000L),
    P0200(7200000L),
    P0300(10800000L),
    P0330(12600000L),
    P0400(14400000L),
    P0430(16200000L),
    P0500(18000000L),
    P0530(19800000L),
    P0600(21600000L),
    P0630(23400000L),
    P0700(25200000L),
    P0800(28800000L),
    P0830(30600000L),
    P0900(32400000L),
    P0930(34200000L),
    P1000(36000000L),
    P1100(39600000L),
    P1200(43200000L),
    P1300(46800000L),
    P1400(50400000L);

    private long mValue;

    ZoneOffset(long value) {
        mValue = value;
    }

    public long getValue() {
        return mValue;
    }
}
