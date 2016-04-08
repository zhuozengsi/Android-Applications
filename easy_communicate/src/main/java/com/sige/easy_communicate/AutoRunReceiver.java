package com.sige.easy_communicate;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AutoRunReceiver extends BroadcastReceiver {

    /**When the system boots completely, it will send a broadcast whose action is this string.*/
    static final String ACTION_BOOT="android.intent.action.BOOT_COMPLETED";
    static final String ACTION_PRESENT = "android.intent.action.USER_PRESENT";
    public AutoRunReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        if(intent.getAction().equals(ACTION_BOOT) || intent.getAction().equals(ACTION_PRESENT)){
            Log.i("RECEIVER","start!");
            // After the system boots, we start this service.
            Intent serviceIntent = new Intent(context, SpeechSynthesizerService.class);
            context.startService(serviceIntent);
        }
    }
}
