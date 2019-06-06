package com.example.andro.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {
    static Switch x;
ListView l;
    ArrayList vector;
Button b1,b2;
    File z;
    String s;
    MediaPlayer myMediaPlayer;
    static SharedPreferences.Editor enable;
    static SharedPreferences read;
    @Override
    protected void onStart() {
        vector.clear();
        if(z.exists())
        Collections.addAll(vector, z.list());
        l.setAdapter(new ArrayAdapter(this,android.R.layout.simple_expandable_list_item_1,vector));
        super.onStart();
    }
    @Override
    protected void onPostResume() {
        vector.clear();
        if(z.exists())
        Collections.addAll(vector, z.list());
        l.setAdapter(new ArrayAdapter(this,android.R.layout.simple_expandable_list_item_1,vector));
        super.onPostResume();
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        x=findViewById(R.id.switch1);
        l=findViewById(android.R.id.list);
        b1=findViewById(R.id.button1);
        b2=findViewById(R.id.button2);
        vector=new ArrayList();
        enable = getSharedPreferences("Enable", MODE_MULTI_PROCESS).edit();
        read = getSharedPreferences("Enable", MODE_MULTI_PROCESS);
        if(read.getBoolean("enable",false)){
            x.setChecked(true);
        }
        else { x.setChecked(false);}
        z=new File("storage/sdcard0/AudioRecorder");
         if(z.exists())
        Collections.addAll(vector, z.list());
        l.setAdapter(new ArrayAdapter(this,android.R.layout.simple_expandable_list_item_1,vector));
        x.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              if(x.isChecked())
                {
                    enable.putBoolean("enable",true);
                    enable.commit();
                startService(new Intent(MainActivity.this,my.class));

                }
                else
                {
                    enable.putBoolean("enable",false);
                    enable.commit();
                  stopService(new Intent(MainActivity.this, my.class));
                  my.onclose();
                }
            }
        });
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new File("storage/sdcard0/AudioRecorder/"+s).delete();
                vector.clear();
                Collections.addAll(vector, z.list());
                l.setAdapter(new ArrayAdapter(MainActivity.this,android.R.layout.simple_expandable_list_item_1, vector));
                Toast.makeText(MainActivity.this, "Done", Toast.LENGTH_SHORT).show();
            }
        });
b2.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        myMediaPlayer.stop();
        b2.setEnabled(false);
    }
});
l.setOnItemClickListener(new AdapterView.OnItemClickListener() {
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        s=(String) vector.get(position);
        Toast.makeText(MainActivity.this, "select "+s, Toast.LENGTH_SHORT).show();
        myMediaPlayer= MediaPlayer.create(MainActivity.this, Uri.parse("storage/sdcard0/AudioRecorder/"+s));
        myMediaPlayer.start();
        b2.setEnabled(true);
    }
});

    }
}
