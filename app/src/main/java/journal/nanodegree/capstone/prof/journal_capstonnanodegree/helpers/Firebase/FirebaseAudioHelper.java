package journal.nanodegree.capstone.prof.journal_capstonnanodegree.helpers.Firebase;

import android.net.Uri;

import journal.nanodegree.capstone.prof.journal_capstonnanodegree.Activities.AudioActivity;

/**
 * Created by Prof-Mohamed Atef on 2/15/2019.
 */

public class FirebaseAudioHelper {
    Uri AudioFileUri;

    public Uri getAudioFileUri() {
        return AudioFileUri;
    }

    public void setAudioFileUri(Uri audioFileUri) {
        AudioFileUri = audioFileUri;
    }

    public FirebaseAudioHelper(Uri AudioFileUri){
        this.AudioFileUri=AudioFileUri;
    }
}
