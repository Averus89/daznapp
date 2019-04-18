package pl.dexbytes.daznapp.fragment;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import pl.dexbytes.daznapp.DaznApplication;

public class EventViewerFragment extends ViewerFragment {

    @Override
    protected void inject() {
        ((DaznApplication)getActivity().getApplication()).getNetworkComponent().inject(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        onRefresh();
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        mDaznApi.getEvents()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMapIterable(x -> x)
                .sorted((a,b) -> Long.compare(a.getDate().getTime(), b.getDate().getTime()))
                .subscribe(this);
    }
}
