package itkido.me.ibmwatsontexttospeech;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ibm.cloud.sdk.core.http.HttpMediaType;
import com.ibm.cloud.sdk.core.http.Response;
import com.ibm.cloud.sdk.core.http.ServiceCall;
import com.ibm.cloud.sdk.core.security.IamAuthenticator;
import com.ibm.watson.assistant.v2.Assistant;
import com.ibm.watson.assistant.v2.model.CreateSessionOptions;
import com.ibm.watson.assistant.v2.model.DialogNodeOutputOptionsElement;
import com.ibm.watson.assistant.v2.model.MessageInput;
import com.ibm.watson.assistant.v2.model.MessageOptions;
import com.ibm.watson.assistant.v2.model.MessageResponse;
import com.ibm.watson.assistant.v2.model.RuntimeResponseGeneric;
import com.ibm.watson.assistant.v2.model.SessionResponse;
import com.ibm.watson.developer_cloud.android.library.audio.MicrophoneHelper;
import com.ibm.watson.developer_cloud.android.library.audio.MicrophoneInputStream;
import com.ibm.watson.developer_cloud.android.library.audio.StreamPlayer;
import com.ibm.watson.text_to_speech.v1.TextToSpeech;
import com.ibm.watson.text_to_speech.v1.model.SynthesizeOptions;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private TextToSpeech textToSpeech;
    StreamPlayer streamPlayer = new StreamPlayer();
    private boolean initialRequest;
    private boolean permissionToRecordAccepted = false;
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private static final int RECORD_REQUEST_CODE = 101;
    private boolean listening = false;
    private MicrophoneInputStream capture;
    private MicrophoneHelper microphoneHelper;

    TextView txtYourMessage;
    EditText editText;
    Button btnConvert;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        txtYourMessage = findViewById(R.id.txtYourMessage);
        editText = findViewById(R.id.editText);
        btnConvert = findViewById(R.id.btnConvert);
        microphoneHelper = new MicrophoneHelper(this);


        btnConvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                convertTextToSpeech();
            }
        });

        createServices();


    }

    private void createServices(){
        textToSpeech = new TextToSpeech(new IamAuthenticator((getApplicationContext().getString(R.string.TTS_apikey))));
        textToSpeech.setServiceUrl(getApplicationContext().getString(R.string.TTS_url));

    }


    private void convertTextToSpeech(){
        String response;
        String yourmessage = editText.getText().toString();



        new SayTask().execute(yourmessage);

        txtYourMessage.setText("Your message = " + yourmessage);

    }

    private class SayTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            streamPlayer.playStream(textToSpeech.synthesize(new SynthesizeOptions.Builder()
                    .text(params[0])
                    .voice(SynthesizeOptions.Voice.EN_US_LISAVOICE)
                    .accept(HttpMediaType.AUDIO_WAV)
                    .build()).execute().getResult());
            return "Did synthesize";
        }
    }

}
