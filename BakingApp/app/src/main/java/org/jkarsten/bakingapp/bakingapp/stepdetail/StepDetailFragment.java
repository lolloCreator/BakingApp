package org.jkarsten.bakingapp.bakingapp.stepdetail;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.jkarsten.bakingapp.bakingapp.OnFragmentInteractionListener;
import org.jkarsten.bakingapp.bakingapp.R;
import org.jkarsten.bakingapp.bakingapp.data.Step;
import org.jkarsten.bakingapp.bakingapp.recipedetail.RecipeDetailFragment;

public class StepDetailFragment extends Fragment {
    private OnFragmentInteractionListener mListener;
    TextView mStepDescTextView;
    View mRootView;

    Step mStep;

    public StepDetailFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_step_detail, container, false);
        return mRootView;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onStart() {
        super.onStart();
        mStepDescTextView = (TextView) mRootView.findViewById(R.id.step_description_textview);
        mStep = (Step) getActivity().getIntent().getSerializableExtra(RecipeDetailFragment.STEP_ARGS);

        mStepDescTextView.setText(mStep.getDescription());
    }
}
