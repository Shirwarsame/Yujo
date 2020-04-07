package com.example.yujoai;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import java.util.Locale;
import android.speech.tts.TextToSpeech;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    // Will contain output speech of User
    private TextView tvxResult;
    //Contains text to convert
    private TextToSpeech textToSpeech;
    private Boolean stillTalking = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvxResult = (TextView) findViewById(R.id.txvResult);

        // Initialize obj to contain text we want Yujo to talk back
        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            // Verification if feature is supported
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int ttsLang = textToSpeech.setLanguage(Locale.US);

                    if (ttsLang == TextToSpeech.LANG_MISSING_DATA
                            || ttsLang == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "The Language is not supported!");
                    } else {
                        Log.i("TTS", "Language Supported.");
                    }
                    Log.i("TTS", "Initialization success.");
                } else {
                    Toast.makeText(getApplicationContext(), "TTS Initialization failed!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    //Gets what User said
    public void getSpeechInput(View view) {
        // Request user to speak and pass through a Recognizer
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        // Takes user input as free form
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

        // Verify if taking input speech is supported
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, 10);
        } else {
            Toast.makeText(this, "Speech is not supported", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 10:
                // Extracting data from Recognizer and adding into Results
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    // After data is extracted move to processing
                    thinkAboutResponse(results);
                }
        }
    }

    // Handles Response processing
     void thinkAboutResponse(ArrayList<String> newResults) {
         String response;
         for (int i = 0; i < newResults.size(); i++) {
             if (newResults.get(i).contains("sad")) {
                 response = "Why do you feel that way?";
                 tvxResult.setText(newResults.get(i));
             }
             if (newResults.get(i).contains("happy")) {
                 response = "Really what happened?";
                 tvxResult.setText(newResults.get(i));
             }
             if (newResults.get(i).contains("confused")) {
                 response = "Think about it for a little while";
                 tvxResult.setText(newResults.get(i));
             }
             else {
                 response = "I didnt get that";
                 tvxResult.setText(newResults.get(i));
             }
             outputResponse(response);
         }
    }

    // Handles Response Output to User
    void outputResponse(String response) {
        int speechStatus = textToSpeech.speak(response, TextToSpeech.QUEUE_FLUSH, null);

        if (speechStatus == TextToSpeech.ERROR) {
            Log.e("TTS", "Error in converting Text to Speech!");
        }
    }
}



