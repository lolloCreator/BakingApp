package org.jkarsten.bakingapp.bakingapp.stepdetail;

import org.jkarsten.bakingapp.bakingapp.BasePresenter;
import org.jkarsten.bakingapp.bakingapp.BaseView;
import org.jkarsten.bakingapp.bakingapp.data.Step;

import java.util.List;

import io.reactivex.disposables.Disposable;

/**
 * Created by juankarsten on 9/1/17.
 */

public interface StepDetailContract {

    interface View extends BaseView {
        void showStep(Step step);
        Disposable createStepSubscription();
        boolean isDualPane();
        void hidePreviousAndNextButton();
        void hideVideoUnavailable();
        void showVideoUnavailable();
    }

    interface Presenter extends BasePresenter {
        void onStepsReady(Step[] steps, int intialPosition);
        void onStepSelected(Step step);
        void onNextButtonPressed();
        void onPreviousButtonPressed();

    }
}
