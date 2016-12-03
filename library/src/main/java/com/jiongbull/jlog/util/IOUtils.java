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

package com.jiongbull.jlog.util;

import android.util.Log;

import java.io.Closeable;
import java.io.IOException;

/**
 * IO相关.
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class IOUtils {

    private static final String TAG = "IOUtils";

    private IOUtils() {
    }

    /**
     * Closes a <code>Closeable</code> unconditionally.
     *
     * @param closeable the objects to close, may be null or already closed
     * @since 2.0
     */
    public static void closeQuietly(final Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (final IOException ioe) {
            Log.e(TAG, ioe.getMessage());
        }
    }
}