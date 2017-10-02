package org.jkarsten.bakingapp.bakingapp.stepdetail;

import org.jkarsten.bakingapp.bakingapp.data.Step;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by juankarsten on 9/1/17.
 */

public class StepDetailPresenter implements StepDetailContract.Presenter {
    private StepDetailContract.View mView;
    private int mPosition;
    private Step[] mSteps;
    private CompositeDisposable mCompositeDisposable;

    public StepDetailPresenter(StepDetailContract.View mView) {
        this.mView = mView;
    }

    @Override
    public void start() {
        if (mView.isDualPane()) {
            mCompositeDisposable = new CompositeDisposable();
            Disposable disposable = mView.createStepSubscription();
            mCompositeDisposable.add(disposable);
            mView.hidePreviousAndNextButton();
        } else if (mSteps != null && mPosition < mSteps.length) {
            checkVideoAvailability(mSteps[mPosition]);
            mView.showStep(mSteps[mPosition]);
        }
    }

    @Override
    public void stop() {
        if (mCompositeDisposable != null && !mCompositeDisposable.isDisposed()) {
            mCompositeDisposable.dispose();
        }
    }

    @Override
    public void destroy() {

    }

    @Override
    public void onStepsReady(Step[] steps, int intialPosition) {
        mSteps = steps;
        mPosition = intialPosition;
    }

    @Override
    public void onStepSelected(Step step) {
        if (mView.isDualPane()) {
            checkVideoAvailability(step);
            mView.showStep(step);
        }
    }

    @Override
    public void onNextButtonPressed() {
        if (!mView.isDualPane() && mSteps != null && mPosition + 1 < mSteps.length) {
            mPosition++;
            checkVideoAvailability(mSteps[mPosition]);
            mView.showStep(mSteps[mPosition]);
        }
    }

    @Override
    public void onPreviousButtonPressed() {
        if (!mView.isDualPane() && mSteps != null && mPosition - 1 >= 0) {
            mPosition--;
            checkVideoAvailability(mSteps[mPosition]);
            mView.showStep(mSteps[mPosition]);
        }
    }

    private void checkVideoAvailability(Step step) {
        String video = null;
        if (step.getVideoURL() != null && step.getVideoURL().length() != 0) {
            video = step.getVideoURL();
        } else if (step.getThumbnailURL() != null && step.getThumbnailURL().length() != 0) {
            video = step.getThumbnailURL();
        }
        if (video == null) {
            mView.showVideoUnavailable();
        } else {
            mView.hideVideoUnavailable();
        }
    }
}
