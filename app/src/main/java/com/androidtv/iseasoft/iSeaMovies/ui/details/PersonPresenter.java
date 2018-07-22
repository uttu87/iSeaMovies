package com.androidtv.iseasoft.iSeaMovies.ui.details;

import android.content.Context;
import android.support.v17.leanback.widget.ImageCardView;
import android.support.v17.leanback.widget.Presenter;
import android.view.ContextThemeWrapper;
import android.view.ViewGroup;

import com.androidtv.iseasoft.iSeaMovies.R;
import com.androidtv.iseasoft.iSeaMovies.dagger.modules.HttpClientModule;
import com.androidtv.iseasoft.iSeaMovies.data.models.CastMember;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

public class PersonPresenter extends Presenter {
    private Context mContext;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        if(mContext == null){
            mContext = new ContextThemeWrapper(parent.getContext(), R.style.PersonCardTheme);
        }
        return new ViewHolder(new ImageCardView(mContext));
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Object item) {
        ImageCardView view = (ImageCardView)viewHolder.view;
        CastMember member = (CastMember) item;

        Glide.with(view.getContext())
                .load(HttpClientModule.POSTER_URL + member.getProfilePath())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(view.getMainImageView());
        view.setTitleText(member.getName());
        view.setContentText(member.getCharacter());
    }

    @Override
    public void onUnbindViewHolder(ViewHolder viewHolder) {

    }
}
