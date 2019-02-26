package journal.nanodegree.capstone.prof.journal_capstonnanodegree.helpers.Data;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;

import java.net.URLEncoder;
import java.util.ArrayList;

import journal.nanodegree.capstone.prof.journal_capstonnanodegree.helpers.Firebase.FirebaseDataHolder;
import journal.nanodegree.capstone.prof.journal_capstonnanodegree.helpers.OptionsEntity;

import static journal.nanodegree.capstone.prof.journal_capstonnanodegree.helpers.Data.NewsProvider.CONTENT_URI;

/**
 * Created by Prof-Mohamed Atef on 2/20/2019.
 */

public class CursorTypeConverter {

    private static String NULL_KEY="Someone";
    public static String URGENT_CATEGORY="Urgent";
    private static OptionsEntity optionsEntity;
    private static ArrayList<OptionsEntity> UrgentArticlesList;
    private static ArrayList<FirebaseDataHolder> FirebaseArticlesList;
    public static Context mContext;
    private static FirebaseDataHolder firebaseDataHolder;

    public CursorTypeConverter(){
        UrgentArticlesList=new ArrayList<>();
        FirebaseArticlesList=new ArrayList<>();
    }



    public static ArrayList<OptionsEntity> AddCursorToArrayList(Cursor cursor) {
        if (cursor.moveToFirst()){
            for (cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){
                optionsEntity=new OptionsEntity();
                optionsEntity.setAUTHOR(cursor.getString(cursor.getColumnIndex(NewsProvider.AUTHOR)));
                optionsEntity.setTITLE(cursor.getString(cursor.getColumnIndex(NewsProvider.TITLE)));
                optionsEntity.setDESCRIPTION(cursor.getString(cursor.getColumnIndex(NewsProvider.DESCRIPTION)));
                optionsEntity.setURL(cursor.getString(cursor.getColumnIndex(NewsProvider.ARTICLE_URL)));
                optionsEntity.setURLTOIMAGE(cursor.getString(cursor.getColumnIndex(NewsProvider.IMAGE_URL)));
                optionsEntity.setPUBLISHEDAT(cursor.getString(cursor.getColumnIndex(NewsProvider.PUBLISHED_AT)));
                optionsEntity.setNAME(cursor.getString(cursor.getColumnIndex(NewsProvider.SOURCE_NAME)));
                UrgentArticlesList.add(optionsEntity);
            }
        }
        return UrgentArticlesList;
    }

    public static ArrayList<FirebaseDataHolder> AddFirebaseCursorToArrayList(Cursor cursor) {
        if (cursor.moveToFirst()){
            for (cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){
                firebaseDataHolder=new FirebaseDataHolder();
                firebaseDataHolder.setUserName(cursor.getString(cursor.getColumnIndex(NewsProvider.AUTHOR)));
                firebaseDataHolder.setTITLE(cursor.getString(cursor.getColumnIndex(NewsProvider.TITLE)));
                firebaseDataHolder.setDESCRIPTION(cursor.getString(cursor.getColumnIndex(NewsProvider.DESCRIPTION)));
                firebaseDataHolder.setImageFileUri(cursor.getString(cursor.getColumnIndex(NewsProvider.IMAGE_URL)));
                firebaseDataHolder.setPUBLISHEDAT(cursor.getString(cursor.getColumnIndex(NewsProvider.PUBLISHED_AT)));
                firebaseDataHolder.setCategoryID(cursor.getString(cursor.getColumnIndex(NewsProvider.SOURCE_NAME)));
                FirebaseArticlesList.add(firebaseDataHolder);
            }
        }
        return FirebaseArticlesList;
    }

    @NonNull
    public static ContentValues saveUrgentOptionsUisngContentProvider(OptionsEntity optionsEntity) {
        ContentValues values = new ContentValues();
        if (optionsEntity.getAUTHOR()!=null){
            values.put(NewsProvider.AUTHOR, optionsEntity.getAUTHOR());
        }else {
            values.put(NewsProvider.AUTHOR,NULL_KEY);
        }
        if (optionsEntity.getTITLE()!=null){
            values.put(NewsProvider.TITLE, optionsEntity.getTITLE());
        }else {
            values.put(NewsProvider.TITLE,NULL_KEY);
        }
        if (optionsEntity.getDESCRIPTION()!=null){
            values.put(NewsProvider.DESCRIPTION, optionsEntity.getDESCRIPTION());
        }else {
            values.put(NewsProvider.DESCRIPTION,NULL_KEY);
        }
        if (optionsEntity.getArticleID()!=null){
            values.put(NewsProvider.ARTICLE_URL, optionsEntity.getArticleID());
        }else {
            values.put(NewsProvider.ARTICLE_URL,NULL_KEY);
        }
        if (optionsEntity.getURLTOIMAGE()!=null){
            values.put(NewsProvider.IMAGE_URL, optionsEntity.getURLTOIMAGE());
        }else {
            values.put(NewsProvider.IMAGE_URL,NULL_KEY);
        }
        if (optionsEntity.getPUBLISHEDAT()!=null){
            values.put(NewsProvider.PUBLISHED_AT, optionsEntity.getPUBLISHEDAT());
        }else {
            values.put(NewsProvider.PUBLISHED_AT,NULL_KEY);
        }
        values.put(NewsProvider.CATEGORY, URGENT_CATEGORY);
        if (optionsEntity.getNAME()!=null){
            values.put(NewsProvider.SOURCE_NAME, optionsEntity.getNAME());
        }
        else {
            values.put(NewsProvider.SOURCE_NAME,NULL_KEY);
        }
        return values;
    }

    @NonNull
    public static ContentValues saveOptionsUisngContentProvider(OptionsEntity optionsEntity, String CategoryName) {
        ContentValues values = new ContentValues();
        if (optionsEntity.getAUTHOR()!=null){
            values.put(NewsProvider.AUTHOR, optionsEntity.getAUTHOR());
        }else {
            values.put(NewsProvider.AUTHOR,NULL_KEY);
        }
        if (optionsEntity.getTITLE()!=null){
            values.put(NewsProvider.TITLE, optionsEntity.getTITLE());
        }else {
            values.put(NewsProvider.TITLE,NULL_KEY);
        }
        if (optionsEntity.getDESCRIPTION()!=null){
            values.put(NewsProvider.DESCRIPTION, optionsEntity.getDESCRIPTION());
        }else {
            values.put(NewsProvider.DESCRIPTION,NULL_KEY);
        }
        if (optionsEntity.getArticleID()!=null){
            values.put(NewsProvider.ARTICLE_URL, optionsEntity.getArticleID());
        }else {
            values.put(NewsProvider.ARTICLE_URL,NULL_KEY);
        }
        if (optionsEntity.getURLTOIMAGE()!=null){
            values.put(NewsProvider.IMAGE_URL, optionsEntity.getURLTOIMAGE());
        }else {
            values.put(NewsProvider.IMAGE_URL,NULL_KEY);
        }
        if (optionsEntity.getPUBLISHEDAT()!=null){
            values.put(NewsProvider.PUBLISHED_AT, optionsEntity.getPUBLISHEDAT());
        }else {
            values.put(NewsProvider.PUBLISHED_AT,NULL_KEY);
        }
        values.put(NewsProvider.CATEGORY, CategoryName);
        if (optionsEntity.getNAME()!=null){
            values.put(NewsProvider.SOURCE_NAME, optionsEntity.getNAME());
        }
        else {
            values.put(NewsProvider.SOURCE_NAME,NULL_KEY);
        }
        return values;
    }


    @NonNull
    public static ContentValues saveFireBDataHolderUsingContentProvider(FirebaseDataHolder firebaseDataHolder, String CategoryName) {
        ContentValues values = new ContentValues();
        if (optionsEntity.getAUTHOR()!=null){
            values.put(NewsProvider.AUTHOR, optionsEntity.getAUTHOR());
        }else {
            values.put(NewsProvider.AUTHOR,NULL_KEY);
        }
        if (optionsEntity.getTITLE()!=null){
            values.put(NewsProvider.TITLE, optionsEntity.getTITLE());
        }else {
            values.put(NewsProvider.TITLE,NULL_KEY);
        }
        if (optionsEntity.getDESCRIPTION()!=null){
            values.put(NewsProvider.DESCRIPTION, optionsEntity.getDESCRIPTION());
        }else {
            values.put(NewsProvider.DESCRIPTION,NULL_KEY);
        }
        if (optionsEntity.getArticleID()!=null){
            values.put(NewsProvider.ARTICLE_URL, optionsEntity.getArticleID());
        }else {
            values.put(NewsProvider.ARTICLE_URL,NULL_KEY);
        }
        if (optionsEntity.getURLTOIMAGE()!=null){
            values.put(NewsProvider.IMAGE_URL, optionsEntity.getURLTOIMAGE());
        }else {
            values.put(NewsProvider.IMAGE_URL,NULL_KEY);
        }
        if (optionsEntity.getPUBLISHEDAT()!=null){
            values.put(NewsProvider.PUBLISHED_AT, optionsEntity.getPUBLISHEDAT());
        }else {
            values.put(NewsProvider.PUBLISHED_AT,NULL_KEY);
        }
        values.put(NewsProvider.CATEGORY, CategoryName);
        if (optionsEntity.getNAME()!=null){
            values.put(NewsProvider.SOURCE_NAME, optionsEntity.getNAME());
        }
        else {
            values.put(NewsProvider.SOURCE_NAME,NULL_KEY);
        }
        return values;
    }
}