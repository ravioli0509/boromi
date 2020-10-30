package com.team41.boromi;

import com.google.firebase.firestore.FirebaseFirestore;
import com.team41.boromi.controllers.BookController;
import com.team41.boromi.dbs.BookDB;
import com.team41.boromi.models.Book;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import com.team41.boromi.constants.CommonConstants.BookWorkflowStage;
import com.team41.boromi.constants.CommonConstants.BookStatus;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class BookControllerTest {
    protected BookStatus status;
    protected BookWorkflowStage workflow;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    BookDB bookDB = new BookDB(db);
    ExecutorService executeService = Executors.newSingleThreadExecutor();
    BookController bookController = new BookController(bookDB, executeService, db);
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
    public void testAdd(){
        String author = "Hoge";
        String owner = "RAVI";
        String ISBN = "12783712";
        String title = "FooBar";
        Book test = new Book(owner, title, author, ISBN);
        bookDB.pushBook(test);
//        boolean result = bookController.addBook(owner, author, ISBN, title);
//        assertTrue(result);
    }

    @Test
    public void testEdit(){
        String bookID = "w4Kuq7xtV9dERvyW7Vbt";
        String title = "Not Harry Potter";
        String author = "JK Rowling";
        String owner = "Ravi";
        String ISBN = "12312313";
        boolean result = bookController.editBook(bookID, author, ISBN, title, status.AVAILABLE, workflow.AVAILABLE);
        assertTrue(result);
    }

    @Test
    public void testDelete(){
        String bookID = "w4Kuq7xtV9dERvyW7Vbt";
        boolean result = bookController.deleteBook(bookID);
        assertTrue(result);
    }


}
