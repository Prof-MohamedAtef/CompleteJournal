package journal.nanodegree.capstone.prof.journal_capstonnanodegree.Adapter;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import butterknife.ButterKnife;
import journal.nanodegree.capstone.prof.journal_capstonnanodegree.Activities.WebViewerActivity;
import journal.nanodegree.capstone.prof.journal_capstonnanodegree.Fragments.ArticlesMasterListFragment;
import journal.nanodegree.capstone.prof.journal_capstonnanodegree.R;
import journal.nanodegree.capstone.prof.journal_capstonnanodegree.helpers.Config;
import journal.nanodegree.capstone.prof.journal_capstonnanodegree.helpers.OptionsEntity;
import static journal.nanodegree.capstone.prof.journal_capstonnanodegree.Activities.ArticleTypesListActivity.URL_KEY;

/**
 * Created by Prof-Mohamed Atef on 1/3/2019.
 */

public class NewsApiRecyclerAdapter extends RecyclerView.Adapter<NewsApiRecyclerAdapter.ViewHOlder> implements Serializable{

    private final String LOG_TAG = NewsApiRecyclerAdapter.class.getSimpleName();
    Context mContext;
    ArrayList<OptionsEntity> feedItemList;
    boolean TwoPane;


    //audio


    protected Chronometer chronometerTimer;
    protected SeekBar seekBar;
    protected LinearLayout linearLayoutRecorder;
    protected LinearLayout linearLayoutPlay;
    public MediaPlayer mPlayer;
    private String fileName = null;
    private int lastProgress = 0;
    private Handler mHandler = new Handler();
    private boolean isPlaying = false;

    public static String NOTHING_TODO="NoTHING_TODO";

    public NewsApiRecyclerAdapter(Context mContext, ArrayList<OptionsEntity> feedItemList, boolean twoPane) {
        this.mContext = mContext;
        this.feedItemList = feedItemList;
        TwoPane = twoPane;
    }

    @NonNull
    @Override
    public NewsApiRecyclerAdapter.ViewHOlder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_api_list_item, null);
        RecyclerView.ViewHolder viewHolder = new NewsApiRecyclerAdapter.ViewHOlder(view);
        return (NewsApiRecyclerAdapter.ViewHOlder) viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final NewsApiRecyclerAdapter.ViewHOlder holder, final int position) {
        final OptionsEntity feedItem = feedItemList.get(position);
        if (feedItem != null) {
            if (feedItem.getAUTHOR() != null && feedItem.getTITLE() != null) {
                holder.Author.setText(feedItem.getAUTHOR());
                holder.Title.setText(feedItem.getTITLE());
                if (feedItem.getDESCRIPTION() != null && feedItem.getNAME() != null) {
                    holder.Description.setText(feedItem.getDESCRIPTION());
                    holder.SourceName.setText(feedItem.getNAME());
                    if (feedItem.getPUBLISHEDAT() != null && feedItem.getURL() != null && feedItem.getURLTOIMAGE() != null) {
                        holder.Date.setText(feedItem.getPUBLISHEDAT());
                        Picasso.with(mContext).load(feedItem.getURLTOIMAGE())
                                .error(R.drawable.stanly)
                                .into(holder.Image);
                    } else {
                        holder.Date.setText("");
                    }
                } else {
                    holder.Description.setText("");
                    holder.SourceName.setText("");
                }
            } else {
                holder.Author.setText("");
                holder.Title.setText("");
            }
            holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // if two pane ---- > Web View
                    // if PHone --------> Web View
                    if (Config.ActivityNum!=0){
                        ((ArticlesMasterListFragment.OnSelectedArticleListener) mContext).onArticleSelected(feedItemList.get(position),TwoPane, position);
                    }

                    if (Config.ActivityNum==0){
                        if (feedItem.getURL() != null) {
                            String url=feedItem.getURL();
                            Intent intent=new Intent(mContext,WebViewerActivity.class);
                            intent.putExtra(URL_KEY,url);
                            mContext.startActivity(intent);
                        }
                    }else {
                        /*
                        Do nothing
                         */
                        Log.e(LOG_TAG, NOTHING_TODO);
                    }
                }
            });

            holder.imageViewPlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!isPlaying&&fileName!=null){
                        isPlaying=true;
                        startPlaying();
                    }else {
                        isPlaying=false;
                        stopPlaying();
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return (null != feedItemList ? feedItemList.size() : 0);
    }

    class ViewHOlder extends RecyclerView.ViewHolder {

        protected LinearLayout linearLayout;
        protected TextView Title;
        protected TextView Author;
        protected TextView Date;
        protected TextView Description;
        protected TextView SourceName;
        protected ImageView Image;
        protected WebView browser;
        protected ImageView imageViewPlay;
        public ViewHOlder(View converview) {
            super(converview);
            /*
            audio
             */
            chronometerTimer=(Chronometer)converview.findViewById(R.id.chronometerTimer);
            this.imageViewPlay=(ImageView)converview.findViewById(R.id.imageViewPlay);
            Config.imageViewPlay=this.imageViewPlay;
            seekBar=(SeekBar)converview.findViewById(R.id.seekBar);
            linearLayoutRecorder=(LinearLayout)converview.findViewById(R.id.linearLayoutRecorder);
            linearLayoutPlay=(LinearLayout) converview.findViewById(R.id.linearLayoutPlay);
            /*
            others
             */
            this.Title = (TextView) converview.findViewById(R.id.title);
            this.Author= (TextView) converview.findViewById(R.id.author);
            this.Date= (TextView) converview.findViewById(R.id.date_publish);
            this.Description= (TextView) converview.findViewById(R.id.description);
            this.SourceName= (TextView) converview.findViewById(R.id.source_name);
            this.Image =(ImageView)converview.findViewById(R.id.image);
            this.linearLayout=(LinearLayout)converview.findViewById(R.id.linearLayout);
            this.browser= (WebView) converview.findViewById(R.id.webview);
            if (Config.ActivityNum==0){
                initializeAudioPlayer();
            }else {
                linearLayoutPlay.setVisibility(View.GONE);
                linearLayoutRecorder.setVisibility(View.GONE);
            }
            if (Config.FragmentNewsApiNum==11){
                Image.setVisibility(View.GONE);
            }
        }
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            seekUpdation();
        }
    };

    private void seekUpdation() {
        if(mPlayer != null){
            int mCurrentPosition = mPlayer.getCurrentPosition() ;
            seekBar.setProgress(mCurrentPosition);
            lastProgress = mCurrentPosition;
        }
        mHandler.postDelayed(runnable, 100);
    }

    private void initializeAudioPlayer() {
        File root = android.os.Environment.getExternalStorageDirectory();
        File file = new File(root.getAbsolutePath() + "/VoiceRecorderSimplifiedCoding/Audios");
        if (file.exists()) {
            fileName = root.getAbsolutePath() + "/VoiceRecorderSimplifiedCoding/Audios/" + "1548717911705" + ".mp3";
        }

        if (fileName!=null){
            linearLayoutPlay.setVisibility(View.VISIBLE);
        }else {
            linearLayoutRecorder.setVisibility(View.GONE);
        }
    }

    private void stopPlaying() {
        try{
            mPlayer.release();
        }catch (Exception e){
            e.printStackTrace();
        }
        mPlayer = null;
        //showing the play button
        Config.imageViewPlay.setImageResource(R.drawable.ic_play);
        chronometerTimer.stop();
    }

    private void startPlaying() {
        mPlayer = new MediaPlayer();
        try {
//fileName is global string. it contains the Uri to the recently recorded audio.
            mPlayer.setDataSource(fileName);
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }
        //making the imageview pause button
        Config.imageViewPlay.setImageResource(R.drawable.ic_pause);
        seekBar.setProgress(lastProgress);
        mPlayer.seekTo(lastProgress);
        seekBar.setMax(mPlayer.getDuration());
        seekUpdation();
        chronometerTimer.start();

        /** once the audio is complete, timer is stopped here**/
        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Config.imageViewPlay.setImageResource(R.drawable.ic_play);
                isPlaying = false;
                chronometerTimer.stop();
            }
        });

        /** moving the track as per the seekBar's position**/
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if( mPlayer!=null && fromUser ){
                    //here the track's progress is being changed as per the progress bar
                    mPlayer.seekTo(progress);
                    //timer is being updated as per the progress of the seekbar
                    chronometerTimer.setBase(SystemClock.elapsedRealtime() - mPlayer.getCurrentPosition());
                    lastProgress = progress;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }
}