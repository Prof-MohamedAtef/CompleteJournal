package journal.nanodegree.capstone.prof.journal_capstonnanodegree.helpers.Firebase;

import android.net.Uri;

/**
 * Created by Prof-Mohamed Atef on 2/15/2019.
 */

public class FirebaseDataHolder {
    String TITLE, DESCRIPTION, CategoryID, PUBLISHEDAT, UserEmail, TokenID;
    String ImageFileUri;

    public FirebaseDataHolder (){
    }

    public FirebaseDataHolder(String title_str, String description_str, String category_id_str, String api_token_str, String image_str, String TokenID) {
        this.TITLE=title_str;
        this.DESCRIPTION=description_str;
        this.CategoryID=category_id_str;
        this.UserEmail=api_token_str;
        this.ImageFileUri=image_str;
        this.TokenID=TokenID;
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
