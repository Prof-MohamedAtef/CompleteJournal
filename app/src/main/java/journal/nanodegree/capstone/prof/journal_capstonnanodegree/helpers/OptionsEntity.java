package journal.nanodegree.capstone.prof.journal_capstonnanodegree.helpers;

import com.facebook.AccessToken;

import java.io.Serializable;

/**
 * Created by Prof-Mohamed Atef on 12/31/2018.
 */

public class OptionsEntity implements Serializable{
    public static Object FBAccessToken;
    String ID;
    String AUTHOR, TITLE, DESCRIPTION, URL, URLTOIMAGE, PUBLISHEDAT, NAME;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getAUTHOR() {
        return AUTHOR;
    }

    public void setAUTHOR(String AUTHOR) {
        this.AUTHOR = AUTHOR;
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

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getURLTOIMAGE() {
        return URLTOIMAGE;
    }

    public void setURLTOIMAGE(String URLTOIMAGE) {
        this.URLTOIMAGE = URLTOIMAGE;
    }

    public String getPUBLISHEDAT() {
        return PUBLISHEDAT;
    }

    public void setPUBLISHEDAT(String PUBLISHEDAT) {
        this.PUBLISHEDAT = PUBLISHEDAT;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public OptionsEntity(String author_str, String title_str, String description_str, String url_str, String url_to_image_str, String published_at_str, String name_str) {
        this.AUTHOR=author_str;
        this.TITLE=title_str;
        this.DESCRIPTION=description_str;
        this.URL=url_str;
        this.URLTOIMAGE=url_to_image_str;
        this.PUBLISHEDAT=published_at_str;
        this.NAME=name_str;
    }
}