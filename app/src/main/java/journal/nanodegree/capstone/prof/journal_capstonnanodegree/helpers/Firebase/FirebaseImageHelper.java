package journal.nanodegree.capstone.prof.journal_capstonnanodegree.helpers.Firebase;

import android.net.Uri;

/**
 * Created by Prof-Mohamed Atef on 2/15/2019.
 */

public class FirebaseImageHelper {
    Uri ImageFileUri;

    public Uri getImageFileUri() {
        return ImageFileUri;
    }

    public void setImageFileUri(Uri imageFileUri) {
        ImageFileUri = imageFileUri;
    }

    public FirebaseImageHelper(Uri ImageFileUri){
        this.ImageFileUri=ImageFileUri;
    }
}
