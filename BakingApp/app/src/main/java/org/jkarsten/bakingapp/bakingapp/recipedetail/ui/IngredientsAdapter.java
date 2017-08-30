package org.jkarsten.bakingapp.bakingapp.recipedetail.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.jkarsten.bakingapp.bakingapp.R;
import org.jkarsten.bakingapp.bakingapp.data.Ingredient;

import java.util.List;

/**
 * Created by juankarsten on 8/30/17.
 */

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.IngredientViewHolder> {
    private Context mContext;
    private List<Ingredient> ingredients;

    public IngredientsAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
        notifyDataSetChanged();
    }

    @Override
    public IngredientViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.ingredient_list_item, parent, false);
        return new IngredientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(IngredientViewHolder holder, int position) {
        if (ingredients != null && ingredients.size() > position) {
            holder.bind(ingredients.get(position), position);
        }
    }

    @Override
    public int getItemCount() {
        if (ingredients == null) {
            return 0;
        }
        Log.d(IngredientsAdapter.class.getSimpleName(), String.valueOf(ingredients.size()));
        return ingredients.size();
    }

    public class IngredientViewHolder extends RecyclerView.ViewHolder {
        TextView mIngredientNumberTextView;
        TextView mIngredientNameTextView;

        public IngredientViewHolder(View itemView) {
            super(itemView);

            mIngredientNumberTextView = (TextView) itemView.findViewById(R.id.ingredient_item_number_textview);
            mIngredientNameTextView = (TextView) itemView.findViewById(R.id.ingredient_item_name_textview);
        }

        public void bind(Ingredient ingredient, int position) {
            mIngredientNumberTextView.setText(String.valueOf(position));
            mIngredientNameTextView.setText(ingredient.getQuantity() + " " + ingredient.getMeasure() + "  "
                    +  ingredient.getIngredient());
        }
    }
}
