package com.androidtv.iseasoft.iSeaMovies.ui.main;

import android.os.Bundle;
import com.androidtv.iseasoft.iSeaMovies.ui.base.BaseTvActivity;


/**
 * Created by <a href="mailto:marcus@gabilheri.com">Marcus Gabilheri</a>
 *
 * @author Marcus Gabilheri
 * @version 1.0
 * @since 10/8/16.
 */

public class MainActivity extends BaseTvActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addFragment(MainFragment.newInstance());
    }
}
