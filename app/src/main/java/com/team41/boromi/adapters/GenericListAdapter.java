package com.team41.boromi.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.team41.boromi.R;
import com.team41.boromi.models.Book;
import com.team41.boromi.models.User;
import java.util.ArrayList;

public class GenericListAdapter extends RecyclerView.Adapter<GenericListAdapter.ViewHolder> {

  private ArrayList<Book> books;
  private int resource;
  private ViewGroup parent;

  public GenericListAdapter(ArrayList<Book> books, int id) {
    this.books = books;
    resource = id;
    System.out.println("ID: ----- " + id);
  }

  @NonNull
  @Override
  public GenericListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
    ViewHolder holder = new ViewHolder(view, resource);
    this.parent = parent;
    return holder;
  }

  @Override
  public void onBindViewHolder(@NonNull GenericListAdapter.ViewHolder holder, int position) {
    Book book = books.get(position);
    if (holder.author != null) {
      holder.author.setText(book.getAuthor());
    }
    if (holder.user != null) {
      // TODO more logic required depending on the page
      holder.user.setText(book.getBorrower());
    }
    if (holder.title != null) {
      holder.title.setText(book.getTitle());
    }
    if (holder.isbn != null) {
      holder.isbn.setText(book.getISBN());
    }
    if (holder.reqom != null) {
      RecyclerView recyclerView = holder.view.findViewById(R.id.reqom_request_list);
      ArrayList<User> requesters = new ArrayList<>();
      requesters.add(new User("testUser", "Andrew", "test123"));
      requesters.add(new User("testUser", "Andrew", "test123"));

      SubListAdapter subListAdapter = new SubListAdapter(requesters);
      recyclerView.setLayoutManager(new LinearLayoutManager(parent.getContext()));
      recyclerView.setAdapter(subListAdapter);
    }
  }

  @Override
  public int getItemCount() {
    return books.size();
  }

  public static class ViewHolder extends RecyclerView.ViewHolder {

    TextView title;
    TextView author;
    TextView isbn;
    TextView user;
    RecyclerView reqom;
    View view;

    public ViewHolder(@NonNull View itemView, int layout) {
      super(itemView);
      view = itemView;
      switch (layout) {
        case (R.layout.available):
          title = itemView.findViewById(R.id.available_title);
          author = itemView.findViewById(R.id.available_author);
          isbn = itemView.findViewById(R.id.available_isbn);
          break;
        case (R.layout.accepted):
          title = itemView.findViewById(R.id.accepted_title);
          author = itemView.findViewById(R.id.accepted_author);
          isbn = itemView.findViewById(R.id.accepted_isbn);
          user = itemView.findViewById(R.id.accepted_user);
          break;
        case (R.layout.borrowing):
          title = itemView.findViewById(R.id.borrowing_title);
          author = itemView.findViewById(R.id.borrowing_author);
          isbn = itemView.findViewById(R.id.borrowing_isbn);
          user = itemView.findViewById(R.id.borrowing_user);
          break;
        case (R.layout.lent):
          title = itemView.findViewById(R.id.lent_title);
          author = itemView.findViewById(R.id.lent_author);
          isbn = itemView.findViewById(R.id.lent_isbn);
          user = itemView.findViewById(R.id.lent_user);
          break;
        case (R.layout.reqbm):
          title = itemView.findViewById(R.id.reqbm_title);
          author = itemView.findViewById(R.id.reqbm_author);
          isbn = itemView.findViewById(R.id.reqbm_isbn);
          user = itemView.findViewById(R.id.reqbm_user);
          break;
        case (R.layout.reqom):
          title = itemView.findViewById(R.id.reqom_title);
          author = itemView.findViewById(R.id.reqom_author);
          isbn = itemView.findViewById(R.id.reqom_isbn);
          user = itemView.findViewById(R.id.reqom_user);
          reqom = itemView.findViewById(R.id.reqom_request_list);
          break;
        case (R.layout.searched):
          title = itemView.findViewById(R.id.searched_title);
          author = itemView.findViewById(R.id.searched_author);
          isbn = itemView.findViewById(R.id.searched_isbn);
          user = itemView.findViewById(R.id.searched_user);
      }

    }
  }
}
