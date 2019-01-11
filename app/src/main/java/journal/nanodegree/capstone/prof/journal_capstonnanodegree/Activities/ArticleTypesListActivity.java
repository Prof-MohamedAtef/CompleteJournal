package journal.nanodegree.capstone.prof.journal_capstonnanodegree.Activities;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import journal.nanodegree.capstone.prof.journal_capstonnanodegree.BuildConfig;
import journal.nanodegree.capstone.prof.journal_capstonnanodegree.R;

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

public class ArticleTypesListActivity extends AppCompatActivity {

    private String URL;
    private String token;
    private String apiKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_types_list);
        token= BuildConfig.token;
        apiKey= BuildConfig.ApiKey;
        Intent intent= getIntent();
        if (intent!=null&&intent.hasExtra(POLITICS)){
            URL="http://webhose.io/filterWebContent?token="+token+"&format=json&ts=1543864001127&sort=crawled&q=thread.country%3AEG%20language%3Aarabic%20site_type%3Anews%20thread.title%3A%D8%B3%D9%8A%D8%A7%D8%B3%D8%A9";
        }else if (intent!=null&&intent.hasExtra(ARTS)){
            URL="http://webhose.io/filterWebContent?token="+token+"&format=json&ts=1543864086443&sort=crawled&q=thread.country%3AEG%20language%3Aarabic%20site_type%3Anews%20thread.title%3A%D9%81%D9%86%D9%88%D9%86";
        }else if (intent!=null&&intent.hasExtra(SPORTS)){
            URL="https://newsapi.org/v2/top-headlines?country=eg&category=sports&apiKey="+apiKey;
        }else if (intent!=null&&intent.hasExtra(REPORTS)){
            // get data from Content Provider of Firebase
        }else if (intent!=null&&intent.hasExtra(FOOD)){
            URL="http://webhose.io/filterWebContent?token="+token+"&format=json&ts=1543863885301&sort=crawled&q=thread.country%3AEG%20language%3Aarabic%20site_type%3Anews%20thread.title%3A";
        }else if (intent!=null&&intent.hasExtra(FAMILY)){
            URL="http://webhose.io/filterWebContent?token="+token+"&format=json&ts=1545130799659&sort=crawled&q=thread.country%3AEG%20language%3Aarabic%20site_type%3Anews%20thread.title%3A%D8%A7%D9%84%D8%A3%D8%B3%D8%B1%D8%A9";
        }else if (intent!=null&&intent.hasExtra(HERITAGE)){
            URL="http://webhose.io/filterWebContent?token="+token+"&format=json&ts=1543863771070&sort=crawled&q=thread.country%3AEG%20language%3Aarabic%20site_type%3Anews%20thread.title%3A%D8%AA%D8%B1%D8%A7%D8%AB";
        }else if (intent!=null&&intent.hasExtra(OPINIONS)){
            URL="http://webhose.io/filterWebContent?token="+token+"&format=json&ts=1543852898977&sort=crawled&q=thread.country%3AEG%20language%3Aarabic%20site_type%3Anews%20thread.title%3A%D8%A2%D8%B1%D8%A7%D8%A1";
        } else if (intent!=null&&intent.hasExtra(TECHNOLOGY)){
            URL="https://newsapi.org/v2/top-headlines?country=eg&category=technology&apiKey="+apiKey;
        } else if (intent!=null&&intent.hasExtra(BUSINESS)){
            URL="https://newsapi.org/v2/top-headlines?country=eg&category=business&apiKey="+apiKey;
        }
    }
}