package journal.nanodegree.capstone.prof.journal_capstonnanodegree.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import journal.nanodegree.capstone.prof.journal_capstonnanodegree.Activities.WebViewerActivity;
import journal.nanodegree.capstone.prof.journal_capstonnanodegree.R;
import journal.nanodegree.capstone.prof.journal_capstonnanodegree.helpers.Config;
import journal.nanodegree.capstone.prof.journal_capstonnanodegree.helpers.Firebase.FirebaseDataHolder;
import journal.nanodegree.capstone.prof.journal_capstonnanodegree.helpers.OptionsEntity;

import static journal.nanodegree.capstone.prof.journal_capstonnanodegree.Activities.ArticleTypesListActivity.TwoPANEExtras_KEY;
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
    public static String KEY_firebase= "firebase";
    private OptionsEntity optionsEntity;
    private TextView read_more;
    private FirebaseDataHolder firebaseDataHolder;


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
        if (Config.RetrieveFirebaseData){
            outState.putSerializable(KEY_firebase, firebaseDataHolder);
        }else {
            outState.putSerializable(KEY_optionsEntity, optionsEntity);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            if (Config.RetrieveFirebaseData){
                firebaseDataHolder = (FirebaseDataHolder) savedInstanceState.getSerializable(KEY_firebase);
                DisplayFirebaseData(firebaseDataHolder);
            }else {
                optionsEntity = (OptionsEntity) savedInstanceState.getSerializable(KEY_optionsEntity);
                DisplayOptionsData(optionsEntity);
            }
        } else if (savedInstanceState == null) {
            final Bundle bundle = getArguments();
            if (bundle != null) {
                if (Config.RetrieveFirebaseData){
                    firebaseDataHolder=(FirebaseDataHolder) bundle.getSerializable(TwoPANEExtras_KEY);
                    DisplayFirebaseData(firebaseDataHolder);
                }else {
                    optionsEntity = (OptionsEntity) bundle.getSerializable(TwoPANEExtras_KEY);
                    DisplayOptionsData(optionsEntity);
                }
            }else {
                if (Config.ArrArticle!=null&&Config.ArrArticle.size()>0) {
                    OptionsEntity optionsEntity = Config.ArrArticle.get(0);
                    DisplayOptionsData(optionsEntity);
                }
            }
        }
    }

    private void DisplayOptionsData(final OptionsEntity optionsEntity) {
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

    private void DisplayFirebaseData(final FirebaseDataHolder firebaseDataHolder) {
        if (firebaseDataHolder!=null){
            if (firebaseDataHolder.getUserName()!=null&&firebaseDataHolder.getTITLE()!=null){
                Author.setText(firebaseDataHolder.getUserName());
                Title.setText(firebaseDataHolder.getTITLE());
                if (firebaseDataHolder.getDESCRIPTION()!=null&&firebaseDataHolder.getCategoryID()!=null){
                    Description.setText(firebaseDataHolder.getDESCRIPTION());
                    SourceName.setText(firebaseDataHolder.getCategoryID());
                    if (firebaseDataHolder.getDate()!=null&&firebaseDataHolder.getImageFileUri()!=null){
                        Date.setText(firebaseDataHolder.getDate());
                        Picasso.with(getActivity()).load(firebaseDataHolder.getImageFileUri())
                                .error(R.drawable.stanly)
                                .into(Image);
//                        linearLayout.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                if (firebaseDataHolder.getURL() != null) {
//                                    String url=firebaseDataHolder.getURL();
//                                    Intent intent=new Intent(getActivity(),WebViewerActivity.class);
//                                    intent.putExtra(URL_KEY,url);
//                                    getActivity().startActivity(intent);
//                                }
//                            }
//                        });
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