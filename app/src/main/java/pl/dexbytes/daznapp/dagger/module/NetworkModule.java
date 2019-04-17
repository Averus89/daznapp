package pl.dexbytes.daznapp.dagger.module;

import android.app.Application;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import java8.util.J8Arrays;
import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import pl.dexbytes.daznapp.dagger.scope.DaznApiScope;
import pl.dexbytes.daznapp.net.BaseUrlChangingInterceptor;
import pl.dexbytes.daznapp.net.DaznApi;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class NetworkModule {
    @Provides
    @Singleton
    Cache providesOkHttpCache(Application application) {
        int cacheSize = 10 * 1024 * 1024; // 10 MiB
        return new Cache(application.getCacheDir(), cacheSize);
    }

    @Provides
    @Singleton
    Interceptor [] providesInterceptors(){
        return new Interceptor[]{
                BaseUrlChangingInterceptor.get(),
                new HttpLoggingInterceptor((message) -> Log.d("DAZN", message))
                        .setLevel(HttpLoggingInterceptor.Level.BODY)
        };
    }

    @Provides
    @Singleton
    OkHttpClient providesOkHttpClient(Cache cacheFile, Interceptor... interceptors){

        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .cache(cacheFile)
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS);

        J8Arrays.stream(interceptors).forEach(builder::addInterceptor);
        return builder.build();
    }

    @Provides
    @Singleton
    Gson providesGson(){
        return new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssz")
                .create();
    }

    @Provides
    @DaznApiScope
    Retrofit providesRetrofit(Gson gson, OkHttpClient okHttpClient){
        return new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl("https://us-central1-dazn-sandbox.cloudfunctions.net")
                .client(okHttpClient)
                .build();
    }

    @Provides
    @DaznApiScope
    DaznApi providesDaznApi(@DaznApiScope Retrofit retrofit){
        return  retrofit.create(DaznApi.class);
    }
}
