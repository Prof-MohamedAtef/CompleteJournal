package journal.nanodegree.capstone.prof.journal_capstonnanodegree.helpers.GenericAsyncTask;

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
import journal.nanodegree.capstone.prof.journal_capstonnanodegree.helpers.OptionsEntity;

/**
 * Created by Prof-Mohamed Atef on 1/3/2019.
 */

public class NewsApiAsyncTask extends AsyncTask <String, Void, ArrayList<OptionsEntity>> {

    private final String LOG_TAG = NewsApiAsyncTask.class.getSimpleName();

    public JSONObject ArticlesJson;
    public JSONArray ArticlesDataArray;
    public JSONObject oneArticleData;
    private ArrayList<OptionsEntity> list = new ArrayList<OptionsEntity>();

    String MAIN_LIST="articles";
    String SOURCE="source";
    String NAME="name";
    String AUTHOR="author";
    String TITLE="title";
    String DESCRIPTION="description";
    String URL_="url";
    String URL_TO_IMAGE="urlToImage";
    String PUBLISHED_AT="publishedAt";
    private OptionsEntity optionsEntity;
    private String Name_STR;
    private String AUTHOR_STR;
    private String TITLE_STR;
    private String DESCRIPTION_STR;
    private String URL_STR;
    private String URL_TO_IMAGE_STR;
    private String PUBLISHED_AT_STR;


    public OnTaskCompleted onTaskCompleted;

    public NewsApiAsyncTask(OnTaskCompleted onTaskCompleted){
        this.onTaskCompleted=onTaskCompleted;
    }



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

            Log.v(LOG_TAG, "Movies JSON String: " + Articles_JsonSTR );
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
            Log.e(LOG_TAG, "didn't got Movies Data from getJsonData method", e);
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<OptionsEntity> result) {
        super.onPostExecute(result);
        if (result != null) {
            onTaskCompleted.onTaskCompleted(result);
        }
    }

    private ArrayList<OptionsEntity> getArticlesJson(String Articles_JsonSTR) throws JSONException {
        ArticlesJson = new JSONObject(Articles_JsonSTR );
        ArticlesDataArray= ArticlesJson.getJSONArray(MAIN_LIST);

        list.clear();
        for (int i = 0; i < ArticlesDataArray.length(); i++) {
            oneArticleData = ArticlesDataArray.getJSONObject(i);
            AUTHOR_STR= oneArticleData.getString(AUTHOR);
            TITLE_STR= oneArticleData.getString(TITLE);
            DESCRIPTION_STR= oneArticleData.getString(DESCRIPTION);
            URL_STR= oneArticleData.getString(URL_);
            URL_TO_IMAGE_STR= oneArticleData.getString(URL_TO_IMAGE);
            PUBLISHED_AT_STR= oneArticleData.getString(PUBLISHED_AT);

            JSONArray SourceJsonArray=oneArticleData.getJSONArray(SOURCE);
            for (int x=0; x<SourceJsonArray.length(); x++){
                JSONObject SourceObject= SourceJsonArray.getJSONObject(x);
                Name_STR = SourceObject.getString(NAME);
                optionsEntity=new OptionsEntity(AUTHOR_STR, TITLE_STR, DESCRIPTION_STR, URL_STR, URL_TO_IMAGE_STR, PUBLISHED_AT_STR, Name_STR);
                list.add(optionsEntity);
            }
        }
        return list;
    }

    public interface OnTaskCompleted{
        void onTaskCompleted(ArrayList<OptionsEntity> result);
    }
}