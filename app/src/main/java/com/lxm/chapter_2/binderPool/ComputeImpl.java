package com.lxm.chapter_2.binderPool;

import android.os.RemoteException;

import com.lxm.chapter_2.aidl.ICompute;

/**
 * Created by lxm on 17/2/8.
 */

public class ComputeImpl extends ICompute.Stub {
    @Override
    public int add(int a, int b) throws RemoteException {
        return a + b;
    }
}
