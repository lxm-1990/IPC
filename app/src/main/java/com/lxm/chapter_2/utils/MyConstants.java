package com.lxm.chapter_2.utils;

import android.os.Environment;

/**
 * Created by lxm on 17/2/7.
 */

public class MyConstants {

    public static final String CHAPTER_2_PATH = Environment.getExternalStorageDirectory().getPath()
            + "/lxm/chapter_2/";
    public static final String CACHE_FILE_PATH = CHAPTER_2_PATH + "usercache";

    public static final int MSG_FROM_CLIENT = 0;
    public static final int MSG_FROM_SERVICE = 1;
}
