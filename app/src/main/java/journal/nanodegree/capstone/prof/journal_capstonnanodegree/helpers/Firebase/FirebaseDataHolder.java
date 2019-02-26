package journal.nanodegree.capstone.prof.journal_capstonnanodegree.helpers.Firebase;

import android.net.Uri;

import java.io.Serializable;

/**
 * Created by Prof-Mohamed Atef on 2/15/2019.
 */

public class FirebaseDataHolder implements Serializable {
    String AudioURL;
    String Key;
    String TITLE, DESCRIPTION, CategoryID, PUBLISHEDAT, UserEmail, TokenID, Date, UserName;
    String ImageFileUri;

    public FirebaseDataHolder (){
    }




    public FirebaseDataHolder(String title_str, String description_str, String category_id_str, String userEmail, String image_str, String TokenID,String date, String user_name) {
        this.TITLE=title_str;
        this.DESCRIPTION=description_str;
        this.CategoryID=category_id_str;
        this.UserEmail=userEmail;
        this.ImageFileUri=image_str;
        this.TokenID=TokenID;
        this.UserName=user_name;
        this.Date=date;
    }

    public String getAudioURL() {
        return AudioURL;
    }

    public void setAudioURL(String audioURL) {
        AudioURL = audioURL;
    }

    public FirebaseDataHolder(String key, String title_str, String description_str, String category_id_str, String api_token_str, String audio_file, String image_str, String date, String user_name, String userEmail) {
        this.Key=key;
        this.TITLE=title_str;
        this.DESCRIPTION=description_str;
        this.CategoryID=category_id_str;
        this.TokenID=api_token_str;
        this.AudioURL=audio_file;
        this.ImageFileUri=image_str;
        this.Date=date;
        this.UserName=user_name;
        this.UserEmail=userEmail;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getTokenID() {
        return TokenID;
    }

    public void setTokenID(String tokenID) {
        TokenID = tokenID;
    }

    public String getTITLE() {
        return TITLE;
    }

    public void setTITLE(String TITLE) {
        this.TITLE = TITLE;
    }

    public String getDESCRIPTION() {
        return DESCRIPTION;
    }

    public void setDESCRIPTION(String DESCRIPTION) {
        this.DESCRIPTION = DESCRIPTION;
    }

    public String getCategoryID() {
        return CategoryID;
    }

    public void setCategoryID(String categoryID) {
        CategoryID = categoryID;
    }

    public String getPUBLISHEDAT() {
        return PUBLISHEDAT;
    }

    public void setPUBLISHEDAT(String PUBLISHEDAT) {
        this.PUBLISHEDAT = PUBLISHEDAT;
    }

    public String getUserEmail() {
        return UserEmail;
    }

    public void setUserEmail(String userEmail) {
        UserEmail = userEmail;
    }

    public String getImageFileUri() {
        return ImageFileUri;
    }

    public void setImageFileUri(String imageFileUri) {
        ImageFileUri = imageFileUri;
    }


}
