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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicReference;

import javax.inject.Inject;

public class BookController {

    private final static String TAG = "BookController";
    final Executor executor;
    BookDB bookDB;
    FirebaseFirestore db;

    @Inject
    public BookController(BookDB bookDB, Executor executor, FirebaseFirestore db){
        this.bookDB = bookDB;
        this.executor = executor;
        this.db = db;
    }

    /**
     * Filters an array of Books into Available books
     *
     * @param books
     * @param bookStatus
     * @return a list of books that are filtered by available books
     */
    public ArrayList<Book> filterBookByStatus(ArrayList<Book> books, BookStatus bookStatus, String username){
        ArrayList<Book> ownedBooks = new ArrayList<>();
        if (isNotNullOrEmpty(username)) {
            executor.execute(() -> {
            });
        } else {
            throw new IllegalArgumentException("Username is either null or empty");
        }
        final ArrayList<Book> userOwnedBooks = bookDB.getUsersOwnedBooks(username);
        return ownedBooks;
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
     * @param Desc
     * @param status
     * @param workflowStage
     */
    public void addBook(String owner, String author, String ISBN, String Desc, BookStatus status, BookWorkflowStage workflowStage,
                        final BookCallback bookCallback) {
        Book addingBook = new Book(owner);
        if( isNotNullOrEmpty(author) || isNotNullOrEmpty(ISBN) || isNotNullOrEmpty(Desc)) {
            addingBook.setAuthor(author);
            addingBook.setISBN(ISBN);
            addingBook.setDesc(Desc);
            addingBook.setStatus(status.AVAILABLE);
            addingBook.setWorkflow(workflowStage.AVAILABLE);
            executor.execute(() -> {
               Book result = bookDB.pushBook(addingBook);
               if (result != null) {
                   Log.d(TAG, " book add success");
                   bookCallback.onSuccess("Book Add Success");
               } else {
                   Log.d(TAG, " book add error");
                   bookCallback.onSuccess("Book Add Error");
               }
            });
        } else {
            throw new IllegalArgumentException("Error in one of the columns");
        }
    }

    public AtomicReference<ArrayList<Book>> getOwnedBooks(String username, final BookCallback bookCallback){
        AtomicReference<ArrayList<Book>> ownedBooks = new AtomicReference<>(new ArrayList<Book>());
        if (isNotNullOrEmpty(username)) {
            executor.execute(() -> {
                ownedBooks.set(bookDB.getUsersOwnedBooks(username));
                if (ownedBooks.get() != null) {
                    Log.d(TAG, " get books success");
                    bookCallback.onFailure("Get Books Success");
                } else {
                    Log.d(TAG, " get books error");
                    bookCallback.onFailure("Get Books Error");
                }
            });
        }
        return ownedBooks;
    }

    public void editBook(String bookID, String author, String ISBN, String desc, BookStatus status, BookWorkflowStage workflowStage,
                         final BookCallback bookCallback){
        if( isNotNullOrEmpty(author) || isNotNullOrEmpty(ISBN) || isNotNullOrEmpty(desc)) {
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
                            editingBook.setDesc(desc);
                            editingBook.setISBN(ISBN);
                            editingBook.setAuthor(author);
                            editingBook.setWorkflow(workflowStage);
                            editingBook.setStatus(status);
                            Book result = bookDB.pushBook(editingBook);
                            bookCallback.onSuccess(task.getResult().toString());

                        } else {
                            Log.d(TAG, "get book error");
                            bookCallback.onFailure(task.getException().toString());
                        }
                    }
                });
        } else {
            throw new IllegalArgumentException("Error in one of the columns");
        }
    }

    public void deleteBook(String bookID, final BookCallback bookCallback) {
        if (isNotNullOrEmpty(bookID)) {
            executor.execute(() -> {
                Boolean result = bookDB.deleteBook(bookID);
                if (result) {
                    Log.d(TAG, " book delete success");
                    bookCallback.onSuccess("Book delete Success");
                } else {
                    Log.d(TAG, " book delete error");
                    bookCallback.onFailure("Book delete error");
                }
            });
        } else {
            throw new IllegalArgumentException("Book ID is null or empty");
        }
    }


    public void findBooks(String keywords) {
        if (isNotNullOrEmpty(keywords)) {
            executor.execute(() -> {
                bookDB.findBooks(keywords);
            });
        } else {
            throw new IllegalArgumentException("Keywords missing");
        }
    }

}