package pl.dexbytes.daznapp;

import android.app.Application;

import pl.dexbytes.daznapp.dagger.component.DaggerNetworkComponent;
import pl.dexbytes.daznapp.dagger.component.NetworkComponent;
import pl.dexbytes.daznapp.dagger.module.ApplicationModule;
import pl.dexbytes.daznapp.dagger.module.NetworkModule;

public class DaznApplication extends Application {
    private NetworkComponent mNetworkComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        initDaggerComponents();
    }

    private void initDaggerComponents() {
        setNetworkComponent(DaggerNetworkComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .networkModule(new NetworkModule())
                .build());
    }


    public NetworkComponent getNetworkComponent() {
        return mNetworkComponent;
    }

    private void setNetworkComponent(NetworkComponent networkComponent) {
        mNetworkComponent = networkComponent;
    }
}
