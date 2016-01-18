package com.geniusmart.loveut.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class MyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences.Editor editor = context.getSharedPreferences(
                "account", Context.MODE_PRIVATE).edit();
        String name = intent.getStringExtra("EXTRA_USERNAME");
        editor.putString("USERNAME", name);
        editor.apply();
    }
}
