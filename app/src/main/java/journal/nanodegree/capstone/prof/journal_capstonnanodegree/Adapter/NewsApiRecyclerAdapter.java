package journal.nanodegree.capstone.prof.journal_capstonnanodegree.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;

import journal.nanodegree.capstone.prof.journal_capstonnanodegree.Fragments.ArticlesMasterListFragment;
import journal.nanodegree.capstone.prof.journal_capstonnanodegree.Fragments.NewsApiFragment;
import journal.nanodegree.capstone.prof.journal_capstonnanodegree.R;
import journal.nanodegree.capstone.prof.journal_capstonnanodegree.helpers.OptionsEntity;

/**
 * Created by Prof-Mohamed Atef on 1/3/2019.
 */

public class NewsApiRecyclerAdapter extends RecyclerView.Adapter<NewsApiRecyclerAdapter.ViewHOlder> implements Serializable {

    Context mContext;
    ArrayList<OptionsEntity> feedItemList;
    boolean TwoPane;

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
    public void onBindViewHolder(@NonNull NewsApiRecyclerAdapter.ViewHOlder holder, final int position) {
        final OptionsEntity feedItem = feedItemList.get(position);
        if (feedItem!=null){
            if (feedItem.getAUTHOR()!=null&&feedItem.getTITLE()!=null){
                holder.Author.setText(feedItem.getAUTHOR());
                holder.Title.setText(feedItem.getTITLE());
                if (feedItem.getDESCRIPTION()!=null&&feedItem.getNAME()!=null){
                   holder.Description.setText(feedItem.getDESCRIPTION());
                   holder.SourceName.setText(feedItem.getNAME());
                   if (feedItem.getPUBLISHEDAT()!=null&&feedItem.getURL()!=null&&feedItem.getURLTOIMAGE()!=null){
                       holder.Date.setText(feedItem.getPUBLISHEDAT());
                       Picasso.with(mContext).load(feedItem.getURLTOIMAGE())
                        .error(R.drawable.stanly)
                        .into(holder.Image);
                   }else {holder.Date.setText("");}
                }else {
                    holder.Description.setText("");
                    holder.SourceName.setText("");
                }
            }else {
                holder.Author.setText("");
                holder.Title.setText("");
            }
                holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((ArticlesMasterListFragment.OnSelectedArticleListener ) mContext).onArticleSelected(feedItemList.get(position),TwoPane, position);
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

        public ViewHOlder(View converview) {
            super(converview);
            this.Title = (TextView) converview.findViewById(R.id.title);
            this.Author= (TextView) converview.findViewById(R.id.author);
            this.Date= (TextView) converview.findViewById(R.id.date_publish);
            this.Description= (TextView) converview.findViewById(R.id.description);
            this.SourceName= (TextView) converview.findViewById(R.id.source_name);
            this.Image =(ImageView)converview.findViewById(R.id.image);
            this.linearLayout=(LinearLayout)converview.findViewById(R.id.linearLayout);
        }
    }
}