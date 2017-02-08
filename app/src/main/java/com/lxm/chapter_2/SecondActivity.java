package com.lxm.chapter_2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.lxm.chapter_2.utils.MyConstants;
import com.lxm.chapter_2.utils.MyUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class SecondActivity extends AppCompatActivity {

    private static final String TAG = "SecondActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
    }

    private void recoverFromFile(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                User user = null;
                File cachedFile = new File(MyConstants.CACHE_FILE_PATH);
                ObjectInputStream objectInputStream = null;
                try {
                    objectInputStream = new ObjectInputStream(new FileInputStream(cachedFile));
                    user = (User) objectInputStream.readObject();
                    Log.d(TAG,"recover user:"+user+user.userId + user.userName+user.isMale);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } finally {
                    MyUtils.close(objectInputStream);
                }
            }
        }).start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        recoverFromFile();
    }
}
