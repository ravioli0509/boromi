package com.team41.boromi.controllers;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import com.team41.boromi.callbacks.BookCallback;
import com.team41.boromi.constants.CommonConstants.BookWorkflowStage;
import com.team41.boromi.models.Book;
import com.team41.boromi.dbs.BookDB;
import static com.team41.boromi.constants.CommonConstants.BookStatus;
import static java.util.concurrent.Executors.newSingleThreadExecutor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

import javax.inject.Inject;

public class BookController {

    private final static String TAG = "BookController";
    ExecutorService executeService = newSingleThreadExecutor();
    protected BookStatus status;
    protected BookWorkflowStage workflow;
    protected BookCallback bookCallback;
    protected Executor executor;
    FirebaseFirestore db;
    BookDB bookDB;

    @Inject
    public BookController(BookDB bookDB, ExecutorService excuteService, FirebaseFirestore db){
        this.bookDB = bookDB;
        this.executeService = excuteService;
        this.db = db;
    }

    /**
     * Filters an array of Books into Available books
     *
     * @param username
     * @return a list of books that are filtered by available books
     */
    public ArrayList<Book> filterBookByAvailable(String username){
        ArrayList<Book> filteredBooks = new ArrayList<>();
        if (isNotNullOrEmpty(username)) {
            executeService.execute(() -> {
                ArrayList<Book> userOwnedBooks = bookDB.getUsersOwnedBooks(username);
                for (Book eachBook : userOwnedBooks){
                    if (status.AVAILABLE == eachBook.getStatus()){
                        filteredBooks.add(eachBook);
                    };
                }
            });
            executeService.shutdown();
        } else {
            Log.d(TAG, " Error in one of the columns");
            bookCallback.onFailure("Error in one of the columns");
        }
        return filteredBooks;
    }

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
            executeService.execute(() -> {
                Book result = bookDB.pushBook(addingBook);
                if (result != null) {
                    Log.d(TAG, " book add success");
                    System.out.println("TEST!");
                    System.out.println(result);
//                    bookCallback.onSuccess("Book Add Success");
                } else {
                    Log.d(TAG, " book add error");
//                    bookCallback.onFailure("Book Add Error");
                }
            });

            executeService.shutdown();
            return true;
        } else {
            Log.d(TAG, " Error in one of the columns");
            bookCallback.onFailure("Error in one of the columns");
            return false;
        }
    }


    public AtomicReference<ArrayList<Book>> getOwnedBooks(String username){
        AtomicReference<ArrayList<Book>> ownedBooks = new AtomicReference<>(new ArrayList<Book>());
        if (isNotNullOrEmpty(username)) {
            executeService.execute(() -> {
                ownedBooks.set(bookDB.getUsersOwnedBooks(username));
                if (ownedBooks.get() != null) {
                    Log.d(TAG, " get books success");
                    bookCallback.onFailure("Get Books Success");
                } else {
                    Log.d(TAG, " get books error");
                    bookCallback.onFailure("Get Books Error");
                }
            });
            executeService.shutdown();
        } else {
            Log.d(TAG, " Error in one of the columns");
            bookCallback.onFailure("Error in one of the columns");
        }
        return ownedBooks;
    }

    public boolean editBook(String bookID, String author, String ISBN, String title, BookStatus bookStatus, BookWorkflowStage workflowStage){
        if( isNotNullOrEmpty(author) || isNotNullOrEmpty(ISBN) || isNotNullOrEmpty(title)) {
            db.collection("books")
                .document(bookID)
                .get()
                .addOnCompleteListener(executor, new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "get book worked");
                            DocumentSnapshot document = task.getResult();
                            Book editingBook = document.toObject(Book.class);
                            editingBook.setTitle(title);
                            editingBook.setISBN(ISBN);
                            editingBook.setAuthor(author);
                            editingBook.setWorkflow(workflowStage);
                            editingBook.setStatus(bookStatus);
                            Book result = bookDB.pushBook(editingBook);
                            bookCallback.onSuccess(task.getResult().toString());
                        } else {
                            Log.d(TAG, "get book error");
                            bookCallback.onFailure(task.getException().toString());
                        }
                    }
                });
            return true;
        } else {
            Log.d(TAG, " Error in one of the columns");
            return false;
        }
    }

    public boolean deleteBook(String bookID) {
        if (isNotNullOrEmpty(bookID)) {
            executeService.execute(() -> {
                Boolean result = bookDB.deleteBook(bookID);
                if (result) {
                    Log.d(TAG, " book delete success");
                    bookCallback.onSuccess("Book delete Success");
                } else {
                    Log.d(TAG, " book delete error");
                    bookCallback.onFailure("Book delete error");
                }
            });
            executeService.shutdown();
            return true;
        } else {
            Log.d(TAG, " Error in one of the columns");
            bookCallback.onFailure("Error in one of the columns");
            return false;
        }
    }

    public AtomicReference<ArrayList<Book>> findBooks(String keywords) {
        AtomicReference<ArrayList<Book>> searchedBooks = new AtomicReference<>(new ArrayList<Book>());
        if (isNotNullOrEmpty(keywords)) {
            executeService.execute(() -> {
               searchedBooks.set(bookDB.findBooks(keywords));
            });
            executeService.shutdown();
        } else {
            bookCallback.onFailure("Keyword missing");
        }
        return searchedBooks;
    }

}