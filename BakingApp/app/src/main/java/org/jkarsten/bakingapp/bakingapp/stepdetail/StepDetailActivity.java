package org.jkarsten.bakingapp.bakingapp.stepdetail;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.jkarsten.bakingapp.bakingapp.OnDualPaneInteractionListener;
import org.jkarsten.bakingapp.bakingapp.R;

import io.reactivex.subjects.PublishSubject;

public class StepDetailActivity extends AppCompatActivity implements OnDualPaneInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_detail);
    }

    @Override
    public boolean isDualPane() {
        return false;
    }

    @Override
    public PublishSubject<Data> getPublisher() {
        throw new UnsupportedOperationException();
    }
}
