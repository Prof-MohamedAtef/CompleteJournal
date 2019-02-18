package journal.nanodegree.capstone.prof.journal_capstonnanodegree.helpers.Firebase;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import journal.nanodegree.capstone.prof.journal_capstonnanodegree.helpers.OptionsEntity;

import static journal.nanodegree.capstone.prof.journal_capstonnanodegree.Fragments.FragmentSoundPlayer.optionsEntity;

/**
 * Created by Prof-Mohamed Atef on 2/17/2019.
 */

public class FirebaseDisplayReports {

    ArrayList<OptionsEntity> FirebaseArticlesList;
    DatabaseReference mDatabase;

    private String KEY;
    private String Category_STR;
    private String Description_STR;
    private String ImageFile_STR;
    private String Title_STR;
    private String Email_STR;
    private ArrayList<OptionsEntity> Diarylist;
    public static String ArticlesList_KEY="ArticlesList_KEY";
    private String AudioFile_STR;
    private String UserName_STR;
    private String AudioFile_KEY="audioFileUri";
    private String Category_KEY="categoryID";
    private String Description_KEY="description";
    private String IMAGE_FILE_KEY="imageFileUri";
    private String TITLE_KEY="title";
    private String Token_STR;
    private String TokenID_KEY="tokenID";
    private String Email_KEY="userEmail";
    private String UserName_KEY="userName";
    private String Data_KEY="data";
    private String LOG_TAG="TAG";

    public FirebaseDisplayReports(DatabaseReference databaseReference){
        this.mDatabase=databaseReference;
    }

    public ArrayList<OptionsEntity> FetchDataFromFirebase() {
        DatabaseReference ThoughtsRef=mDatabase.child(Data_KEY);
        ValueEventListener valueEventListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                FirebaseArticlesList.clear();
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    KEY=ds.getKey();
                    AudioFile_STR = ds.child(AudioFile_KEY).getValue(String.class);
                    Category_STR = ds.child(Category_KEY).getValue(String.class);
                    Description_STR = ds.child(Description_KEY).getValue(String.class);
                    ImageFile_STR= ds.child(IMAGE_FILE_KEY).getValue(String.class);
                    Title_STR= ds.child(TITLE_KEY).getValue(String.class);
                    Token_STR= ds.child(TokenID_KEY).getValue(String.class);
                    Email_STR= ds.child(Email_KEY).getValue(String.class);
                    UserName_STR= ds.child(UserName_KEY).getValue(String.class);
                    Log.d(LOG_TAG, AudioFile_STR+ " / " + Email_STR+ " / " + Category_STR+ " / " + Description_STR+ " / " + ImageFile_STR+ " / " + Title_STR+ " / " + Token_STR+ " / " + UserName_STR);

                    optionsEntity=new OptionsEntity(KEY, Email_STR, UserName_STR, Token_STR, Category_STR,Title_STR,ImageFile_STR, AudioFile_STR,Description_STR);
                    Diarylist.add(optionsEntity);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        ThoughtsRef.addListenerForSingleValueEvent(valueEventListener);
        return Diarylist;
    }
}
