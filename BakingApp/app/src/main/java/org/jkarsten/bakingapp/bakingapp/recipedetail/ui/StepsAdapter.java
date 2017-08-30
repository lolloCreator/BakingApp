package org.jkarsten.bakingapp.bakingapp.recipedetail.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.jkarsten.bakingapp.bakingapp.R;
import org.jkarsten.bakingapp.bakingapp.data.Step;

import java.util.List;

/**
 * Created by juankarsten on 8/30/17.
 */

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.StepViewHolder> {
    List<Step> mSteps;
    Context mContext;
    OnStepSelected mOnStepSelected;

    public StepsAdapter(Context mContext, OnStepSelected onStepSelected) {
        this.mContext = mContext;
        mOnStepSelected = onStepSelected;
    }

    public void setSteps(List<Step> steps) {
        this.mSteps = steps;
        notifyDataSetChanged();
    }

    @Override
    public StepViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.step_list_item, parent, false);
        return new StepViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StepViewHolder holder, int position) {
        if (mSteps != null && position < mSteps.size()) {
            Step step = mSteps.get(position);
            holder.bind(step, position);
        }
    }

    @Override
    public int getItemCount() {
        if (mSteps == null)
            return 0;
        return mSteps.size();
    }

    class StepViewHolder extends RecyclerView.ViewHolder {
        View mView;
        TextView mNumberTextView;
        TextView mStepTextView;

        public StepViewHolder(View itemView) {
            super(itemView);

            mView = itemView;

            mNumberTextView = (TextView) itemView.findViewById(R.id.step_item_number_textview);
            mStepTextView = (TextView) itemView.findViewById(R.id.step_item_name_textview);
        }

        public void bind(final Step step, final int position) {
            mNumberTextView.setText(String.valueOf(position));
            mStepTextView.setText(step.getShortDescription());

            mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnStepSelected.onSelected(step, position);
                }
            });
        }
    }

    public interface OnStepSelected {
        void onSelected(Step step, int position);
    }
}
