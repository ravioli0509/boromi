package com.team41.boromi.book;

import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener;
import com.google.android.material.tabs.TabLayout.Tab;
import com.team41.boromi.BookActivity;
import com.team41.boromi.R;
import com.team41.boromi.adapters.PagerAdapter;
import com.team41.boromi.constants.CommonConstants.BookStatus;
import com.team41.boromi.constants.CommonConstants.BookWorkflowStage;
import com.team41.boromi.models.Book;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass. Use the {@link OwnedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OwnedFragment extends Fragment {

  private TabLayout tabLayout;
  private ViewPager2 viewPager2;
  private PagerAdapter pagerAdapter;
  private BookActivity bookActivity;

  public OwnedFragment() {
    // Required empty public constructor
  }

  /**
   * Use this factory method to create a new instance of this fragment using the provided
   * parameters.
   *
   * @return A new instance of fragment BookFragment.
   */
  // TODO: Rename and change types and number of parameters
  public static OwnedFragment newInstance() {
    OwnedFragment fragment = new OwnedFragment();
    Bundle args = new Bundle();
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_owned, container, false);
    bookActivity = (BookActivity) getActivity();
    tabLayout = (TabLayout) view.findViewById(R.id.tabs_sub_owned);
    viewPager2 = view.findViewById(R.id.view_pager_owned);
    TabItem availableTab = (TabItem) view.findViewById(R.id.tabs_sub_owned_available);
    TabItem requestsTab = (TabItem) view.findViewById(R.id.tabs_sub_owned_requests);
    TabItem acceptedTab = (TabItem) view.findViewById(R.id.tabs_sub_owned_accepted);
    TabItem lentTab = (TabItem) view.findViewById(R.id.tabs_sub_owned_lent);

    // add fragments to tabs
    pagerAdapter = new PagerAdapter(getChildFragmentManager(), getLifecycle());

    Bundle bundle;
    ArrayList<Book> bookDataList = new ArrayList<>();
    bookDataList.add(new Book("owner", "T11_1", "title", "123", BookStatus.AVAILABLE,
        BookWorkflowStage.AVAILABLE, "borower"));
    bookDataList.add(new Book("owner", "T11_2", "title", "123", BookStatus.AVAILABLE,
        BookWorkflowStage.AVAILABLE, "borower"));
    bundle = bookActivity.setupBundle(R.layout.available, bookDataList,
        "These are all the books that you own that are available for other people to borrow");
    pagerAdapter.addFragment(
        new Pair<Class<? extends Fragment>, Bundle>(GenericListFragment.class, bundle));

    bookDataList = new ArrayList<>();
    bookDataList.add(new Book("owner", "T12_1", "title", "123", BookStatus.AVAILABLE,
        BookWorkflowStage.AVAILABLE, "borower"));
    bookDataList.add(new Book("owner", "T12_2", "title", "123", BookStatus.AVAILABLE,
        BookWorkflowStage.AVAILABLE, "borower"));
    bundle = bookActivity.setupBundle(R.layout.reqom, bookDataList,
        "These are all the books you own that other people have requested to borrow");
    pagerAdapter.addFragment(
        new Pair<Class<? extends Fragment>, Bundle>(GenericListFragment.class, bundle));

    bookDataList = new ArrayList<>();
    bookDataList.add(new Book("owner", "T13_1", "title", "123", BookStatus.AVAILABLE,
        BookWorkflowStage.AVAILABLE, "borower"));
    bookDataList.add(new Book("owner", "T13_2", "title", "123", BookStatus.AVAILABLE,
        BookWorkflowStage.AVAILABLE, "borower"));
    bundle = bookActivity.setupBundle(R.layout.accepted, bookDataList,
        "These are all the book requests that you have accepted");
    pagerAdapter.addFragment(
        new Pair<Class<? extends Fragment>, Bundle>(GenericListFragment.class, bundle));

    bookDataList = new ArrayList<>();
    bookDataList.add(new Book("owner", "T14_1", "title", "123", BookStatus.AVAILABLE,
        BookWorkflowStage.AVAILABLE, "borower"));
    bookDataList.add(new Book("owner", "T14_2", "title", "123", BookStatus.AVAILABLE,
        BookWorkflowStage.AVAILABLE, "borower"));
    bundle = bookActivity.setupBundle(R.layout.lent, bookDataList,
        "These are all your books that are being borrowed by other people");
    pagerAdapter.addFragment(
        new Pair<Class<? extends Fragment>, Bundle>(GenericListFragment.class, bundle));

    // Configure viewpager2 options and initialize page adapter
    viewPager2.setUserInputEnabled(false);
    viewPager2.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
    viewPager2.setOffscreenPageLimit(tabLayout.getTabCount());
    viewPager2.setAdapter(pagerAdapter);

    tabLayout.addOnTabSelectedListener(new OnTabSelectedListener() {
      @Override
      public void onTabSelected(Tab tab) {
        viewPager2.setCurrentItem(tab.getPosition());
      }

      @Override
      public void onTabUnselected(Tab tab) {

      }

      @Override
      public void onTabReselected(Tab tab) {

      }
    });
    return view;
  }
}