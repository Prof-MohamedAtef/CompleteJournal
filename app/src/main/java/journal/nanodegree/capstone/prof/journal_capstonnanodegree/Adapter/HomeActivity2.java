package journal.nanodegree.capstone.prof.journal_capstonnanodegree.Adapter;

import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import journal.nanodegree.capstone.prof.journal_capstonnanodegree.R;

public class HomeActivity2 extends AppCompatActivity {

    ConstraintLayout homeActivity2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home2);
        homeActivity2=(ConstraintLayout)findViewById(R.id.homeActivity2);
    }
}
