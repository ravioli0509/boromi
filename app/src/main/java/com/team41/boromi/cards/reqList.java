package com.team41.boromi.cards;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.team41.boromi.models.User;

import java.util.ArrayList;
import com.team41.boromi.R;

public class reqList extends ArrayAdapter<User> {

    private ArrayList<User> reqs;
    private Context context;

    public reqList(Context context, ArrayList<User> reqs){
        super(context,0, reqs);
        this.reqs = reqs;
        this.context = context;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        return super.getView(position, convertView, parent);
        View view = convertView;

        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.req_list_entry, parent,false);
        }

        User user = reqs.get(position);

        TextView reqT = view.findViewById(R.id.user);

        reqT.setText(user.getUsername());

        return view;

    }
}
