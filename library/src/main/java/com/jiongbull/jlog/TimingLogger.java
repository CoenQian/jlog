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

package com.jiongbull.jlog;

import android.os.SystemClock;

import java.util.ArrayList;

/**
 * A utility class to help log timings splits throughout a method call.
 * Typical usage is:
 *
 * <pre>
 *     TimingLogger timings = new TimingLogger(TAG, "methodA", logger);
 *     // ... do some work A ...
 *     timings.addSplit("work A");
 *     // ... do some work B ...
 *     timings.addSplit("work B");
 *     // ... do some work C ...
 *     timings.addSplit("work C");
 *     timings.dumpToLog();
 * </pre>
 *
 * <p>The dumpToLog call would add the following to the log:</p>
 *
 * <pre>
 *     I/TAG     ( 3459): methodA begin
 *     I/TAG     ( 3459): methodA 9 ms, work A
 *     I/TAG     ( 3459): methodA 1 ms, work B
 *     I/TAG     ( 3459): methodA 6 ms, work C
 *     I/TAG     ( 3459): methodA end, 16 ms
 * </pre>
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class TimingLogger {

    private static final String TAG = "timing";
    /** Stores the time of each split. */
    private ArrayList<Long> mSplits;
    /** Stores the labels for each split. */
    private ArrayList<String> mSplitLabels;
    /**
     * The Log tag.
     */
    private String mTag;
    /** A label to be included in every log. */
    private String mLabel;
    /** Logger is used for log. */
    private Logger mLogger;

    /**
     * Create and initialize a TimingLogger object that will log using
     * the specific tag.
     *
     * @param label  a string to be displayed with each log
     * @param logger used for log
     */
    public TimingLogger(String label, Logger logger) {
        reset(TAG, label, logger);
    }

    /**
     * Create and initialize a TimingLogger object that will log using
     * the specific tag.
     *
     * @param tag    the log tag to use while logging the timings
     * @param label  a string to be displayed with each log
     * @param logger used for log
     */
    public TimingLogger(String tag, String label, Logger logger) {
        reset(tag, label, logger);
    }

    /**
     * Clear and initialize a TimingLogger object that will log using
     * the specific tag.
     *
     * @param tag    the log tag to use while logging the timings
     * @param label  a string to be displayed with each log
     * @param logger used for log
     */
    private void reset(String tag, String label, Logger logger) {
        mTag = tag;
        mLabel = label;
        mLogger = logger;
        reset();
    }

    /**
     * Clear and initialize a TimingLogger object that will log using
     * the tag and label that was specified previously, either via
     * the constructor or a call to reset(tag, label).
     */
    private void reset() {
        if (mSplits == null) {
            mSplits = new ArrayList<>();
            mSplitLabels = new ArrayList<>();
        } else {
            mSplits.clear();
            mSplitLabels.clear();
        }
        addSplit(null);
    }

    /**
     * Add a split for the current time, labeled with splitLabel.
     *
     * @param splitLabel a label to associate with this split.
     */
    @SuppressWarnings("SameParameterValue")
    private void addSplit(String splitLabel) {
        long now = SystemClock.elapsedRealtime();
        mSplits.add(now);
        mSplitLabels.add(splitLabel);
    }

    /**
     * Dumps the timings to the log using Logger.i().
     */
    public void dumpToLog() {
        mLogger.i(mTag, mLabel + " begin");
        final long first = mSplits.get(0);
        long now = first;
        for (int i = 1; i < mSplits.size(); i++) {
            now = mSplits.get(i);
            final String splitLabel = mSplitLabels.get(i);
            final long prev = mSplits.get(i - 1);

            mLogger.i(mTag, mLabel + " " + (now - prev) + " ms, " + splitLabel);
        }
        mLogger.i(mTag, mLabel + " end, " + (now - first) + " ms");
    }
}
