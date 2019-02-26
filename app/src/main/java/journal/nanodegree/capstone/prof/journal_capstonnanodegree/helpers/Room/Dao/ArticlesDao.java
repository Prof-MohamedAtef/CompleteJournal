package journal.nanodegree.capstone.prof.journal_capstonnanodegree.helpers.Room.Dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import journal.nanodegree.capstone.prof.journal_capstonnanodegree.helpers.Room.ArticlesEntity;

/**
 * Created by Prof-Mohamed Atef on 2/26/2019.
 */

public interface ArticlesDao {
    @Insert
    long InsertArticle(ArticlesEntity articleEntity);

    @Query("DELETE FROM Articles WHERE CATEGORY LIKE :CATEGORY")
    abstract int deleteByCATEGORY(String CATEGORY);

    @Query("SELECT * From Articles where CATEGORY LIKE :CATEGORY")
    LiveData<List<ArticlesEntity>> getArticlesDataByCategory(String CATEGORY);

    @Query("SELECT * From Articles")
    LiveData<List<ArticlesEntity>> getAllArticlesData();
}
