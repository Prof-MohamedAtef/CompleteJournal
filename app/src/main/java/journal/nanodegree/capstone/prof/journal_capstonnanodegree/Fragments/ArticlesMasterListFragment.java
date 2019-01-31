package journal.nanodegree.capstone.prof.journal_capstonnanodegree.Fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import journal.nanodegree.capstone.prof.journal_capstonnanodegree.Adapter.NewsApiRecyclerAdapter;
import journal.nanodegree.capstone.prof.journal_capstonnanodegree.Adapter.UrgentNewsAdapter;
import journal.nanodegree.capstone.prof.journal_capstonnanodegree.Adapter.WebHoseRecyclerAdapter;
import journal.nanodegree.capstone.prof.journal_capstonnanodegree.BuildConfig;
import journal.nanodegree.capstone.prof.journal_capstonnanodegree.R;
import journal.nanodegree.capstone.prof.journal_capstonnanodegree.helpers.GenericAsyncTask.NewsApiAsyncTask;
import journal.nanodegree.capstone.prof.journal_capstonnanodegree.helpers.GenericAsyncTask.WebHoseApiAsyncTask;
import journal.nanodegree.capstone.prof.journal_capstonnanodegree.helpers.Network.VerifyConnection;
import journal.nanodegree.capstone.prof.journal_capstonnanodegree.helpers.OptionsEntity;

import static journal.nanodegree.capstone.prof.journal_capstonnanodegree.Activities.ArticleTypesListActivity.Frags_KEY;
import static journal.nanodegree.capstone.prof.journal_capstonnanodegree.Activities.ArticleTypesListActivity.NEWSAPI_KEY;
import static journal.nanodegree.capstone.prof.journal_capstonnanodegree.Activities.ArticleTypesListActivity.TwoPANEExtras_KEY;
import static journal.nanodegree.capstone.prof.journal_capstonnanodegree.Activities.ArticleTypesListActivity.URL_KEY;
import static journal.nanodegree.capstone.prof.journal_capstonnanodegree.Activities.ArticleTypesListActivity.WebHoseAPIKEY;
import static journal.nanodegree.capstone.prof.journal_capstonnanodegree.Activities.HomeActivity.UrgentURL;

/**
 * Created by Prof-Mohamed Atef on 1/10/2019.
 */

public class ArticlesMasterListFragment extends android.app.Fragment implements NewsApiAsyncTask.OnNewsTaskCompleted,
        WebHoseApiAsyncTask.OnWebHoseTaskCompleted,
        NewsApiAsyncTask.OnNewsUrgentTaskCompleted{
    String apiKey;
    private String URL;
    private RecyclerView recyclerView;
    private RecyclerView recyclerView_Horizontal;
    private boolean TwoPane;
    private ArrayList<OptionsEntity> UrgentArticlesList;
    private ArrayList<OptionsEntity> TypesArticlesList;
    private String WebHoseVerifier, NewsApiVerifier;
    private String KEY_ArticleTypeArray="ArticleTypeArr";
    private String KEY_UrgentArray="UrgentArr";
    private String MustImplementListener="must Implement OnSelectedArticleListener";
    private GridLayoutManager layoutManager;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (UrgentArticlesList!=null) {
            outState.putSerializable(KEY_UrgentArray , UrgentArticlesList);
        }
        if (TypesArticlesList!=null){
            outState.putSerializable(KEY_ArticleTypeArray, TypesArticlesList);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle=getArguments();
        apiKey= BuildConfig.ApiKey;
        URL= bundle.getString(URL_KEY);
        NewsApiVerifier=bundle.getString(NEWSAPI_KEY);
        WebHoseVerifier=bundle.getString(WebHoseAPIKEY);
        TwoPane=bundle.getBoolean(TwoPANEExtras_KEY);
        if (savedInstanceState!=null){
            if (UrgentArticlesList.isEmpty()){
                UrgentArticlesList= (ArrayList<OptionsEntity>) savedInstanceState.getSerializable(KEY_UrgentArray);
                PopulateUrgentArticles(UrgentArticlesList);
            }
            if (TypesArticlesList.isEmpty()){
                TypesArticlesList=(ArrayList<OptionsEntity>) savedInstanceState.getSerializable(KEY_ArticleTypeArray);
                PopulateTypesList(TypesArticlesList);
                PopulateTypesList(TypesArticlesList);
            }
        }else {
            VerifyConnection verifyConnection=new VerifyConnection(getActivity());
            verifyConnection.checkConnection();
            if (verifyConnection.isConnected()){
                ConnectToAPIs();
            }else {
                // Show Snack
            }
        }
    }

    private void ConnectToAPIs() {
        if (WebHoseVerifier.equals(URL)){
            WebHoseVerifier=null;
            WebHoseApiAsyncTask webHoseApiAsyncTask=new WebHoseApiAsyncTask(this, getActivity());
            webHoseApiAsyncTask.execute(URL);
        }else if (NewsApiVerifier.equals(URL)){
            NewsApiVerifier=null;
            NewsApiAsyncTask newsApiAsyncTask=new NewsApiAsyncTask((NewsApiAsyncTask.OnNewsTaskCompleted) this, getActivity());
            newsApiAsyncTask.execute(URL);
        }
        NewsApiAsyncTask newsApiAsyncTask=new NewsApiAsyncTask((NewsApiAsyncTask.OnNewsUrgentTaskCompleted) this, getActivity());
        newsApiAsyncTask.execute(UrgentURL+apiKey);
    }

    private void PopulateTypesList(ArrayList<OptionsEntity> typesArticlesList) {
        NewsApiRecyclerAdapter mAdapter=new NewsApiRecyclerAdapter(getActivity(),typesArticlesList, TwoPane);
        mAdapter.notifyDataSetChanged();
        RecyclerView.LayoutManager mLayoutManager=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.articles_fragment_master_list,container,false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        recyclerView_Horizontal = (RecyclerView) rootView.findViewById(R.id.recycler_view_horizontal);
        return rootView;
    }

    @Override
    public void onNewsApiTaskCompleted(ArrayList<OptionsEntity> result) {
        if (result!=null&&result.size()>0){
            PopulateTypesList(result);
            TypesArticlesList=result;
        }
    }

    @Override
    public void onWebHoseTaskCompleted(ArrayList<OptionsEntity> result) {
        if (result!=null&&result.size()>0){
            TypesArticlesList=result;
            WebHoseRecyclerAdapter mAdapter=new WebHoseRecyclerAdapter(getActivity(),result, TwoPane);
            mAdapter.notifyDataSetChanged();
            RecyclerView.LayoutManager mLayoutManager=new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(mAdapter);
        }
    }

    @Override
    public void onNewsUrgentApiTaskCompleted(ArrayList<OptionsEntity> result) {
        if (result!=null&&result.size()>0){
            PopulateUrgentArticles(result);
            UrgentArticlesList=result;
        }
    }

    public interface OnSelectedArticleListener {
        void onArticleSelected(OptionsEntity optionsEntity, boolean TwoPane, int position);
    }

    OnSelectedArticleListener mCallback;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            mCallback= (OnSelectedArticleListener) context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString()+MustImplementListener);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try{
            mCallback= (OnSelectedArticleListener) activity;
        }catch (ClassCastException e){
            throw new ClassCastException(activity.toString()+MustImplementListener);
        }
    }
}