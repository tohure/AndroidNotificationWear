package pe.tohure.mynotificationwear;

import android.content.Intent;
import android.support.v4.app.RemoteInput;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ReplyActivity extends AppCompatActivity {

    private CharSequence messageVoice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply);
        messageVoice = getMessageText(getIntent());
        init();
    }

    private void init() {
        TextView lblResponse = (TextView) findViewById(R.id.lblResponse);
        lblResponse.setText(messageVoice);
    }

    private CharSequence getMessageText(Intent intent) {
        Bundle remoteInput = RemoteInput.getResultsFromIntent(intent);
        if (remoteInput != null){
            return remoteInput.getCharSequence(MainActivity.EXTRA_VOICE_REPLY);
        }
        return "";
    }


}
