package com.example.yujopet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
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

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final double MIN_OPENGL_VERSION = 3.0;

    int mBackgroundIndex = 0; //Buddha is default

    Button mShowARButton,mUpButton, mDownButton,mRestartButton;
    ArFragment arFragment;

    ModelRenderable virtualPetRenderable,virtualPetRenderable2,virtualPetRenderable3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!checkIsSupportedDeviceOrFinish(this)) {
            return;
        }
        setContentView(R.layout.activity_main);

        String [] faces = new String[]{"rijksmuseum_buddha_head.sfb", "CMA-amenhotep-iii.sfb", "plato.sfb"};


        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.ux_fragment);
        mShowARButton = (Button) findViewById(R.id.buttonDisplay);
        mDownButton = (Button) findViewById(R.id.buttonDown);
        mUpButton = (Button) findViewById(R.id.buttonUp);
        mRestartButton = (Button) findViewById(R.id.restartButton);
        mShowARButton.setEnabled(false);

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

                });




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



}






