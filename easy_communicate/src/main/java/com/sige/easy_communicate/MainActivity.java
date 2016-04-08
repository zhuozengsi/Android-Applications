package com.sige.easy_communicate;

import android.app.AlertDialog;
import android.app.Service;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.MovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.sunflower.FlowerCollector;

public class MainActivity extends AppCompatActivity {

    private String TAG = "MainActivity";
    private SpeechSynthesizer mSpeechSynthesizer;
    private ClipboardManager mClipboardManager;
    //to display the content of clipboard.
    private TextView mClipboardTextView;
    private Intent mServiceIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        init();
    }

    private void init(){

        SpeechUtility.createUtility(this, "appid=5703839c,lib_name=libmsc");
        if(!SpeechUtility.getUtility().checkServiceInstalled()){
            //if we do not have installed the dependency app.
            final String str ="http://pan.baidu.com/s/1dE5Z4wp";
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.dialog_title);
            builder.setMessage(R.string.diolog_message);
            builder.setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    Uri uri = Uri.parse(str);
                    intent.setData(uri);
                    startActivity(intent);

                }
            });

            //make the dialog appear until its dependency software has been installed.
            builder.setCancelable(false);

            builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });

            builder.create().show();
        }

        mSpeechSynthesizer = SpeechSynthesizer.createSynthesizer(this,null);
        mSpeechSynthesizer.setParameter(SpeechConstant.VOICE_NAME, "xiaoyan");

        mClipboardTextView = (TextView)findViewById(R.id.clipboard_textView);
        //Make the content of the TextView scrolled.
        mClipboardTextView.setMovementMethod(ScrollingMovementMethod.getInstance());
        //get the clipboard service.
        mClipboardManager = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
        //when the clipboard updates, some methods of the listener will be invoked.
        mClipboardManager.addPrimaryClipChangedListener(listener);

        mServiceIntent = new Intent(this, SpeechSynthesizerService.class);

    }

    public void bindService(View view){
        //start the SpeechSynthesizerService.
        startService(mServiceIntent);
        mSpeechSynthesizer.startSpeaking(getResources().getString(R.string.start_speech),mSynListener);

    }

    public void unbindService(View view){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.quit_dialog_title);
        builder.setMessage(R.string.quit_dialog_message);
        builder.setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mSpeechSynthesizer.startSpeaking(getResources().getString(R.string.stop_speech), mSynListener);
                stopService(mServiceIntent);
            }
        });

        //make the dialog appear until its dependency software has been installed.
        builder.setCancelable(true);
        builder.setNegativeButton(R.string.cancel, null);
        builder.create().show();
        mSpeechSynthesizer.startSpeaking(getResources().getString(R.string.quit_dialog_message),mSynListener);

    }

    @Override
    protected void onResume(){
        super.onResume();

    }

    @Override
    protected void onPause(){
        super.onPause();
        Log.i(TAG, "onPause()");
    }

    @Override
    protected void onStop(){
        //mClipboardManager.removePrimaryClipChangedListener(listener);
        super.onStop();
        Log.i(TAG, "onStop()");
    }

    @Override
    protected void onDestroy(){
        mSpeechSynthesizer.stopSpeaking();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private ClipboardManager.OnPrimaryClipChangedListener listener = new ClipboardManager.OnPrimaryClipChangedListener() {
        @Override
        public void onPrimaryClipChanged() {
            if(mClipboardTextView!= null){
                CharSequence content = mClipboardManager.getPrimaryClip().getItemAt(0).getText();
                if (content!= null)
                    mClipboardTextView.setText(content);
            }
        }
    };

    private SynthesizerListener mSynListener = new SynthesizerListener() {

        @Override
        public void onSpeakBegin() {

        }

        @Override
        public void onBufferProgress(int i, int i1, int i2, String s) {

        }

        @Override
        public void onSpeakPaused() {

        }

        @Override
        public void onSpeakResumed() {

        }

        @Override
        public void onSpeakProgress(int i, int i1, int i2) {

        }

        @Override
        public void onCompleted(SpeechError speechError) {

        }

        @Override
        public void onEvent(int i, int i1, int i2, Bundle bundle) {

        }
    };


}
