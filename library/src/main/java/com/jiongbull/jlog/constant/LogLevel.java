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

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.jiongbull.jlog.constant.LogLevel.DEBUG;
import static com.jiongbull.jlog.constant.LogLevel.ERROR;
import static com.jiongbull.jlog.constant.LogLevel.INFO;
import static com.jiongbull.jlog.constant.LogLevel.JSON;
import static com.jiongbull.jlog.constant.LogLevel.VERBOSE;
import static com.jiongbull.jlog.constant.LogLevel.WARN;
import static com.jiongbull.jlog.constant.LogLevel.WTF;

/**
 * 日志级别.
 */
@StringDef({VERBOSE, DEBUG, INFO, JSON, WARN, ERROR, WTF})
@Retention(RetentionPolicy.SOURCE)
public @interface LogLevel {
    String VERBOSE = "VERBOSE";
    String DEBUG = "DEBUG";
    String INFO = "INFO";
    String JSON = "JSON";
    String WARN = "WARN";
    String ERROR = "ERROR";
    String WTF = "WTF";
}
