/*
 * Copyright 2016 JiongBull
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

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.jiongbull.jlog.constant.LogSegment.FOUR_HOURS;
import static com.jiongbull.jlog.constant.LogSegment.ONE_HOUR;
import static com.jiongbull.jlog.constant.LogSegment.SIX_HOURS;
import static com.jiongbull.jlog.constant.LogSegment.THREE_HOURS;
import static com.jiongbull.jlog.constant.LogSegment.TWELVE_HOURS;
import static com.jiongbull.jlog.constant.LogSegment.TWENTY_FOUR_HOURS;
import static com.jiongbull.jlog.constant.LogSegment.TWO_HOURS;

/**
 * 日志时间切片.
 */
@IntDef({ONE_HOUR, TWO_HOURS, THREE_HOURS, FOUR_HOURS, SIX_HOURS, TWELVE_HOURS, TWENTY_FOUR_HOURS})
@Retention(RetentionPolicy.SOURCE)
public @interface LogSegment {
    int ONE_HOUR = 1;
    int TWO_HOURS = 2;
    int THREE_HOURS = 3;
    int FOUR_HOURS = 4;
    int SIX_HOURS = 6;
    int TWELVE_HOURS = 12;
    int TWENTY_FOUR_HOURS = 24;
}
