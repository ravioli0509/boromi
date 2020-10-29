package com.team41.boromi;

import com.google.firebase.firestore.FirebaseFirestore;
import com.team41.boromi.dbs.BookDB;
import com.team41.boromi.models.Book;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class BookDBTest {

	BookDB bookDB;
	ArrayList<Book> testBooks = new ArrayList<>();

	@Before
	public void setup() {
		bookDB = new BookDB(FirebaseFirestore.getInstance());

		// Push a book to firestore so each test has something to work with
		// This assumes correct functionality of the pushBook method but
		// I didn't know a better way to automate the process of setting up
		// A consistent testing environment
		testBooks.add(bookDB.pushBook(new Book("testuser1")));
	}

	@After
	public void tearDown() {
		// Deletes all the test books from firestore to rollback the changes made during the test
		// This assumes correct functionality of the deleteBook method but
		// I didn't know a better way to automate the process of setting up
		// A consistent testing environment
		for (Book book : testBooks)
			bookDB.deleteBook(book.getBookId());

		testBooks.clear();
	}

	@Test
	public void testPushBook() throws InterruptedException {
		Book book = new Book("testuser1");

		Book resultBook = bookDB.pushBook(book);
		testBooks.add(resultBook);

		assertNotNull(resultBook);
	}

	@Test
	public void testGetUsersOwnedBooks() {
		ArrayList<Book> ownedBooks = bookDB.getUsersOwnedBooks("testuser1");
		assertEquals(ownedBooks.size(), 1, 0);
	}

	@Test
	public void testDeleteBook() {
		boolean deleteSuccessful = bookDB.deleteBook(testBooks.get(0).getBookId());
		assertTrue(deleteSuccessful);
	}
}
