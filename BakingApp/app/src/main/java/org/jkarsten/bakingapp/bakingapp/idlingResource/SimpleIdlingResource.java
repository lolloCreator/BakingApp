package org.jkarsten.bakingapp.bakingapp.idlingResource;

import android.support.annotation.Nullable;
import android.support.test.espresso.IdlingResource;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by juankarsten on 9/23/17.
 */

public class SimpleIdlingResource implements IdlingResource {
    AtomicBoolean mIsIdleResource = new AtomicBoolean(true);
    @Nullable private volatile ResourceCallback mCallback;
    AtomicInteger activeProcesses = new AtomicInteger(0);

    @Override
    public String getName() {
        return SimpleIdlingResource.class.getSimpleName();
    }

    @Override
    public boolean isIdleNow() {
        return mIsIdleResource.get();
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback callback) {
        mCallback = callback;
    }

    public void setIsIdleResource(boolean isIdleResource) {
        if (isIdleResource && activeProcesses.get() > 0) {
            activeProcesses.decrementAndGet();
        }

        if (isIdleResource && activeProcesses.get() <= 0) {
            activeProcesses.set(0);
            mIsIdleResource.set(isIdleResource);
        } else if (!isIdleResource) {
            activeProcesses.incrementAndGet();
            mIsIdleResource.set(isIdleResource);
        }

        if (mIsIdleResource.get() && mCallback != null) { // not busy
            mCallback.onTransitionToIdle();
        }
    }
}
