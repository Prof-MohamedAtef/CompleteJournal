package journal.nanodegree.capstone.prof.journal_capstonnanodegree.helpers.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.support.annotation.NonNull;

import java.util.List;

import journal.nanodegree.capstone.prof.journal_capstonnanodegree.helpers.BasicApp;
import journal.nanodegree.capstone.prof.journal_capstonnanodegree.helpers.Config;
import journal.nanodegree.capstone.prof.journal_capstonnanodegree.helpers.Room.ArticlesEntity;

/**
 * Created by Prof-Mohamed Atef on 2/24/2019.
 */

public class ArticlesViewModel extends AndroidViewModel {

    String Category;

    private final MediatorLiveData<List<ArticlesEntity>> mObserverMediatorLiveDataListUrgentArticles;

    public ArticlesViewModel(@NonNull Application application) {
        super(application);
        Config.application=application;
        this.mObserverMediatorLiveDataListUrgentArticles = new MediatorLiveData<>();
        this.mObserverMediatorLiveDataListUrgentArticles.setValue(null);
        LiveData<List<ArticlesEntity>> UrgentArticlesList=((BasicApp)application).getRepository().LoadUrgentArticle(this.Category);
        mObserverMediatorLiveDataListUrgentArticles.addSource(UrgentArticlesList, mObserverMediatorLiveDataListUrgentArticles::setValue);
    }

    public void setCategory(String category){
        this.Category=category;
        LiveData<List<ArticlesEntity>> LoadedArticlesList=((BasicApp) Config.application).getRepository().LoadUrgentArticle(category);
        mObserverMediatorLiveDataListUrgentArticles.addSource(LoadedArticlesList, mObserverMediatorLiveDataListUrgentArticles::setValue);
    }

    public String getCategory() {
        return Category;
    }

    public MediatorLiveData<List<ArticlesEntity>> getmObserverMediatorLiveDataListUrgentArticles() {
        return mObserverMediatorLiveDataListUrgentArticles;
    }

}
