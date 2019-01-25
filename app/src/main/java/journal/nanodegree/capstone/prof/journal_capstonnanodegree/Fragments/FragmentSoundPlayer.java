package journal.nanodegree.capstone.prof.journal_capstonnanodegree.Fragments;

import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import journal.nanodegree.capstone.prof.journal_capstonnanodegree.R;
import journal.nanodegree.capstone.prof.journal_capstonnanodegree.helpers.OptionsEntity;

/**
 * Created by Prof-Mohamed Atef on 1/10/2019.
 */

public class FragmentSoundPlayer extends android.app.Fragment implements ExoPlayer.EventListener{

    TrackSelector trackSelector;
    MediaSource AudioSource;
    DefaultDataSourceFactory dataSourceFactory;
    SimpleExoPlayer mSimpleExoPlayer;
    PlayerView playerView;
    String AudioString;
    Uri AudioUri;
    public static OptionsEntity optionsEntity;
    public static String KEY_optionsEntity="Options";
    private ImageView audio_muted;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_audio_player, container, false);
        playerView=(PlayerView) rootView.findViewById(R.id.player_view);
        audio_muted=(ImageView)rootView.findViewById(R.id.audio_muted);
        audio_muted.setVisibility(View.GONE);
        playerView.setVisibility(View.GONE);
        return rootView;
    }

    private void releasePlayer() {
        if (mSimpleExoPlayer != null) {
            mSimpleExoPlayer.stop();
            mSimpleExoPlayer.release();
            mSimpleExoPlayer = null;
            dataSourceFactory = null;
            AudioSource = null;
            trackSelector = null;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        releasePlayer();
    }

    @Override
    public void onStop() {
        super.onStop();
        releasePlayer();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        releasePlayer();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        releasePlayer();
    }

    @Override
    public void onResume() {
        super.onResume();
//        initAudioMediaPlayer(AudioUri);
        initializePlayer(AudioUri);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(KEY_optionsEntity,optionsEntity);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            optionsEntity = (OptionsEntity) savedInstanceState.getSerializable(KEY_optionsEntity);
            DisplayData(optionsEntity);
        } else if (savedInstanceState == null) {
            final Bundle bundle = getArguments();
            if (bundle != null) {
                optionsEntity = (OptionsEntity) bundle.getSerializable("twoPaneExtras");
                DisplayData(optionsEntity);
            }
        }
    }

    private void DisplayData(OptionsEntity optionsEntity) {
        AudioString = optionsEntity.getAudioFile();
        if (AudioString!=null){
        AudioUri = Uri.parse(AudioString);
        playerView.setVisibility(View.VISIBLE);
        initializePlayer(AudioUri);

        }else {
            audio_muted.setVisibility(View.VISIBLE);
            Drawable x =ContextCompat.getDrawable(getActivity(),R.drawable.audio_mute);
            Picasso.with(getActivity()).load(String.valueOf(x))
                    .error(R.drawable.audio_mute)
                    .into(audio_muted);
        }
    }

    public void initializePlayer(Uri AudioUri){
        try {
            if (mSimpleExoPlayer == null) {
                // Create an instance of the ExoPlayer.
                TrackSelector trackSelector = new DefaultTrackSelector();
                LoadControl loadControl = new DefaultLoadControl();
                mSimpleExoPlayer = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector, loadControl);
                playerView.setPlayer(mSimpleExoPlayer);
                // Set the ExoPlayer.EventListener to this activity.
                mSimpleExoPlayer.addListener(this);
                // Prepare the MediaSource.
                String userAgent = com.google.android.exoplayer2.util.Util.getUserAgent(getActivity(), "MoAtefBackingApp");
                MediaSource mediaSource = new ExtractorMediaSource(AudioUri, new DefaultDataSourceFactory(
                        getActivity(), userAgent), new DefaultExtractorsFactory(), null, null);
                mSimpleExoPlayer.prepare(mediaSource);
                mSimpleExoPlayer.setPlayWhenReady(true);
            }
        }catch (Exception e){
            Toast.makeText(getActivity(), getString(R.string.unavailable), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {

    }

    @Override
    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity(int reason) {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }

    @Override
    public void onSeekProcessed() {

    }
}