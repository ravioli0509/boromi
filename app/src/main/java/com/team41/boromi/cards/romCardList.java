package com.team41.boromi.cards;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.team41.boromi.models.Book;

import java.util.ArrayList;
import com.team41.boromi.R;
import com.team41.boromi.models.User;

public class romCardList extends ArrayAdapter<Book> {

    ListView reqList;
    private ArrayAdapter<User> reqAdapter;
    private ArrayList<User> reqDataList;

    private ArrayList<Book> books;
    private Context context;

    public romCardList(Context context, ArrayList<Book> books){
        super(context,0,books);
        this.books = books;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        View view = convertView;
        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.accepted, parent, false);
        }
        Book book = books.get(position);
        TextView title = view.findViewById(R.id.title);
        TextView author = view.findViewById(R.id.author);
        TextView user = view.findViewById(R.id.user);
        TextView isbn = view.findViewById(R.id.isbn);


        author.setText(book.getAuthor());
        title.setText(book.getTitle());
        user.setText(book.getBorrower());
        isbn.setText("ISBN: #"+book.getISBN());

        return view;

    }
}
