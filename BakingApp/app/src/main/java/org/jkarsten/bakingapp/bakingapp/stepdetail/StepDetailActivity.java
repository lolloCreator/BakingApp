package org.jkarsten.bakingapp.bakingapp.stepdetail;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import org.jkarsten.bakingapp.bakingapp.OnDualPaneInteractionListener;
import org.jkarsten.bakingapp.bakingapp.R;
import org.jkarsten.bakingapp.bakingapp.data.Step;

import io.reactivex.subjects.PublishSubject;

public class StepDetailActivity extends AppCompatActivity implements OnDualPaneInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @Override
    public boolean isDualPane() {
        return false;
    }

    @Override
    public PublishSubject<Step> getPublisher() {
        throw new UnsupportedOperationException();
    }
}
