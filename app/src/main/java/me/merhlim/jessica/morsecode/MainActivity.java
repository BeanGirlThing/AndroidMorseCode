package me.merhlim.jessica.morsecode;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;
    private static final int CAMERA_REQUEST = 50;
    private boolean flashLightStatus = false;

    public void dialogue(String title, String text, String button) {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
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

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction transaction = fm.beginTransaction();
            Fragment selectedFragment = null;
            switch (item.getItemId()) {
                case R.id.navigation_tomorse:
                    selectedFragment = texttomorse.newInstance();
                    Log.i("MORSE","Switched to the tomorse fragment");
                    FragmentTransaction morsefrag = getSupportFragmentManager().beginTransaction();
                    morsefrag.replace(R.id.frame_layout, selectedFragment);
                    morsefrag.commit();
                    return true;
                case R.id.navigation_info:
                    selectedFragment = info.newInstance();
                    Log.i("MORSE","Switched to the info fragment");
                    FragmentTransaction infofrag = getSupportFragmentManager().beginTransaction();
                    infofrag.replace(R.id.frame_layout, selectedFragment);
                    infofrag.commit();
                    return true;
                case R.id.navigation_settings:
                    selectedFragment = Settings.newInstance();
                    Log.i("MORSE","Switched to the settings fragment");
                    FragmentTransaction settfrag = getSupportFragmentManager().beginTransaction();
                    settfrag.replace(R.id.frame_layout, selectedFragment);
                    settfrag.commit();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, texttomorse.newInstance());
        transaction.commit();

        if (!this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
            this.dialogue("No flashlight","There is no flashlight hardware on this device","OK");
        }

        requestPerms();

    }

    private void requestPerms(){
        ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.CAMERA}, CAMERA_REQUEST);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode) {
            case CAMERA_REQUEST :
                if (grantResults.length > 0  &&  grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
                    mBuilder.setTitle("Camera Permission Required");
                    mBuilder.setMessage("Android classes 'flashlight' as part of the camera permission, please accept to continue");
                    mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            requestPerms();
                        }
                    });
                    mBuilder.setNeutralButton("More Information", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Uri uri = Uri.parse("googlechrome://navigate?url=https://developer.android.com/guide/topics/media/camera");
                            Intent i = new Intent(Intent.ACTION_VIEW, uri);
                            if (i.resolveActivity(getPackageManager()) == null) {
                                i.setData(Uri.parse("https://developer.android.com/guide/topics/media/camera"));
                            }
                            startActivity(i);
                            requestPerms();
                        }
                    });

                    AlertDialog start = mBuilder.create();
                    start.show();
                }
                break;
        }
    }

}
