package journal.nanodegree.capstone.prof.journal_capstonnanodegree.Fragments;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import journal.nanodegree.capstone.prof.journal_capstonnanodegree.Adapter.NewsApiRecyclerAdapter;
import journal.nanodegree.capstone.prof.journal_capstonnanodegree.Adapter.UrgentNewsAdapter;
import journal.nanodegree.capstone.prof.journal_capstonnanodegree.Listeners.SnackBarLauncher;
import journal.nanodegree.capstone.prof.journal_capstonnanodegree.R;
import journal.nanodegree.capstone.prof.journal_capstonnanodegree.helpers.Config;
import journal.nanodegree.capstone.prof.journal_capstonnanodegree.helpers.Data.CursorTypeConverter;
import journal.nanodegree.capstone.prof.journal_capstonnanodegree.helpers.Data.NewsProvider;
import journal.nanodegree.capstone.prof.journal_capstonnanodegree.helpers.GenericAsyncTask.NewsApiAsyncTask;
import journal.nanodegree.capstone.prof.journal_capstonnanodegree.helpers.Network.VerifyConnection;
import journal.nanodegree.capstone.prof.journal_capstonnanodegree.helpers.OptionsEntity;
import journal.nanodegree.capstone.prof.journal_capstonnanodegree.helpers.Room.ArticlesEntity;
import journal.nanodegree.capstone.prof.journal_capstonnanodegree.helpers.Services.UrgentWidgetService;
import journal.nanodegree.capstone.prof.journal_capstonnanodegree.helpers.SessionManagement;
import journal.nanodegree.capstone.prof.journal_capstonnanodegree.helpers.Network.SnackBarClassLauncher;
import journal.nanodegree.capstone.prof.journal_capstonnanodegree.helpers.ViewModel.ArticlesViewModel;

import static journal.nanodegree.capstone.prof.journal_capstonnanodegree.Activities.ArticleTypesListActivity.CATEGORY_NAME;
import static journal.nanodegree.capstone.prof.journal_capstonnanodegree.Activities.ArticleTypesListActivity.CategoryName;
import static journal.nanodegree.capstone.prof.journal_capstonnanodegree.helpers.Data.CursorTypeConverter.URGENT_CATEGORY;
import static journal.nanodegree.capstone.prof.journal_capstonnanodegree.helpers.Data.NewsProvider.CONTENT_URI;

/**
 * Created by Prof-Mohamed Atef on 1/3/2019.
 */

public class NewsApiFragment extends Fragment implements NewsApiAsyncTask.OnNewsTaskCompleted,
ArticlesMasterListFragment.OnSelectedArticleListener, SnackBarLauncher {

    SnackBarClassLauncher snackBarLauncher;
    Snackbar snackbar;
    private java.lang.String Urgent="urgent";
    String URL;
    private boolean TwoPane;
    RecyclerView recyclerView;
    SessionManagement sessionManagement;
    @BindView(R.id.LinearUiIdentifier)
    LinearLayout LinearUiIdentifier;
    private Cursor cursor;
    private ArrayList<OptionsEntity> UrgentArticlesList;
    private CursorTypeConverter cursorTypeConverter;
    private String KEY_POSITION="KEY_POSITION";
    RecyclerView.LayoutManager mLayoutManager;
    private String KEY_Urgent="KEY_Urgent";

    private GridLayoutManager layoutManager;
    private String NewsApiFrag_KEY="NewsApiFrag_KEY";
    private ArticlesViewModel articlesViewModel;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        connectToApi();
    }


    private void initializeViewModel(){
        articlesViewModel = ViewModelProviders.of((FragmentActivity)getActivity()).get(ArticlesViewModel.class);
        articlesViewModel.setCategory(KEY_Urgent);
        articlesViewModel.getmObserverMediatorLiveDataListUrgentArticles().observe((LifecycleOwner) getActivity(), new Observer<List<ArticleEntity>>() {
            @Override
            public void onChanged(@Nullable List<ArticlesEntity> articleEntities) {
                if (articleEntities!=null){
                    if (articleEntities.size()>0){
                        PopulateUrgentArticles(articleEntities);
                    }else if (articleEntities.size()==0){
                        VerifyConnection verifyConnection=new VerifyConnection(getActivity());
                        if (verifyConnection.isConnected()){
                            connectToApi();
                        }else {
                            // no internet frag
                        }
                    }
                }
            }
        });
    }
    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState!=null){
            UrgentArticlesList= (ArrayList<OptionsEntity>) savedInstanceState.getSerializable(KEY_Urgent);
            PopulateUrgentArticles(UrgentArticlesList);
        }
    }

//    private void connectToApi() {
//        VerifyConnection verifyConnection=new VerifyConnection(getActivity());
//        verifyConnection.checkConnection();
//        if (verifyConnection.isConnected()){
//            checkSavedOfflineData(URGENT_CATEGORY);
//            NewsApiAsyncTask newsApiAsyncTask=new NewsApiAsyncTask(this, getActivity());
//            newsApiAsyncTask.execute(URL);
//        }else {
//            String selection= NewsProvider.CATEGORY+"=?";
//            String[] selectionArgs = new String[1];
//            selectionArgs[0] =URGENT_CATEGORY;
//            ContentResolver resolver=getActivity().getContentResolver();
//            cursor=resolver.query(CONTENT_URI, null, selection, selectionArgs, NewsProvider.CATEGORY);
////            cursor = getActivity().managedQuery(CONTENT_URI, null, null, null, NewsProvider.CATEGORY);
//            if (cursor!=null){
//                cursorTypeConverter=new CursorTypeConverter();
//                UrgentArticlesList= cursorTypeConverter.AddCursorToArrayList(cursor);
//                if (UrgentArticlesList.size()>0){
//                    PopulateUrgentArticles(UrgentArticlesList);
//                }
//            }else {
//                snackbar=NetCut();
//                snackBarLauncher.SnackBarInitializer(snackbar);
//                // Show Snack
//                // redirect no internet fragment
//            }
//        }
//    }

    private void checkSavedOfflineData(String category){
        String selection= NewsProvider.CATEGORY+"=?";
        String[] selectionArgs = new String[1];
        selectionArgs[0] = category;
        ContentResolver resolver=getActivity().getContentResolver();
        cursor=resolver.query(CONTENT_URI, null, selection, selectionArgs, NewsProvider.CATEGORY);
//        cursor = getActivity().managedQuery(CONTENT_URI, null, selection, selectionArgs, NewsProvider.CATEGORY);
        if (cursor.moveToFirst()) {
            ContentResolver CR = getActivity().getContentResolver();
            CR.delete(CONTENT_URI, null, null);
        }
    }

    private Snackbar NetCut() {
        return snackbar= Snackbar
                .make(LinearUiIdentifier, getActivity().getResources().getString(R.string.no_internet), Snackbar.LENGTH_LONG)
                .setAction(getActivity().getResources().getString(R.string.retry), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        connectToApi();
                    }
                });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_ui_identifier,container,false);
        ButterKnife.bind(this, rootView);
        recyclerView=(RecyclerView)rootView.findViewById(R.id.recycler_view);
        Bundle bundle=getArguments();
        snackBarLauncher=new SnackBarClassLauncher();
        sessionManagement=new SessionManagement(getActivity());
        URL= bundle.getString(Urgent);
        if (rootView.findViewById(R.id.two_pane)!=null){
            TwoPane=true;
            Config.TwoPane=true;
        }
        return rootView;
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(KEY_Urgent,UrgentArticlesList);
    }

    @Override
    public void onNewsApiTaskCompleted(ArrayList<ArticlesEntity> result) {
        if (result!=null){
            if (result.size()>0){
//                for (ArticlesEntity optionsEntity:result){
//                    ContentValues values = CursorTypeConverter.saveUrgentOptionsUisngContentProvider(optionsEntity);
//                    Uri uri = getActivity().getContentResolver().insert(
//                            CONTENT_URI, values);
//                }
                PopulateUrgentArticles(result);
            }
            String UrgentTextLines;
            String UrgentOneLine = null;
            for (ArticlesEntity x : result){
                UrgentTextLines=x.getTITLE().toString();
                UrgentOneLine+=UrgentTextLines+".\n";
            }
            sessionManagement.createUrgentIntoPrefs(UrgentOneLine);
            UrgentWidgetService.startActionFillWidget(getActivity());
        }
    }

    private void PopulateUrgentArticles(ArrayList<ArticlesEntity> result) {
        NewsApiRecyclerAdapter mAdapter=new NewsApiRecyclerAdapter(getActivity(),result, TwoPane);
        mAdapter.notifyDataSetChanged();
        mLayoutManager=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        snackBarLauncher.SnackBarLoadedData(LinearUiIdentifier,getActivity());
    }

    @Override
    public void onArticleSelected(ArticlesEntity optionsEntity, boolean TwoPane, int position) {
    }

    public interface NewsApiSelectedArticleListener {
        void onNewsApiArticleSelected(OptionsEntity optionsEntity, boolean TwoPane, int position);
    }

    @Override
    public void onNoInternetConnection() {
        snackbar=NetCut();
        snackBarLauncher.SnackBarInitializer(snackbar);
    }
}