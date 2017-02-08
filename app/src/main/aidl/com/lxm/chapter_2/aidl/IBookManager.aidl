// IBookManager.aidl
package com.lxm.chapter_2.aidl;

import com.lxm.chapter_2.aidl.Book;
import com.lxm.chapter_2.aidl.IOnNewBookArrivedListener;
// Declare any non-default types here with import statements

interface IBookManager {
    List<Book> getBookList();
    void addBook(in Book book);
    void registerListener(IOnNewBookArrivedListener listener);
    void unregisterListener(IOnNewBookArrivedListener listener);
}
