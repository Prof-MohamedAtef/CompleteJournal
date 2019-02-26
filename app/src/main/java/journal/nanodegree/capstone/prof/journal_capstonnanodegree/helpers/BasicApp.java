package journal.nanodegree.capstone.prof.journal_capstonnanodegree.helpers;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import journal.nanodegree.capstone.prof.journal_capstonnanodegree.helpers.Room.AppDatabase;
import journal.nanodegree.capstone.prof.journal_capstonnanodegree.helpers.Room.LiveDataRepo;

/**
 * Created by Prof-Mohamed Atef on 2/26/2019.
 */

public class BasicApp extends Application{
    private AppExecutors mAppExecutors;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(getBaseContext());
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mAppExecutors = new AppExecutors();
    }

    public AppDatabase getDatabase() {
        return AppDatabase.getInstance(this, mAppExecutors);
    }

    public LiveDataRepo getRepository() {
        return LiveDataRepo.getLiveDataRepoInstance(getDatabase());
    }
}
