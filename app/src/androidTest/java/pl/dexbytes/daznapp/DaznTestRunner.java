package pl.dexbytes.daznapp;

import com.squareup.rx2.idler.Rx2Idler;

import androidx.test.runner.AndroidJUnitRunner;
import io.reactivex.plugins.RxJavaPlugins;

public final class DaznTestRunner extends AndroidJUnitRunner {
    @Override
    public void onStart() {
        RxJavaPlugins.setInitComputationSchedulerHandler(Rx2Idler.create("RxJava 2.x Computation Scheduler"));
        RxJavaPlugins.setInitIoSchedulerHandler(Rx2Idler.create("RxJava 2.x IO Scheduler"));
        RxJavaPlugins.setInitNewThreadSchedulerHandler(Rx2Idler.create("RxJava 2.x New Thread Scheduler"));
        RxJavaPlugins.setInitSingleSchedulerHandler(Rx2Idler.create("RxJava 2.x Single Scheduler"));
        super.onStart();
    }
}
