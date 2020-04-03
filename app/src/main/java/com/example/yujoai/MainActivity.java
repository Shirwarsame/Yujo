package com.example.yujoai;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{

    // Will contain output speech of User
    private TextView tvxResult;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvxResult = (TextView) findViewById(R.id.txvResult);

    }

    //Gets what User said
    public void getSpeechInput(View view)
    {
        // Request user to speak and pass through a Recognizer
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);


        // Takes user input as free form
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

        if(intent.resolveActivity(getPackageManager()) != null)
        {
            startActivityForResult(intent, 10);
        }
        else
        {
            Toast.makeText(this, "Speech is not supported", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode)
        {
            case 10:
                if(resultCode == RESULT_OK && data != null)
                {
                   ArrayList<String> results =  data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                }
        }
    }
}
