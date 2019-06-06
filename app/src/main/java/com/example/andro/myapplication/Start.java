package com.example.andro.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class Start extends BroadcastReceiver{
    SharedPreferences read;
    @Override
    public void onReceive(Context context, Intent intent) {
        read =context.getSharedPreferences("Enable", context.MODE_MULTI_PROCESS);
        if(read.getBoolean("enable", false))
        context.startService(new Intent(context,my.class));
    }
}
