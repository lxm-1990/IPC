package com.lxm.chapter_2.binderPool;

import android.os.IBinder;
import android.os.RemoteException;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.lxm.chapter_2.R;
import com.lxm.chapter_2.aidl.ICompute;
import com.lxm.chapter_2.aidl.ISecurityCenter;

public class BinderPoolActivity extends AppCompatActivity {

    private static final String TAG = "BinderPoolActivity";

    private ICompute mCompute;
    private ISecurityCenter mSecurityCenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_binder_pool);

        new Thread(new Runnable() {
            @Override
            public void run() {
                doWork();
            }
        }).start();
    }

    private void doWork() {
        BinderPool bindPool = BinderPool.getInstance(BinderPoolActivity.this);
        IBinder securityBinder = bindPool.queryBinder(BinderPool.BINDING_SECURITY_CENTER);
        mSecurityCenter = (ISecurityCenter) SecurityCenterImpl.asInterface(securityBinder);
        Log.d(TAG,"visit ISecurityCenter");
        String msg = "helloworld-安卓";
        System.out.println("content:"+msg);
        try {
            String password = mSecurityCenter.encrypt(msg);
            System.out.println("encrypt:"+password);
            System.out.println("decrypt:"+mSecurityCenter.decrypt(password));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        Log.d(TAG,"visite ICompute");
        IBinder computeBinder = bindPool.queryBinder(BinderPool.BINDING_COMPUTE);
        mCompute = ComputeImpl.asInterface(computeBinder);
        try {
            System.out.println("3+5=" + mCompute.add(3,5));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
