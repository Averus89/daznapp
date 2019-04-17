package pl.dexbytes.daznapp.fragment;

import android.os.Handler;

import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import pl.dexbytes.daznapp.DaznApplication;

public class ScheduleViewerFragment extends ViewerFragment {
    private Handler mTimer = new Handler();
    private Runnable mTimerTask = () -> mDaznApi.getSchedule()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .flatMapIterable(x -> x)
            .sorted((a,b) -> Long.compare(a.getDate().getTime(), b.getDate().getTime()))
            .subscribe(ScheduleViewerFragment.this);

    @Override
    protected void inject() {
        ((DaznApplication)getActivity().getApplication()).getNetworkComponent().inject(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        mTimer.postDelayed(mTimerTask, 0);
    }

    @Override
    public void onPause() {
        super.onPause();
        mTimer.removeCallbacks(mTimerTask);
    }

    @Override
    public void onComplete() {
        super.onComplete();
        mTimer.postDelayed(mTimerTask, TimeUnit.SECONDS.toMillis(30));
    }
}
