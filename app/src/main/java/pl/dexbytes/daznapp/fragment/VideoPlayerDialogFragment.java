package pl.dexbytes.daznapp.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.devbrackets.android.exomedia.listener.OnPreparedListener;
import com.devbrackets.android.exomedia.ui.widget.VideoView;

import pl.dexbytes.daznapp.R;
import pl.dexbytes.daznapp.utils.StringUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class VideoPlayerDialogFragment extends DialogFragment implements OnPreparedListener {
    private static final String ARG_VIDEO_URL = "arg_video_url";
    String mVideoUrl;
    VideoView mVideoView;

    public VideoPlayerDialogFragment() {
        // Required empty public constructor
    }

    public static VideoPlayerDialogFragment newInstance(String url) {
        Bundle args = new Bundle();
        args.putString(ARG_VIDEO_URL, url);
        VideoPlayerDialogFragment fragment = new VideoPlayerDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null && StringUtils.isNotEmpty(getArguments().getString(ARG_VIDEO_URL))){
            mVideoUrl = getArguments().getString(ARG_VIDEO_URL);
        } else {
            dismiss();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_video_player_dialog, container, false);
        mVideoView = view.findViewById(R.id.video_view);
        mVideoView.setOnPreparedListener(this);
        mVideoView.setVideoURI(Uri.parse(mVideoUrl));
        return view;
    }

    @Override
    public void onPrepared() {
        mVideoView.start();
    }
}
