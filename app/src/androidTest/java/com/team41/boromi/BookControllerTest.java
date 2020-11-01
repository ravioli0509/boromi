package com.team41.boromi;


import android.util.Log;
import android.widget.ArrayAdapter;

import com.google.firebase.firestore.FirebaseFirestore;
import com.team41.boromi.controllers.BookController;
import com.team41.boromi.dbs.BookDB;
import com.team41.boromi.models.Book;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.team41.boromi.constants.CommonConstants.BookWorkflowStage;
import com.team41.boromi.constants.CommonConstants.BookStatus;
import static com.team41.boromi.constants.CommonConstants.DB_TIMEOUT;
import com.team41.boromi.callbacks.BookCallback;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

public class BookControllerTest {
    private final static String TAG = "BookControllerTest";
    protected BookStatus status;
    protected BookWorkflowStage workflow;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    BookDB bookDB = new BookDB(db);
    ExecutorService executorService = new ThreadPoolExecutor(1, 4, 1, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>() );
    BookController bookController = new BookController(bookDB, executorService, db);
    ArrayList<Book> testBooks = new ArrayList<>();

    @Before
    public void setup(){
        testBooks.add(new Book("testuser1", "The Hobbit", "JK Rowling", "123456789012"));
    }

    @After
    public void tearDown(){
        testBooks.clear();
    }

    @Test
    public void testAdd() throws InterruptedException {
        String author = "Check workflow test";
        String owner = "RaviTest";
        String ISBN = "127837122";
        String title = "FooBar";
        bookController.addBook(owner, author, ISBN, title, new BookCallback() {
            @Override
            public void onSuccess(ArrayList<Book> books) {
                ArrayList<Book> test = bookDB.getUsersOwnedBooks(owner);
                assertEquals(test.size(), 1);
            }

            @Override
            public void onFailure(Exception e) {
                Log.d(TAG, "Success");
                Log.d(TAG, "FAILED");
                fail();
            }
        });
        Thread.sleep(DB_TIMEOUT);
    }

    @Test
    public void testEdit() throws InterruptedException{
        String bookID = "3f095b91-b25a-44a4-aee8-59160f59c17b";
        String title = "Test Edit";
        String author = "JK Rowling";
        String ISBN = "12312313";
        bookController.editBook(bookID, author, ISBN, title, new BookCallback() {
            @Override
            public void onSuccess(ArrayList<Book> books) {
                Book booktest = bookDB.getBook(bookID);
                Log.d(TAG, "Success");
                assertEquals(booktest.getTitle(), "Test Edit");
            }

            @Override
            public void onFailure(Exception e) {
                Log.d(TAG, "Success");
                Log.d(TAG, "FAILED");
                fail();
            }
        });
        Thread.sleep(DB_TIMEOUT);

    }

    @Test
    public void testDelete() throws InterruptedException {
        String bookID = "87c2be16-7f75-4052-b75c-9b2a8e01febe";
        bookController.deleteBook(bookID, new BookCallback() {
            @Override
            public void onSuccess(ArrayList<Book> books) {
                Book book = bookDB.getBook(bookID);
                assertNull(book);
            }

            @Override
            public void onFailure(Exception e) {
                Log.d(TAG, "Success");
                Log.d(TAG, "FAILED");
                fail();
            }
        });
        Thread.sleep(DB_TIMEOUT);
    }

    @Test
    public void testOwnerBooksByFilter() throws InterruptedException {
        String owner = "testuser1";

        Book booktest1 = new Book(owner, "Toy Story 1", "JK Rowling", "123456789020");
        booktest1.setStatus(status.AVAILABLE);
        booktest1.setWorkflow(workflow.AVAILABLE);

        Book booktest2 = new Book(owner, "Toy Story 2", "JK Rowling", "123456789021");
        booktest2.setStatus(status.REQUESTED);
        booktest2.setWorkflow(workflow.REQUESTED);

        Book booktest3 = new Book(owner, "Toy Story 3", "JK Rowling", "123456789022");
        booktest3.setStatus(status.ACCEPTED);
        booktest3.setWorkflow(workflow.ACCEPTED);

        Book booktest4 = new Book(owner, "Toy Story 4", "JK Rowling", "123456789023");
        booktest4.setStatus(status.BORROWED);
        booktest4.setWorkflow(workflow.PENDINGRETURN);

        Book booktest5 = new Book(owner, "Toy Story 5", "JK Rowling", "123456789024");
        booktest5.setStatus(status.AVAILABLE);
        booktest5.setWorkflow(workflow.AVAILABLE);

        Book booktest6 = new Book(owner, "Toy Story 6", "JK Rowling", "123456789025");
        booktest6.setStatus(status.ACCEPTED);
        booktest6.setWorkflow(workflow.ACCEPTED);

        Book booktest7 = new Book(owner, "Toy Story 7", "JK Rowling", "123456789026");
        booktest7.setStatus(status.REQUESTED);
        booktest7.setWorkflow(workflow.REQUESTED);

        Book booktest8 = new Book(owner, "Toy Story 8", "JK Rowling", "123456789027");
        booktest8.setStatus(status.BORROWED);
        booktest8.setWorkflow(workflow.PENDINGRETURN);

        bookDB.pushBook(booktest1);
        bookDB.pushBook(booktest2);
        bookDB.pushBook(booktest3);
        bookDB.pushBook(booktest4);
        bookDB.pushBook(booktest5);
        bookDB.pushBook(booktest6);
        bookDB.pushBook(booktest7);
        bookDB.pushBook(booktest8);

        bookController.getOwnerAvailableBooks(owner, new BookCallback() {
            @Override
            public void onSuccess(ArrayList<Book> books) {
                Log.d(TAG, "Success");
                assertEquals(books.size(), 2);
            }

            @Override
            public void onFailure(Exception e) {
                Log.d(TAG, "FAILED");
                fail();
            }
        });
        Thread.sleep(DB_TIMEOUT);

        bookController.getOwnerRequestedBooks(owner, new BookCallback() {
            @Override
            public void onSuccess(ArrayList<Book> books) {
                Log.d(TAG, "Success");
                assertEquals(books.size(), 2);
            }

            @Override
            public void onFailure(Exception e) {
                Log.d(TAG, "FAILED");
                fail();
            }
        });
        Thread.sleep(DB_TIMEOUT);

        bookController.getOwnerAcceptedBooks(owner, new BookCallback() {
            @Override
            public void onSuccess(ArrayList<Book> books) {
                Log.d(TAG, "Success");
                assertEquals(books.size(), 2);
            }

            @Override
            public void onFailure(Exception e) {
                Log.d(TAG, "FAILED");
                fail();
            }
        });
        Thread.sleep(DB_TIMEOUT);

        bookController.getOwnerBorrowedBooks(owner, new BookCallback() {
            @Override
            public void onSuccess(ArrayList<Book> books) {
                Log.d(TAG, "Success");
                assertEquals(books.size(), 2);
            }

            @Override
            public void onFailure(Exception e) {
                Log.d(TAG, "FAILED");
                fail();
            }
        });
        Thread.sleep(DB_TIMEOUT);

        bookController.getOwnerAvailableBooks("Ming Test", new BookCallback() {
            @Override
            public void onSuccess(ArrayList<Book> books) {
                Log.d(TAG, "FAILED");
                fail();
            }

            @Override
            public void onFailure(Exception e) {
                Log.d(TAG, "Success");
                assertTrue(true);
            }
        });
        Thread.sleep(DB_TIMEOUT);
    }

    @Test
    public void testGetOwnedBooks() throws InterruptedException {
        bookController.getOwnedBooks("testuser1", new BookCallback() {
            @Override
            public void onSuccess(ArrayList<Book> books) {
                Log.d(TAG, "Success");
                assertEquals(books.size(), 1);
            }

            @Override
            public void onFailure(Exception e) {
                Log.d(TAG, "FAILED");
                fail();
            }
        });
        Thread.sleep(DB_TIMEOUT);
    }

    @Test
    public void testFindBooks() throws InterruptedException {
        Book booktest1 = new Book("testuser1", "Harry Potter and the Chamber of Secrets", "JK Rowling", "123456789020");
        booktest1.setStatus(status.AVAILABLE);
        booktest1.setWorkflow(workflow.AVAILABLE);

        Book booktest2 = new Book("testuser1", "Harry Potter and the Philosopher's Stone", "JK Rowling", "123456789021");
        booktest2.setStatus(status.REQUESTED);
        booktest2.setWorkflow(workflow.REQUESTED);

        Book booktest3 = new Book("testuser1", "Toy Story 3", "Ming", "123456789022");
        booktest3.setStatus(status.ACCEPTED);
        booktest3.setWorkflow(workflow.ACCEPTED);

        bookDB.pushBook(booktest1);
        bookDB.pushBook(booktest2);
        bookDB.pushBook(booktest3);

        bookController.findBooks("Harry", new BookCallback() {
            @Override
            public void onSuccess(ArrayList<Book> books) {
                Log.d(TAG, "Success");
                assertEquals(books.size(), 2);
            }

            @Override
            public void onFailure(Exception e) {
                Log.d(TAG, "FAILED");
                fail();
            }
        });

        Thread.sleep(DB_TIMEOUT);

        bookController.findBooks("", new BookCallback() {
            @Override
            public void onSuccess(ArrayList<Book> books) {
                Log.d(TAG, "Success");
                assertNull(books);
            }

            @Override
            public void onFailure(Exception e) {
                Log.d(TAG, "FAILED");
                fail();
            }
        });

        Thread.sleep(DB_TIMEOUT);

        bookController.findBooks("Chamber", new BookCallback() {
            @Override
            public void onSuccess(ArrayList<Book> books) {
                Log.d(TAG, "Success");
                assertEquals(books.size(), 1);
            }

            @Override
            public void onFailure(Exception e) {
                Log.d(TAG, "FAILED");
                fail();
            }
        });

    }


}
