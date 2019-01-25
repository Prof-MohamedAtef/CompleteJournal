package journal.nanodegree.capstone.prof.journal_capstonnanodegree.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;
import journal.nanodegree.capstone.prof.journal_capstonnanodegree.BuildConfig;
import journal.nanodegree.capstone.prof.journal_capstonnanodegree.Fragments.ArticlesMasterListFragment;
import journal.nanodegree.capstone.prof.journal_capstonnanodegree.Fragments.FragmentArticleViewer;
import journal.nanodegree.capstone.prof.journal_capstonnanodegree.Fragments.FragmentSoundPlayer;
import journal.nanodegree.capstone.prof.journal_capstonnanodegree.R;
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
import static journal.nanodegree.capstone.prof.journal_capstonnanodegree.BuildConfig.token;

public class ArticleTypesListActivity extends AppCompatActivity implements ArticlesMasterListFragment.OnSelectedArticleListener,
        SwipeRefreshLayout.OnRefreshListener{

    public static String WebHoseVerifier="null", NewsApiVerifier="null";
    private String URL;
    private boolean mTwoPaneUi;
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
//    @BindView(R.id.toolbar)
    Toolbar mToolbar;
//    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;
//    @BindView(R.id.coordinator_layout)
//    CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_types_list);
//        ButterKnife.bind(this);

        if (findViewById(R.id.ArticleDetails)!=null) {
            mTwoPaneUi = true;
        }else {
            mTwoPaneUi = false;
            mToolbar=(Toolbar)findViewById(R.id.toolbar);
            setSupportActionBar(mToolbar);
            assert getSupportActionBar()!=null;
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
            mSwipeRefreshLayout=(SwipeRefreshLayout)findViewById(R.id.swipe_refresh_layout);
            mSwipeRefreshLayout.setOnRefreshListener(this);
        }
        token= BuildConfig.token;
        apiKey= BuildConfig.ApiKey;

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
            // get data from Content Provider of Firebase
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

        Bundle bundle2=new Bundle();
        bundle2.putString(URL_KEY,URL);
        bundle2.putString(NEWSAPI_KEY,NewsApiVerifier);
        bundle2.putString(WebHoseAPIKEY,WebHoseVerifier);
        ArticlesMasterListFragment articlesMasterListFragment= new ArticlesMasterListFragment();
        articlesMasterListFragment.setArguments(bundle2);
        getFragmentManager().beginTransaction()
                .replace(R.id.master_list_fragment, articlesMasterListFragment, "frags")
                .commit();
        FragmentSoundPlayer fragmentSoundPlayer=new FragmentSoundPlayer();
        FragmentArticleViewer fragmentArticleViewer=new FragmentArticleViewer();
        if (findViewById(R.id.ArticleDetails)!=null){
            mTwoPaneUi=true;
            if (savedInstanceState!=null){
                getFragmentManager().beginTransaction()
                        .replace(R.id.Audio_container, fragmentSoundPlayer, "frags")
                        .commit();
                getFragmentManager().beginTransaction()
                        .replace(R.id.Article_container, fragmentArticleViewer, "frags")
                        .commit();
            }
        }else {
            mTwoPaneUi=false;
        }
    }


    @Override
    public void onArticleSelected(OptionsEntity optionsEntity, boolean TwoPane, int position) {
        if (mTwoPaneUi) {
            Bundle twoPaneExtras = new Bundle();
            twoPaneExtras.putSerializable("twoPaneExtras", optionsEntity);
            twoPaneExtras.putInt("position",position);
            FragmentSoundPlayer soundPlayer=new FragmentSoundPlayer();
            FragmentArticleViewer articleViewer=new FragmentArticleViewer();
            soundPlayer.setArguments(twoPaneExtras);
            articleViewer.setArguments(twoPaneExtras);
            getFragmentManager().beginTransaction()
                    .replace(R.id.Audio_container, soundPlayer, "Sound")
                    .commit();
            getFragmentManager().beginTransaction()
                    .replace(R.id.Article_container, articleViewer, "Article")
                    .commit();
        }else if (!mTwoPaneUi){
            Intent intent = new Intent(this, ArticleDetailsActivity.class)
                    .putExtra("ArticleInfo", optionsEntity)
                    .putExtra("position", position);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            this.startActivity(intent);
        }
    }

    @Override
    public void onRefresh() {
        Toast.makeText(getApplicationContext()," It's Okay", Toast.LENGTH_LONG);
    }
}