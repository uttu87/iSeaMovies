package com.androidtv.iseasoft.iSeaMovies;

import android.app.Application;

import com.androidtv.iseasoft.iSeaMovies.dagger.components.ApplicationComponent;
import com.androidtv.iseasoft.iSeaMovies.dagger.components.DaggerApplicationComponent;
import com.androidtv.iseasoft.iSeaMovies.dagger.modules.ApplicationModule;
import com.androidtv.iseasoft.iSeaMovies.dagger.modules.HttpClientModule;

import timber.log.Timber;

/**
 * Created by <a href="mailto:marcus@gabilheri.com">Marcus Gabilheri</a>
 *
 * @author Marcus Gabilheri
 * @version 1.0
 * @since 10/11/16.
 */

public class App extends Application {

    private static App instance;
    private ApplicationComponent mApplicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

        // Creates Dagger Graph
        mApplicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .httpClientModule(new HttpClientModule())
                .build();

        mApplicationComponent.inject(this);
    }

    public static App instance() {
        return instance;
    }

    public ApplicationComponent appComponent() {
        return mApplicationComponent;
    }

}
