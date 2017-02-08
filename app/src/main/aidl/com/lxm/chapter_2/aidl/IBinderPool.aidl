// IBinderPool.aidl
package com.lxm.chapter_2.aidl;

// Declare any non-default types here with import statements

interface IBinderPool {
    IBinder queryBinder(int binderCode);
}
