package com.team41.boromi.callbacks;


import com.team41.boromi.models.Book;

import java.util.ArrayList;

/**
 * An interface defining callback methods for books
 */
public interface BookCallback {

    void onSuccessFilterBook(ArrayList<Book> availableBooks, ArrayList<Book> requestedBooks, ArrayList<Book> acceptedBook, ArrayList<Book> borrowedBooks);

    void onSuccessAddBook(String message);

    void onSuccessEditBook(String message);

    void onSuccessGetOwnedBooks(ArrayList<Book> ownedBooks);

    void onSuccessDeleteBook(String message);

    void onSuccessFindBooks(ArrayList<Book> foundBooks);

    void onFailure(Exception exception);
}
