package journal.nanodegree.capstone.prof.journal_capstonnanodegree.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import journal.nanodegree.capstone.prof.journal_capstonnanodegree.Adapter.NewsApiRecyclerAdapter;
import journal.nanodegree.capstone.prof.journal_capstonnanodegree.R;
import journal.nanodegree.capstone.prof.journal_capstonnanodegree.helpers.Config;
import journal.nanodegree.capstone.prof.journal_capstonnanodegree.helpers.GenericAsyncTask.NewsApiAsyncTask;
import journal.nanodegree.capstone.prof.journal_capstonnanodegree.helpers.Network.VerifyConnection;
import journal.nanodegree.capstone.prof.journal_capstonnanodegree.helpers.OptionsEntity;
import journal.nanodegree.capstone.prof.journal_capstonnanodegree.helpers.Services.UrgentWidgetService;
import journal.nanodegree.capstone.prof.journal_capstonnanodegree.helpers.SessionManagement;

/**
 * Created by Prof-Mohamed Atef on 1/3/2019.
 */

public class NewsApiFragment extends Fragment implements NewsApiAsyncTask.OnNewsTaskCompleted,
ArticlesMasterListFragment.OnSelectedArticleListener{

    private java.lang.String Urgent="urgent";
    String URL;
    private boolean TwoPane;
    RecyclerView recyclerView;
    SessionManagement sessionManagement;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        VerifyConnection verifyConnection=new VerifyConnection(getActivity());
        verifyConnection.checkConnection();
        if (verifyConnection.isConnected()){
            NewsApiAsyncTask newsApiAsyncTask=new NewsApiAsyncTask(this, getActivity());
            newsApiAsyncTask.execute(URL);
        }else {
            // Show Snack
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_ui_identifier,container,false);
        recyclerView=(RecyclerView)rootView.findViewById(R.id.recycler_view);
        Bundle bundle=getArguments();
        sessionManagement=new SessionManagement(getActivity());
        URL= bundle.getString(Urgent);
        if (rootView.findViewById(R.id.two_pane)!=null){
            TwoPane=true;
            Config.TwoPane=true;
        }
        return rootView;
    }

    @Override
    public void onNewsApiTaskCompleted(ArrayList<OptionsEntity> result) {
        if (result!=null){
            NewsApiRecyclerAdapter mAdapter=new NewsApiRecyclerAdapter(getActivity(),result, TwoPane);
            mAdapter.notifyDataSetChanged();
            RecyclerView.LayoutManager mLayoutManager=new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(mAdapter);
            String UrgentTextLines;
            String UrgentOneLine = null;
            for (OptionsEntity x : result){
                UrgentTextLines=x.getTITLE().toString();
                UrgentOneLine+=UrgentTextLines+".\n";
            }
            sessionManagement.createUrgentIntoPrefs(UrgentOneLine);
            UrgentWidgetService.startActionFillWidget(getActivity());
        }
    }

    @Override
    public void onArticleSelected(OptionsEntity optionsEntity, boolean TwoPane, int position) {

    }

    public interface NewsApiSelectedArticleListener {
        void onNewsApiArticleSelected(OptionsEntity optionsEntity, boolean TwoPane, int position);
    }
}