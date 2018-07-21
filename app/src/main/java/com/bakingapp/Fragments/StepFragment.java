package com.bakingapp.Fragments;


import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bakingapp.R;
import com.bakingapp.Utilities.Singleton;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.google.android.exoplayer2.upstream.cache.CacheDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepFragment extends Fragment{

    @BindView(R.id.exo_play) PlayerView playerView;
    @BindView(R.id.text) TextView description;
    @BindView(R.id.thumbnail) ImageView thumbnail;

    SimpleExoPlayer player;

    public StepFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_step, container, false);
        ButterKnife.bind(this, view);

        String thumbnailURL = Singleton.getInstance(getContext()).getRecipes().get(getRecipe()).getSteps().get(getStep()).getThumbnailURL();
        if (!thumbnailURL.isEmpty() && !thumbnailURL.substring(thumbnailURL.length() - 3, thumbnailURL.length()).equals("mp4")){
            Picasso.get().load(thumbnailURL).into(thumbnail);
//            Toast.makeText(getContext(), thumbnailURL, Toast.LENGTH_SHORT).show();
        }else {
            Log.d("ERROR_THUMBNAIL", "MP4 FILE FOUND");
        }

        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelection = new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelection);
        player = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector);
        playerView.setPlayer(player);
        MediaSource mediaSource = new ExtractorMediaSource(Uri.parse(Singleton.getInstance(getContext()).getRecipes().get(getRecipe()).getSteps().get(getStep()).getVideoURL()),
                new DefaultDataSourceFactory(getContext(), getString(R.string.app_name)),
                new DefaultExtractorsFactory(),
                null,
                null);
        player.prepare(mediaSource);
        Log.d("VIDEO_LINK", Singleton.getInstance(getContext()).getRecipes().get(getRecipe()).getSteps().get(getStep()).getVideoURL());
        player.setPlayWhenReady(true);

        description.setText(Singleton.getInstance(getContext()).getRecipes().get(getRecipe()).getSteps().get(getStep()).getDescription());
        return view;
    }

    private int getRecipe(){
        return getArguments().getInt("recipePosition");
    }

    private int getStep(){
        return getArguments().getInt("stepPosition");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putLong("PLAYBACK_POSITION", player.getCurrentPosition());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null)
            player.seekTo(savedInstanceState.getInt("SCROLL_POSITION"));
        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public void onStop() {
        playerView.getPlayer().release();
        super.onStop();
    }

    @Override
    public void onPause() {
        playerView.getPlayer().release();
        super.onPause();
    }
}
