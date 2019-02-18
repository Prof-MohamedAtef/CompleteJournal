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

import de.hdodenhof.circleimageview.CircleImageView;
import journal.nanodegree.capstone.prof.journal_capstonnanodegree.Fragments.ArticlesMasterListFragment;
import journal.nanodegree.capstone.prof.journal_capstonnanodegree.R;
import journal.nanodegree.capstone.prof.journal_capstonnanodegree.helpers.Config;
import journal.nanodegree.capstone.prof.journal_capstonnanodegree.helpers.Firebase.FirebaseDataHolder;
import journal.nanodegree.capstone.prof.journal_capstonnanodegree.helpers.OptionsEntity;

/**
 * Created by Prof-Mohamed Atef on 2/17/2019.
 */

public class FirebaseRecyclerAdapter extends RecyclerView.Adapter<FirebaseRecyclerAdapter.ViewHOlder> implements Serializable {

    Context mContext;
    ArrayList<FirebaseDataHolder> feedItemList;
    boolean TwoPane;

    public FirebaseRecyclerAdapter(Context mContext, ArrayList<FirebaseDataHolder> feedItemList, boolean twoPane) {
        this.mContext = mContext;
        this.feedItemList = feedItemList;
        TwoPane = twoPane;

    }

    @NonNull
    @Override
    public FirebaseRecyclerAdapter.ViewHOlder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_api_list_item, null);
        RecyclerView.ViewHolder viewHolder = new FirebaseRecyclerAdapter.ViewHOlder(view);
        return (FirebaseRecyclerAdapter.ViewHOlder) viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FirebaseRecyclerAdapter.ViewHOlder holder, final int position) {
        final FirebaseDataHolder feedItem = feedItemList.get(position);
        if (feedItem != null) {
            if (feedItem.getUserName() != null && feedItem.getTITLE() != null) {
                holder.Author.setText(feedItem.getUserName());
                holder.Title.setText(feedItem.getTITLE());
                if (feedItem.getDESCRIPTION() != null && feedItem.getCategoryID() != null) {
                    holder.Description.setText(feedItem.getDESCRIPTION());
                    holder.SourceName.setText(feedItem.getCategoryID());
                    if (feedItem.getDate() != null && feedItem.getImageFileUri() != null) {
                        holder.Date.setText(feedItem.getDate());
                        Picasso.with(mContext).load(feedItem.getImageFileUri())
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
                    if (Config.ActivityNum!=0){
                        ((ArticlesMasterListFragment.OnFirebaseArticleSelectedListener) mContext).onFirebaseArticleSelected(feedItemList.get(position),TwoPane, position);
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

        protected TextView Title;
        protected TextView Author;
        protected TextView Date;
        protected TextView Description;
        protected TextView SourceName;
        protected ImageView Image;
        protected WebView browser;
        protected LinearLayout linearLayout;

        public ViewHOlder(View converview) {
            super(converview);
            this.Title = (TextView) converview.findViewById(R.id.title);
            this.Author= (TextView) converview.findViewById(R.id.author);
            this.Date= (TextView) converview.findViewById(R.id.date_publish);
            this.Description= (TextView) converview.findViewById(R.id.description);
            this.SourceName= (TextView) converview.findViewById(R.id.source_name);
            this.Image =(ImageView)converview.findViewById(R.id.image);
            this.browser= (WebView) converview.findViewById(R.id.webview);
            this.linearLayout=(LinearLayout)converview.findViewById(R.id.linearLayout);
        }
    }
}