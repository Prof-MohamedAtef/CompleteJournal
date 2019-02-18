package journal.nanodegree.capstone.prof.journal_capstonnanodegree.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import journal.nanodegree.capstone.prof.journal_capstonnanodegree.Adapter.UrgentNewsAdapter;
import journal.nanodegree.capstone.prof.journal_capstonnanodegree.BuildConfig;
import journal.nanodegree.capstone.prof.journal_capstonnanodegree.R;
import journal.nanodegree.capstone.prof.journal_capstonnanodegree.helpers.GenericAsyncTask.NewsApiAsyncTask;
import journal.nanodegree.capstone.prof.journal_capstonnanodegree.helpers.Network.VerifyConnection;
import journal.nanodegree.capstone.prof.journal_capstonnanodegree.helpers.OptionsEntity;

import static journal.nanodegree.capstone.prof.journal_capstonnanodegree.Activities.ArticleTypesListActivity.TwoPANEExtras_KEY;
import static journal.nanodegree.capstone.prof.journal_capstonnanodegree.Activities.HomeActivity.UrgentURL;

/**
 * Created by Prof-Mohamed Atef on 2/18/2019.
 */

public class FragmentUrgentArticles extends android.app.Fragment implements NewsApiAsyncTask.OnNewsUrgentTaskCompleted{

    private String KEY_UrgentArray="UrgentArr";
    private String apiKey;
    private NewsApiAsyncTask newsApiAsyncTask;
    private GridLayoutManager layoutManager;
    private RecyclerView recyclerView_Horizontal;
    private boolean TwoPane;
    private ArrayList<OptionsEntity> UrgentArticlesList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiKey= BuildConfig.ApiKey;
        UrgentArticlesList=new ArrayList<OptionsEntity>();
        Bundle bundle=getArguments();
        TwoPane=bundle.getBoolean(TwoPANEExtras_KEY);
        if (savedInstanceState!=null){
            if (UrgentArticlesList.isEmpty()){
                UrgentArticlesList= (ArrayList<OptionsEntity>) savedInstanceState.getSerializable(KEY_UrgentArray);
                PopulateUrgentArticles(UrgentArticlesList);
            }
        }else {
            VerifyConnection verifyConnection=new VerifyConnection(getActivity());
            verifyConnection.checkConnection();
            if (verifyConnection.isConnected()){
                newsApiAsyncTask=new NewsApiAsyncTask((NewsApiAsyncTask.OnNewsUrgentTaskCompleted) this, getActivity());
                newsApiAsyncTask.execute(UrgentURL+apiKey);
            }else {
                // Show Snack
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.urgent_articles_fragment,container,false);
        recyclerView_Horizontal = (RecyclerView) rootView.findViewById(R.id.recycler_view_horizontal);
        return rootView;
    }

    @Override
    public void onNewsUrgentApiTaskCompleted(ArrayList<OptionsEntity> result) {
        if (result!=null&&result.size()>0){
            PopulateUrgentArticles(result);
            UrgentArticlesList=result;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (UrgentArticlesList!=null) {
            outState.putSerializable(KEY_UrgentArray , UrgentArticlesList);
        }
    }

    private void PopulateUrgentArticles(ArrayList<OptionsEntity> urgentArticlesList) {
        UrgentNewsAdapter mAdapter=new UrgentNewsAdapter(getActivity(),urgentArticlesList, TwoPane);
        mAdapter.notifyDataSetChanged();
        layoutManager=(GridLayoutManager)recyclerView_Horizontal.getLayoutManager();
        RecyclerView.LayoutManager mLayoutManager=new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView_Horizontal.setLayoutManager(mLayoutManager);
        recyclerView_Horizontal.setItemAnimator(new DefaultItemAnimator());
        recyclerView_Horizontal.setAdapter(mAdapter);
    }
}
