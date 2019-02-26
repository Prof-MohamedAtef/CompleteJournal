package journal.nanodegree.capstone.prof.journal_capstonnanodegree.helpers;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import journal.nanodegree.capstone.prof.journal_capstonnanodegree.helpers.Firebase.FirebaseDataHolder;

/**
 * Created by Prof-Mohamed Atef on 1/1/2019.
 */

public class Config {
    private static String TAG = Config.class.toString();
    public static Context mContext;
    public static CoordinatorLayout mCoordinatorLayout;
    public static LinearLayout activityLinearHome;
    public static boolean TwoPane;
    public static int position;
    public static String currentImagePAth;
    public static ImageView imageViewPlay;
    public static String imageBitmap;
    public static String image_name;
    public static String selectedImagePath;
    public static File StorageDir;
    public static ArrayList<OptionsEntity> ArrArticle;
    public static int ActivityNum;
    public static int FragmentNewsApiNum;
    public static int FragmentWebHoseApiNum;
    public static int FragmentFirebaseApiNum;
    public static String UrgentURL;
    public static String apiKey;
    public static ArrayList<String> CategoriesList;
    public static int Category_id;
    public static boolean RetrieveFirebaseData=false;
    public static int RecyclerPosition;
    public static String CategoryName;
    public static ArrayList<FirebaseDataHolder> FirebaseArticlesList;
    public static Application application;
}
