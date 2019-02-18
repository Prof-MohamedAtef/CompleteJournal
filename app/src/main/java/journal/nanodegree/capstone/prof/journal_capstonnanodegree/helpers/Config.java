package journal.nanodegree.capstone.prof.journal_capstonnanodegree.helpers;

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

/**
 * Created by Prof-Mohamed Atef on 1/1/2019.
 */

public class Config {
    public static final URL BASE_URL;
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
    public static String UrgentURL;
    public static String apiKey;
    public static ArrayList<String> CategoriesList;
    public static int Category_id;
    public static boolean RetrieveFirebaseData=false;


    static {
        URL url = null;
        try {
            url = new URL("https://go.udacity.com/xyz-reader-json" );
        } catch (MalformedURLException ignored) {
            // TODO: throw a real error
            Log.e(TAG, "Please check your internet connection.");
        }

        BASE_URL = url;
    }
}
