package journal.nanodegree.capstone.prof.journal_capstonnanodegree.Listeners;

import android.content.ContentProviderOperation;

import java.util.ArrayList;

/**
 * Created by Prof-Mohamed Atef on 2/19/2019.
 */

public interface InstantiateContentProvider {
    void OnContentProviderInstantiated(ArrayList<ContentProviderOperation> result);
}
