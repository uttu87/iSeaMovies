package com.androidtv.iseasoft.iSeaMovies.ui.details;

import android.support.v17.leanback.widget.DetailsOverviewLogoPresenter;
import android.support.v17.leanback.widget.FullWidthDetailsOverviewRowPresenter;
import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.RowPresenter;
import android.view.View;

public class CustomMovieDetailsPresenter extends FullWidthDetailsOverviewRowPresenter {
    public CustomMovieDetailsPresenter(Presenter detailsPresenter, DetailsOverviewLogoPresenter logoPresenter) {
        super(detailsPresenter, logoPresenter);
    }

    @Override
    protected void onBindRowViewHolder(RowPresenter.ViewHolder viewHolder, Object item){
        super.onBindRowViewHolder(viewHolder, item);

        FullWidthDetailsOverviewRowPresenter.ViewHolder vh = (FullWidthDetailsOverviewRowPresenter.ViewHolder)viewHolder;
        View v = vh.getOverviewView();
        v.setBackgroundColor(getBackgroundColor());
        v.findViewById(android.support.v17.leanback.R.id.details_overview_actions_background)
                .setBackgroundColor(getActionsBackgroundColor());
    }
}
