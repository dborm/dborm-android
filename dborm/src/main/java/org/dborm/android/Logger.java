package org.dborm.android;

import android.util.Log;

import org.dborm.core.api.DbormLogger;

/**
 * Created by shk on 16/7/27.
 */
public class Logger implements DbormLogger {

    private String target = "org.dborm.test";

    @Override
    public void debug(String msg) {
        Log.d(target, msg);
    }

    @Override
    public void error(Throwable e) {
        Log.e(target, "", e);
    }

    @Override
    public void error(String msg, Throwable e) {
        Log.e(target, msg, e);
    }
}
