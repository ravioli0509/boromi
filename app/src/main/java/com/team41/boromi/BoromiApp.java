package com.team41.boromi;

import android.app.Application;
import com.team41.boromi.dagger.AppComponent;
import com.team41.boromi.dagger.DaggerAppComponent;

public class BoromiApp extends Application {

  public AppComponent appComponent = DaggerAppComponent.create();

}
