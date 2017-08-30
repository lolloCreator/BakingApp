package org.jkarsten.bakingapp.bakingapp.data;

import org.jkarsten.bakingapp.bakingapp.data.source.FoodDataSource;
import org.jkarsten.bakingapp.bakingapp.data.source.remote.RemoteFoodDataSource;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;

/**
 * Created by juankarsten on 8/30/17.
 */

@Module
public class FoodDataModule {
    @Provides
    public FoodDataSource provideRemoteDataSource() {
        OkHttpClient mClient = new OkHttpClient();
        return new RemoteFoodDataSource(mClient);
    }


}
