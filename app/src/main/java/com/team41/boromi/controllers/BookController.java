package com.team41.boromi.controllers;

import android.util.Log;


import com.google.firebase.firestore.FirebaseFirestore;

import com.team41.boromi.callbacks.BookCallback;
import com.team41.boromi.constants.CommonConstants.BookWorkflowStage;
import com.team41.boromi.models.Book;
import com.team41.boromi.dbs.BookDB;
import static com.team41.boromi.constants.CommonConstants.BookStatus;
import org.apache.commons.lang3.StringUtils; // For case sensitive keyword search

import java.util.ArrayList;
import java.util.concurrent.Executor;

import javax.inject.Inject;

public class BookController implements BookCallback{

    private final static String TAG = "BookController";
    protected BookStatus status;
    protected BookWorkflowStage workflow;
    protected Executor executor;
    FirebaseFirestore db;
    BookDB bookDB;

    @Inject
    public BookController(BookDB bookDB, Executor executor, FirebaseFirestore db){
        this.bookDB = bookDB;
        this.executor = executor;
        this.db = db;
    }

    /**
     * Filter books using their book status, and separate them into 4 array lists
     *
     * @param owner
     * @return a list of books that are filtered by available books
     */
    public void filterBookByStatus(String owner){
        ArrayList<Book> availableBooks = new ArrayList<>();
        ArrayList<Book> requestedBooks = new ArrayList<>();
        ArrayList<Book> acceptedBooks = new ArrayList<>();
        ArrayList<Book> borrowedBooks = new ArrayList<>();
        if (isNotNullOrEmpty(owner)) {
            executor.execute(() -> {
                ArrayList<Book> userOwnedBooks = bookDB.getUsersOwnedBooks(owner);
                if (userOwnedBooks != null) {
                    for (Book eachBook : userOwnedBooks){
                        BookStatus thisStatus = eachBook.getStatus();
                        if (status.AVAILABLE == thisStatus){
                            availableBooks.add(eachBook);
                        } else if (status.REQUESTED == thisStatus) {
                            requestedBooks.add(eachBook);
                        } else if (status.ACCEPTED == thisStatus) {
                            acceptedBooks.add(eachBook);
                        } else if (status.BORROWED == thisStatus) {
                            borrowedBooks.add(eachBook);
                        } else {
                            Log.d(TAG, "Status is null");
                            onFailure(new IllegalArgumentException());
                        }
                    }
                    Log.d(TAG, "Call backing All Filtered Books");
                    onSuccessFilterBook(availableBooks, requestedBooks, acceptedBooks, borrowedBooks);
                } else {
                    Log.d(TAG, "Book empty");
                    onFailure(new IllegalArgumentException());
                }
            });
        } else {
            Log.d(TAG, " Error in one of the columns");
            onFailure(new IllegalArgumentException());
        }
    }

    /**
     * static method to check null/empty
     * @param str
     * @return
     */
    public static boolean isNotNullOrEmpty(String str) {
        if (!str.trim().isEmpty() && str != null) {
            return true;
        }
        return false;
    }

    /**
     * Adds Book to DB asynchronously. On Success or Failure, it will have a callback to let the ui know
     * @param owner
     * @param author
     * @param ISBN
     * @param title
     */
    public Boolean addBook(String owner, String author, String ISBN, String title) {
        if(isNotNullOrEmpty(author) && isNotNullOrEmpty(ISBN) && isNotNullOrEmpty(title)) {
            Book addingBook = new Book(owner, title, author, ISBN);
            addingBook.setStatus(status.AVAILABLE);
            addingBook.setWorkflow(workflow.AVAILABLE);
            executor.execute(() -> {
                Book result = bookDB.pushBook(addingBook);
                if (result != null) {
//                    Log.d(TAG, " book add success");
                    onSuccessAddBook("Book Add Success");
                } else {
                    Log.d(TAG, " book add error");
                    onFailure(new IllegalArgumentException());
                }
            });
            return true;
        } else {
            Log.d(TAG, " Error in one of the columns");
            onFailure(new IllegalArgumentException());
            return false;
        }
    }

    /**
     * Edits book description by getting book from BookDB
     * @param bookID
     * @param author
     * @param ISBN
     * @param title
     * @return
     */
    public Boolean editBook(String bookID, String author, String ISBN, String title){
        if (isNotNullOrEmpty(author) && isNotNullOrEmpty(ISBN) && isNotNullOrEmpty(title)) {
            executor.execute(() -> {
                Book editingBook = bookDB.getBook(bookID);
                if (editingBook != null) {
                    editingBook.setTitle(title);
                    editingBook.setAuthor(author);
                    editingBook.setISBN(ISBN);
                    bookDB.pushBook(editingBook);
                    Log.d(TAG, " book add success");
                    onSuccessEditBook("Successful edit");
                } else {
                    Log.d(TAG, " book add error");
                    onFailure(new IllegalArgumentException());
                }
            });
            return true;
        }
        return false;
    }

    /**
     * Get Owner's Books
     * @param owner
     */
    public boolean getOwnedBooks(String owner){
        if (isNotNullOrEmpty(owner)) {
            executor.execute(() -> {
                ArrayList<Book> ownedBooks = bookDB.getUsersOwnedBooks(owner);
                if (ownedBooks != null) {
                    Log.d(TAG, " get books success");
                    onSuccessGetOwnedBooks(ownedBooks);
                } else {
                    Log.d(TAG, " get books error");
                    onFailure(new IllegalArgumentException());
                }
            });
            return true;
        } else {
            Log.d(TAG, " Error in one of the columns");
            onFailure(new IllegalArgumentException());
            return false;
        }
    }

    /**
     * Deletes book using bookID
     * @param bookID
     */
    public boolean deleteBook(String bookID) {
        if (isNotNullOrEmpty(bookID)) {
            executor.execute(() -> {
                Boolean result = bookDB.deleteBook(bookID);
                if (result) {
                    Log.d(TAG, " book delete success");
                    onSuccessDeleteBook("Book delete Success");
                } else {
                    Log.d(TAG, " book delete error");
                    onFailure(new IllegalArgumentException());
                }
            });
            return true;
        } else {
            Log.d(TAG, " Error in one of the columns");
            onFailure(new IllegalArgumentException());
            return false;
        }
    }

    /**
     * Gets all the books from book DB and searches by comparing title and keyword
     * @param keywords
     * @return
     */
    public ArrayList<Book> findBooks(String keywords) {
        ArrayList<Book> searchedBooks = new ArrayList<Book>();
        if (isNotNullOrEmpty(keywords)) {
            executor.execute(() -> {
               ArrayList<Book> allBooks = bookDB.getAllBooks();
               if (allBooks != null) {
                   for(Book eachBook : allBooks) {
                       String title = eachBook.getTitle();
                       if (StringUtils.containsIgnoreCase(title, keywords)){
                           searchedBooks.add(eachBook);
                       }
                   }
                   onSuccessFindBooks(searchedBooks);
               } else {
                   Log.d(TAG, " Error in one of the columns");
                   onFailure(new IllegalArgumentException());
               }
            });
        } else {
            Log.d(TAG, "Keyword is empty or null");
            onFailure(new IllegalArgumentException());
        }
        return searchedBooks;
    }

    @Override
    public void onSuccessFilterBook(ArrayList<Book> availableBooks, ArrayList<Book> requestedBooks, ArrayList<Book> acceptedBook, ArrayList<Book> borrowedBooks) {
        Log.d(TAG, availableBooks.toString());
        Log.d(TAG, requestedBooks.toString());
        Log.d(TAG, acceptedBook.toString());
        Log.d(TAG, borrowedBooks.toString());
    }

    @Override
    public void onSuccessAddBook(String message) {
        Log.d(TAG, message);
    }

    @Override
    public void onSuccessEditBook(String message) {
        Log.d(TAG, message);
    }

    @Override
    public void onSuccessGetOwnedBooks(ArrayList<Book> ownedBooks) {
        Log.d(TAG, ownedBooks.toString());
    }

    @Override
    public void onSuccessDeleteBook(String message) {
        Log.d(TAG, message);
    }

    @Override
    public void onSuccessFindBooks(ArrayList<Book> foundBooks) {
        Log.d(TAG, foundBooks.toString());
    }

    @Override
    public void onFailure(Exception exception) {
        Log.d(TAG, exception.toString());
    }
}