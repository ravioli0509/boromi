package com.team41.boromi.utility;

import android.graphics.Bitmap;
import android.os.Build;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.team41.boromi.BookActivity;
import com.team41.boromi.R;
import com.team41.boromi.book.EditBookFragment;
import com.team41.boromi.book.GenericListFragment;
import com.team41.boromi.callbacks.BookCallback;
import com.team41.boromi.controllers.BookController;
import com.team41.boromi.models.Book;

import java.util.ArrayList;


public class CustomClickListener implements View.OnClickListener, PopupMenu.OnMenuItemClickListener, EditBookFragment.EditBookFragmentListener {
    Book book;
    BookController bookController;
    GenericListFragment genericListFragment;
    FragmentActivity editBookFragment;

    public CustomClickListener(Book book, BookController bookController, GenericListFragment genericListFragment) {
        this.book = book;
        this.bookController = bookController;
        this.genericListFragment = genericListFragment;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void onClick(View view) {
        PopupMenu popup = new PopupMenu(view.getContext(), view);
        popup.setOnMenuItemClickListener(this::onMenuItemClick);
        popup.inflate(R.menu.book_edit_delete_menu);
        popup.setForceShowIcon(true);
        popup.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.edit_book:
                Log.d("edit!", book.getTitle());
                FragmentManager fragmentManager = editBookFragment.getSupportFragmentManager();
                EditBookFragment editBookFragment = EditBookFragment.newInstance(book);
                editBookFragment.show(fragmentManager, "editBook");

                return true;
            case R.id.delete_book:
                Log.d("delete!", book.getTitle());
                bookController.deleteBook(book.getBookId(), new BookCallback() {
                    @Override
                    public void onSuccess(ArrayList<Book> books) {
                        genericListFragment.getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (genericListFragment.getParent().equals("Owned")) {
                                    ((BookActivity) genericListFragment.getActivity()).updateFragment("OwnedFragment", genericListFragment.tag);
                                } else if (genericListFragment.getParent().equals("Borrowed")) {
                                    ((BookActivity) genericListFragment.getActivity()).updateFragment("BorrowedFragment", genericListFragment.tag);
                                }
                            }
                        });
                    }

                    @Override
                    public void onFailure(Exception e) {

                    }
                });

        }
        return false;
    }

    @Override
    public void onComplete(String author, String title, String isbn, Bitmap image) {

    }
}
