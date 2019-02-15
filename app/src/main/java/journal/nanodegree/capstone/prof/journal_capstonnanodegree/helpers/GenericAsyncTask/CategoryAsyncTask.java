package journal.nanodegree.capstone.prof.journal_capstonnanodegree.helpers.GenericAsyncTask;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import journal.nanodegree.capstone.prof.journal_capstonnanodegree.R;
import journal.nanodegree.capstone.prof.journal_capstonnanodegree.helpers.OptionsEntity;

/**
 * Created by Prof-Mohamed Atef on 2/14/2019.
 */

public class CategoryAsyncTask extends AsyncTask<String, Void, ArrayList<OptionsEntity>> {
    private final String LOG_TAG = CategoryAsyncTask.class.getSimpleName();
    private final Context mContext;
    private ProgressDialog dialog;
    public OnCategoriesCompleted onCategoriesCompleted;

    public CategoryAsyncTask(OnCategoriesCompleted onCategoriesCompleted, Context context){
        this.onCategoriesCompleted=onCategoriesCompleted;
        dialog=new ProgressDialog(context);
        mContext=context;
    }

    public JSONObject CategoriesJson;
    public JSONArray CategoriesDataArray;
    public JSONObject oneCategoryData;

    private ArrayList<OptionsEntity> list = new ArrayList<OptionsEntity>();

    String MAIN_LIST="data";
    String ID="id";
    String CategoryName="name";
    String ID_STR, CategoryName_STR;
    private OptionsEntity optionsEntity;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        try{
            if (dialog!=null&&dialog.isShowing()){
                this.dialog.dismiss();
            }else {
                this.dialog.setMessage(mContext.getResources().getString(R.string.loading));
                this.dialog.show();
            }
        }catch (Exception e){
            Log.v(LOG_TAG, "Problem in ProgressDialogue" );
        }
    }

    @Override
    protected void onPostExecute(ArrayList<OptionsEntity> result) {
        super.onPostExecute(result);
        if (result != null) {
            if (onCategoriesCompleted!=null) {
                onCategoriesCompleted.onCategoriesCompleted(result);
                if (dialog.isShowing()){
                    dialog.dismiss();
                }
            }
        }
    }

    @Override
    protected ArrayList<OptionsEntity> doInBackground(String... strings) {
        String UsersDesires_JsonSTR = null;
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        if (strings.length == 0) {
            return null;
        }
        try {
            URL url = new URL(strings[0]);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                UsersDesires_JsonSTR = null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }
            if (buffer.length() == 0) {
                return null;
            }
            UsersDesires_JsonSTR = buffer.toString();
            Log.v(LOG_TAG, "Users Desires String: " + UsersDesires_JsonSTR);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error here Exactly ", e);
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }
        try {
            return getCategories(UsersDesires_JsonSTR);
        } catch (JSONException e) {
            Log.e(LOG_TAG, "didn't got Users Desires from getJsonData method", e);
            e.printStackTrace();
        }
        return null;
    }

    private ArrayList<OptionsEntity> getCategories(String usersDesires_jsonSTR) throws JSONException {
        CategoriesJson = new JSONObject(usersDesires_jsonSTR);
        CategoriesDataArray= CategoriesJson.getJSONArray(MAIN_LIST);
        list.clear();
        for (int i = 0; i < CategoriesDataArray.length(); i++) {
            // Get the JSON object representing a movie per each loop
            oneCategoryData= CategoriesDataArray.getJSONObject(i);
            ID_STR = oneCategoryData.getString(ID);
            CategoryName_STR = oneCategoryData.getString(CategoryName);
            OptionsEntity entity = new OptionsEntity(ID_STR,CategoryName_STR);
            list.add(entity);
        }
        return list;
    }

    public interface OnCategoriesCompleted{
        void onCategoriesCompleted(ArrayList<OptionsEntity> result);
    }
}
