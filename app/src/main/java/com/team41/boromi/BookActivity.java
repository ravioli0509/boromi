package com.team41.boromi;

import android.os.Bundle;
import android.util.Pair;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener;
import com.google.android.material.tabs.TabLayout.Tab;
import com.team41.boromi.adapters.PagerAdapter;
import com.team41.boromi.book.OwnedFragment;
import com.team41.boromi.book.BorrowedFragment;
import com.team41.boromi.book.MapFragment;
import com.team41.boromi.book.SearchFragment;
import com.team41.boromi.book.SettingsFragment;
import java.util.ArrayList;

public class BookActivity extends AppCompatActivity {

  private ViewPager2 viewPager2;
  private PagerAdapter pagerAdapter;
  private TabLayout tabLayout;
  private ArrayList<Pair<Class<? extends Fragment>, Bundle>> fragmentList = new ArrayList<Pair<Class<? extends Fragment>, Bundle>>();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_book);

    tabLayout = findViewById(R.id.tabs_main);
    viewPager2 = findViewById(R.id.view_pager_main);
    TabItem tabBorrow = findViewById(R.id.tab_borrowed_books);
    TabItem tabOwned = findViewById(R.id.tab_owned_books);
    TabItem tabSearch = findViewById(R.id.tab_search);
    TabItem tabMap = findViewById(R.id.tab_location);
    TabItem tabSettings = findViewById(R.id.tab_settings);

    // Add fragments for each tab
    fragmentList.add(new Pair<>(OwnedFragment.class, null));
    fragmentList.add(new Pair<>(BorrowedFragment.class, null));
    fragmentList.add(new Pair<>(SearchFragment.class, null));
    fragmentList.add(new Pair<>(MapFragment.class, null));
    fragmentList.add(new Pair<>(SettingsFragment.class, null));

    // configure viewpager2 and initialize page adapter
    viewPager2.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
    viewPager2.setOffscreenPageLimit(tabLayout.getTabCount());
    viewPager2.setUserInputEnabled(false);
    pagerAdapter = new PagerAdapter(getSupportFragmentManager(), getLifecycle(), fragmentList);
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
  }
}
