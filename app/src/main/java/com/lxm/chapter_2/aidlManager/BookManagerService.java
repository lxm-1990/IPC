package com.lxm.chapter_2.aidlManager;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.os.SystemClock;
import android.util.Log;

import com.lxm.chapter_2.aidl.Book;
import com.lxm.chapter_2.aidl.IBookManager;
import com.lxm.chapter_2.aidl.IOnNewBookArrivedListener;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class BookManagerService extends Service {
    private static final String TAG = "BookManagerService";

    private AtomicBoolean mIsServicesDestoryed = new AtomicBoolean(false);

    private CopyOnWriteArrayList<Book> mBookList = new CopyOnWriteArrayList<Book>();

//    private CopyOnWriteArrayList<IOnNewBookArrivedListener> mListenerList
//            = new CopyOnWriteArrayList<IOnNewBookArrivedListener>();

    private RemoteCallbackList<IOnNewBookArrivedListener> mListenerList = new RemoteCallbackList<IOnNewBookArrivedListener>();

    private Binder mBinder = new IBookManager.Stub() {

        @Override
        public List<Book> getBookList() throws RemoteException {
            //SystemClock.sleep(5000);
            return mBookList;
        }

        @Override
        public void addBook(Book book) throws RemoteException {
            mBookList.add(book);
        }

        @Override
        public void registerListener(IOnNewBookArrivedListener listener) throws RemoteException {
//            if (!mListenerList.contains(listener)) {
//                mListenerList.add(listener);
//            } else {
//                Log.d(TAG,"already exists");
//            }
//            Log.d(TAG,"registerListener,size:" + mListenerList.size());
            mListenerList.register(listener);
            final int N = mListenerList.beginBroadcast();
            mListenerList.finishBroadcast();
            Log.d(TAG, "registerListener, current size:" + N);
        }

        @Override
        public void unregisterListener(IOnNewBookArrivedListener listener) throws RemoteException {
//            if (mListenerList.contains(listener)) {
//                mListenerList.remove(listener);
//                Log.d(TAG,"unregister listener succeed");
//            } else {
//                Log.d(TAG,"not found,can not unregister");
//            }
//            Log.d(TAG,"unregisterListener,current size:" + mListenerList.size());
            mListenerList.unregister(listener);
            final int N = mListenerList.beginBroadcast();
            mListenerList.finishBroadcast();
            Log.d(TAG,"unregisterListener,current size:" + N);
        }

        @Override
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            int check = checkCallingOrSelfPermission("com.lxm.chapter_2.permission.ACCESS_BOOK_SERVICE");
            if (check == PackageManager.PERMISSION_DENIED) {
                return false;
            }

            String packageName = null;
            String[] packages = getPackageManager().getPackagesForUid(getCallingUid());
            if (packages != null && packages.length > 0) {
                packageName = packages[0];
            }
            if (!packageName.startsWith("com.lxm")) {
                return false;
            }
            return super.onTransact(code, data, reply, flags);
        }
    };
    @Override
    public IBinder onBind(Intent intent) {
        //在onbind验证权限
//        int check = checkCallingOrSelfPermission("com.lxm.chapter_2.permission.ACCESS_BOOK_SERVICE");
//        if (check == PackageManager.PERMISSION_DENIED){
//            return null;
//        }
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mBookList.add(new Book(1,"Android"));
        mBookList.add(new Book(2,"Ios"));

        new Thread(new serviceWorker()).start();

        //20s后终止服务
//        new Handler().postDelayed(new Runnable()
//        {
//            public void run()
//            {
//                stopSelf();
//                System.exit(0);
//            }
//        }, 20000);
    }

    @Override
    public void onDestroy() {
        mIsServicesDestoryed.set(true);
        super.onDestroy();
    }

    private void onNewBookArrived(Book book) throws RemoteException{
        mBookList.add(book);
//        Log.d(TAG,"onNewBookArrived,notify listeners:" + mListenerList.size());
//        for (int i = 0 ; i < mListenerList.size() ; ++i ){
//            IOnNewBookArrivedListener listener = mListenerList.get(i);
//            Log.i(TAG,"onNewBookArrived,notify listener:" + listener);
//            listener.onNewBookArrived(book);
//        }
        final int N = mListenerList.beginBroadcast();
        for (int i = 0; i < N; i ++) {
            IOnNewBookArrivedListener l = mListenerList.getBroadcastItem(i);
            if (l != null) {
                try {
                    l.onNewBookArrived(book);
                } catch (RemoteException e){
                    e.printStackTrace();
                }

            }
        }
        mListenerList.finishBroadcast();
    }
    private class serviceWorker implements Runnable {

        @Override
        public void run() {
            while (!mIsServicesDestoryed.get()) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                int bookId = mBookList.size() + 1;
                Book newBook = new Book(bookId, "new book#" + bookId);
                try {
                    onNewBookArrived(newBook);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
