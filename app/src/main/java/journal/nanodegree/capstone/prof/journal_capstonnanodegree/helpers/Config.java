package journal.nanodegree.capstone.prof.journal_capstonnanodegree.helpers;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.util.Log;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Prof-Mohamed Atef on 1/1/2019.
 */

public class Config {
    public static final URL BASE_URL;
    private static String TAG = Config.class.toString();
    public static Context mContext;
    public static CoordinatorLayout mCoordinatorLayout;
    public static boolean TwoPane;
    public static int position;
    public static String currentImagePAth;
    public static String imageBitmap;
    public static String image_name;
    public static String selectedImagePath;
    public static File StorageDir;


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
