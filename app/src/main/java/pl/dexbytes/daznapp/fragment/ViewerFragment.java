package pl.dexbytes.daznapp.fragment;


import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.integration.recyclerview.RecyclerViewPreloader;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.util.ViewPreloadSizeProvider;

import java.util.List;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import pl.dexbytes.daznapp.R;
import pl.dexbytes.daznapp.adapter.EventAdapter;
import pl.dexbytes.daznapp.dagger.scope.DaznApiScope;
import pl.dexbytes.daznapp.glide.GlideApp;
import pl.dexbytes.daznapp.glide.GlideRequest;
import pl.dexbytes.daznapp.glide.GlideRequests;
import pl.dexbytes.daznapp.model.Event;
import pl.dexbytes.daznapp.net.DaznApi;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class ViewerFragment extends Fragment implements Observer<Event> {
    private static final int PRELOAD_AHEAD_ITEMS = 10;
    private static final String STATE_POSITION_INDEX = "state_position_index";
    private static final String STATE_POSITION_OFFSET = "state_position_offset";
    private LinearLayoutManager layoutManager;
    private RecyclerView mRecyclerView;
    private EventAdapter mEventAdapter;
    @Inject
    @DaznApiScope
    DaznApi mDaznApi;

    public ViewerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inject();
    }

    protected abstract void inject();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_viewver, container, false);
        initRecyclerView(view);
        return view;
    }

    private void initRecyclerView(View view) {
        mRecyclerView = view.findViewById(R.id.recycler_view);
        final GlideRequests glideRequests = GlideApp.with(this);
        GlideRequest<Drawable> fullRequest = glideRequests
                .asDrawable()
                .centerCrop()
                .placeholder(new ColorDrawable(Color.GRAY));
        GlideRequest<Drawable> thumbRequest = glideRequests
                .asDrawable()
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .transition(withCrossFade());

        layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mEventAdapter = new EventAdapter(getContext(), fullRequest, thumbRequest);
        mEventAdapter.setOnItemClickListener(e ->
                VideoPlayerDialogFragment.newInstance(e.getVideoUrl())
                        .show(getChildFragmentManager(), "VideoPlayer"));
        ViewPreloadSizeProvider<Event> preloadSizeProvider = new ViewPreloadSizeProvider<>();
        RecyclerViewPreloader<Event> preloader =
                new RecyclerViewPreloader<>(
                        GlideApp.with(this),
                        mEventAdapter,
                        preloadSizeProvider,
                        PRELOAD_AHEAD_ITEMS);
        mEventAdapter.setPreloadSizeProvider(preloadSizeProvider);
        mRecyclerView.setAdapter(mEventAdapter);
        mRecyclerView.addOnScrollListener(preloader);
        mRecyclerView.setItemViewCacheSize(0);
        mRecyclerView.setRecyclerListener(holder -> {
            EventAdapter.EventViewHolder vh = (EventAdapter.EventViewHolder) holder;
            glideRequests.clear(vh.getImage());
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            int index = savedInstanceState.getInt(STATE_POSITION_INDEX);
            int offset = savedInstanceState.getInt(STATE_POSITION_OFFSET);
            layoutManager.scrollToPositionWithOffset(index, offset);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        int index = layoutManager.findFirstVisibleItemPosition();
        View topView = mRecyclerView.getChildAt(0);
        int offset = topView != null ? topView.getTop() : 0;
        outState.putInt(STATE_POSITION_INDEX, index);
        outState.putInt(STATE_POSITION_OFFSET, offset);
    }

    private int mIndexPosition = 0;
    private List<Event> mEventList;

    @Override
    public void onSubscribe(Disposable d) {
        mEventList = mEventAdapter.getItems();
        mEventAdapter.clear();
        mIndexPosition = layoutManager.findFirstVisibleItemPosition();
    }

    @Override
    public void onNext(Event event) {
        mEventAdapter.add(event);
    }

    @Override
    public void onError(Throwable e) {
        mEventAdapter.addAll(mEventList);
        Toast.makeText(getContext(), R.string.error_toast, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onComplete() {
        mEventAdapter.notifyDataSetChanged();
        layoutManager.scrollToPosition(mIndexPosition);
    }

}
