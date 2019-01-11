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
 * Created by Prof-Mohamed Atef on 1/10/2019.
 */

public class WebHoseApiAsyncTask extends AsyncTask<String, Void, ArrayList<OptionsEntity>> {

    private final String LOG_TAG = WebHoseApiAsyncTask.class.getSimpleName();

    private ProgressDialog dialog;
    public JSONObject ArticlesJson;
    public JSONArray ArticlesDataArray;
    public JSONObject oneArticleData;
    private ArrayList<OptionsEntity> list = new ArrayList<OptionsEntity>();
    private String MAIN_LIST;


    @Override
    protected ArrayList<OptionsEntity> doInBackground(String... params) {
        String Articles_JsonSTR = null;

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        if (params.length == 0) {
            return null;
        }

        try {

            URL url = new URL(params[0]);
            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                Articles_JsonSTR  = null;
            }

            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }
            if (buffer.length() == 0) {
                return null;
            }

            Articles_JsonSTR = buffer.toString();

            Log.v(LOG_TAG, "Articles JSON String: " + Articles_JsonSTR );
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
            return getArticlesJson(Articles_JsonSTR );
        } catch (JSONException e) {
            Log.e(LOG_TAG, "didn't got Articles Data from getJsonData method", e);
            e.printStackTrace();
        }
        return null;
    }

    private ArrayList<OptionsEntity> getArticlesJson(String articles_jsonSTR) throws JSONException {
        ArticlesJson = new JSONObject(articles_jsonSTR );
        ArticlesDataArray= ArticlesJson.getJSONArray(MAIN_LIST);

        list.clear();
        for (int i = 0; i < ArticlesDataArray.length(); i++) {
            oneArticleData = ArticlesDataArray.getJSONObject(i);
            /*AUTHOR_STR = oneArticleData.getString(AUTHOR);
            TITLE_STR = oneArticleData.getString(TITLE);
            DESCRIPTION_STR = oneArticleData.getString(DESCRIPTION);
            URL_STR = oneArticleData.getString(URL_);
            URL_TO_IMAGE_STR = oneArticleData.getString(URL_TO_IMAGE);
            PUBLISHED_AT_STR = oneArticleData.getString(PUBLISHED_AT);
            JSONObject SourceJsonObj = oneArticleData.getJSONObject(SOURCE);
            Name_STR = SourceJsonObj.getString(NAME);
            if (AUTHOR_STR==null){
                AUTHOR_STR="";
            }
            if (TITLE_STR==null){
                TITLE_STR="";
            }
            if (DESCRIPTION_STR==null){
                DESCRIPTION_STR="";
            }
            if (PUBLISHED_AT_STR==null){
                PUBLISHED_AT_STR="";
            }
            if (Name_STR==null){
                Name_STR="";
            }
            optionsEntity = new OptionsEntity(AUTHOR_STR, TITLE_STR, DESCRIPTION_STR, URL_STR, URL_TO_IMAGE_STR, PUBLISHED_AT_STR, Name_STR);
            list.add(optionsEntity);*/
        }
        return list;
    }

    @Override
    protected void onPostExecute(ArrayList<OptionsEntity> result) {
        super.onPostExecute(result);
        if (result!=null){
            onWebHoseTaskCompleted.onWebHoseTaskCompleted(result);
            if (dialog.isShowing()){
                dialog.dismiss();
            }
        }
    }

    public OnWebHoseTaskCompleted onWebHoseTaskCompleted;
    Context mContext;

    public WebHoseApiAsyncTask(OnWebHoseTaskCompleted onWebHoseTaskCompleted, Context mContext) {
        this.onWebHoseTaskCompleted = onWebHoseTaskCompleted;
        this.mContext = mContext;
        dialog=new ProgressDialog(mContext);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        this.dialog.setMessage(mContext.getResources().getString(R.string.loading));
        this.dialog.show();
    }

    public interface OnWebHoseTaskCompleted{
        void onWebHoseTaskCompleted(ArrayList<OptionsEntity> result);
    }
}