package com.example.yujopet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.speech.tts.Voice;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Toast;


import com.google.ar.core.Anchor;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

import android.os.Bundle;
import android.widget.ToggleButton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Locale;


import org.alicebot.ab.AIMLProcessor;
import org.alicebot.ab.Bot;
import org.alicebot.ab.Chat;
import org.alicebot.ab.Graphmaster;
import org.alicebot.ab.MagicBooleans;
import org.alicebot.ab.MagicStrings;
import org.alicebot.ab.PCAIMLProcessorExtension;

public class MainActivity extends AppCompatActivity implements RecognitionListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final double MIN_OPENGL_VERSION = 3.0;

    int mBackgroundIndex = 0; //Buddha is default

    //Adding speech capabilities
    public Bot bot;
    public static Chat chat;
    private TextToSpeech tts;
    private Voice voice;
    private SpeechRecognizer speech = null;
    private Intent recognizerIntent;
    private String LOG_TAG = "VoiceRecognitionActivity";
    private static final int REQUEST_RECORD_PERMISSION = 45;

    Button mShowARButton,mUpButton, mDownButton,mRestartButton;
    ToggleButton mButtonSend;
    ArFragment arFragment;

    ModelRenderable virtualPetRenderable,virtualPetRenderable2,virtualPetRenderable3;

    //@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!checkIsSupportedDeviceOrFinish(this)) {
            return;
        }
        setContentView(R.layout.activity_main);


        String [] faces = new String[]{"rijksmuseum_buddha_head.sfb", "CMA-amenhotep-iii.sfb", "plato.sfb"};


        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.ux_fragment);
        mButtonSend = (ToggleButton) findViewById(R.id.talkButton);
        mShowARButton = (Button) findViewById(R.id.buttonDisplay);
        mDownButton = (Button) findViewById(R.id.buttonDown);
        mUpButton = (Button) findViewById(R.id.buttonUp);
        mRestartButton = (Button) findViewById(R.id.restartButton);
        mShowARButton.setEnabled(false);

        //Text to speech
        speech = SpeechRecognizer.createSpeechRecognizer(this);
        Log.i(LOG_TAG, "isRecognitionAvailable: " + SpeechRecognizer.isRecognitionAvailable(this));

        speech.setRecognitionListener(this);
        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE,
                "en");
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3);


        mButtonSend.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                ActivityCompat.requestPermissions
                        (MainActivity.this,
                                new String[]{Manifest.permission.RECORD_AUDIO},
                                REQUEST_RECORD_PERMISSION);
            }
        });

        mButtonSend.setEnabled(false);

        mDownButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBackgroundIndex--;
                if (mBackgroundIndex < 0) {
                    mBackgroundIndex = 0;
                }
                switch(mBackgroundIndex){
                    case 0:
                        mShowARButton.setText(R.string.india);
                        mShowARButton.setBackgroundResource(R.drawable.buddha);
                        mBackgroundIndex =0;
                        break;
                    case 1:
                        mShowARButton.setText(R.string.egypt);
                        mShowARButton.setBackgroundResource(R.drawable.egypt);
                        mBackgroundIndex =1;
                        break;
                    case 2:
                        mShowARButton.setText(R.string.greece);
                        mShowARButton.setBackgroundResource(R.drawable.greece);
                        mBackgroundIndex =2;
                        break;
                    default:
                        break;
                }
            }
        });

        mUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBackgroundIndex++;
                if (mBackgroundIndex > 2) {
                    mBackgroundIndex = 2;
                }

                switch(mBackgroundIndex){
                    case 0:
                mShowARButton.setText(R.string.india);
                        mShowARButton.setBackgroundResource(R.drawable.buddha);
                        mBackgroundIndex =0;
                        break;
                    case 1:
                        mShowARButton.setText(R.string.egypt);
                        mShowARButton.setBackgroundResource(R.drawable.egypt);
                        mBackgroundIndex =1;
                        break;
                    case 2:
                        mShowARButton.setText(R.string.greece);
                        mShowARButton.setBackgroundResource(R.drawable.greece);
                        mBackgroundIndex =2;
                        break;
                default:
                    break;
                }
            }
        });


        mRestartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent restartIntent = getBaseContext().getPackageManager()
                        .getLaunchIntentForPackage(getBaseContext().getPackageName());
                restartIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(restartIntent);
            }
        });


        mShowARButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Log.e("Hello World", faces[mBackgroundIndex] +" " +mBackgroundIndex);
            }
        });




        setupModel();

        arFragment.setOnTapArPlaneListener(
                (HitResult hitresult, Plane plane, MotionEvent motionevent) -> {
                    if (virtualPetRenderable == null) {
                        return;
                    }
                    Anchor anchor = hitresult.createAnchor();
                    AnchorNode anchorNode = new AnchorNode(anchor);
                    anchorNode.setParent(arFragment.getArSceneView().getScene());
                    anchorNode.setWorldPosition(new Vector3(0.4f,0.1f,1.4f));

                    createModel(anchorNode, mBackgroundIndex);
                    mUpButton.setEnabled(false);
                    mDownButton.setEnabled(false);
                    mButtonSend.setEnabled(true);

                });


        // Initialize obj for Speech Output
        tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            // Verification if feature is supported
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    voice = new Voice("en-us-x-sfg#male_1-local",
                            Locale.getDefault(), 1, 1, false, null);
                    tts.setVoice(voice);
                }
            }

        });

        //Checking SD card availablility
        boolean a = isSDCARDAvailable();
        //Receiving the assets from the app directory
        AssetManager assets = getResources().getAssets();
        File jayDir = new File(Environment.getExternalStorageDirectory().toString() + "/hari/bots/Hari");
        boolean b = jayDir.mkdirs();
        if (jayDir.exists()) {
            //Reading the file
            try {
                for (String dir : assets.list("Hari")) {
                    File subdir = new File(jayDir.getPath() + "/" + dir);
                    boolean subdir_check = subdir.mkdirs();
                    for (String file : assets.list("Hari/" + dir)) {
                        File f = new File(jayDir.getPath() + "/" + dir + "/" + file);
                        if (f.exists()) {
                            continue;
                        }
                        InputStream in = null;
                        OutputStream out = null;
                        in = assets.open("Hari/" + dir + "/" + file);
                        out = new FileOutputStream(jayDir.getPath() + "/" + dir + "/" + file);
                        //Copy file from assets to the mobile's SD card or any secondary memory
                        copyFile(in, out);
                        in.close();
                        in = null;
                        out.flush();
                        out.close();
                        out = null;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //Get the working directory where assets are stored
        MagicStrings.root_path = Environment.getExternalStorageDirectory().toString() + "/hari";
        System.out.println("Working Directory = " + MagicStrings.root_path);
        AIMLProcessor.extension =  new PCAIMLProcessorExtension();
        //Initialize user request and bot response
        bot = new Bot("hari", MagicStrings.root_path, "chat");
        chat = new Chat(bot);
        String[] args = null;
        mainFunction(args);

    }




    public void createModel(AnchorNode anchorNode, int mBackgroundIndex){
            if(mBackgroundIndex == 0){
                TransformableNode buddha = new TransformableNode(arFragment.getTransformationSystem());
                buddha.setParent(anchorNode);

                buddha.setLocalScale(new Vector3(0.1f,0.1f,0.1f));

                //Remove moving and pinching from sculpture
                buddha.getScaleController().setSensitivity(0);
                buddha.getTranslationController().setEnabled(false);

                buddha.getScaleController().setMinScale(0.3f);

                buddha.setLocalPosition(new Vector3(0.1f,0.9f,0.1f));

                buddha.setRenderable(virtualPetRenderable);
                buddha.select();
            }

            if(mBackgroundIndex == 1){
            TransformableNode pharaoh = new TransformableNode(arFragment.getTransformationSystem());
            pharaoh.setParent(anchorNode);

            pharaoh.setLocalScale(new Vector3(0.1f,0.1f,0.1f));

            //Remove moving and pinching from sculpture
            pharaoh.getScaleController().setSensitivity(0);
            pharaoh.getTranslationController().setEnabled(false);
            pharaoh.getScaleController().setMinScale(0.3f);

            pharaoh.setLocalPosition(new Vector3(0.1f,0.9f,0.1f));

            pharaoh.setRenderable(virtualPetRenderable2);
            pharaoh.select();
        }


            if(mBackgroundIndex == 2){
            TransformableNode plato = new TransformableNode(arFragment.getTransformationSystem());
            plato.setParent(anchorNode);

            //Remove moving and pinching from sculpture
            plato.getScaleController().setSensitivity(0);


            plato.getTranslationController().setEnabled(false);
            plato.getScaleController().setMinScale(0.3f);

            plato.setLocalPosition(new Vector3(0.1f,0.9f,0.1f));

            plato.setRenderable(virtualPetRenderable3);
            plato.select();
        }

    }

    public void setupModel(){
        ModelRenderable.builder()
                .setSource(this, Uri.parse("rijksmuseum_buddha_head.sfb"))
                .build()
                .thenAccept(renderable -> virtualPetRenderable = renderable)
                .exceptionally(throwable -> {
                    Toast toast = Toast.makeText(this, "Unable to load andy renderable", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    return null;
                });

        ModelRenderable.builder()
                .setSource(this, Uri.parse("CMA-amenhotep-iii.sfb"))
                .build()
                .thenAccept(renderable -> virtualPetRenderable2 = renderable)
                .exceptionally(throwable -> {
                    Toast toast = Toast.makeText(this, "Unable to load andy renderable", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    return null;
                });

        ModelRenderable.builder()
                .setSource(this, Uri.parse("plato.sfb"))
                .build()
                .thenAccept(renderable -> virtualPetRenderable3 = renderable)
                .exceptionally(throwable -> {
                    Toast toast = Toast.makeText(this, "Unable to load andy renderable", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    return null;
                });
    }

    public static boolean checkIsSupportedDeviceOrFinish(final Activity activity) {
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            Log.e(TAG, "Sceneform requires Android N or later");
            Toast.makeText(activity, "Sceneform requires Android N or later", Toast.LENGTH_LONG).show();
            activity.finish();
            return false;    }
        String openGlVersionString = ((ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE))
                .getDeviceConfigurationInfo()
                .getGlEsVersion();

        if(Double.parseDouble(openGlVersionString) < MIN_OPENGL_VERSION) {
            Log.e(TAG, "Sceneform requires OpenGL ES 3.0 later");
            Toast.makeText(activity, "Sceneform requires OpenGL ES 3.0 or later", Toast.LENGTH_LONG).show();
            activity.finish();
            return false;
        }
        return true;
    }


    //Check SD card availability
    public static boolean isSDCARDAvailable(){
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)? true :false;
    }
    //Copying the file
    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while((read = in.read(buffer)) != -1){
            out.write(buffer, 0, read);
        }
    }

    // Handles Speech Output to User
    void outputResponse(String response) {
        int speechStatus = tts.speak(response, TextToSpeech.QUEUE_FLUSH, null);

        if (speechStatus == TextToSpeech.ERROR) {
            Log.e("TTS", "Error in converting Text to Speech!");
        }
    }

    // Processes input data and obtain response from AI
    public void processData(ArrayList<String> processingData) {
        String message = processingData.get(0);
        //bot
        String response = chat.multisentenceRespond(message);
        if (TextUtils.isEmpty(message)) {
            return;
        }
        outputResponse(response);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_RECORD_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    speech.startListening(recognizerIntent);
                } else {
                    Toast.makeText(MainActivity.this, "Permission Denied!", Toast
                            .LENGTH_SHORT).show();
                }
        }
    }

    public static void mainFunction(String[] args) {
        MagicBooleans.trace_mode = false;
        System.out.println("trace mode = " + MagicBooleans.trace_mode);
        Graphmaster.enableShortCuts = true;
    }

    @Override
    public void onResume() {
        super.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
    }
    @Override
    protected void onStop() {
        super.onStop();
        if (speech != null) {
            speech.destroy();
            Log.i(LOG_TAG, "destroy");
        }
    }
    @Override
    public void onBeginningOfSpeech() {
        Log.i(LOG_TAG, "onBeginningOfSpeech");
    }
    @Override
    public void onBufferReceived(byte[] buffer) {
        Log.i(LOG_TAG, "onBufferReceived: " + buffer);
    }
    @Override
    public void onEndOfSpeech() {
        Log.i(LOG_TAG, "onEndOfSpeech");

    }
    @Override
    public void onError(int errorCode) {
        String errorMessage = getErrorText(errorCode);
        Log.d(LOG_TAG, "FAILED " + errorMessage);
    }
    @Override
    public void onEvent(int arg0, Bundle arg1) {
        Log.i(LOG_TAG, "onEvent");
    }
    @Override
    public void onPartialResults(Bundle arg0) {
        Log.i(LOG_TAG, "onPartialResults");
    }
    @Override
    public void onReadyForSpeech(Bundle arg0) {
        Log.i(LOG_TAG, "onReadyForSpeech");
    }
    @Override
    public void onResults(Bundle results) {
        Log.i(LOG_TAG, "onResults");
        ArrayList<String> matches = results
                .getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        processData(matches);
    }
    @Override
    public void onRmsChanged(float rmsdB) {
        Log.i(LOG_TAG, "onRmsChanged: " + rmsdB);
    }
    public static String getErrorText(int errorCode) {
        String message;
        switch (errorCode) {
            case SpeechRecognizer.ERROR_AUDIO:
                message = "Audio recording error";
                break;
            case SpeechRecognizer.ERROR_CLIENT:
                message = "Client side error";
                break;
            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                message = "Insufficient permissions";
                break;
            case SpeechRecognizer.ERROR_NETWORK:
                message = "Network error";
                break;
            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                message = "Network timeout";
                break;
            case SpeechRecognizer.ERROR_NO_MATCH:
                message = "No match";
                break;
            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                message = "RecognitionService busy";
                break;
            case SpeechRecognizer.ERROR_SERVER:
                message = "error from server";
                break;
            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                message = "No speech input";
                break;
            default:
                message = "Didn't understand, please try again.";
                break;
        }
        return message;
    }
}






