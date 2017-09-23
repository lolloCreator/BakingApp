package org.jkarsten.bakingapp.bakingapp.recipedetail;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.jkarsten.bakingapp.bakingapp.OnDualPaneInteractionListener;
import org.jkarsten.bakingapp.bakingapp.R;
import org.jkarsten.bakingapp.bakingapp.data.Food;
import org.jkarsten.bakingapp.bakingapp.data.FoodDataModule;
import org.jkarsten.bakingapp.bakingapp.data.Ingredient;
import org.jkarsten.bakingapp.bakingapp.data.Step;
import org.jkarsten.bakingapp.bakingapp.idlingResource.SimpleIdlingResource;
import org.jkarsten.bakingapp.bakingapp.recipedetail.ui.IngredientsAdapter;
import org.jkarsten.bakingapp.bakingapp.recipedetail.ui.StepsAdapter;
import org.jkarsten.bakingapp.bakingapp.stepdetail.StepDetailActivity;
import org.jkarsten.bakingapp.bakingapp.widget.BakingWidgetProvider;
import org.jkarsten.bakingapp.bakingapp.widget.ListViewRemoteViewFactory;

import java.util.List;

import javax.inject.Inject;

import timber.log.Timber;

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
    FloatingActionButton favoriteButton;

    private static SimpleIdlingResource mSimpleIdlingResource;

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

        favoriteButton = (FloatingActionButton) mRootView.findViewById(R.id.favorite_toggle_button);
        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Timber.d("favoriteButton clicked");
                if (mSimpleIdlingResource != null)
                    mSimpleIdlingResource.setIsIdleResource(false);
                mPresenter.onClickFavoriteButton();
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        DaggerRecipeDetailComponent
                .builder()
                .recipeDetailModule(new RecipeDetailModule(this))
                .foodDataModule(new FoodDataModule(context))
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

        if (mSimpleIdlingResource != null)
            mSimpleIdlingResource.setIsIdleResource(false);
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

    @Override
    public void updateWidget(Food food) {
        //ListViewRemoteViewFactory.sendBroadcast(getActivity(), food);
        //Trigger data update to handle the GridView widgets and force a data refresh
        BakingWidgetProvider.notifyDataSetChanged(getContext());
        if (mSimpleIdlingResource != null)
            mSimpleIdlingResource.setIsIdleResource(true);
    }

    @Override
    public void showImage(boolean favorite) {
        if (favorite) {
            favoriteButton.setImageResource(R.drawable.pentagon_made_of_stars);
        } else {
            favoriteButton.setImageResource(R.drawable.favourite_star);
        }
    }

    public static SimpleIdlingResource getSimpleIdlingResource() {
        if (mSimpleIdlingResource == null) {
            mSimpleIdlingResource = new SimpleIdlingResource();
        }
        return mSimpleIdlingResource;
    }
}
