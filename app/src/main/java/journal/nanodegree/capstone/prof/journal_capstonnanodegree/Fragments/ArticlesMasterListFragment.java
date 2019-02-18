package journal.nanodegree.capstone.prof.journal_capstonnanodegree.Fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import journal.nanodegree.capstone.prof.journal_capstonnanodegree.Adapter.FirebaseRecyclerAdapter;
import journal.nanodegree.capstone.prof.journal_capstonnanodegree.Adapter.NewsApiRecyclerAdapter;
import journal.nanodegree.capstone.prof.journal_capstonnanodegree.Adapter.UrgentNewsAdapter;
import journal.nanodegree.capstone.prof.journal_capstonnanodegree.Adapter.WebHoseRecyclerAdapter;
import journal.nanodegree.capstone.prof.journal_capstonnanodegree.BuildConfig;
import journal.nanodegree.capstone.prof.journal_capstonnanodegree.R;
import journal.nanodegree.capstone.prof.journal_capstonnanodegree.helpers.Config;
import journal.nanodegree.capstone.prof.journal_capstonnanodegree.helpers.Firebase.FirebaseDataHolder;
import journal.nanodegree.capstone.prof.journal_capstonnanodegree.helpers.Firebase.FirebaseReportsAsyncTask;
import journal.nanodegree.capstone.prof.journal_capstonnanodegree.helpers.Firebase.FirebaseHelper;
import journal.nanodegree.capstone.prof.journal_capstonnanodegree.helpers.GenericAsyncTask.NewsApiAsyncTask;
import journal.nanodegree.capstone.prof.journal_capstonnanodegree.helpers.GenericAsyncTask.WebHoseApiAsyncTask;
import journal.nanodegree.capstone.prof.journal_capstonnanodegree.helpers.Network.VerifyConnection;
import journal.nanodegree.capstone.prof.journal_capstonnanodegree.helpers.OptionsEntity;
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
        NewsApiAsyncTask.OnNewsUrgentTaskCompleted,
FirebaseReportsAsyncTask.OnDownloadCompleted{
    String apiKey;
    private String URL;
    private RecyclerView recyclerView;
    private RecyclerView recyclerView_Horizontal;
    private boolean TwoPane;
    private DatabaseReference mDatabase;
    FirebaseHelper firebaseHelper;
    ArrayList<FirebaseDataHolder> FirebaseArticlesList;

    private FirebaseReportsAsyncTask firebaseReportsAsyncTask;
    private NewsApiAsyncTask newsApiAsyncTask;

    private String KEY;
    private String Category_STR;
    private String Description_STR;
    private String ImageFile_STR;
    private String Title_STR;
    private String Email_STR;
    public static String ArticlesList_KEY="ArticlesList_KEY";
    private String AudioFile_STR;
    private String UserName_STR;
    private String AudioFile_KEY="audioFileUri";
    private String Category_KEY="categoryID";
    private String Description_KEY="description";
    private String IMAGE_FILE_KEY="imageFileUri";
    private String TITLE_KEY="title";
    private String Token_STR;
    private String TokenID_KEY="tokenID";
    private String Email_KEY="userEmail";
    private String UserName_KEY="userName";
    private String Data_KEY="data";
    private String LOG_TAG="TAG";
    FirebaseDataHolder firebaseDataHolder;

    private ProgressDialog dialog;
    private String Date_KEY="date";
    private String Date_STR;

    @Override
    public void onStart() {
        super.onStart();
        firebaseHelper=new FirebaseHelper();
    }

    private ArrayList<OptionsEntity> UrgentArticlesList;
    private ArrayList<OptionsEntity> TypesArticlesList;
    private String WebHoseVerifier, NewsApiVerifier;
    private String KEY_ArticleTypeArray="ArticleTypeArr";
    private String KEY_UrgentArray="UrgentArr";
    private String MustImplementListener="must Implement OnSelectedArticleListener";
    private GridLayoutManager layoutManager;
    private int FragmentNewsApiNum=11;
    private int FragmentWebHoseApiNum=22;
    private int FragmentFirebaseApiNum=33;

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

    public ArrayList<FirebaseDataHolder> FetchDataFromFirebase() {
        DatabaseReference ThoughtsRef=mDatabase.child(Data_KEY);
        ValueEventListener valueEventListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                FirebaseArticlesList.clear();
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    KEY=ds.getKey();
                    AudioFile_STR = ds.child(AudioFile_KEY).getValue(String.class);
                    Category_STR = ds.child(Category_KEY).getValue(String.class);
                    Date_STR = ds.child(Date_KEY).getValue(String.class);
                    Description_STR = ds.child(Description_KEY).getValue(String.class);
                    ImageFile_STR= ds.child(IMAGE_FILE_KEY).getValue(String.class);
                    Title_STR= ds.child(TITLE_KEY).getValue(String.class);
                    Token_STR= ds.child(TokenID_KEY).getValue(String.class);
                    Email_STR= ds.child(Email_KEY).getValue(String.class);
                    UserName_STR= ds.child(UserName_KEY).getValue(String.class);
                    Log.d(LOG_TAG, AudioFile_STR+ " / " + Email_STR+ " / " + Category_STR+ " / " + Description_STR+ " / " + ImageFile_STR+ " / " + Title_STR+ " / " + Token_STR+ " / " + UserName_STR);

                    firebaseDataHolder=new FirebaseDataHolder(KEY, Title_STR, Description_STR, Category_STR, Token_STR, AudioFile_STR , ImageFile_STR, Date_STR, UserName_STR,Email_STR);
                    FirebaseArticlesList.add(firebaseDataHolder);
                }

                if (FirebaseArticlesList.size()>0){
                    PopulateFirebaseList(FirebaseArticlesList);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };

        ThoughtsRef.addListenerForSingleValueEvent(valueEventListener);
        return FirebaseArticlesList;
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
        FirebaseArticlesList=new ArrayList<>();
        if (mDatabase==null){
            FirebaseDatabase database= FirebaseDatabase.getInstance();
            mDatabase=database.getInstance().getReference();
        }
        if (Config.RetrieveFirebaseData){
            VerifyConnection verifyConnection=new VerifyConnection(getActivity());
            verifyConnection.checkConnection();
            if (verifyConnection.isConnected()){
//                firebaseReportsAsyncTask =new FirebaseReportsAsyncTask(this, getActivity(),mDatabase);
//                firebaseReportsAsyncTask.execute();
                FetchDataFromFirebase();
            }
        }else {
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
                VerifyConnection verifyConnection=new VerifyConnection(getActivity());
                verifyConnection.checkConnection();
                if (verifyConnection.isConnected()){
                    ConnectToAPIs();
                }else {
                    // Show Snack
                }
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
            newsApiAsyncTask=new NewsApiAsyncTask((NewsApiAsyncTask.OnNewsTaskCompleted) this, getActivity());
            newsApiAsyncTask.execute(URL);
        }
        if (!TwoPane){
            newsApiAsyncTask=new NewsApiAsyncTask((NewsApiAsyncTask.OnNewsUrgentTaskCompleted) this, getActivity());
            newsApiAsyncTask.execute(UrgentURL+apiKey);
        }
    }

    private void PopulateTypesList(ArrayList<OptionsEntity> typesArticlesList) {
        NewsApiRecyclerAdapter mAdapter=new NewsApiRecyclerAdapter(getActivity(),typesArticlesList, TwoPane);
        mAdapter.notifyDataSetChanged();
        RecyclerView.LayoutManager mLayoutManager=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        Config.FragmentNewsApiNum=FragmentNewsApiNum;
    }

    private void PopulateFirebaseList(ArrayList<FirebaseDataHolder> typesArticlesList) {
        FirebaseRecyclerAdapter mAdapter=new FirebaseRecyclerAdapter(getActivity(),typesArticlesList, TwoPane);
        mAdapter.notifyDataSetChanged();
        RecyclerView.LayoutManager mLayoutManager=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        Config.FragmentFirebaseApiNum=FragmentFirebaseApiNum;
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
            Config.ArrArticle=result;
        }
    }

    @Override
    public void onWebHoseTaskCompleted(ArrayList<OptionsEntity> result) {
        if (result!=null&&result.size()>0){
            TypesArticlesList=result;
            Config.ArrArticle=result;
            WebHoseRecyclerAdapter mAdapter=new WebHoseRecyclerAdapter(getActivity(),result, TwoPane);
            mAdapter.notifyDataSetChanged();
            RecyclerView.LayoutManager mLayoutManager=new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(mAdapter);
            Config.FragmentWebHoseApiNum=FragmentWebHoseApiNum;
        }
    }

    @Override
    public void onNewsUrgentApiTaskCompleted(ArrayList<OptionsEntity> result) {
        if (result!=null&&result.size()>0){
            PopulateUrgentArticles(result);
            UrgentArticlesList=result;
        }
    }

    @Override
    public void onDownloadTaskCompleted(ArrayList<FirebaseDataHolder> result) {
        if (result!=null&&result.size()>0){
            PopulateFirebaseList(result);
        }
    }


    public interface OnSelectedArticleListener {
        void onArticleSelected(OptionsEntity optionsEntity, boolean TwoPane, int position);
    }

    public interface OnFirebaseArticleSelectedListener {
        void onFirebaseArticleSelected(FirebaseDataHolder firebaseDataHolder, boolean TwoPane, int position);
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