package org.jkarsten.bakingapp.bakingapp.recipedetail;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.jkarsten.bakingapp.bakingapp.R;
import org.jkarsten.bakingapp.bakingapp.data.Food;
import org.jkarsten.bakingapp.bakingapp.foodlist.FoodListActivity;
import org.jkarsten.bakingapp.bakingapp.recipedetail.ui.IngredientsAdapter;

public class RecipeDetailFragment extends Fragment {
    private OnFragmentInteractionListener mListener;
    boolean mDualPane;

    View mRootView;
    RecyclerView mIngredientsRecyclerView;
    RecyclerView mStepsRecyclerView;
    IngredientsAdapter mIngredientsAdapter;

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
        mIngredientsRecyclerView = (RecyclerView) mRootView.findViewById(R.id.ingredients_recylerview);
        mStepsRecyclerView = (RecyclerView) mRootView.findViewById(R.id.steps_recyclerview);

        mIngredientsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mIngredientsAdapter = new IngredientsAdapter(getContext());
        mIngredientsRecyclerView.setAdapter(mIngredientsAdapter);
        if (mFood != null) {
            mIngredientsAdapter.setIngredients(mFood.getIngredients());
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
            mDualPane = mListener.isDualPane();

            AppCompatActivity activityCompat = (AppCompatActivity) context;
            mFood = (Food) activityCompat.getIntent().getSerializableExtra(FoodListActivity.FOOD_ARGS);
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
        initRecyclerView();
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction();
        boolean isDualPane();
    }
}
