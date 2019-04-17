package pl.dexbytes.daznapp.dagger.component;

import javax.inject.Singleton;

import dagger.Component;
import pl.dexbytes.daznapp.activity.MainActivity;
import pl.dexbytes.daznapp.dagger.module.ApplicationModule;
import pl.dexbytes.daznapp.dagger.module.NetworkModule;
import pl.dexbytes.daznapp.dagger.scope.DaznApiScope;
import pl.dexbytes.daznapp.fragment.EventViewerFragment;
import pl.dexbytes.daznapp.fragment.ScheduleViewerFragment;
import pl.dexbytes.daznapp.net.DaznApi;

@Singleton
@Component(modules = {ApplicationModule.class, NetworkModule.class})
public interface NetworkComponent {
    @DaznApiScope
    DaznApi getDaznApi();

    void inject(MainActivity activity);

    void inject(EventViewerFragment fragment);

    void inject(ScheduleViewerFragment fragment);
}
