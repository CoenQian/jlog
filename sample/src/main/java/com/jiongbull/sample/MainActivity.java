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

package com.jiongbull.sample;

import com.jiongbull.jlog.JLog;

import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * HOME PAGE.
 */
public class MainActivity extends AppCompatActivity {

    private static final String URL_BLOG = "http://jiongbull.com";
    private static final String URL_GITHUB = "https://github.com/JiongBull";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.action_blog:
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse(URL_BLOG));
                startActivity(intent);
                break;
            case R.id.action_github:
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse(URL_GITHUB));
                startActivity(intent);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void logNormal(View view) {
        JLog.v("Default tag");
        JLog.d("Default tag");
        JLog.i("Default tag");
        JLog.w("Default tag");
        JLog.e("Default tag");
        JLog.wtf("Default tag");
    }

    public void logTag(View view) {
        String tag = "LOG_WITH_TAG";
        JLog.v(tag, "Custom tag");
        JLog.d(tag, "Custom tag");
        JLog.i(tag, "Custom tag");
        JLog.w(tag, "Custom tag");
        JLog.e(tag, "Custom tag");
        JLog.wtf(tag, "Custom tag");
    }

    public void logMoreThan4000(View view) {
        AssetManager am = getAssets();
        try {
            InputStream is = am.open("4000.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line;
            String content = "";
            while ((line = br.readLine()) != null) {
                content += line;
            }
            JLog.i(content);
            br.close();
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void logJson(View view) {
        JLog.json("{\"location\":{\"id\":\"C23NB62W20TF\",\"name\":\"西雅图\",\"country\":\"US\",\"path\":\"西雅图,华盛顿州,美国\",\"timezone\":\"America/Los_Angeles\",\"timezone_offset\":\"-08:00\"}}");
    }

    public void logProguard(View view) {
        Foo foo = new Foo();
        foo.now();
    }
}