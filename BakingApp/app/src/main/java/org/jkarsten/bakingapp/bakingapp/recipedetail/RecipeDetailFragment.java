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
import org.jkarsten.bakingapp.bakingapp.data.Ingredient;
import org.jkarsten.bakingapp.bakingapp.data.Step;
import org.jkarsten.bakingapp.bakingapp.foodlist.ui.FoodImageUtil;
import org.jkarsten.bakingapp.bakingapp.recipedetail.ui.IngredientsAdapter;
import org.jkarsten.bakingapp.bakingapp.recipedetail.ui.StepsAdapter;
import org.jkarsten.bakingapp.bakingapp.stepdetail.StepDetailActivity;

import java.util.List;

import javax.inject.Inject;

import static org.jkarsten.bakingapp.bakingapp.foodlist.FoodListActivity.FOOD_ARGS;

public class RecipeDetailFragment extends Fragment implements StepsAdapter.OnStepSelected,
        RecipeDetailContract.View {
    public static final String STEP_ARGS = "STEP_ARGS";
    private OnDualPaneInteractionListener mListener;

    View mRootView;
    ImageView mHeaderImageView;
    RecyclerView mIngredientsRecyclerView;
    RecyclerView mStepsRecyclerView;
    IngredientsAdapter mIngredientsAdapter;
    StepsAdapter mStepsAdapter;

    @Inject
    RecipeDetailContract.Presenter mPresenter;

    public RecipeDetailFragment() {
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
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        DaggerRecipeDetailComponent
                .builder()
                .recipeDetailModule(new RecipeDetailModule(this))
                .build()
                .inject(this);

        if (context instanceof OnDualPaneInteractionListener) {
            mListener = (OnDualPaneInteractionListener) context;

            if (context instanceof AppCompatActivity) {
                AppCompatActivity activityCompat = (AppCompatActivity) getContext();
                Food food = activityCompat.getIntent().getParcelableExtra(FOOD_ARGS);
                mPresenter.onFoodReady(food);
            }
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

        Toolbar toolbar = (Toolbar) mRootView.findViewById(R.id.toolbar);
        AppCompatActivity activity = (AppCompatActivity)getActivity();
        activity.setSupportActionBar(toolbar);

        mPresenter.start();
    }

    @Override
    public void onStop() {
        super.onStop();
        mPresenter.stop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.destroy();
    }

    @Override
    public void onSelected(Step step, int position) {
        mPresenter.onStepSelected(position);
    }

    @Override
    public void goToStepActivity(List<Step> steps, int position) {
        Intent intent = new Intent(getContext(), StepDetailActivity.class);
        intent.putExtra(FOOD_ARGS, steps.toArray(new Step[steps.size()]));
        intent.putExtra(STEP_ARGS, position);
        startActivity(intent);
    }

    @Override
    public boolean isDualPane() {
        if (mListener == null)
            return false;
        return mListener.isDualPane();
    }

    @Override
    public void showStepDetail(Step step) {
        mListener.getPublisher().onNext(step);
    }

    @Override
    public void showIngredients(List<Ingredient> ingredients) {
        if (ingredients != null) {
            mIngredientsAdapter.setIngredients(ingredients);
        }
    }

    @Override
    public void showSteps(List<Step> steps) {
        if (steps != null) {
            mStepsAdapter.setSteps(steps);
        }
    }

    @Override
    public void setHeaderImage(String foodImage) {
        //FoodImageUtil.getFoodImageURL(mFood);

        mHeaderImageView = (ImageView) mRootView.findViewById(R.id.image_header);
        Picasso.with(getContext()).load(foodImage).into(mHeaderImageView);
    }

    @Override
    public void setFoodTitle(String title) {
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) mRootView.findViewById(R.id.collapsing_toolbar_layout);
        collapsingToolbarLayout.setTitle(title);
    }
}
