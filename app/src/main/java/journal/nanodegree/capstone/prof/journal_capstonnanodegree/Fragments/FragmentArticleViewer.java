package journal.nanodegree.capstone.prof.journal_capstonnanodegree.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.squareup.picasso.Picasso;

import journal.nanodegree.capstone.prof.journal_capstonnanodegree.Activities.WebViewerActivity;
import journal.nanodegree.capstone.prof.journal_capstonnanodegree.R;
import journal.nanodegree.capstone.prof.journal_capstonnanodegree.helpers.Config;
import journal.nanodegree.capstone.prof.journal_capstonnanodegree.helpers.OptionsEntity;

import static journal.nanodegree.capstone.prof.journal_capstonnanodegree.Activities.ArticleTypesListActivity.URL_KEY;

/**
 * Created by Prof-Mohamed Atef on 1/10/2019.
 */

public class FragmentArticleViewer extends android.app.Fragment {

    private TextView Title;
    private TextView Author;
    private TextView Date;
    private TextView Description;
    private TextView SourceName;
    private ImageView Image;
    private LinearLayout linearLayout;
    public static String KEY_optionsEntity = "Options";
    private OptionsEntity optionsEntity;
    private TextView read_more;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.article_viewer_fragment, container, false);
        Title = (TextView) rootView.findViewById(R.id.title);
        Author = (TextView) rootView.findViewById(R.id.author);
        Date = (TextView) rootView.findViewById(R.id.date_publish);
        Description = (TextView) rootView.findViewById(R.id.description);
        SourceName = (TextView) rootView.findViewById(R.id.source_name);
        Image = (ImageView) rootView.findViewById(R.id.image);
        linearLayout = (LinearLayout) rootView.findViewById(R.id.linearLayout);
        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(KEY_optionsEntity,optionsEntity);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            optionsEntity = (OptionsEntity) savedInstanceState.getSerializable(KEY_optionsEntity);
            DisplayData(optionsEntity);
        } else if (savedInstanceState == null) {
            final Bundle bundle = getArguments();
            if (bundle != null) {
                optionsEntity = (OptionsEntity) bundle.getSerializable("twoPaneExtras");
                DisplayData(optionsEntity);
            }else {
                if (Config.ArrArticle!=null&&Config.ArrArticle.size()>0) {
                    OptionsEntity optionsEntity = Config.ArrArticle.get(0);
                    DisplayData(optionsEntity);
                }
            }
        }
    }

    private void DisplayData(final OptionsEntity optionsEntity) {
        if (optionsEntity!=null){
            if (optionsEntity.getAUTHOR()!=null&&optionsEntity.getTITLE()!=null){
                Author.setText(optionsEntity.getAUTHOR());
                Title.setText(optionsEntity.getTITLE());
                if (optionsEntity.getDESCRIPTION()!=null&&optionsEntity.getNAME()!=null){
                    Description.setText(optionsEntity.getDESCRIPTION());
                    SourceName.setText(optionsEntity.getNAME());
                    if (optionsEntity.getPUBLISHEDAT()!=null&&optionsEntity.getURL()!=null&&optionsEntity.getURLTOIMAGE()!=null){
                        Date.setText(optionsEntity.getPUBLISHEDAT());
                        Picasso.with(getActivity()).load(optionsEntity.getURLTOIMAGE())
                                .error(R.drawable.stanly)
                                .into(Image);
                        linearLayout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (optionsEntity.getURL() != null) {
                                    String url=optionsEntity.getURL();
                                    Intent intent=new Intent(getActivity(),WebViewerActivity.class);
                                    intent.putExtra(URL_KEY,url);
                                    getActivity().startActivity(intent);
                                }
                            }
                        });
                    }else {Date.setText("");}
                }else {
                    Description.setText("");
                    SourceName.setText("");
                }
            }else {
                Author.setText("");
                Title.setText("");
            }
        }else {
            Date.setText("");
            Description.setText("");
            SourceName.setText("");
            Author.setText("");
            Title.setText("");
        }
    }
}