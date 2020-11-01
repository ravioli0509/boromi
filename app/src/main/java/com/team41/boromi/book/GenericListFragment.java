package com.team41.boromi.book;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import androidx.fragment.app.Fragment;

import com.team41.boromi.BookActivity;
import com.team41.boromi.R;
import com.team41.boromi.cards.romCardList;
import com.team41.boromi.constants.CommonConstants.BookWorkflowStage;
import com.team41.boromi.constants.CommonConstants.BookStatus;
import com.team41.boromi.models.Book;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass. Use the {@link GenericListFragment#newInstance} factory
 * method to create an instance of this fragment.
 */
public class GenericListFragment extends Fragment {

  // TODO: Rename parameter arguments, choose names that match
  // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
  private static final String ARG_PARAM1 = "msg";
  private ListView bookList;
  private ArrayAdapter<Book> bookAdapter;
  private ArrayList<Book> bookDataList;
  private Context context = ;
  // TODO: Rename and change types of parameters
  private String tempMsg;

  public GenericListFragment() {
    // Required empty public constructor
  }

  /**
   * Use this factory method to create a new instance of this fragment using the provided
   * parameters.
   *
   * @return A new instance of fragment OwnedAcceptedFragment.
   */
  // TODO: Rename and change types and number of parameters
  public static GenericListFragment newInstance(Bundle bundle) {
    GenericListFragment fragment = new GenericListFragment();
    fragment.setArguments(bundle);
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      tempMsg = getArguments().getString(ARG_PARAM1);
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_generic_list, container, false);
    TextView tempMsgView = view.findViewById(R.id.tempMessage);
    bookList = view.findViewById(R.id.cardList);
    String []Titles = {"The Hobbit", "The Stand", "Hitchhiker's Guide to the Galaxy"}
    String []Authors = {"J.R.R Tolkein", "Stephen King", "Douglas Adams"};
    String []ISBN = {"1234567890123", "1234567890124", "1234567890125"};
    String []owner = {"benlala", "brock818", "minmin"};
    String[]borrower = {"brock818", "benlala", "minmin"};
    BookWorkflowStage []workflow = {BookWorkflowStage.AVAILABLE, BookWorkflowStage.AVAILABLE, BookWorkflowStage.AVAILABLE};
    bookDataList = new ArrayList<>();
    for (int i = 0; i<Titles.length; i++){
        bookDataList.add((new Book(owner[i], Titles[i], Authors[i], ISBN[i], workflow[i], borrower[i])));
    }
    tempMsgView.setText(tempMsg);
    return view;
  }
}