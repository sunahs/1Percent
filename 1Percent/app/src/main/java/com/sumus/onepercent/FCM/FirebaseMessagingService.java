package com.sumus.onepercent.FCM;

/**
 * Created by MINI on 2016-10-07.
 */
/*
푸시메세지가 들어왔을때 실제 사용자에게 푸시알림을 만들어서 띄워주는 클래스 입니다.
Api를 통해 푸시 알림을 전송하면 입력한 내용이 message에 담겨서 오게 됩니다.
*/
 import android.Manifest;
 import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
 import android.content.pm.PackageManager;
 import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
 import android.os.Vibrator;
 import android.support.v4.app.ActivityCompat;
 import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.RemoteMessage;
 import com.sumus.onepercent.MainActivity;
 import com.sumus.onepercent.MoreActivity.PushActivity;
 import com.sumus.onepercent.Object.MySharedPreference;
 import com.sumus.onepercent.R;
 import com.sumus.onepercent.SQLite.SettingObject;

 import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Map;
 import java.util.Set;


public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {
    private static final String TAG = "FirebaseMsgService";
    private boolean m_isVibrator = true;

    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Map<String, String> data = remoteMessage.getData();
        String title = data.get("title");
        String msg = data.get("message");
        String message = null;
        try {
            message = URLDecoder.decode(msg, "euc-kr");
            Log.d("SUN","title : " + title + " / msg : "+ message);
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.d("SUN","push " + ex.getMessage().toString());
        }

        String push = ((MainActivity)MainActivity.mContext).pref.getPreferences("fcm","push");
        String pushvibe = ((MainActivity)MainActivity.mContext).pref.getPreferences("fcm","vibe");
        Log.d("SUN","service *** push : "+ push + " pushvibe : "+ pushvibe);

        if( push.equals("yes")) {
                sendPushNotification(message);
                PowerManager pm = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
                PowerManager.WakeLock wakelock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG");
                wakelock.acquire(5000);

            if(!pushvibe.equals("yes")){
                Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                long[] pattern = {1, 1};
                vibe.vibrate(pattern, -1);
            }
        }
    }

    private void sendPushNotification(String message) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,  PendingIntent.FLAG_ONE_SHOT);


        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.app_icon).setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.app_icon))
                .setContentTitle("1PERCENT")
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri).setLights(000000255, 500, 2000)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());

    }

}
