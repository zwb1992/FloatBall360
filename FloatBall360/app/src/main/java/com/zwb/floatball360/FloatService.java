package com.zwb.floatball360;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by zwb
 * Description
 * Date 2017/5/27.
 */

public class FloatService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        FloatManager.getInstance(this).showFloatBall();
    }
}
