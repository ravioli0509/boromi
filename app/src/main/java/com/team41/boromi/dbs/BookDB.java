package com.team41.boromi.dbs;

import static com.team41.boromi.constants.CommonConstants.DB_TIMEOUT;

import android.util.Log;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.gson.Gson;
import com.team41.boromi.constants.CommonConstants.BookStatus;
import com.team41.boromi.models.Book;
import com.team41.boromi.models.BookRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Firebase calls for book collection
 */
@Singleton
public class BookDB {

  private final static String TAG = "BookDB";
  private final String DB_COLLECTION = "books";
  private final FirebaseFirestore db;
  private final CollectionReference booksRef;
  private final Gson gson = new Gson();
  protected BookStatus status;

  @Inject
  public BookDB(FirebaseFirestore db) {
    this.db = db;
    booksRef = db.collection(DB_COLLECTION);
  }

  /**
   * Attempts to get all books owned by the user
   *
   * @param username The username of the authenticated username
   * @return A list of all the books owned by the user
   */
  public ArrayList<Book> getUsersOwnedBooks(String username) {
    final ArrayList<Book> ownedBooks = new ArrayList<>();

    QuerySnapshot res;

    // Gets all books with the owner field equal to the uuid
    try {
      res = Tasks.await(
          booksRef.whereEqualTo("owner", username).get(),
          DB_TIMEOUT,
          TimeUnit.MILLISECONDS
      );
    } catch (Exception e) { // failed
      Log.w(TAG, e.getCause());
      return null;
    }

    for (DocumentSnapshot document : res.getDocuments()) {
      ownedBooks.add(document.toObject(Book.class));
    }

    return ownedBooks;
  }

  /**
   * Attempts to delete a book
   *
   * @param bookId The uuid of the book in firestore.
   * @return True if the delete was successful, false otherwise
   */
  public boolean deleteBook(String bookId) {
    try {
      Tasks.await(
          booksRef.document(bookId).delete(),
          DB_TIMEOUT,
          TimeUnit.MILLISECONDS
      );
      return true;
    } catch (Exception e) {
      Log.w(TAG, e.getCause());
      return false;
    }
  }

  public List<Book> getAcceptedWithBorrower(String borrower) {
    final ArrayList<Book> availableBooks = new ArrayList<>();

    QuerySnapshot res;

    // Gets all books with the owner field equal to the uuid
    try {
      res = Tasks.await(
          booksRef.whereEqualTo("borrower", borrower).whereEqualTo("status", status.ACCEPTED).get(),
          DB_TIMEOUT,
          TimeUnit.MILLISECONDS
      );
    } catch (Exception e) { // failed
      Log.w(TAG, e.getCause());
      return null;
    }

    for (DocumentSnapshot document : res.getDocuments()) {
      availableBooks.add(document.toObject(Book.class));
    }

    return availableBooks;
  }

  /**
   * Attempts to push a book to the database
   *
   * @param book The book object the push
   * @return Null if the push fails, otherwise returns the book object
   */
  public Book pushBook(Book book) {

    try {
      booksRef.document(book.getBookId()).set(book, SetOptions.merge());
      return book;
    } catch (Exception e) {
      Log.w(TAG, e.getCause());
      return null;
    }
  }

  /**
   * Searches book by querying keywords
   *
   * @param keywords keywords to search for
   * @return list of all the books found
   */
  public ArrayList<Book> findBooks(String keywords) {
    final ArrayList<Book> foundBooks = new ArrayList<>();

    QuerySnapshot res;

    try {
      res = Tasks.await(
          booksRef.startAt(keywords).endAt(keywords + "\uf8ff").get(),
          DB_TIMEOUT,
          TimeUnit.MILLISECONDS);
    } catch (Exception e) {
      Log.w(TAG, e.getCause());
      return null;
    }

    for (DocumentSnapshot document : res.getDocuments()) {
      foundBooks.add(document.toObject(Book.class));
    }

    return foundBooks;
  }

  /**
   * Get Book using BookID
   *
   * @param bookID id of the book
   * @return book
   */
  public Book getBook(String bookID) {
    DocumentSnapshot res;

    try {
      res = Tasks.await(
          booksRef.document(bookID).get(),
          DB_TIMEOUT,
          TimeUnit.MILLISECONDS);
    } catch (Exception e) {
      Log.w(TAG, e.getCause());
      return null;
    }

    Book getBook = res.toObject(Book.class);

    return getBook;
  }


  /**
   * gets all books from the DB
   *
   * @return
   */
  public ArrayList<Book> getAllBooks() {
    ArrayList<Book> bookList = new ArrayList<>();

    QuerySnapshot res;

    try {
      res = Tasks.await(
          booksRef.get(),
          DB_TIMEOUT,
          TimeUnit.MILLISECONDS
      );

    } catch (Exception e) {
      Log.w(TAG, e.getCause());
      return null;
    }

    for (DocumentSnapshot document : res.getDocuments()) {
      bookList.add(document.toObject(Book.class));
    }

    return bookList;
  }

  /**
   * Get owner requested books
   *
   * @param owner id of the owner
   * @return list of books owned by the owner
   */
  public ArrayList<Book> getOwnerRequestedBooks(String owner) {
    final ArrayList<Book> requestedBooks = new ArrayList<>();

    QuerySnapshot res;

    // Gets all books with the owner field equal to the uuid
    try {
      res = Tasks.await(
          booksRef.whereEqualTo("owner", owner).whereEqualTo("status", status.REQUESTED).get(),
          DB_TIMEOUT,
          TimeUnit.MILLISECONDS
      );
    } catch (Exception e) { // failed
      Log.w(TAG, e.getCause());
      return null;
    }

    for (DocumentSnapshot document : res.getDocuments()) {
      requestedBooks.add(document.toObject(Book.class));
    }

    return requestedBooks;
  }

  /**
   * Gets books that are owned by the owner and are borrowed
   *
   * @param owner id of the owner
   * @return list of books
   */
  public ArrayList<Book> getOwnerBorrowedBooks(String owner) {
    final ArrayList<Book> borrowedBooks = new ArrayList<>();

    QuerySnapshot res;

    // Gets all books with the owner field equal to the uuid
    try {
      res = Tasks.await(
          booksRef.whereEqualTo("owner", owner).whereEqualTo("status", status.BORROWED).get(),
          DB_TIMEOUT,
          TimeUnit.MILLISECONDS
      );
    } catch (Exception e) { // failed
      Log.w(TAG, e.getCause());
      return null;
    }

    for (DocumentSnapshot document : res.getDocuments()) {
      borrowedBooks.add(document.toObject(Book.class));
    }

    return borrowedBooks;
  }

  /**
   * Get books that owner owns that are accepted to borrow
   *
   * @param owner id of the owner
   * @return list of books
   */
  public ArrayList<Book> getOwnerAcceptedBooks(String owner) {
    final ArrayList<Book> acceptedBooks = new ArrayList<>();

    QuerySnapshot res;

    // Gets all books with the owner field equal to the uuid
    try {
      res = Tasks.await(
          booksRef.whereEqualTo("owner", owner).whereEqualTo("status", status.ACCEPTED).get(),
          DB_TIMEOUT,
          TimeUnit.MILLISECONDS
      );
    } catch (Exception e) { // failed
      Log.w(TAG, e.getCause());
      return null;
    }

    for (DocumentSnapshot document : res.getDocuments()) {
      acceptedBooks.add(document.toObject(Book.class));
    }

    return acceptedBooks;
  }

  /**
   * Get books that owner owns and are available
   *
   * @param owner id of the owner
   * @return list of books
   */
  public ArrayList<Book> getOwnerAvailableBooks(String owner) {
    final ArrayList<Book> availableBooks = new ArrayList<>();

    QuerySnapshot res;

    // Gets all books with the owner field equal to the uuid
    try {
      res = Tasks.await(
          booksRef.whereEqualTo("owner", owner).whereEqualTo("status", status.AVAILABLE).get(),
          DB_TIMEOUT,
          TimeUnit.MILLISECONDS
      );
    } catch (Exception e) { // failed
      Log.w(TAG, e.getCause());
      return null;
    }

    for (DocumentSnapshot document : res.getDocuments()) {
      availableBooks.add(document.toObject(Book.class));
    }

    return availableBooks;
  }

  /**
   * Get book by id
   *
   * @param bid id of the book
   * @return book
   */
  public Book getBookById(String bid) {
    DocumentSnapshot res;

    try {
      res = Tasks.await(booksRef.document(bid).get(), DB_TIMEOUT, TimeUnit.MILLISECONDS);
    } catch (Exception e) { // failed
      Log.w(TAG, e.getCause());
      return null;
    }

    if (res.exists()) {    // if user exists
      return res.toObject(Book.class);
    } else {              // if user fails
      return null;
    }
  }

  /**
   * returns book and the bookrequest accompanying it
   *
   * @param BookRequestList List of books
   * @return Map of key:Book and value:List of BookRequest
   */
  public Map<Book, List<BookRequest>> getBooksWithRequestList(List<BookRequest> BookRequestList) {
    Map<Book, List<BookRequest>> bookMap = new HashMap<>();
    Book mockCompareBook = new Book();
    for (BookRequest br : BookRequestList) {
      mockCompareBook.setBookId(br.getBookId());
      if (bookMap.containsKey(mockCompareBook)) {
        bookMap.get(mockCompareBook).add(br);
        continue;
      }

      Book b = getBookById(br.getBookId());
      if (b == null) {
        Log.w(TAG, "book: " + b.getBookId() + " doesn't exists but it was requested");
        continue;
      }
      bookMap.put(b, new ArrayList<>());
      bookMap.get(b).add(br);
    }

    return bookMap;
  }

  /**
   * This method returns a list of books that user is borrowing from other owners.
   *
   * @param username id of the user
   * @return list of books
   */
  public ArrayList<Book> getOwnerBorrowingBooks(String username) {
    final ArrayList<Book> borrowingBooks = new ArrayList<>();

    QuerySnapshot res;

    // Gets all books with the owner field equal to the uuid
    try {
      res = Tasks.await(
          booksRef.whereEqualTo("borrower", username).whereEqualTo("status", status.BORROWED).get(),
          DB_TIMEOUT,
          TimeUnit.MILLISECONDS
      );
    } catch (Exception e) { // failed
      Log.w(TAG, e.getCause());
      return null;
    }

    for (DocumentSnapshot document : res.getDocuments()) {
      borrowingBooks.add(document.toObject(Book.class));
    }

    return borrowingBooks;
  }
}
