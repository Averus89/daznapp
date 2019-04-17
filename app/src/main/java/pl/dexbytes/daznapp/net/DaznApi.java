package pl.dexbytes.daznapp.net;

import java.util.List;

import io.reactivex.Observable;
import pl.dexbytes.daznapp.model.Event;
import retrofit2.http.GET;

public interface DaznApi {
    @GET("/getSchedule")
    Observable<List<Event>> getSchedule();

    @GET("/getEvents")
    Observable<List<Event>> getEvents();
}
