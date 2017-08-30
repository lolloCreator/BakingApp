package org.jkarsten.bakingapp.bakingapp.recipedetail;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.jkarsten.bakingapp.bakingapp.OnFragmentInteractionListener;
import org.jkarsten.bakingapp.bakingapp.R;
import org.jkarsten.bakingapp.bakingapp.data.Food;
import org.jkarsten.bakingapp.bakingapp.foodlist.FoodListActivity;

public class RecipeDetailActivity extends AppCompatActivity implements OnFragmentInteractionListener{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);



    }

    @Override
    public boolean isDualPane() {
        return findViewById(R.id.recipe_detail_fragment) != null;
    }

    @Override
    public void onFragmentInteraction() {

    }

}
