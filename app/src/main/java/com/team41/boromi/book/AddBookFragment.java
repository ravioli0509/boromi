package com.team41.boromi.book;

import android.icu.text.CaseMap.Title;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.team41.boromi.R;
import java.util.concurrent.Executor;

public class AddBookFragment extends DialogFragment {

  private Button addBook;
  private EditText author;
  private EditText title;
  private EditText isbn;

  public AddBookFragment() {}
  public static AddBookFragment newInstance() {
    AddBookFragment addBookFragment = new AddBookFragment();
    Bundle args = new Bundle();
    addBookFragment.setArguments(args);
    return addBookFragment;
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.add_book_fragment, container);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    addBook = (Button) view.findViewById(R.id.add_book_add_button);
    author = (EditText) view.findViewById(R.id.add_book_author);
    title = (EditText) view.findViewById(R.id.add_book_title);
    isbn = (EditText) view.findViewById(R.id.add_book_isbn);

    addBook.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        AddBookFragmentListener listener = (AddBookFragmentListener) getActivity();
        listener.onComplete(author.getText().toString(), title.getText().toString(), isbn.getText().toString());
        dismiss();
      }
    });
  }

  public interface AddBookFragmentListener {
    void onComplete(String author, String title, String isbn);
  }
}
