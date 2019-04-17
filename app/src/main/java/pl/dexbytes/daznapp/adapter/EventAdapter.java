package pl.dexbytes.daznapp.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.ListPreloader;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.util.ViewPreloadSizeProvider;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import pl.dexbytes.daznapp.R;
import pl.dexbytes.daznapp.glide.GlideRequest;
import pl.dexbytes.daznapp.model.Event;
import pl.dexbytes.daznapp.utils.StringUtils;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder>
        implements ListPreloader.PreloadModelProvider<Event> {
    private Context mContext;
    private final LayoutInflater mLayoutInflater;
    private final List<Event> mEvents;
    private GlideRequest<Drawable> mFullRequest;
    private GlideRequest<Drawable> mThumbRequest;
    private ViewPreloadSizeProvider<Event> mPreloadSizeProvider;
    private OnItemClickListener mOnItemClickListener;

    public EventAdapter(Context context, GlideRequest<Drawable> fullRequest, GlideRequest<Drawable> thumbRequest){
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        mEvents = new ArrayList<>();
        mFullRequest = fullRequest;
        mThumbRequest = thumbRequest;
    }

    public void clear(){
        mEvents.clear();
    }

    public void add(Event event){
        mEvents.add(event);
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.card_view_item, parent, false);
        EventViewHolder vh = new EventViewHolder(view);
        mPreloadSizeProvider.setView(vh.mImage);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        final Event event = mEvents.get(position);
        String photoUrl = event.getImageUrl();
        mFullRequest.load(photoUrl)
                .thumbnail(mThumbRequest.load(photoUrl))
                .into(holder.mImage);
        holder.mTitle.setText(event.getTitle());
        holder.mSubtitle.setText(event.getSubtitle());
        holder.mDate.setText(formatDate(mContext, event.getDate()));
        if(StringUtils.isNotEmpty(event.getVideoUrl()) && getOnItemClickListener() != null) {
            holder.mCardView.setOnClickListener(v -> getOnItemClickListener().onClick(event));
        }
    }

    @Override
    public int getItemCount() {
        return mEvents != null ? mEvents.size() : 0;
    }

    @Override
    public long getItemId(int i) {
        return mEvents.get(i).getId();
    }

    @NonNull
    @Override
    public List<Event> getPreloadItems(int position) {
        return mEvents.subList(position, position + 1);
    }

    @Nullable
    @Override
    public RequestBuilder<?> getPreloadRequestBuilder(@NonNull Event item) {
        return mFullRequest.thumbnail(mThumbRequest.load(item.getImageUrl())).load(item.getImageUrl());
    }

    public void setPreloadSizeProvider(ViewPreloadSizeProvider<Event> preloadSizeProvider) {
        mPreloadSizeProvider = preloadSizeProvider;
    }

    public OnItemClickListener getOnItemClickListener() {
        return mOnItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    private static CharSequence formatDate(Context context, Date date){
        return DateUtils.getRelativeDateTimeString(
                context,
                date.getTime(),
                DateUtils.DAY_IN_MILLIS,
                DateUtils.DAY_IN_MILLIS * 3,
                DateUtils.FORMAT_SHOW_TIME);
    }

    public List<Event> getItems() {
        return mEvents;
    }

    public void addAll(List<Event> eventList) {
        mEvents.addAll(eventList);
    }

    public class EventViewHolder extends RecyclerView.ViewHolder{
        AppCompatImageView mImage;
        AppCompatTextView mTitle, mSubtitle, mDate;
        CardView mCardView;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            mImage = itemView.findViewById(R.id.event_image);
            mTitle = itemView.findViewById(R.id.event_title);
            mSubtitle = itemView.findViewById(R.id.event_subtitle);
            mDate = itemView.findViewById(R.id.event_date);
            mCardView = itemView.findViewById(R.id.card_view);
        }

        public AppCompatImageView getImage() {
            return mImage;
        }
    }
    
    public interface OnItemClickListener {
        void onClick(Event event);
    }
}
