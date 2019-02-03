package journal.nanodegree.capstone.prof.journal_capstonnanodegree.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

import butterknife.BindView;
import butterknife.ButterKnife;
import journal.nanodegree.capstone.prof.journal_capstonnanodegree.R;

import static journal.nanodegree.capstone.prof.journal_capstonnanodegree.Activities.ArticleTypesListActivity.URL_KEY;

public class WebViewerActivity extends AppCompatActivity {

    @BindView(R.id.webview)
    WebView webview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
        setContentView(R.layout.activity_web_viewer);
        setTheme(R.style.ArishTheme);
        ButterKnife.bind(this);

        Intent intent=getIntent();
        String url = intent.getExtras().getString(URL_KEY);
        webview.loadUrl(url);
    }
}
