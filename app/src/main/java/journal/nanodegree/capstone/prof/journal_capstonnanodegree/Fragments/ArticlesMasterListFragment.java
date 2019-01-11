package journal.nanodegree.capstone.prof.journal_capstonnanodegree.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import journal.nanodegree.capstone.prof.journal_capstonnanodegree.BuildConfig;
import journal.nanodegree.capstone.prof.journal_capstonnanodegree.R;
import journal.nanodegree.capstone.prof.journal_capstonnanodegree.helpers.GenericAsyncTask.NewsApiAsyncTask;
import journal.nanodegree.capstone.prof.journal_capstonnanodegree.helpers.GenericAsyncTask.WebHoseApiAsyncTask;
import journal.nanodegree.capstone.prof.journal_capstonnanodegree.helpers.OptionsEntity;
import static journal.nanodegree.capstone.prof.journal_capstonnanodegree.Activities.HomeActivity.ARTS;
import static journal.nanodegree.capstone.prof.journal_capstonnanodegree.Activities.HomeActivity.BUSINESS;
import static journal.nanodegree.capstone.prof.journal_capstonnanodegree.Activities.HomeActivity.FAMILY;
import static journal.nanodegree.capstone.prof.journal_capstonnanodegree.Activities.HomeActivity.FOOD;
import static journal.nanodegree.capstone.prof.journal_capstonnanodegree.Activities.HomeActivity.HERITAGE;
import static journal.nanodegree.capstone.prof.journal_capstonnanodegree.Activities.HomeActivity.OPINIONS;
import static journal.nanodegree.capstone.prof.journal_capstonnanodegree.Activities.HomeActivity.POLITICS;
import static journal.nanodegree.capstone.prof.journal_capstonnanodegree.Activities.HomeActivity.REPORTS;
import static journal.nanodegree.capstone.prof.journal_capstonnanodegree.Activities.HomeActivity.SPORTS;
import static journal.nanodegree.capstone.prof.journal_capstonnanodegree.Activities.HomeActivity.TECHNOLOGY;
import static journal.nanodegree.capstone.prof.journal_capstonnanodegree.BuildConfig.token;

/**
 * Created by Prof-Mohamed Atef on 1/10/2019.
 */

public class ArticlesMasterListFragment extends android.app.Fragment implements NewsApiAsyncTask.OnNewsTaskCompleted, WebHoseApiAsyncTask.OnWebHoseTaskCompleted {

    private String URL;
    private RecyclerView recyclerView;
    private RecyclerView recyclerView_Horizontal;
    private boolean TwoPane;
    private ArrayList<OptionsEntity> UrgentArticlesList;
    private ArrayList<OptionsEntity> TypesArticlesList;
    private String token;
    private String apiKey;
    private static final String WEBHOSE = "http://webhose.io/filterWebContent?token=";
    private String NEWSAPI="https://newsapi.org/v2/top-headlines?country=eg&category=";
    private String WEBHOSEDETAILS="sort=crawled&q=thread.country%3AEG%20language%3Aarabic%20site_type%3Anews%20thread.";
    private String WebHoseVerifier, NewsApiVerifier;
    private String KEY_ArticleTypeArray="ArticleTypeArr";
    private String KEY_UrgentArray="UrgentArr";

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
        token= BuildConfig.token;
        apiKey= BuildConfig.ApiKey;
        if (savedInstanceState!=null){
            if (UrgentArticlesList.isEmpty()){
                UrgentArticlesList= (ArrayList<OptionsEntity>) savedInstanceState.getSerializable(KEY_UrgentArray);
                PopulateUrgentArticles(UrgentArticlesList);
            }
            if (TypesArticlesList.isEmpty()){
                TypesArticlesList=(ArrayList<OptionsEntity>) savedInstanceState.getSerializable(KEY_ArticleTypeArray);
                PopulateTypesList(TypesArticlesList);
            }
        }else {
            Intent intent= getActivity().getIntent();
            if (intent!=null&&intent.hasExtra(POLITICS)){
                URL=WEBHOSE+token+"&format=json&ts=1543864001127&"+WEBHOSEDETAILS+"title%3A%D8%B3%D9%8A%D8%A7%D8%B3%D8%A9";
                WebHoseVerifier=URL;
            }else if (intent!=null&&intent.hasExtra(ARTS)){
                URL=WEBHOSE+token+"&format=json&ts=1543864086443&"+WEBHOSEDETAILS+"title%3A%D9%81%D9%86%D9%88%D9%86";
                WebHoseVerifier=URL;
            }else if (intent!=null&&intent.hasExtra(SPORTS)){
                URL=NEWSAPI+"sports&apiKey="+apiKey;
                NewsApiVerifier=URL;
            }else if (intent!=null&&intent.hasExtra(REPORTS)){
                // get data from Content Provider of Firebase
            }else if (intent!=null&&intent.hasExtra(FOOD)){
                URL=WEBHOSE+token+"&format=json&ts=1543863885301&"+WEBHOSEDETAILS+"title%3A";
                WebHoseVerifier=URL;
            }else if (intent!=null&&intent.hasExtra(FAMILY)){
                URL=WEBHOSE+token+"&format=json&ts=1545130799659&"+WEBHOSEDETAILS+"title%3A%D8%A7%D9%84%D8%A3%D8%B3%D8%B1%D8%A9";
                WebHoseVerifier=URL;
            }else if (intent!=null&&intent.hasExtra(HERITAGE)){
                URL=WEBHOSE+token+"&format=json&ts=1543863771070&"+WEBHOSEDETAILS+"title%3A%D8%AA%D8%B1%D8%A7%D8%AB";
                WebHoseVerifier=URL;
            }else if (intent!=null&&intent.hasExtra(OPINIONS)){
                URL=WEBHOSE+token+"&format=json&ts=1543852898977&"+WEBHOSEDETAILS+"title%3A%D8%A2%D8%B1%D8%A7%D8%A1";
                WebHoseVerifier=URL;
            } else if (intent!=null&&intent.hasExtra(TECHNOLOGY)){
                URL=NEWSAPI+"technology&apiKey="+apiKey;
                NewsApiVerifier=URL;
            } else if (intent!=null&&intent.hasExtra(BUSINESS)){
                URL=NEWSAPI+"business&apiKey="+apiKey;
                NewsApiVerifier=URL;
            }
//            GetUrgentAsyncTask getUrgentAsyncTask=new GetUrgentAsyncTask(this);
//            getUrgentAsyncTask.execute(getResources().getString(R.string.UrgentAPI));
            if (WebHoseVerifier.equals(URL)){
                WebHoseVerifier=null;
                WebHoseApiAsyncTask webHoseApiAsyncTask=new WebHoseApiAsyncTask(this, getActivity());
                webHoseApiAsyncTask.execute(URL);
            }else if (NewsApiVerifier.equals(URL)){
                NewsApiVerifier=null;
                NewsApiAsyncTask newsApiAsyncTask=new NewsApiAsyncTask(this, getActivity());
                newsApiAsyncTask.execute(URL);
            }
//            GetArticlesTypesAsyncTask getArticlesTypesAsyncTask=new GetArticlesTypesAsyncTask(this);
//            getArticlesTypesAsyncTask.execute(URL);
        }
    }

    private void PopulateTypesList(ArrayList<OptionsEntity> typesArticlesList) {

    }

    private void PopulateUrgentArticles(ArrayList<OptionsEntity> urgentArticlesList) {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.articles_fragment_master_list,container,false);
        recyclerView=(RecyclerView)rootView.findViewById(R.id.recycler_view);
        recyclerView_Horizontal=(RecyclerView)rootView.findViewById(R.id.recycler_view_horizontal);
        if (rootView.findViewById(R.id.two_pane)!=null){
            TwoPane=true;
        }
        return rootView;
    }

    @Override
    public void onTaskCompleted(ArrayList<OptionsEntity> result) {
        if (result!=null){
        }
    }

    @Override
    public void onWebHoseTaskCompleted(ArrayList<OptionsEntity> result) {
        if (result!=null){
        }
    }
}