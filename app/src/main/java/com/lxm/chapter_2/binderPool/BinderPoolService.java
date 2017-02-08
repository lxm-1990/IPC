package com.lxm.chapter_2.binderPool;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.lxm.chapter_2.aidl.IBinderPool;

import static com.lxm.chapter_2.binderPool.BinderPool.BINDING_SECURITY_CENTER;

public class BinderPoolService extends Service {

    private static final String TAG = "BinderPoolService";
    public static final int BINDING_COMPUTE = 0;
    public static final int BINDING_SECURITY_CENTER = 1;
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind");
        return mBinderPool;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private Binder mBinderPool = new IBinderPool.Stub() {

        @Override
        public IBinder queryBinder(int binderCode) throws RemoteException {
            IBinder binder = null;
            switch (binderCode) {
                case BINDING_SECURITY_CENTER: {
                    binder = new SecurityCenterImpl();
                    break;
                }
                case BINDING_COMPUTE: {
                    binder = new ComputeImpl();
                    break;
                }
                default:
                    break;
            }

            return binder;
        }
    };
}