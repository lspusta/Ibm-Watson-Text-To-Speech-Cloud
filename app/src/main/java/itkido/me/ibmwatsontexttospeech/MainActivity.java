package itkido.me.ibmwatsontexttospeech;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.ibm.cloud.sdk.core.http.HttpMediaType;
import com.ibm.cloud.sdk.core.security.IamAuthenticator;
import com.ibm.watson.developer_cloud.android.library.audio.StreamPlayer;
import com.ibm.watson.text_to_speech.v1.TextToSpeech;
import com.ibm.watson.text_to_speech.v1.model.SynthesizeOptions;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private TextToSpeech textToSpeech;
    StreamPlayer streamPlayer = new StreamPlayer();
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


        btnConvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "Loading", Snackbar.LENGTH_LONG).show();
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
                    .voice(SynthesizeOptions.Voice.EN_US_MICHAELV3VOICE)
                    .accept(HttpMediaType.AUDIO_WAV)
                    .build()).execute().getResult());
            return "Did synthesize";
        }
    }

}
