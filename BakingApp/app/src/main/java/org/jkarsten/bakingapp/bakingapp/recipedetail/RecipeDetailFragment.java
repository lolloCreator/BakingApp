package org.jkarsten.bakingapp.bakingapp.recipedetail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.jkarsten.bakingapp.bakingapp.OnDualPaneInteractionListener;
import org.jkarsten.bakingapp.bakingapp.R;
import org.jkarsten.bakingapp.bakingapp.data.Food;
import org.jkarsten.bakingapp.bakingapp.data.Step;
import org.jkarsten.bakingapp.bakingapp.foodlist.ui.FoodImageUtil;
import org.jkarsten.bakingapp.bakingapp.recipedetail.ui.IngredientsAdapter;
import org.jkarsten.bakingapp.bakingapp.recipedetail.ui.StepsAdapter;
import org.jkarsten.bakingapp.bakingapp.stepdetail.StepDetailActivity;

import static org.jkarsten.bakingapp.bakingapp.foodlist.FoodListActivity.FOOD_ARGS;

public class RecipeDetailFragment extends Fragment implements StepsAdapter.OnStepSelected {
    public static final String STEP_ARGS = "STEP_ARGS";
    private OnDualPaneInteractionListener mListener;
    boolean mDualPane;

    View mRootView;
    ImageView mHeaderImageView;
    RecyclerView mIngredientsRecyclerView;
    RecyclerView mStepsRecyclerView;
    IngredientsAdapter mIngredientsAdapter;
    StepsAdapter mStepsAdapter;


    private Food mFood;

    public RecipeDetailFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDualPane = false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_recipe_detail, container, false);
        return mRootView;
    }

    private void initRecyclerView() {
        mStepsRecyclerView = (RecyclerView) mRootView.findViewById(R.id.steps_recyclerview);
        mStepsAdapter = new StepsAdapter(getContext(), this);
        mStepsRecyclerView.setAdapter(mStepsAdapter);
        mStepsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mIngredientsRecyclerView = (RecyclerView) mRootView.findViewById(R.id.ingredients_recylerview);
        mIngredientsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mIngredientsAdapter = new IngredientsAdapter(getContext());
        mIngredientsRecyclerView.setAdapter(mIngredientsAdapter);

        if (mFood != null) {
            mIngredientsAdapter.setIngredients(mFood.getIngredients());
            mStepsAdapter.setSteps(mFood.getSteps());
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnDualPaneInteractionListener) {
            mListener = (OnDualPaneInteractionListener) context;
            mDualPane = mListener.isDualPane();

            AppCompatActivity activityCompat = (AppCompatActivity) context;
            mFood = (Food) activityCompat.getIntent().getSerializableExtra(FOOD_ARGS);
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnDualPaneInteractionListener");
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
        initRecyclerView();
        FoodImageUtil.getFoodImageURL(mFood);

        mHeaderImageView = (ImageView) mRootView.findViewById(R.id.image_header);
        Picasso.with(getContext()).load(mFood.getImage()).into(mHeaderImageView);

        Toolbar toolbar = (Toolbar) mRootView.findViewById(R.id.toolbar);
        AppCompatActivity activity = (AppCompatActivity)getActivity();
        activity.setSupportActionBar(toolbar);

        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) mRootView.findViewById(R.id.collapsing_toolbar_layout);
        collapsingToolbarLayout.setTitle(mFood.getName());
    }

    @Override
    public void onSelected(Step step, int position) {
        goToStepActivity(step, position);
    }

    public void goToStepActivity(Step step, int position) {
        Intent intent = new Intent(getContext(), StepDetailActivity.class);
        intent.putExtra(FOOD_ARGS, mFood);
        intent.putExtra(STEP_ARGS, position);
        startActivity(intent);
    }
}
