package pe.tohure.mynotificationwear;

import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.RemoteInput;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    public static final int NOTIFICATION_ID = 1;
    public static final String EXTRA_VOICE_REPLY = "extra_voice_reply";
    public static final String EXTRA_VOICE_REPLY_CHOICE = "extra_voice_reply_choice";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void sendNotification(View view) {

        //Standar Intent
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://developer.android.com/reference/android/app/Notification.html"));
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.drawable.ic_bike_notification);
        builder.setAutoCancel(true);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        builder.setContentTitle("Wearables Notifications");
        builder.setContentText("Time to learn about notifications!");
        builder.setSubText("Tap to view some features");
        builder.setContentIntent(pendingIntent);

        //Intent (action) para Lanzar sólo en el Wear
        Intent actionIntent = new Intent(this, SecondActivity.class);
        PendingIntent actionPendinIntent = PendingIntent.getActivity(this, 0, actionIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Action action = new NotificationCompat.Action.Builder(R.drawable.ic_bike_notification, "Lanza Segunda Actividad", actionPendinIntent).build();


        //Voice Reply
        //Este titulo aparecerá antes de enviar la respuesta
        String replyLabel = getResources().getString(R.string.reply_label);
        RemoteInput remoteInput = new RemoteInput.Builder(EXTRA_VOICE_REPLY).setLabel(replyLabel).build();
        Intent replyIntent = new Intent(this, ReplyActivity.class);
        PendingIntent replyPendingIntent = PendingIntent.getActivity(this, 0, replyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Action actionVoice = new NotificationCompat.Action.Builder(R.drawable.ic_reply_voice,
                "Respuesta Simple",replyPendingIntent)
                .addRemoteInput(remoteInput)
                .build();

        //Voice Reply Choice
        //Este titulo aparecerá antes de enviar la respuesta
        String replyLabelChoice = getResources().getString(R.string.reply_label_choice);
        String[] replyChoices = getResources().getStringArray(R.array.reply_choices);
        RemoteInput remoteInputchoice = new RemoteInput.Builder(EXTRA_VOICE_REPLY_CHOICE)
                .setLabel(replyLabelChoice)
                .setChoices(replyChoices)
                .build();
        Intent replyIntentchoice = new Intent(this, ReplyChoiceActivity.class);
        PendingIntent replyPendingIntentchoice = PendingIntent.getActivity(this, 0, replyIntentchoice, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Action actionVoiceChoice = new NotificationCompat.Action.Builder(R.drawable.ic_reply_choice,
                "Respuesta de elección",replyPendingIntentchoice)
                .addRemoteInput(remoteInputchoice)
                .build();

        //Background sólo para el wearable, recomendable 400x400 o 600x400
        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.lorem_cat);

        //Agregando Acciones al wearable
        NotificationCompat.WearableExtender wearableExtender = new NotificationCompat.WearableExtender()
                //Hide default Icon true/false
                .setHintHideIcon(false)
                //Set BackGround
                .setBackground(icon)
                //Set Wearable actions
                .addAction(action)
                .addAction(actionVoice)
                .addAction(actionVoiceChoice);
        builder.extend(wearableExtender);

        //Action sólo en wearable
        //builder.extend(new NotificationCompat.WearableExtender().addAction(action));

        //Intent Action común tanto para el Wear como el móvil
        Intent mapIntent = new Intent(Intent.ACTION_VIEW);
        Uri geoUri = Uri.parse("geo:0,0?q=" + Uri.encode("Chiclayo"));
        mapIntent.setData(geoUri);
        PendingIntent mapPendIntent = PendingIntent.getActivity(this, 0, mapIntent, 0);

        //Action tanto para wearable y móvil (siempre que wearable no tenga predefinida una accion
        builder.addAction(R.drawable.ic_map_noti, "Ir mapa", mapPendIntent);

        //Texto de tipo largo tanto para teléfono y wearable
        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
        bigTextStyle.bigText("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum");
        builder.setStyle(bigTextStyle);

        //Lanzamiento de Notificacion
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
        notificationManagerCompat.notify(NOTIFICATION_ID, builder.build());
    }
}
