package journal.nanodegree.capstone.prof.journal_capstonnanodegree.helpers.GenericAsyncTask;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import journal.nanodegree.capstone.prof.journal_capstonnanodegree.R;
import journal.nanodegree.capstone.prof.journal_capstonnanodegree.helpers.OptionsEntity;

/**
 * Created by Prof-Mohamed Atef on 2/14/2019.
 */

public class InsertArticleAsyncTask extends AsyncTask<String, Void, ArrayList<OptionsEntity>> {

    private final String LOG_TAG = InsertArticleAsyncTask.class.getSimpleName();
    private final Context mContext;
    private final ProgressDialog dialog;
    private final OnUploadCompleted onUploadCompleted;

    public JSONObject ArticlesJson;
    public JSONArray ArticlesJsonAray;
    public JSONObject oneArticleData;

    String MAIN_LIST="data";
    String TITLE="title", DESCRIPTION="desc", CATEGORY_ID="category_id", API_TOKEN="api_token", IMAGE="img";

    private String Json_Url;
    private String REQUEST_METHOD="POST";
    ArrayList<OptionsEntity> list = new ArrayList<OptionsEntity>();


    public InsertArticleAsyncTask(OnUploadCompleted onUploadCompleted, Context context,
                                  String title, String desc, String categ_id, Bitmap img_bmp, String token_id){
        this.onUploadCompleted=onUploadCompleted;
        dialog = new ProgressDialog(context);
        this.mContext=context;
        this.Title_P=title;
        this.Description_P=desc;
        this.Category_id_P=categ_id;
        this.imageBitmap_P=img_bmp;
        this.TokenID_P=token_id;
    }

    @Override
    protected void onPostExecute(ArrayList<OptionsEntity> result) {
        super.onPostExecute(result);
        if (result != null) {
            if (onUploadCompleted!=null){
                onUploadCompleted.onUploadTaskCompleted(result);
                if (dialog.isShowing()){
                    dialog.dismiss();
                }
            }
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Json_Url = "http://fla4news.com/news/api/v1/create_news";
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


    String Title_P,Description_P, Category_id_P,Date_STR_P, fileName_P, TokenID_P;
    Bitmap imageBitmap_P;

    String TITLE_STR;
    String DESCRIPTION_STR;
    String CATEGORY_ID_STR;
    String API_TOKEN_STR;
    String IMAGE_STR;
    String data_String_insert = "";
    InputStream inputStream;
    String UsersDesires_JsonSTR = null;
    BufferedReader reader = null;
    @Override
    protected ArrayList<OptionsEntity> doInBackground(String... args) {

//        Title_P = args[0];
//        Description_P= args[1];
//        Category_id_P= args[2];
//        imageBitmap_P= args[3];
////        fileName_P= args[4];
//        TokenID_P= args[4];

        try {
            URL rl = new URL(Json_Url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) rl.openConnection();
            httpURLConnection.setRequestMethod(REQUEST_METHOD);
            httpURLConnection.setDoInput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

            if (Title_P != null && Description_P == null && Category_id_P== null && Date_STR_P!= null&& imageBitmap_P!=null&&fileName_P!=null&&TokenID_P!=null) {
                data_String_insert =
                        URLEncoder.encode("title", "UTF-8") + "=" + URLEncoder.encode(Title_P, "UTF-8") + "&" +
                                URLEncoder.encode("desc", "UTF-8") + "=" + URLEncoder.encode(Description_P, "UTF-8") + "&" +
                                URLEncoder.encode("category_id", "UTF-8") + "=" + URLEncoder.encode(Category_id_P, "UTF-8") + "&" +
//                                URLEncoder.encode("img", "UTF-8") + "=" + URLEncoder.encode(imageBitmap_P, "UTF-8") + "&" +
                                URLEncoder.encode("api_token", "UTF-8") + "=" + URLEncoder.encode(TokenID_P, "UTF-8");
            }
            bufferedWriter.write(data_String_insert);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();
            InputStream inputStream = httpURLConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                UsersDesires_JsonSTR = null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            try {
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
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
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
                return getPostedArticle(UsersDesires_JsonSTR);
            } catch (JSONException e) {
                Log.e(LOG_TAG, "didn't got Users Desires from getJsonData method", e);
                e.printStackTrace();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private ArrayList<OptionsEntity> getPostedArticle(String usersDesires_jsonSTR)
            throws JSONException {
        ArticlesJson = new JSONObject(usersDesires_jsonSTR);
        ArticlesJsonAray = ArticlesJson.getJSONArray(MAIN_LIST);
        list.clear();
        for (int i = 0; i < ArticlesJsonAray.length(); i++) {
            // Get the JSON object representing a movie per each loop
            oneArticleData = ArticlesJsonAray.getJSONObject(i);
            TITLE_STR = oneArticleData.getString(TITLE);
            DESCRIPTION_STR= oneArticleData.getString(DESCRIPTION);
            CATEGORY_ID_STR= oneArticleData.getString(CATEGORY_ID);
            API_TOKEN_STR= oneArticleData.getString(API_TOKEN);
            IMAGE_STR= oneArticleData.getString(IMAGE);
            OptionsEntity optionsEntity=new OptionsEntity(TITLE_STR, DESCRIPTION_STR, CATEGORY_ID_STR, API_TOKEN_STR, IMAGE_STR);
            list.add(optionsEntity);
        }
        return list;
    }

    public interface OnUploadCompleted{
        void onUploadTaskCompleted(ArrayList<OptionsEntity> result);
    }
}