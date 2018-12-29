package me.merhlim.jessica.morsecode;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import static android.content.Context.CAMERA_SERVICE;

public class texttomorse extends Fragment {
    public static texttomorse newInstance() {
        texttomorse fragment = new texttomorse();
        return fragment;
    }
    public void dialogue(String title, String text, String button) {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getContext());
        mBuilder.setTitle(title);
        mBuilder.setMessage(text);
        mBuilder.setPositiveButton(button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog start = mBuilder.create();
        start.show();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_texttomorse, container, false);


        ImageButton fLightOn = (ImageButton) v.findViewById(R.id.fLightOn);
        fLightOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flashLightOn();
            }
        });

        ImageButton fLightOff = (ImageButton) v.findViewById(R.id.fLightOff);
        fLightOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flashLightOff();
            }
        });

        final Switch twoFive = (Switch) v.findViewById(R.id.UnitSpeed1);
        final Switch fiveZero = (Switch) v.findViewById(R.id.UnitSpeed2);
        final Switch sevenFive = (Switch) v.findViewById(R.id.UnitSpeed3);
        twoFive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fiveZero.setChecked(false);
                sevenFive.setChecked(false);
            }
        });
        fiveZero.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                twoFive.setChecked(false);
                sevenFive.setChecked(false);
            }
        });
        sevenFive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                twoFive.setChecked(false);
                fiveZero.setChecked(false);
            }
        });
        ImageButton sos = (ImageButton) v.findViewById(R.id.emergency);
        sos.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String sos = "...@---@...!";
                String humanSOS = "... --- ...";
                playLightSequence(sos,500,0);

            }
        });
        ImageButton playSequence = (ImageButton) v.findViewById(R.id.playSequence);
        final EditText inputField = (EditText) v.findViewById(R.id.editText);
        final TextView outputField = (TextView) v.findViewById(R.id.MorseOutput);
        outputField.setMovementMethod(new ScrollingMovementMethod());
        outputField.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ClipboardManager cManager = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData cData = ClipData.newPlainText("text", outputField.getText());
                cManager.setPrimaryClip(cData);
                Toast.makeText(getContext(), "Copied", Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        playSequence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputtedText = inputField.getText().toString();
                long unitSpeed;
                if(twoFive.isChecked()) {
                    unitSpeed = 250;
                } else if (fiveZero.isChecked()) {
                    unitSpeed = 500;
                } else if(sevenFive.isChecked()) {
                    unitSpeed = 750;
                } else {
                    dialogue("Unit speed error","Unit speed hasn't been selected","ok");
                    unitSpeed = 0;
                }
                if (unitSpeed != 0){
                    String[] morseProcessed = MorseProcessing.stringToMorseSequence(inputtedText);
                    outputField.setText(morseProcessed[1]);
                    playLightSequence(morseProcessed[0],unitSpeed,0);

                }

            }
        });

        return v;
    }

    public void playLightSequence(String morseSequence, long unitLength, int position){
        Handler handler = new Handler(Looper.getMainLooper());
        String focus = String.valueOf(morseSequence.charAt(position));
        String nextCharacter;
        if (!focus.equals("!")) {
            nextCharacter = String.valueOf(morseSequence.charAt(position+1));
        } else {
            nextCharacter = "END";
        }
        if(focus.equals(".")){
            flashLightOn();
            handler.postDelayed(new Runnable() {
                String morseSequence;
                String nextCharacter;
                long unitLength;
                int position;
                String focus;
                public void run() {
                    flashLightOff();
                    if (!(nextCharacter.equals("#") || nextCharacter.equals("@") || focus.equals("#") || focus.equals("@"))){
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.postDelayed(new Runnable() {
                            String morseSequence;
                            long unitLength;
                            int position;
                            public void run() {
                                playLightSequence(morseSequence,unitLength,position+1);
                            }
                            public Runnable init(String morseSequence, long unitLength, int position) {
                                this.morseSequence=morseSequence;
                                this.unitLength=unitLength;
                                this.position=position;
                                return(this);
                            }
                        }.init(morseSequence,unitLength,position), unitLength);
                    } else {
                        playLightSequence(morseSequence,unitLength,position+1);
                    }

                }
                public Runnable init(String morseSequence, String nextCharacter, long unitLength, int position, String focus) {
                    this.morseSequence=morseSequence;
                    this.nextCharacter=nextCharacter;
                    this.unitLength=unitLength;
                    this.position=position;
                    this.focus=focus;
                    return(this);
                }
            }.init(morseSequence,nextCharacter,unitLength,position,focus), unitLength);
        } else if (focus.equals("-")){
            flashLightOn();
            handler.postDelayed(new Runnable() {
                String morseSequence;
                String nextCharacter;
                long unitLength;
                int position;
                String focus;
                public void run() {
                    flashLightOff();
                    if (!(nextCharacter.equals("#") || nextCharacter.equals("@") || focus.equals("#") || focus.equals("@"))){
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.postDelayed(new Runnable() {
                            String morseSequence;
                            long unitLength;
                            int position;
                            public void run() {
                                playLightSequence(morseSequence,unitLength,position+1);
                            }
                            public Runnable init(String morseSequence, long unitLength, int position) {
                                this.morseSequence=morseSequence;
                                this.unitLength=unitLength;
                                this.position=position;
                                return(this);
                            }
                        }.init(morseSequence,unitLength,position), unitLength);
                    } else {
                        playLightSequence(morseSequence,unitLength,position+1);
                    }
                }
                public Runnable init(String morseSequence, String nextCharacter, long unitLength, int position, String focus) {
                    this.morseSequence=morseSequence;
                    this.nextCharacter=nextCharacter;
                    this.unitLength=unitLength;
                    this.position=position;
                    this.focus=focus;
                    return(this);
                }
            }.init(morseSequence,nextCharacter,unitLength,position,focus), unitLength*3);
        } else if (focus.equals("@")) {
            handler.postDelayed(new Runnable() {
                String morseSequence;
                String nextCharacter;
                long unitLength;
                int position;
                String focus;
                public void run() {
                    playLightSequence(morseSequence,unitLength,position+1);
                }
                public Runnable init(String morseSequence, String nextCharacter, long unitLength, int position, String focus) {
                    this.morseSequence=morseSequence;
                    this.nextCharacter=nextCharacter;
                    this.unitLength=unitLength;
                    this.position=position;
                    this.focus=focus;
                    return(this);
                }
            }.init(morseSequence,nextCharacter,unitLength,position,focus), unitLength*3);
        } else if (focus.equals("#")){
            handler.postDelayed(new Runnable() {
                String morseSequence;
                String nextCharacter;
                long unitLength;
                int position;
                String focus;
                public void run() {
                    playLightSequence(morseSequence,unitLength,position+1);
                }
                public Runnable init(String morseSequence, String nextCharacter, long unitLength, int position, String focus) {
                    this.morseSequence=morseSequence;
                    this.nextCharacter=nextCharacter;
                    this.unitLength=unitLength;
                    this.position=position;
                    this.focus=focus;
                    return(this);
                }
            }.init(morseSequence,nextCharacter,unitLength,position,focus), unitLength*7);
        }


    }

    public void flashLightOn() {
        Context c = getContext();
        CameraManager cameraManager = (CameraManager) c.getSystemService(CameraManager.class);
        try {
            String cameraId = cameraManager.getCameraIdList()[0];
            cameraManager.setTorchMode(cameraId, true);
        } catch (CameraAccessException e) {
        }
    }

    public void flashLightOff() {
        Context c = getContext();
        CameraManager cameraManager = (CameraManager) c.getSystemService(CameraManager.class);
        try {
            String cameraId = cameraManager.getCameraIdList()[0];
            cameraManager.setTorchMode(cameraId, false);
        } catch (CameraAccessException e) {
        }
    }

}
