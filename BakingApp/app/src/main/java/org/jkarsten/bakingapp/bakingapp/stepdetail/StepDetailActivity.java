package org.jkarsten.bakingapp.bakingapp.stepdetail;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.jkarsten.bakingapp.bakingapp.OnFragmentInteractionListener;
import org.jkarsten.bakingapp.bakingapp.R;

public class StepDetailActivity extends AppCompatActivity implements OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_detail);
    }

    @Override
    public void onFragmentInteraction() {

    }

    @Override
    public boolean isDualPane() {
        return findViewById(R.id.step_detail_fragment) != null;
    }
}
