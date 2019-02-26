package journal.nanodegree.capstone.prof.journal_capstonnanodegree.Fragments;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
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
import journal.nanodegree.capstone.prof.journal_capstonnanodegree.helpers.Config;
import journal.nanodegree.capstone.prof.journal_capstonnanodegree.helpers.Data.CursorTypeConverter;
import journal.nanodegree.capstone.prof.journal_capstonnanodegree.helpers.Data.NewsProvider;
import journal.nanodegree.capstone.prof.journal_capstonnanodegree.helpers.GenericAsyncTask.NewsApiAsyncTask;
import journal.nanodegree.capstone.prof.journal_capstonnanodegree.helpers.Network.VerifyConnection;
import journal.nanodegree.capstone.prof.journal_capstonnanodegree.helpers.OptionsEntity;

import static journal.nanodegree.capstone.prof.journal_capstonnanodegree.Activities.ArticleTypesListActivity.CATEGORY_NAME;
import static journal.nanodegree.capstone.prof.journal_capstonnanodegree.Activities.ArticleTypesListActivity.TwoPANEExtras_KEY;
import static journal.nanodegree.capstone.prof.journal_capstonnanodegree.Activities.HomeActivity.UrgentURL;
import static journal.nanodegree.capstone.prof.journal_capstonnanodegree.helpers.Data.CursorTypeConverter.URGENT_CATEGORY;
import static journal.nanodegree.capstone.prof.journal_capstonnanodegree.helpers.Data.NewsProvider.CONTENT_URI;

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
    private Cursor cursor;
    private CursorTypeConverter converter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiKey= BuildConfig.ApiKey;
        UrgentArticlesList=new ArrayList<OptionsEntity>();
        Bundle bundle=getArguments();
        TwoPane=bundle.getBoolean(TwoPANEExtras_KEY);
        converter=new CursorTypeConverter();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            if (UrgentArticlesList.isEmpty()) {
                UrgentArticlesList = (ArrayList<OptionsEntity>) savedInstanceState.getSerializable(KEY_UrgentArray);
                PopulateUrgentArticles(UrgentArticlesList);
            }
        } else {
            // Show Snack
            String selection = NewsProvider.CATEGORY + "=?";
            String[] selectionArgs = new String[1];
            selectionArgs[0] = URGENT_CATEGORY;
            ContentResolver resolver = getActivity().getContentResolver();
            cursor = resolver.query(CONTENT_URI, null, selection, selectionArgs, NewsProvider.CATEGORY);
//                    cursor = getActivity().managedQuery(CONTENT_URI, null, selection, selectionArgs, NewsProvider.CATEGORY);
            if (cursor != null) {
                UrgentArticlesList = converter.AddCursorToArrayList(cursor);
                if (UrgentArticlesList.size() > 0) {
                    PopulateUrgentArticles(UrgentArticlesList);
                }
            } else {
                // Show Snack
                // redirect no internet fragment
            }
        }
    }

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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.urgent_articles_fragment,container,false);
        recyclerView_Horizontal = (RecyclerView) rootView.findViewById(R.id.recycler_view_horizontal);
        return rootView;
    }

    @Override
    public void onNewsUrgentApiTaskCompleted(ArrayList<OptionsEntity> result) {
//        if (result!=null&&result.size()>0){
//            for (OptionsEntity optionsEntity:result){
//                ContentValues values = CursorTypeConverter.saveUrgentOptionsUisngContentProvider(optionsEntity);
//                Uri uri = getActivity().getContentResolver().insert(
//                        CONTENT_URI, values);
//            }
//
//            PopulateUrgentArticles(result);
//            UrgentArticlesList=result;
//            Config.ArrArticle=result;
//        }
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
