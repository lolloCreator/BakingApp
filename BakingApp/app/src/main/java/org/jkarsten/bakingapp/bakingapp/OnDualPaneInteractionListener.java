package org.jkarsten.bakingapp.bakingapp;

import org.jkarsten.bakingapp.bakingapp.data.Food;
import org.jkarsten.bakingapp.bakingapp.data.Step;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by juankarsten on 8/30/17.
 */

public interface OnDualPaneInteractionListener {
    boolean isDualPane();
    PublishSubject<Step> getPublisher();

     class Data {
        private Food mFood;
        private boolean mInital;

        public Data(Food food) {
            this.mFood = food;
            mInital = false;
        }

        public Food getmFood() {
            return mFood;
        }

        public void setmFood(Food mFood) {
            this.mFood = mFood;
        }

        public boolean ismInital() {
            return mInital;
        }

        public void setmInital(boolean mInital) {
            this.mInital = mInital;
        }
    }
}
