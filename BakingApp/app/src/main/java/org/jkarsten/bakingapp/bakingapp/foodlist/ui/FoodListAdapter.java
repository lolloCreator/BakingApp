package org.jkarsten.bakingapp.bakingapp.foodlist.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.jkarsten.bakingapp.bakingapp.R;
import org.jkarsten.bakingapp.bakingapp.data.Food;
import org.jkarsten.bakingapp.bakingapp.foodlist.FoodListActivity;
import org.jkarsten.bakingapp.bakingapp.idlingResource.SimpleIdlingResource;

import java.util.List;

/**
 * Created by juankarsten on 8/30/17.
 */

public class FoodListAdapter extends RecyclerView.Adapter<FoodListAdapter.FoodListViewHolder> {
    private SimpleIdlingResource mSimpleIdlingResource;
    private List<Food> foods;
    private Context mContext;
    private OnFoodSelected mOnFoodSelected;

    public FoodListAdapter(Context mContext, OnFoodSelected onFoodSelected) {
        this.mContext = mContext;
        this.mOnFoodSelected = onFoodSelected;
    }

    public FoodListAdapter(Context context, OnFoodSelected onFoodSelected, SimpleIdlingResource simpleIdlingResource) {
        this(context, onFoodSelected);
        mSimpleIdlingResource = simpleIdlingResource;
    }

    public void setFoods(List<Food> foods) {
        this.foods = foods;
        notifyDataSetChanged();
    }

    @Override
    public FoodListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.food_list_item, parent, false);
        FoodListViewHolder vh = new FoodListViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(FoodListViewHolder holder, int position) {
        if (foods != null && foods.size() > position) {
            Food food = foods.get(position);
            holder.bind(food);
        }
    }

    @Override
    public int getItemCount() {
        if (foods == null) {
            return 0;
        }
        Log.d(FoodListAdapter.class.getSimpleName(), foods.size()+"");
        return foods.size();
    }

    public class FoodListViewHolder extends RecyclerView.ViewHolder {
        ImageView mFoodImageView;
        TextView mFoodNameTextView;

        public FoodListViewHolder(View itemView) {
            super(itemView);

            mFoodImageView = (ImageView) itemView.findViewById(R.id.food_item_image);
            mFoodNameTextView = (TextView) itemView.findViewById(R.id.food_item_name_textview);
        }

        public void bind(final Food food) {
            String url = FoodImageUtil.getFoodImageURL(food);
            if (mSimpleIdlingResource != null)
                mSimpleIdlingResource.setIsIdleResource(false);
            Picasso.with(mContext)
                    .load(url)
                    .into(mFoodImageView, new Callback() {
                        @Override
                        public void onSuccess() {
                            if (mSimpleIdlingResource != null)
                                mSimpleIdlingResource.setIsIdleResource(true);
                        }

                        @Override
                        public void onError() {
                            if (mSimpleIdlingResource != null)
                                mSimpleIdlingResource.setIsIdleResource(true);
                        }
                    });

            mFoodNameTextView.setText(food.getName());

            mFoodImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnFoodSelected.onSelected(food);
                }
            });
        }
    }

    public interface OnFoodSelected {
        void onSelected(Food food);
    }
}
