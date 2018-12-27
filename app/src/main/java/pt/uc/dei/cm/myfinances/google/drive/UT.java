package pt.uc.dei.cm.myfinances.google.drive;
/**
 * Copyright 2015 Sean Janson. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Adapted by Rúben Saldanha on 20-12-2018
 *
 */

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.io.File;

final public class UT {
    public UT() {
    }

    private static final String L_TAG = "_X_";

    public static final String MYROOT = "MyFinances";
    public static final String MIME_TEXT = "text/plain";
    public static final String MIME_FLDR = "application/vnd.google-apps.folder";

    static final String TITL = "titl";
    public static final String GDID = "gdid";
    static final String MIME = "mime";

    private static final String TITL_FMT = "yyMMdd-HHmmss";

    private static SharedPreferences pfs;
    static Context acx;

    public static void init(Context ctx) {
        acx = ctx.getApplicationContext();
        pfs = PreferenceManager.getDefaultSharedPreferences(acx);
    }

    public static class AM {
        private AM() {
        }

        private static final String ACC_NAME = "account_name";
        private static String mEmail = null;

        public static void setEmail(String email) {
            UT.pfs.edit().putString(ACC_NAME, (mEmail = email)).apply();
        }

        public static String getEmail() {
            return mEmail != null ? mEmail : (mEmail = UT.pfs.getString(ACC_NAME, null));
        }
    }

    static ContentValues newCVs(String titl, String gdId, String mime) {
        ContentValues cv = new ContentValues();
        if (titl != null) cv.put(TITL, titl);
        if (gdId != null) cv.put(GDID, gdId);
        if (mime != null) cv.put(MIME, mime);
        return cv;
    }

    private static File cchFile(String flNm) {
        File cche = UT.acx.getExternalCacheDir();
        return (cche == null || flNm == null) ? null : new File(cche.getPath() + File.separator + flNm);
    }

    static void le(Throwable ex) {
        Log.e(L_TAG, Log.getStackTraceString(ex));
    }

    public static void lg(String msg) {
        Log.d(L_TAG, msg);
    }
}


