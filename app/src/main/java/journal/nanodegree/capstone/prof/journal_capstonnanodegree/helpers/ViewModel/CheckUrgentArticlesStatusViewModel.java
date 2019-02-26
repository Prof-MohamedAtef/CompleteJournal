package journal.nanodegree.capstone.prof.journal_capstonnanodegree.helpers.ViewModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import java.util.List;

import journal.nanodegree.capstone.prof.journal_capstonnanodegree.helpers.Room.AppDatabase;
import journal.nanodegree.capstone.prof.journal_capstonnanodegree.helpers.Room.ArticlesEntity;

/**
 * Created by Prof-Mohamed Atef on 2/24/2019.
 */

public class CheckUrgentArticlesStatusViewModel extends ViewModel {

    private LiveData<List<ArticlesEntity>> Category;

    public LiveData<List<ArticlesEntity>> getCategory(){
        return Category;
    }


    public CheckUrgentArticlesStatusViewModel(AppDatabase mDatabase, String category) {
        Category=mDatabase.articlesDao().getArticlesDataByCategory(category);
    }
}
