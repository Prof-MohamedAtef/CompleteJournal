package journal.nanodegree.capstone.prof.journal_capstonnanodegree.Activities;

import android.app.LoaderManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Date;
import butterknife.BindView;
import butterknife.ButterKnife;
import journal.nanodegree.capstone.prof.journal_capstonnanodegree.BuildConfig;
import journal.nanodegree.capstone.prof.journal_capstonnanodegree.Fragments.NewsApiFragment;
import journal.nanodegree.capstone.prof.journal_capstonnanodegree.Fragments.WebhoseApiFragment;
import journal.nanodegree.capstone.prof.journal_capstonnanodegree.Listeners.SnackBarLauncher;
import journal.nanodegree.capstone.prof.journal_capstonnanodegree.R;
import journal.nanodegree.capstone.prof.journal_capstonnanodegree.helpers.Config;
import journal.nanodegree.capstone.prof.journal_capstonnanodegree.helpers.OptionsEntity;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        NewsApiFragment.NewsApiSelectedArticleListener{

    private final String LOG_TAG = HomeActivity.class.getSimpleName();
    private ProgressDialog progressDialog;
    private Handler handler;
    private Toolbar toolbar;
    ImageView LogoImage;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    boolean isInternetConnected;
    Bundle webServiceNewsApi=null;
    Bundle webServiceWebHose=null;
    String apiKey,token;
    NewsApiFragment newsApiFragment;
    WebhoseApiFragment webhoseApiFragment;
    public static String POLITICS="Politics";
    public static String ARTS="arts";
    public static String SPORTS="sports";
    public static String REPORTS="reports";
    public static String FOOD="food";
    public static String FAMILY="family";
    public static String HERITAGE="heritage";
    public static String OPINIONS="opinions";
    public static String TECHNOLOGY="technology";
    public static String BUSINESS="business";

    private boolean checkConnection() {
        return isInternetConnected=isConnected();
    }

    public boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE); //NetworkApplication.getInstance().getApplicationContext()
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork!=null){
            return isInternetConnected= activeNetwork.isConnected();
        }else
            return isInternetConnected=false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setTheme(R.style.ArishTheme);
        apiKey= BuildConfig.ApiKey;
        token=BuildConfig.token;
        newsApiFragment=new NewsApiFragment();
        webhoseApiFragment=new WebhoseApiFragment();
        webServiceNewsApi=new Bundle();
        webServiceWebHose=new Bundle();
        handler = new Handler();

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setIcon(R.drawable.icon);

        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header=navigationView.getHeaderView(0);
        LogoImage=(ImageView)header.findViewById(R.id.russia_2018_img);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                if(menuItem.isChecked()) menuItem.setChecked(false);
                else menuItem.setChecked(true);
                drawerLayout.closeDrawers();
                switch (menuItem.getItemId()){
                    case R.id.publish:
                        Intent intent=new Intent(getApplicationContext(),AddArticleActivity.class);
                        startActivity(intent);
                        return true;
                    case R.id.urgent:
                        displayUrgent();
                        return true;
                    case R.id.politics:
//                        webServiceWebHose.putString("politics","http://webhose.io/filterWebContent?token="+token+"&format=json&ts=1543864001127&sort=crawled&q=thread.country%3AEG%20language%3Aarabic%20site_type%3Anews%20thread.title%3A%D8%B3%D9%8A%D8%A7%D8%B3%D8%A9");
//                        webhoseApiFragment.setArguments(webServiceWebHose);
//                        getSupportFragmentManager().beginTransaction()
//                                .replace(R.id.container_frame, webhoseApiFragment, "webHose")
//                                .commit();
                        Intent intent2=new Intent(getApplicationContext(),ArticleTypesListActivity.class);
                        intent2.putExtra("ArticleType",POLITICS);
                        startActivity(intent2);
                        return true;
                    case R.id.art_culture:
                        Intent intent3=new Intent(getApplicationContext(),ArticleTypesListActivity.class);
                        intent3.putExtra("ArticleType",ARTS);
                        startActivity(intent3);
//                        webServiceWebHose.putString("arts","http://webhose.io/filterWebContent?token="+token+"&format=json&ts=1543864086443&sort=crawled&q=thread.country%3AEG%20language%3Aarabic%20site_type%3Anews%20thread.title%3A%D9%81%D9%86%D9%88%D9%86");
//                        webhoseApiFragment.setArguments(webServiceWebHose);
//                        getSupportFragmentManager().beginTransaction()
//                                .replace(R.id.container_frame, webhoseApiFragment, "webHose")
//                                .commit();
                        return true;
                    case R.id.sports:
                        Intent intent4=new Intent(getApplicationContext(), ArticleTypesListActivity.class);
                        intent4.putExtra("ArticleType",SPORTS);
                        startActivity(intent4);
//                        webServiceNewsApi.putString("sports","https://newsapi.org/v2/top-headlines?country=eg&category=sports&apiKey="+apiKey);
//                        newsApiFragment.setArguments(webServiceNewsApi);
//                        getSupportFragmentManager().beginTransaction()
//                                .replace(R.id.container_frame, newsApiFragment, "newsApi")
//                                .commit();
                        return true;
                    case R.id.reports:
                        Intent intent5=new Intent(getApplicationContext(), ArticleTypesListActivity.class);
                        intent5.putExtra("ArticleType",REPORTS);
                        startActivity(intent5);
                        // get data from content provider or firebase
                        return true;
                    case R.id.food:
                        Intent intent1=new Intent(getApplicationContext(), ArticleTypesListActivity.class);
                        intent1.putExtra("ArticleType", FOOD);
                        startActivity(intent1);
//                        webServiceWebHose.putString("food","http://webhose.io/filterWebContent?token="+token+"&format=json&ts=1543863885301&sort=crawled&q=thread.country%3AEG%20language%3Aarabic%20site_type%3Anews%20thread.title%3A");
//                        webhoseApiFragment.setArguments(webServiceWebHose);
//                        getSupportFragmentManager().beginTransaction()
//                                .replace(R.id.container_frame, webhoseApiFragment, "webHose")
//                                .commit();
                        return true;
                    case R.id.family:
                        Intent intent6=new Intent(getApplicationContext(), ArticleTypesListActivity.class);
                        intent6.putExtra("ArticleType",FAMILY);
                        startActivity(intent6);
//                        webServiceWebHose.putString("family","http://webhose.io/filterWebContent?token="+token+"&format=json&ts=1545130799659&sort=crawled&q=thread.country%3AEG%20language%3Aarabic%20site_type%3Anews%20thread.title%3A%D8%A7%D9%84%D8%A3%D8%B3%D8%B1%D8%A9");
//                        webhoseApiFragment.setArguments(webServiceWebHose);
//                        getSupportFragmentManager().beginTransaction()
//                                .replace(R.id.container_frame, webhoseApiFragment, "webHose")
//                                .commit();
                        return true;
                    case R.id.heritage:
                        Intent intent7=new Intent(getApplicationContext(), ArticleTypesListActivity.class);
                        intent7.putExtra("ArticleType",HERITAGE);
                        startActivity(intent7);
//                        webServiceWebHose.putString("heritage","http://webhose.io/filterWebContent?token="+token+"&format=json&ts=1543863771070&sort=crawled&q=thread.country%3AEG%20language%3Aarabic%20site_type%3Anews%20thread.title%3A%D8%AA%D8%B1%D8%A7%D8%AB");
//                        webhoseApiFragment.setArguments(webServiceWebHose);
//                        getSupportFragmentManager().beginTransaction()
//                                .replace(R.id.container_frame, webhoseApiFragment, "webHose")
//                                .commit();
                        return true;
                    case R.id.opinions:
                        Intent intent8=new Intent(getApplicationContext(),ArticleTypesListActivity.class);
                        intent8.putExtra("ArticleType",OPINIONS);
                        startActivity(intent8);
//                        webServiceWebHose.putString("opinions","http://webhose.io/filterWebContent?token="+token+"&format=json&ts=1543852898977&sort=crawled&q=thread.country%3AEG%20language%3Aarabic%20site_type%3Anews%20thread.title%3A%D8%A2%D8%B1%D8%A7%D8%A1");
//                        webhoseApiFragment.setArguments(webServiceWebHose);
//                        getSupportFragmentManager().beginTransaction()
//                                .replace(R.id.container_frame, webhoseApiFragment, "webHose")
//                                .commit();
                        return true;
                    case R.id.technology:
                        Intent intent9=new Intent(getApplicationContext(), ArticleTypesListActivity.class);
                        intent9.putExtra("ArticleType",TECHNOLOGY);
                        startActivity(intent9);
//                        webServiceNewsApi.putString("technology","https://newsapi.org/v2/top-headlines?country=eg&category=technology&apiKey="+apiKey);
//                        newsApiFragment.setArguments(webServiceNewsApi);
//                        getSupportFragmentManager().beginTransaction()
//                                .replace(R.id.container_frame, newsApiFragment, "newsApi")
//                                .commit();
                        return true;
                    case R.id.business:
                        Intent intent10=new Intent(getApplicationContext(), ArticleTypesListActivity.class);
                        intent10.putExtra("ArticleType",BUSINESS);
                        startActivity(intent10);
//                        webServiceNewsApi.putString("business","https://newsapi.org/v2/top-headlines?country=eg&category=business&apiKey="+apiKey);
//                        newsApiFragment.setArguments(webServiceNewsApi);
//                        getSupportFragmentManager().beginTransaction()
//                                .replace(R.id.container_frame, newsApiFragment, "newsApi")
//                                .commit();
                        return true;
                    default:
                        return true;
                }
            }
        });
        // Initializing Drawer Layout and ActionBarToggle
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar, R.string.openDrawer, R.string.closeDrawer){
//            @Override
//            public boolean onOptionsItemSelected(MenuItem item) {
//                if (item != null && item.getItemId() == android.R.id.home) {
//                    if (drawerLayout.isDrawerOpen(Gravity.RIGHT)) {
//                        drawerLayout.closeDrawer(Gravity.RIGHT);
//                    }
//                    else {
//                        drawerLayout.openDrawer(Gravity.RIGHT);
//                    }
//                }
//                return false;
//            }
//
            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);

            }
            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
            }
        };
        //Setting the actionbarToggle to drawer layout
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        //calling sync state is necessay or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();

        checkConnection();
        if (isInternetConnected==true){
            displayUrgent();
        }
    }

    private void displayUrgent() {
        webServiceNewsApi.putString("urgent","https://newsapi.org/v2/top-headlines?country=eg&categor=%D8%B9%D8%A7%D8%AC%D9%84&apiKey="+apiKey);
        newsApiFragment.setArguments(webServiceNewsApi);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container_frame, newsApiFragment, "newsApi")
                .commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    @Override
    public void onNewsApiArticleSelected(OptionsEntity optionsEntity, boolean TwoPane, int position) {
        // redirect to article details activity
        Intent intent = new Intent(this, ArticleTypesListActivity.class);
        Config.position=position;
        if (TwoPane){
            intent.putExtra("TwoPane",TwoPane);
            intent.putExtra("optionsEntity", optionsEntity);
            intent.putExtra("position", position);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            this.startActivity(intent);
        }else {
            intent.putExtra("TwoPane",TwoPane);
            intent.putExtra("optionsEntity", optionsEntity);
            intent.putExtra("position", position);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            this.startActivity(intent);
        }
    }
}