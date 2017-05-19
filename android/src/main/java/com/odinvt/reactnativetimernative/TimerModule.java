package com.odinvt.reactnativetimernative;

import android.util.Log;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeMap;


/**
 * React Native Timer
 * from：https://github.com/Odinvt
 * Author: Oussama El Bacha
 * GitHub:https://github.com/Odinvt
 * Email:oussama.elbacha@gmail.com
 */
public class TimerModule extends ReactContextBaseJavaModule{
    public TimerModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return "RNTimer";
    }

    /**
     * GET current time
     */
    @ReactMethod
    public void getTime(String js_time, Callback success) {
        long time= System.currentTimeMillis();
        long js_time_long = Long.valueOf(js_time);
        long diff = Math.abs(time - js_time_long);

        WritableMap resultData = new WritableNativeMap();
        resultData.putString("time", String.valueOf(time));
        resultData.putString("diff", String.valueOf(diff));


        success.invoke(resultData);
    }
}