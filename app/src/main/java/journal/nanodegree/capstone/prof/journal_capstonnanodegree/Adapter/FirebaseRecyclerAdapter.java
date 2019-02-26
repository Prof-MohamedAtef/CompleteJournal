package journal.nanodegree.capstone.prof.journal_capstonnanodegree.Adapter;

import android.content.Context;
import android.database.Cursor;
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
import journal.nanodegree.capstone.prof.journal_capstonnanodegree.R;
import journal.nanodegree.capstone.prof.journal_capstonnanodegree.helpers.Config;
import journal.nanodegree.capstone.prof.journal_capstonnanodegree.helpers.Firebase.FirebaseDataHolder;

/**
 * Created by Prof-Mohamed Atef on 2/17/2019.
 */

public class FirebaseRecyclerAdapter extends RecyclerView.Adapter<FirebaseRecyclerAdapter.ViewHOlder> implements Serializable {

    Context mContext;
    ArrayList<FirebaseDataHolder> feedItemList;
    Cursor mCursor;
    boolean TwoPane;

    public FirebaseRecyclerAdapter(Context mContext, ArrayList<FirebaseDataHolder> feedItemList, boolean twoPane) {
        this.mContext = mContext;
        this.feedItemList = feedItemList;
        TwoPane = twoPane;

    }

    @NonNull
    @Override
    public FirebaseRecyclerAdapter.ViewHOlder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.firebase_reports_list_item, null);
        RecyclerView.ViewHolder viewHolder = new FirebaseRecyclerAdapter.ViewHOlder(view);
        return (FirebaseRecyclerAdapter.ViewHOlder) viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FirebaseRecyclerAdapter.ViewHOlder holder, final int position) {
        final FirebaseDataHolder feedItem = feedItemList.get(position);
        /*

         */
//        mCursor.moveToPosition(position);
//        holder.Author.setText(mCursor.getString(ArticleLoader.Query.AUTHOR));
//        holder.SourceName.setText(mCursor.getString(ArticleLoader.Query.Category));
//        holder.Date.setText(mCursor.getString(ArticleLoader.Query.PUBLISHED_DATE));
//        Picasso.with(mContext).load(mCursor.getString(ArticleLoader.Query.ImageFile_URL))
//                                    .error(R.drawable.breaking_news)
//                                    .into(holder.Image);
        /*

         */
        if (feedItem != null) {
            if (feedItem.getUserName() != null) {
                holder.Author.setText(feedItem.getUserName());
                if (feedItem.getCategoryID() != null) {
                    holder.SourceName.setText(feedItem.getCategoryID());
                    if (feedItem.getDate() != null ) {
                        holder.Date.setText(feedItem.getDate());
                        if (feedItem.getImageFileUri()!=null){
                            Picasso.with(mContext).load(feedItem.getImageFileUri())
                                    .error(R.drawable.breaking_news)
                                    .into(holder.Image);
                        }
                    } else {
                        holder.Date.setText("");
                    }
                } else {
                    holder.Description.setText("");
                    holder.SourceName.setText("");
                }
            } else {
                holder.Author.setText("");
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
        int size=0;
        if (feedItemList!=null){
            size=(null != feedItemList ? feedItemList.size() : 0);
        }if (mCursor!=null){
            size=(null != mCursor ? mCursor.getCount() : 0);
        }
        return size;
    }

    class ViewHOlder extends RecyclerView.ViewHolder {

        protected TextView Author;
        protected TextView Date;
        protected TextView Description;
        protected TextView SourceName;
        protected WebView browser;
        protected LinearLayout linearLayout;
        protected ImageView Image;

        public ViewHOlder(View converview) {
            super(converview);
            this.Author= (TextView) converview.findViewById(R.id.author);
            this.Date= (TextView) converview.findViewById(R.id.date_publish);
            this.SourceName= (TextView) converview.findViewById(R.id.source_name);
            this.browser= (WebView) converview.findViewById(R.id.webview);
            this.Image =(ImageView)converview.findViewById(R.id.image);
            this.linearLayout=(LinearLayout)converview.findViewById(R.id.linearLayout);
        }
    }
}