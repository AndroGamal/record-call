package com.example.andro.myapplication;


import android.app.Service;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.TelephonyManager;
import android.widget.Toast;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;


public class my extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        runCallRecording();
        return null;
    }

    boolean enter = true;
    Handler handler = new Handler();
    String AUDIO_RECORDER_FILE_EXT_3GP = ".3gp";
    String AUDIO_RECORDER_FILE_EXT_MP4 = ".mp4";
    String AUDIO_RECORDER_FOLDER = "AudioRecorder";
    MediaRecorder recorder = null;
    static TelephonyManager telephonyManager;
    int currentFormat = 0;
    int output_formats[] = {MediaRecorder.OutputFormat.MPEG_4,
            MediaRecorder.OutputFormat.THREE_GPP};
    private String file_exts[] = {AUDIO_RECORDER_FILE_EXT_MP4,
            AUDIO_RECORDER_FILE_EXT_3GP};
static private Timer timer;
    public void runCallRecording() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (telephonyManager.getCallState() == TelephonyManager.CALL_STATE_OFFHOOK) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getBaseContext(), "Start Call Recording", Toast.LENGTH_LONG).show();
                        }
                    });
                    enter = false;
                    recorder = new MediaRecorder();
                    recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                    recorder.setOutputFormat(output_formats[currentFormat]);
                    recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                    recorder.setOutputFile(getFilename());
                    recorder.setOnErrorListener(errorListener);
                    recorder.setOnInfoListener(infoListener);
                    try {
                        recorder.prepare();
                        recorder.start();

                    } catch (Exception e) {

                    }
                    while (!enter) {
                        if (telephonyManager.getCallState() != TelephonyManager.CALL_STATE_OFFHOOK) {
                            try {
                                if (null != recorder) {
                                    recorder.stop();
                                    recorder.reset();
                                    recorder.release();
                                    recorder = null;
                                }
                            } catch (Exception stopException) {

                            }
                            enter = true;
                        }
                    }
                }

            }
        }, 0, 1);

      }

    private String getFilename() {
        String filepath = Environment.getExternalStorageDirectory().getPath();
        File file = new File(filepath, AUDIO_RECORDER_FOLDER);

        if (!file.exists()) {
            file.mkdirs();
        }

        return (file.getAbsolutePath() + "/" + System.currentTimeMillis() + file_exts[currentFormat]);
    }

    private MediaRecorder.OnErrorListener errorListener = new MediaRecorder.OnErrorListener() {
        @Override
        public void onError(MediaRecorder mr, int what, int extra) {
            Toast.makeText(my.this,
                    "Error: " + what + ", " + extra, Toast.LENGTH_SHORT).show();
        }
    };

    private MediaRecorder.OnInfoListener infoListener = new MediaRecorder.OnInfoListener() {
        @Override
        public void onInfo(MediaRecorder mr, int what, int extra) {
            Toast.makeText(my.this,
                    "Warning: " + what + ", " + extra, Toast.LENGTH_SHORT)
                    .show();
        }
    };
public static void onclose(){
    try{timer.cancel();}
    catch ( Exception e){
        e.getStackTrace();
    }
    finally {

    }
}
    @Override
    public void onCreate() {
        super.onCreate();
        onclose();
        timer=new Timer();
        telephonyManager = (TelephonyManager) getSystemService(MainActivity.TELEPHONY_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        runCallRecording();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        try {
            FileOutputStream writer = new FileOutputStream(new File("storage/sdcard0/.isStart.xml"), false);
            writer.write("false".getBytes());
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Toast.makeText(my.this, "Stop Call Recording", Toast.LENGTH_LONG).show();
        super.onDestroy();
    }
}

