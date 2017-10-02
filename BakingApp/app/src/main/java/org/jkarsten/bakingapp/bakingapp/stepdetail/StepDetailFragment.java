package org.jkarsten.bakingapp.bakingapp.stepdetail;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import org.jkarsten.bakingapp.bakingapp.OnDualPaneInteractionListener;
import org.jkarsten.bakingapp.bakingapp.R;
import org.jkarsten.bakingapp.bakingapp.data.Step;
import org.jkarsten.bakingapp.bakingapp.foodlist.FoodListActivity;
import org.jkarsten.bakingapp.bakingapp.recipedetail.RecipeDetailFragment;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.PublishSubject;

public class StepDetailFragment extends Fragment implements StepDetailContract.View {
    private static final String PLAYER_POSITION = "PLAYER_POSITION";
    private OnDualPaneInteractionListener mListener;
    TextView mStepDescTextView;
    View mRootView;
    TextView mVideoUnavailableTextView;

    //private Step[] mSteps;
    //Step mStep;


    //private int mStepPosition;
    private SimpleExoPlayerView mPlayerView;
    private SimpleExoPlayer mExoPlayer;
    private long mPlayerPosition;
    private Button mPreviousButton;
    private Button mNextButton;

    @Inject
    StepDetailContract.Presenter mPresenter;

    public StepDetailFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_step_detail, container, false);
        if (savedInstanceState != null)
            mPlayerPosition = savedInstanceState.getLong(PLAYER_POSITION, C.TIME_UNSET);
        return mRootView;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnDualPaneInteractionListener) {
            mListener = (OnDualPaneInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnDualPaneInteractionListener");
        }

        DaggerStepDetailComponent.builder()
                .stepDetailModule(new StepDetailModule(this))
                .build()
                .inject(this);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onStart() {
        super.onStart();
        extractIntentData();
        mStepDescTextView = (TextView) mRootView.findViewById(R.id.description_textview);
        mPreviousButton = (Button) mRootView.findViewById(R.id.previous_button);
        mNextButton = (Button) mRootView.findViewById(R.id.next_button);
        mVideoUnavailableTextView = (TextView) mRootView.findViewById(R.id.video_unavailable_textView);

        mPresenter.start();
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.onNextButtonPressed();
            }
        });
        mPreviousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.onPreviousButtonPressed();
            }
        });
    }

    @Override
    public void hideVideoUnavailable() {
        mVideoUnavailableTextView.setVisibility(View.GONE);
    }

    @Override
    public void showVideoUnavailable() {
        mVideoUnavailableTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hidePreviousAndNextButton() {
        mNextButton.setVisibility(View.GONE);
        mPreviousButton.setVisibility(View.GONE);
    }

    private void extractIntentData() {
        if (mListener == null || mListener.isDualPane())
            return;

        int stepPosition = getActivity().getIntent().getIntExtra(RecipeDetailFragment.STEP_ARGS, 0);
        Parcelable[] parcelables = getActivity().getIntent().getParcelableArrayExtra(FoodListActivity.FOOD_ARGS);
        Step[] steps = new Step[parcelables.length];
        int ii=0;
        for (Parcelable parcelable: parcelables) {
            steps[ii++] = (Step) parcelable;
        }
        mPresenter.onStepsReady(steps, stepPosition);
    }

    private void releasePlayer() {
        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    private void initMediaSource(String videoURL) {
        if (mExoPlayer != null && videoURL != null && videoURL.length() > 0) {
            Uri mediaUri = Uri.parse(videoURL);
            String userAgent = Util.getUserAgent(getContext(),
                    getContext().getString(R.string.app_name));
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri,
                    new DefaultDataSourceFactory(getContext(), userAgent),
                    new DefaultExtractorsFactory(), null, null);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(true);
            if (mPlayerPosition != C.TIME_UNSET) {
                mExoPlayer.seekTo(mPlayerPosition);
            }
        }
    }

    private void intializePlayer() {
        mPlayerView = (SimpleExoPlayerView) mRootView.findViewById(R.id.player_view);
        DefaultLoadControl loadControl = new DefaultLoadControl();
        TrackSelector trackSelector = new DefaultTrackSelector();
        mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
        mPlayerView.setPlayer(mExoPlayer);
    }

    @Override
    public void showStep(Step step) {
        if (step != null) {
            mStepDescTextView.setText(step.getDescription());

            if (mExoPlayer != null) {
                releasePlayer();
            }
            intializePlayer();
            if (step.getVideoURL() == null || step.getVideoURL().length() == 0) {
                initMediaSource(step.getThumbnailURL());
            } else {
                initMediaSource(step.getVideoURL());
            }
        }
    }

    @Override
    public Disposable createStepSubscription() {
        PublishSubject<Step> publishSubject = mListener.getPublisher();
        return publishSubject.subscribe(new Consumer<Step>() {
            @Override
            public void accept(Step step) throws Exception {
                mPresenter.onStepSelected(step);
            }
        });
    }

    @Override
    public boolean isDualPane() {
        if (mListener == null) {
            return false;
        }
        return mListener.isDualPane();
    }

    @Override
    public void onStop() {
        super.onStop();
        mPresenter.stop();
        releasePlayer();
    }

    @Override
    public void onPause() {
        super.onPause();
        mPlayerPosition = mExoPlayer.getCurrentPosition();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(PLAYER_POSITION, mPlayerPosition);
    }
}
