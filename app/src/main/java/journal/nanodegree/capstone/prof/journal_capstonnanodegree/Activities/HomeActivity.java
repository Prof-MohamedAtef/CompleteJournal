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
import android.support.annotation.Nullable;
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
import android.view.ViewParent;
import android.widget.Adapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import journal.nanodegree.capstone.prof.journal_capstonnanodegree.BuildConfig;
import journal.nanodegree.capstone.prof.journal_capstonnanodegree.Fragments.NewsApiFragment;
import journal.nanodegree.capstone.prof.journal_capstonnanodegree.Fragments.NoInternetFragment;
import journal.nanodegree.capstone.prof.journal_capstonnanodegree.Fragments.WebhoseApiFragment;
import journal.nanodegree.capstone.prof.journal_capstonnanodegree.Listeners.SnackBarLauncher;
import journal.nanodegree.capstone.prof.journal_capstonnanodegree.R;
import journal.nanodegree.capstone.prof.journal_capstonnanodegree.helpers.Config;
import journal.nanodegree.capstone.prof.journal_capstonnanodegree.helpers.Network.SnackBarClassLauncher;
import journal.nanodegree.capstone.prof.journal_capstonnanodegree.helpers.Network.VerifyConnection;
import journal.nanodegree.capstone.prof.journal_capstonnanodegree.helpers.OptionsEntity;
import journal.nanodegree.capstone.prof.journal_capstonnanodegree.helpers.SessionManagement;

import static android.widget.Toast.LENGTH_LONG;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        NewsApiFragment.NewsApiSelectedArticleListener,
NoInternetFragment.onReloadInternetServiceListener{

    private final String LOG_TAG = HomeActivity.class.getSimpleName();
    private ProgressDialog progressDialog;
    Snackbar snackbar;
    SnackBarClassLauncher snackBarLauncher;
    private Handler handler;
    private Toolbar toolbar;
    ImageView LogoImage;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    Bundle webServiceNewsApi=null;
    Bundle webServiceWebHose=null;
    String apiKey,token;
    NewsApiFragment newsApiFragment;
    NoInternetFragment noInternetFragment;
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
    public static String UrgentURL="https://newsapi.org/v2/top-headlines?country=eg&categor=%D8%B9%D8%A7%D8%AC%D9%84&apiKey=";
    public static String POLITICS_URL="http://webhose.io/filterWebContent?token=43939f70-364f-4f3c-9c4f-84ac4f5ece38&format=json&ts=1543864001127&sort=crawled&q=thread.country%3AEG%20language%3Aarabic%20site_type%3Anews%20thread.title%3A%D8%B3%D9%8A%D8%A7%D8%B3%D8%A9";
    public static String ArticleType="ArticleType";
    private SessionManagement sessionManagement;
    private HashMap<String, String> user;
    private String TokenID;
    private String LoggedEmail;
    private String LoggedUserName;
    private String LoggedProfilePic;
    private TextView EmailText;
    private TextView UserNameText;
    private ImageView ProfilePicView;
    private int Activity_Num=0;
    View view;
    private String Urgent_KEY="urgent";
//    RelativeLayout home_linear;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        ButterKnife.bind(this);
        setContentView(R.layout.activity_home);
        setTheme(R.style.ArishTheme);
//        home_linear=(RelativeLayout)findViewById(R.id.home_linear);
        Config.ActivityNum=Activity_Num;
        apiKey= BuildConfig.ApiKey;
        token=BuildConfig.token;
        newsApiFragment=new NewsApiFragment();
        noInternetFragment=new NoInternetFragment();
        snackBarLauncher=new SnackBarClassLauncher();
        webhoseApiFragment=new WebhoseApiFragment();
        webServiceNewsApi=new Bundle();
        webServiceWebHose=new Bundle();
        handler = new Handler();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header=navigationView.getHeaderView(0);
        EmailText=(TextView)header.findViewById(R.id.Email);
        UserNameText=(TextView)header.findViewById(R.id.UserName);
        ProfilePicView=(ImageView)header.findViewById(R.id.profile_image);
        final Bundle bundle=new Bundle();
        sessionManagement=new SessionManagement(getApplicationContext());
        user=sessionManagement.getUserDetails();
        if (user!=null){
            LoggedEmail = user.get(SessionManagement.KEY_EMAIL);
            LoggedUserName=user.get(SessionManagement.KEY_NAME);
            LoggedProfilePic=user.get(SessionManagement.KEY_Profile_Pic);
            TokenID=user.get(SessionManagement.KEY_idToken);
            if (LoggedEmail!=null){
                EmailText.setText(LoggedEmail);
            }
            if (LoggedUserName!=null){
                UserNameText.setText(LoggedUserName);
            }
            if (LoggedProfilePic!=null){
                Picasso.with(getApplicationContext()).load(LoggedProfilePic)
                        .error(R.drawable.news)
                        .into(ProfilePicView);
            }
        }
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                if(menuItem.isChecked()) menuItem.setChecked(false);
                else menuItem.setChecked(true);
                drawerLayout.closeDrawers();
                switch (menuItem.getItemId()){
                    case R.id.publish:
                        Intent intent=new Intent(getApplicationContext(),PublishArticleActivity.class);
                        startActivity(intent);
                        return true;
                    case R.id.urgent:
                        displayUrgent();
                        return true;
                    case R.id.politics:
                        bundle.putString(ArticleType,POLITICS);
                        Intent intent2=new Intent(getApplicationContext(),ArticleTypesListActivity.class);
                        intent2.putExtras(bundle);
                        startActivity(intent2);
                        return true;
                    case R.id.art_culture:
                        bundle.putString(ArticleType,ARTS);
                        Intent intent3=new Intent(getApplicationContext(),ArticleTypesListActivity.class);
                        intent3.putExtras(bundle);
                        startActivity(intent3);
                        return true;
                    case R.id.sports:
                        bundle.putString(ArticleType,SPORTS);
                        Intent intent4=new Intent(getApplicationContext(), ArticleTypesListActivity.class);
                        intent4.putExtras(bundle);
                        startActivity(intent4);
                        return true;
                    case R.id.reports:
                        bundle.putString(ArticleType,REPORTS);
                        Intent intent5=new Intent(getApplicationContext(), ArticleTypesListActivity.class);
                        intent5.putExtras(bundle);
                        startActivity(intent5);
                        // get data from content provider or firebase
                        return true;
                    case R.id.food:
                        bundle.putString(ArticleType,FOOD);
                        Intent intent1=new Intent(getApplicationContext(), ArticleTypesListActivity.class);
                        intent1.putExtras(bundle);
                        startActivity(intent1);
                        return true;
                    case R.id.family:
                        bundle.putString(ArticleType,FAMILY);
                        Intent intent6=new Intent(getApplicationContext(), ArticleTypesListActivity.class);
                        intent6.putExtras(bundle);
                        startActivity(intent6);
                        return true;
                    case R.id.heritage:
                        bundle.putString(ArticleType,HERITAGE);
                        Intent intent7=new Intent(getApplicationContext(), ArticleTypesListActivity.class);
                        intent7.putExtras(bundle);
                        startActivity(intent7);
                        return true;
                    case R.id.opinions:
                        bundle.putString(ArticleType,OPINIONS);
                        Intent intent8=new Intent(getApplicationContext(),ArticleTypesListActivity.class);
                        intent8.putExtras(bundle);
                        startActivity(intent8);
                        return true;
                    case R.id.technology:
                        bundle.putString(ArticleType,TECHNOLOGY);
                        Intent intent9=new Intent(getApplicationContext(), ArticleTypesListActivity.class);
                        intent9.putExtras(bundle);
                        startActivity(intent9);
                        return true;
                    case R.id.business:
                        bundle.putString(ArticleType,BUSINESS);
                        Intent intent10=new Intent(getApplicationContext(), ArticleTypesListActivity.class);
                        intent10.putExtras(bundle);
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
        SnackBasedConnection();
    }

    private void SnackBasedConnection() {
        VerifyConnection verifyConnection=new VerifyConnection(getApplicationContext());
        verifyConnection.checkConnection();
        if (verifyConnection.isConnected()){
            displayUrgent();
        }else {
            // Show Snack
            snackbar=NetCut();
            snackBarLauncher.SnackBarInitializer(snackbar);
            Config.UrgentURL=UrgentURL;
            Config.apiKey=apiKey;
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container_frame, noInternetFragment, "newsApi")
                    .commit();
        }
    }

    private void displayUrgent() {
        if (UrgentURL!=null&&apiKey!=null){
            webServiceNewsApi.putString(Urgent_KEY,UrgentURL+apiKey);
        }else {
            webServiceNewsApi.putString(Urgent_KEY,Config.UrgentURL+Config.apiKey);
        }
//        webServiceNewsApi.putString("urgent",POLITICS_URL);
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

    private Snackbar NetCut() {
        return snackbar= Snackbar
                .make(drawerLayout, getApplicationContext().getResources().getString(R.string.no_internet), Snackbar.LENGTH_LONG)
                .setAction(getApplicationContext().getResources().getString(R.string.retry), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SnackBasedConnection();
                    }
                });

    }

    @Override
    public void ReloadInternetService() {
        SnackBasedConnection();
    }
}