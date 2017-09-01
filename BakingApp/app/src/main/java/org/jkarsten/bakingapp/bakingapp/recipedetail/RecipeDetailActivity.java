package org.jkarsten.bakingapp.bakingapp.recipedetail;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.jkarsten.bakingapp.bakingapp.OnDualPaneInteractionListener;
import org.jkarsten.bakingapp.bakingapp.R;
import org.jkarsten.bakingapp.bakingapp.data.Step;

import io.reactivex.subjects.PublishSubject;

public class RecipeDetailActivity extends AppCompatActivity implements OnDualPaneInteractionListener {
    private PublishSubject<Step> mPublishSubject;
    private boolean mDualPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        mDualPane = (findViewById(R.id.dual_pane_layout) != null);


    }

    @Override
    protected void onStart() {
        super.onStart();

        if (isDualPane()) {
            mPublishSubject = PublishSubject.create();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isDualPane()) {
            mPublishSubject.onComplete();
        }
    }

    @Override
    public boolean isDualPane() {
        return mDualPane;
    }

    @Override
    public PublishSubject<Step> getPublisher() {
        if (!isDualPane()) {
            throw new UnsupportedOperationException();
        }
        return mPublishSubject;
    }
}
