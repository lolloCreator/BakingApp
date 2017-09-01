package org.jkarsten.bakingapp.bakingapp.stepdetail;

import dagger.Module;
import dagger.Provides;

/**
 * Created by juankarsten on 9/1/17.
 */
@Module
public class StepDetailModule {
    StepDetailContract.View mView;

    public StepDetailModule(StepDetailContract.View mView) {
        this.mView = mView;
    }

    @Provides
    public StepDetailContract.Presenter providePresenter() {
        return new StepDetailPresenter(mView);
    }
}
