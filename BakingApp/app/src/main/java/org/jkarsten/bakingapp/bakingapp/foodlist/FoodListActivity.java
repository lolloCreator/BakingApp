package org.jkarsten.bakingapp.bakingapp.foodlist;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import org.jkarsten.bakingapp.bakingapp.R;
import org.jkarsten.bakingapp.bakingapp.data.Food;
import org.jkarsten.bakingapp.bakingapp.data.FoodDataModule;
import org.jkarsten.bakingapp.bakingapp.foodlist.ui.FoodListAdapter;
import org.jkarsten.bakingapp.bakingapp.recipedetail.RecipeDetailActivity;

import java.util.List;

import javax.inject.Inject;

import dagger.internal.DaggerCollections;

public class FoodListActivity extends AppCompatActivity implements FoodListContract.View,
        FoodListAdapter.OnFoodSelected {
    public static final String FOOD_ARGS = "food args";


    @Inject
    FoodListContract.Presenter mPresenter;

    RecyclerView mFoodListRecyclerView;
    FoodListAdapter mFoodListAdapter;
    GridLayoutManager mGridLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DaggerFoodListComponent
                .builder()
                .foodListModule(new FoodListModule(this))
                .foodDataModule(new FoodDataModule(this))
                .build()
                .inject(this);

        mFoodListRecyclerView = (RecyclerView) findViewById(R.id.food_list_recyclerview);
        mGridLayoutManager = new GridLayoutManager(this, 2);
        mFoodListRecyclerView.setLayoutManager(mGridLayoutManager);
        mFoodListAdapter = new FoodListAdapter(this, this);
        mFoodListRecyclerView.setAdapter(mFoodListAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mPresenter.start();
    }

    @Override
    protected void onStop() {
        mPresenter.stop();
        super.onStop();
    }

    @Override
    public void showNoInternet() {

    }

    @Override
    public void showFoods(List<Food> foods) {
        mFoodListAdapter.setFoods(foods);
    }

    @Override
    public void goToFoodActivity(Food food) {
        Intent intent = new Intent(this, RecipeDetailActivity.class);
        intent.putExtra(FOOD_ARGS, food);
        startActivity(intent);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void onSelected(Food food) {
        mPresenter.viewFood(food);
    }
}
