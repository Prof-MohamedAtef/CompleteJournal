package journal.nanodegree.capstone.prof.journal_capstonnanodegree.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;

import journal.nanodegree.capstone.prof.journal_capstonnanodegree.Fragments.ArticlesMasterListFragment;
import journal.nanodegree.capstone.prof.journal_capstonnanodegree.Fragments.NewsApiFragment;
import journal.nanodegree.capstone.prof.journal_capstonnanodegree.R;
import journal.nanodegree.capstone.prof.journal_capstonnanodegree.helpers.GenericAsyncTask.WebHoseApiAsyncTask;
import journal.nanodegree.capstone.prof.journal_capstonnanodegree.helpers.OptionsEntity;

/**
 * Created by Prof-Mohamed Atef on 1/11/2019.
 */

public class WebHoseRecyclerAdapter extends  RecyclerView.Adapter<WebHoseRecyclerAdapter.ViewHOlder> implements Serializable {

    private final String LOG_TAG = WebHoseRecyclerAdapter.class.getSimpleName();
    Context mContext;
    ArrayList<OptionsEntity> feedItemList;
    boolean TwoPane;

    public WebHoseRecyclerAdapter(Context mContext, ArrayList<OptionsEntity> feedItemList, boolean twoPane) {
        this.mContext = mContext;
        this.feedItemList = feedItemList;
        TwoPane = twoPane;

    }

    @NonNull
    @Override
    public WebHoseRecyclerAdapter.ViewHOlder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_api_list_item, null);
        RecyclerView.ViewHolder viewHolder = new WebHoseRecyclerAdapter.ViewHOlder(view);
        return (WebHoseRecyclerAdapter.ViewHOlder) viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull WebHoseRecyclerAdapter.ViewHOlder holder, final int position) {
        final OptionsEntity feedItem = feedItemList.get(position);
        if (feedItem!=null){
            if (feedItem.getAUTHOR()!=null&&feedItem.getTITLE()!=null){
                holder.Author.setText(feedItem.getAUTHOR());
                holder.Title.setText(feedItem.getTITLE());
                if (feedItem.getDESCRIPTION()!=null&&feedItem.getNAME()!=null){
                    holder.Description.setText(feedItem.getDESCRIPTION());
                    holder.SourceName.setText(feedItem.getNAME());
                    String ImagePath=feedItem.getURLTOIMAGE().toString();
                    if (feedItem.getPUBLISHEDAT()!=null&&feedItem.getURLTOIMAGE()!=null){
                        holder.Date.setText(feedItem.getPUBLISHEDAT());
                        if (ImagePath!=null&&!ImagePath.equals("")){
                            Picasso.with(mContext).load(ImagePath)
                                    .error(R.drawable.stanly)
                                    .into(holder.Image);
                        }else {
                            Picasso.with(mContext).load(R.drawable.stanly).into(holder.Image);
                            Log.v(LOG_TAG, "No URL To Image Returned" );
                        }
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
                    ((ArticlesMasterListFragment.OnSelectedArticleListener) mContext).onArticleSelected(feedItemList.get(position),TwoPane, position);
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
        protected LinearLayout linearLayout;

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