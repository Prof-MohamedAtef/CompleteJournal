package journal.nanodegree.capstone.prof.journal_capstonnanodegree.helpers.Firebase;

import android.app.Application;
import android.net.Uri;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Prof-Mohamed Atef on 2/15/2019.
 */

public class FirebaseHelper extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("data");
        databaseReference.keepSynced(true);
    }
}
