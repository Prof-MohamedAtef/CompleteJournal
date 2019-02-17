package journal.nanodegree.capstone.prof.journal_capstonnanodegree.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import journal.nanodegree.capstone.prof.journal_capstonnanodegree.BuildConfig;
import journal.nanodegree.capstone.prof.journal_capstonnanodegree.Fragments.ArticlesMasterListFragment;
import journal.nanodegree.capstone.prof.journal_capstonnanodegree.Fragments.FragmentArticleViewer;
import journal.nanodegree.capstone.prof.journal_capstonnanodegree.Fragments.FragmentSoundPlayer;
import journal.nanodegree.capstone.prof.journal_capstonnanodegree.Fragments.NoInternetFragment;
import journal.nanodegree.capstone.prof.journal_capstonnanodegree.R;
import journal.nanodegree.capstone.prof.journal_capstonnanodegree.helpers.Config;
import journal.nanodegree.capstone.prof.journal_capstonnanodegree.helpers.Firebase.FirebaseHelper;
import journal.nanodegree.capstone.prof.journal_capstonnanodegree.helpers.Network.SnackBarClassLauncher;
import journal.nanodegree.capstone.prof.journal_capstonnanodegree.helpers.Network.VerifyConnection;
import journal.nanodegree.capstone.prof.journal_capstonnanodegree.helpers.OptionsEntity;
import static journal.nanodegree.capstone.prof.journal_capstonnanodegree.Activities.HomeActivity.ARTS;
import static journal.nanodegree.capstone.prof.journal_capstonnanodegree.Activities.HomeActivity.ArticleType;
import static journal.nanodegree.capstone.prof.journal_capstonnanodegree.Activities.HomeActivity.BUSINESS;
import static journal.nanodegree.capstone.prof.journal_capstonnanodegree.Activities.HomeActivity.FAMILY;
import static journal.nanodegree.capstone.prof.journal_capstonnanodegree.Activities.HomeActivity.FOOD;
import static journal.nanodegree.capstone.prof.journal_capstonnanodegree.Activities.HomeActivity.HERITAGE;
import static journal.nanodegree.capstone.prof.journal_capstonnanodegree.Activities.HomeActivity.OPINIONS;
import static journal.nanodegree.capstone.prof.journal_capstonnanodegree.Activities.HomeActivity.POLITICS;
import static journal.nanodegree.capstone.prof.journal_capstonnanodegree.Activities.HomeActivity.REPORTS;
import static journal.nanodegree.capstone.prof.journal_capstonnanodegree.Activities.HomeActivity.SPORTS;
import static journal.nanodegree.capstone.prof.journal_capstonnanodegree.Activities.HomeActivity.TECHNOLOGY;
import static journal.nanodegree.capstone.prof.journal_capstonnanodegree.Fragments.FragmentSoundPlayer.optionsEntity;

public class ArticleTypesListActivity extends AppCompatActivity implements ArticlesMasterListFragment.OnSelectedArticleListener,
        SwipeRefreshLayout.OnRefreshListener{

    public static String WebHoseVerifier="null", NewsApiVerifier="null";
    private String URL;
    private boolean mTwoPaneUi=false;
    private static final String WEBHOSE = "http://webhose.io/filterWebContent?token=";
    private String NEWSAPI="https://newsapi.org/v2/top-headlines?country=eg&category=";
    private String WEBHOSEDETAILS="sort=crawled&q=thread.country%3AEG%20language%3Aarabic%20site_type%3Anews%20thread.";
    private String token;
    private String apiKey;
    public static String URL_KEY="URL_KEY";
    public static String NEWSAPI_KEY="NEWSAPIKEY";
    public static String WebHoseAPIKEY="WenHoseAPIKEY";
    private String Arts;
    private String ArticleType_;
    public static String TwoPANEExtras_KEY="twoPaneExtras";
    private String Position_KEY="position";
    private String ArticleInfo_KEY="ArticleInfo";
    public static final String Frags_KEY="frags";
    private String SoundFrag_KEY="Sound";
    private String ArticleFrag_KEY="Article";
    Toolbar mToolbar;
    SwipeRefreshLayout mSwipeRefreshLayout;
    private int Activity_Num=1;
    NoInternetFragment noInternetFragment;
    @BindView(R.id.master_list_fragment)
    FrameLayout master_list_fragment;
    private boolean ContentProviderHasData;
    FirebaseHelper firebaseHelper;
    private DatabaseReference mDatabase;
    ArrayList<OptionsEntity> FirebaseArticlesList;
    private String KEY;
    private String Category_STR;
    private String Description_STR;
    private String ImageFile_STR;
    private String Title_STR;
    private String Email_STR;
    private ArrayList<OptionsEntity> Diarylist;
    private String ArticlesList_KEY="ArticlesList_KEY";


    @Override
    public void onStart() {
        super.onStart();
        firebaseHelper=new FirebaseHelper();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_types_list);
        ButterKnife.bind(this);
        noInternetFragment=new NoInternetFragment();
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        if (findViewById(R.id.coordinator_layout_twoPane)!=null) {
            mTwoPaneUi = true;
        } else {
            mTwoPaneUi = false;
        }
        FirebaseArticlesList=new ArrayList<>();
        Config.ActivityNum=Activity_Num;
        token= BuildConfig.token;
        apiKey= BuildConfig.ApiKey;
        if (mDatabase==null){
            FirebaseDatabase database= FirebaseDatabase.getInstance();
            mDatabase=database.getInstance().getReference();
        }
        Bundle bundle=getIntent().getExtras();
        ArticleType_=bundle.getString(ArticleType);
        if (ArticleType_.equals(ARTS)){
            URL=WEBHOSE+token+"&format=json&ts=1543864086443&"+WEBHOSEDETAILS+"title%3A%D9%81%D9%86%D9%88%D9%86";
            WebHoseVerifier=URL;
        }else if (ArticleType_.equals(POLITICS)){
            URL=WEBHOSE+token+"&format=json&ts=1543864001127&"+WEBHOSEDETAILS+"title%3A%D8%B3%D9%8A%D8%A7%D8%B3%D8%A9";
            WebHoseVerifier=URL;
        }else if (ArticleType_.equals(SPORTS)){
            URL=NEWSAPI+"sports&apiKey="+apiKey;
            NewsApiVerifier=URL;
        }else if (ArticleType_.equals(REPORTS)){
            // get data from Content Provider or Firebase
            VerifyConnection verifyConnection=new VerifyConnection(getApplicationContext());
            verifyConnection.checkConnection();
            if (verifyConnection.isConnected()){
                // get data from firebase
                FetchDataFromFirebase();
            }else {
                //if no data in content provider // redirect to add article activity
                // Show Snack
                ContentProviderHasData=false;
                if (!ContentProviderHasData){
                    snackbar=NetCut();
                    snackBarLauncher.SnackBarInitializer(snackbar);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container_frame, noInternetFragment, "newsApi")
                            .commit();
                }
            }
        }else if (ArticleType_.equals(FOOD)){
            URL=WEBHOSE+token+"&format=json&ts=1543863885301&"+WEBHOSEDETAILS+"title%3A";
            WebHoseVerifier=URL;
        }else if (ArticleType_.equals(FAMILY)){
            URL=WEBHOSE+token+"&format=json&ts=1545130799659&"+WEBHOSEDETAILS+"title%3A%D8%A7%D9%84%D8%A3%D8%B3%D8%B1%D8%A9";
            WebHoseVerifier=URL;
        }else if (ArticleType_.equals(HERITAGE)){
            URL=WEBHOSE+token+"&format=json&ts=1543863771070&"+WEBHOSEDETAILS+"title%3A%D8%AA%D8%B1%D8%A7%D8%AB";
            WebHoseVerifier=URL;
        }else if (ArticleType_.equals(OPINIONS)){
            URL=WEBHOSE+token+"&format=json&ts=1543852898977&"+WEBHOSEDETAILS+"title%3A%D8%A2%D8%B1%D8%A7%D8%A1";
            WebHoseVerifier=URL;
        }else if (ArticleType_.equals(TECHNOLOGY)){
            URL=NEWSAPI+"technology&apiKey="+apiKey;
            NewsApiVerifier=URL;
        }else if (ArticleType_.equals(BUSINESS)){
            URL=NEWSAPI+"business&apiKey="+apiKey;
            NewsApiVerifier=URL;
        }

        Diarylist= new ArrayList<>();
        Bundle bundle2=new Bundle();
        bundle2.putString(URL_KEY,URL);
        bundle2.putString(NEWSAPI_KEY,NewsApiVerifier);
        bundle2.putString(WebHoseAPIKEY,WebHoseVerifier);
        bundle2.putBoolean(TwoPANEExtras_KEY,mTwoPaneUi);
        if (Diarylist.size()>0){
            bundle2.putSerializable(ArticlesList_KEY,Diarylist);
        }
        ArticlesMasterListFragment articlesMasterListFragment= new ArticlesMasterListFragment();
        articlesMasterListFragment.setArguments(bundle2);
        getFragmentManager().beginTransaction()
                .replace(R.id.master_list_fragment, articlesMasterListFragment, Frags_KEY)
                .commit();
        FragmentSoundPlayer fragmentSoundPlayer=new FragmentSoundPlayer();
        FragmentArticleViewer fragmentArticleViewer=new FragmentArticleViewer();
        if (findViewById(R.id.coordinator_layout_twoPane)!=null){
            getFragmentManager().beginTransaction()
                    .replace(R.id.Audio_container, fragmentSoundPlayer, Frags_KEY)
                    .commit();
            getFragmentManager().beginTransaction()
                    .replace(R.id.Article_container, fragmentArticleViewer, Frags_KEY)
                    .commit();
        }
    }

    private void FetchDataFromFirebase() {
        DatabaseReference ThoughtsRef=mDatabase.child("data");
        ValueEventListener valueEventListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                FirebaseArticlesList.clear();
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    KEY=ds.getKey();
                    Category_STR = ds.child("categoryID").getValue(String.class);
                    Description_STR = ds.child("description").getValue(String.class);
                    ImageFile_STR= ds.child("imageFileUri").getValue(String.class);
                    Title_STR= ds.child("title").getValue(String.class);
                    Email_STR= ds.child("userEmail").getValue(String.class);
                    Log.d("TAG", Email_STR+ " / " + Category_STR+ " / " + Description_STR+ " / " + ImageFile_STR+ " / " + Title_STR);

                    optionsEntity=new OptionsEntity(KEY, Email_STR, Category_STR,Title_STR,ImageFile_STR,Description_STR);
                    Diarylist.add(optionsEntity);
                }







//                if (Diarylist.size()>0) {
//                    int maxID = DB.getInitialMaxValue();
//                    if (maxID > 0) {
//                        boolean Deleted = DB.deleteAll();
//                        if (Deleted == true) {
//                            for (final OptionsEntity optionsEntity : Diarylist) {
//                                // Delete then Insert
//                                boolean inserted = DB.InsertToDiary(optionsEntity.getKey(),optionsEntity.getThoughtDate(),optionsEntity.getThought_Str(),optionsEntity.getStatus_ImgUrl(),optionsEntity.getStatus_Str(),optionsEntity.getEmail());
//                                if (inserted == true) {
//                                }
//                            }
//                        }
//                    } else {
//                        // Delete then Insert
//                        for (OptionsEntity optionsEntity : Diarylist) {
//                            boolean inserted = DB.InsertToDiary(optionsEntity.getKey(),optionsEntity.getThoughtDate(),optionsEntity.getThought_Str(),optionsEntity.getStatus_ImgUrl(),optionsEntity.getStatus_Str(),optionsEntity.getEmail());
//                            if (inserted == true) {
//                            }
//                        }
//
//                    }
//                    LaunchDiaryContent(Diarylist);
//                }else {
//                    Intent intent = new Intent(getActivity(), AddToDiary.class);
//                    getActivity().startActivity(intent);
//                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        ThoughtsRef.addListenerForSingleValueEvent(valueEventListener);
    }

    @Override
    public void onArticleSelected(OptionsEntity optionsEntity, boolean TwoPane, int position) {
        if (mTwoPaneUi) {
            Bundle twoPaneExtras = new Bundle();
            twoPaneExtras.putSerializable(TwoPANEExtras_KEY, optionsEntity);
            twoPaneExtras.putInt(Position_KEY,position);
            FragmentSoundPlayer soundPlayer=new FragmentSoundPlayer();
            FragmentArticleViewer articleViewer=new FragmentArticleViewer();
            soundPlayer.setArguments(twoPaneExtras);
            articleViewer.setArguments(twoPaneExtras);
            getFragmentManager().beginTransaction()
                    .replace(R.id.Audio_container, soundPlayer, SoundFrag_KEY)
                    .commit();
            getFragmentManager().beginTransaction()
                    .replace(R.id.Article_container, articleViewer, ArticleFrag_KEY)
                    .commit();
        }else if (!mTwoPaneUi){
            Intent intent = new Intent(this, ArticleDetailsActivity.class)
                    .putExtra(ArticleInfo_KEY, optionsEntity)
                    .putExtra(Position_KEY, position);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            this.startActivity(intent);
        }
    }

    @Override
    public void onRefresh() {
        Toast.makeText(getApplicationContext(),getString(R.string.okay), Toast.LENGTH_LONG);
    }

    private Snackbar NetCut() {
        return snackbar= Snackbar
                .make(master_list_fragment, getApplicationContext().getResources().getString(R.string.no_internet), Snackbar.LENGTH_LONG)
                .setAction(getApplicationContext().getResources().getString(R.string.retry), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SnackBasedConnection();
                    }
                });

    }

    private void SnackBasedConnection() {
        VerifyConnection verifyConnection=new VerifyConnection(getApplicationContext());
        verifyConnection.checkConnection();
        if (verifyConnection.isConnected()){
            // get fata from firebase
        }else {
            //if no data in content provider
            // Show Snack
            snackbar=NetCut();
            snackBarLauncher.SnackBarInitializer(snackbar);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container_frame, noInternetFragment, "newsApi")
                    .commit();
        }
    }

    Snackbar snackbar;
    SnackBarClassLauncher snackBarLauncher;
}