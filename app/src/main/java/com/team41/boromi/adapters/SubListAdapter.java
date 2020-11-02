package com.team41.boromi.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.team41.boromi.R;
import com.team41.boromi.models.User;
import java.util.ArrayList;

public class SubListAdapter extends RecyclerView.Adapter<SubListAdapter.ViewHolder> {

  private ArrayList<User> usersRequested;

  public SubListAdapter(ArrayList<User> usersRequested) {
    this.usersRequested = usersRequested;
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.req_list_entry, parent, false);
    SubListAdapter.ViewHolder holder = new ViewHolder(view);
    return holder;
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    holder.user.setText(usersRequested.get(position).getUsername());
  }

  @Override
  public int getItemCount() {
    return usersRequested.size();
  }

  public static class ViewHolder extends RecyclerView.ViewHolder {

    TextView user;

    public ViewHolder(@NonNull View itemView) {
      super(itemView);
      user = itemView.findViewById(R.id.req_list_entry_user);
    }
  }
}
