package org.jkarsten.bakingapp.bakingapp.stepdetail;

import dagger.Component;

/**
 * Created by juankarsten on 9/1/17.
 */

@Component(modules = {StepDetailModule.class})
public interface StepDetailComponent {
    void inject(StepDetailFragment fragment);
}
