package com.example.robertduriancik.meteorito.fragments;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.robertduriancik.meteorito.R;
import com.example.robertduriancik.meteorito.adapters.MeteoriteListAdapter;
import com.example.robertduriancik.meteorito.api.NasaDataApi;
import com.example.robertduriancik.meteorito.api.NasaDataService;
import com.example.robertduriancik.meteorito.models.MeteoriteLanding;
import com.example.robertduriancik.meteorito.models.MeteoriteLandingsCount;
import com.example.robertduriancik.meteorito.utils.EndlessRecyclerOnScrollListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MeteoriteListFragment.OnMeteoriteListFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class MeteoriteListFragment extends Fragment implements MeteoriteListAdapter.OnMeteoriteListAdapterInteractionListener {
    private static final String TAG = "MeteoriteListFragment";

    private static final String LIST_STATE_KEY = "list_state_key";
    private static final String LIST_CONTENT_KEY = "list_content_key";
    private static final String LANDINGS_COUNT_KEY = "landings_count_key";

    private OnMeteoriteListFragmentInteractionListener mListener;
    private NasaDataService mNasaDataService;
    private ArrayList<MeteoriteLanding> mMeteoriteLandings;
    private MeteoriteListAdapter mListAdapter;
    private int mLandingsCountValue;

    @BindView(R.id.swipe_container)
    SwipeRefreshLayout mSwipeContainer;
    @BindView(R.id.meteorite_list)
    RecyclerView mRecyclerView;
    @BindView(R.id.empty_list)
    TextView mEmptyState;
    @BindView(R.id.item_progress_bar)
    ProgressBar mProgressBar;
    @BindView(R.id.meteorite_landings_count)
    TextView mLandingsCount;

    public MeteoriteListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMeteoriteLandings = new ArrayList<>();
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_list_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_legend) {
            showLegend();
            return true;
        } else
            return super.onOptionsItemSelected(item);
    }

    private void showLegend() {
        Context context = getContext();
        View legendView = View.inflate(context, R.layout.legend_dialog, null);

        AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setView(legendView)
                .setTitle("Legend")
                .setPositiveButton(R.string.dialog_close, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Dialog closed
                    }
                })
                .setCancelable(true)
                .show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meteorite_list, container, false);
        ButterKnife.bind(this, view);

        mNasaDataService = new NasaDataApi(getContext()).getService();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());

        if (savedInstanceState != null) {
            layoutManager.onRestoreInstanceState(savedInstanceState.getParcelable(LIST_STATE_KEY));
            mLandingsCountValue = savedInstanceState.getInt(LANDINGS_COUNT_KEY);
            List<MeteoriteLanding> list = savedInstanceState.getParcelableArrayList(LIST_CONTENT_KEY);
            if (list != null) {
                mMeteoriteLandings.addAll(list);
            }
        }

        prepareRecyclerView(layoutManager);
        mLandingsCount.setText(String.valueOf(mLandingsCountValue));

        if (mMeteoriteLandings.isEmpty()) {
            showEmptyState();

            loadLandings(false);
            loadLandingsCount();
        }

        prepareSwipeContainer();

        return view;
    }

    private void prepareSwipeContainer() {
        mSwipeContainer.setColorSchemeColors(ContextCompat.getColor(getContext(), R.color.colorAccent));
        mSwipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadLandings(true);
                loadLandingsCount();
            }
        });
    }

    private void prepareRecyclerView(LinearLayoutManager layoutManager) {
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        mListAdapter = new MeteoriteListAdapter(mMeteoriteLandings);
        mListAdapter.addOnMeteoriteListAdapterInteractionListener(this);
        mListAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                MeteoriteLanding landing = mListAdapter.getItem(0);
                if (mListener != null && landing != null) {
                    mListener.onDataLoaded(landing);
                }
            }
        });
        mRecyclerView.setAdapter(mListAdapter);
        mRecyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int totalItemsCount) {
                loadMoreLandings(totalItemsCount);
            }
        });
    }

    private void showEmptyState() {
        mRecyclerView.setVisibility(View.GONE);
        mEmptyState.setVisibility(View.VISIBLE);
    }

    private void showLoadedState() {
        mEmptyState.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onItemClick(MeteoriteLanding meteoriteLanding) {
        if (mListener != null) {
            mListener.onMeteoriteListItemClick(meteoriteLanding);
        }
    }

    private void loadLandings(final boolean isRefreshing) {
        Call<List<MeteoriteLanding>> landingCall = mNasaDataService.getMeteoriteLandings(10, 0);

        landingCall.enqueue(new Callback<List<MeteoriteLanding>>() {
            @Override
            public void onResponse(@NonNull Call<List<MeteoriteLanding>> call, @NonNull Response<List<MeteoriteLanding>> response) {
                List<MeteoriteLanding> list = response.body();
                boolean loaded;

                if (isRefreshing) {
                    loaded = mListAdapter.refresh(list);
                    mSwipeContainer.setRefreshing(false);
                } else {
                    loaded = mListAdapter.add(list);
                }

                if (loaded) {
                    showLoadedState();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<MeteoriteLanding>> call, @NonNull Throwable t) {
                Log.e(TAG, "onFailure: loadLandings ", t);
                if (isRefreshing) {
                    mSwipeContainer.setRefreshing(false);
                }
            }
        });
    }

    private void loadMoreLandings(int offset) {
        mProgressBar.setVisibility(View.VISIBLE);

        final Call<List<MeteoriteLanding>> landingCall = mNasaDataService.getMeteoriteLandings(10, offset);
        mRecyclerView.post(new Runnable() {
            @Override
            public void run() {
                landingCall.enqueue(new Callback<List<MeteoriteLanding>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<MeteoriteLanding>> call, @NonNull Response<List<MeteoriteLanding>> response) {
                        mListAdapter.add(response.body());
                        mProgressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<MeteoriteLanding>> call, @NonNull Throwable t) {
                        Log.e(TAG, "onFailure: loadMoreLandings ", t);
                        mProgressBar.setVisibility(View.GONE);
                    }
                });
            }
        });

    }

    private void loadLandingsCount() {
        Call<List<MeteoriteLandingsCount>> countCall = mNasaDataService.getMeteoriteLandingsCount();

        countCall.enqueue(new Callback<List<MeteoriteLandingsCount>>() {
            @Override
            public void onResponse(@NonNull Call<List<MeteoriteLandingsCount>> call, @NonNull Response<List<MeteoriteLandingsCount>> response) {
                List<MeteoriteLandingsCount> list = response.body();
                if (list != null) {
                    mLandingsCountValue = list.get(0).getCount();

                    ValueAnimator animator = ValueAnimator.ofInt(0, mLandingsCountValue);
                    animator.setDuration(1500);
                    animator.start();
                    animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            mLandingsCount.setText(String.valueOf(animation.getAnimatedValue()));
                        }
                    });
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<MeteoriteLandingsCount>> call, @NonNull Throwable t) {
                Log.e(TAG, "onFailure: loadLandingsCount ", t);
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(LIST_STATE_KEY, mRecyclerView.getLayoutManager().onSaveInstanceState());
        outState.putParcelableArrayList(LIST_CONTENT_KEY, mMeteoriteLandings);
        outState.putInt(LANDINGS_COUNT_KEY, Integer.parseInt(mLandingsCount.getText().toString()));

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnMeteoriteListFragmentInteractionListener) {
            mListener = (OnMeteoriteListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnMeteoriteListFragmentInteractionListener {
        void onMeteoriteListItemClick(MeteoriteLanding meteoriteLanding);

        void onDataLoaded(MeteoriteLanding meteoriteLanding);
    }
}
