package journal.nanodegree.capstone.prof.journal_capstonnanodegree.Activities;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import butterknife.ButterKnife;
import journal.nanodegree.capstone.prof.journal_capstonnanodegree.R;

public class ArticleDetailsActivity extends AppCompatActivity {


//    private MyPagerAdapter mPagerAdapter;
//    ViewPager mPager;
//
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_details);
//        ButterKnife.bind(this);
//        mPagerAdapter = new MyPagerAdapter(getFragmentManager());
//        mPager.setAdapter(mPagerAdapter);
//        mPager.setCurrentItem(mCurrentPosition);
//        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                if (mCursor != null) {
//                    mCursor.moveToPosition(position);
//                    mCurrentPosition = position;
//                }
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//            }
//        });
//
    }
}
