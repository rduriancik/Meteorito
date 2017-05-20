package com.example.robertduriancik.meteorito.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.robertduriancik.meteorito.R;
import com.example.robertduriancik.meteorito.adapters.MeteoriteListAdapter;
import com.example.robertduriancik.meteorito.api.NasaDataApi;
import com.example.robertduriancik.meteorito.api.NasaDataService;
import com.example.robertduriancik.meteorito.models.MeteoriteLanding;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


///**
// * A simple {@link Fragment} subclass.
// * Activities that contain this fragment must implement the
// * {@link MeteoriteListFragment.OnFragmentInteractionListener} interface
// * to handle interaction events.
// * Use the {@link MeteoriteListFragment#newInstance} factory method to
// * create an instance of this fragment.
// */
public class MeteoriteListFragment extends Fragment implements MeteoriteListAdapter.onMeteoriteListAdapterInteraction {
    //    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//
//    private String mParam1;
//    private String mParam2;
//
//    private OnFragmentInteractionListener mListener;
//
//    public MeteoriteListFragment() {
//        // Required empty public constructor
//    }
//
//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment MeteoriteListFragment.
//     */
//    public static MeteoriteListFragment newInstance(String param1, String param2) {
//        MeteoriteListFragment fragment = new MeteoriteListFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
//    }
    private static final String TAG = "MeteoriteListFragment";

    private List<MeteoriteLanding> mMeteoriteLandingList;
    private MeteoriteListAdapter mMeteoriteListAdapter;
    private NasaDataService mNasaDataService;

    @BindView(R.id.meteorite_list)
    RecyclerView mRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meteorite_list, container, false);
        ButterKnife.bind(this, view);

        mMeteoriteLandingList = new ArrayList<>();
        mNasaDataService = new NasaDataApi().getService();
        prepareRecyclerView();

        return view;
    }

    private void prepareRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setHasFixedSize(true);
        mMeteoriteListAdapter = new MeteoriteListAdapter(mRecyclerView, mMeteoriteLandingList, MeteoriteListFragment.this);
        mRecyclerView.setAdapter(mMeteoriteListAdapter);
        loadLandings();
    }

    private void loadLandings() {
        Call<List<MeteoriteLanding>> call = mNasaDataService.getMeteoriteLandings(50, 0);
        call.enqueue(new Callback<List<MeteoriteLanding>>() {
            @Override
            public void onResponse(Call<List<MeteoriteLanding>> call, Response<List<MeteoriteLanding>> response) {
                List<MeteoriteLanding> list = response.body();
                if (list != null) {
                    mMeteoriteLandingList.addAll(list);
                    mMeteoriteListAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<MeteoriteLanding>> call, Throwable t) {
                Log.e(TAG, "onFailure: An error was thrown during data fetching", t);
            }
        });
    }

    @Override
    public void onItemClick(MeteoriteLanding meteoriteLanding) {
        Toast.makeText(getActivity(), meteoriteLanding.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLoadMore() {
        if (mMeteoriteLandingList != null) {
            mMeteoriteLandingList.add(null);
            mRecyclerView.post(new Runnable() {
                @Override
                public void run() {
                    mMeteoriteListAdapter.notifyItemInserted(mMeteoriteLandingList.size() - 1);
                }
            });

            Call<List<MeteoriteLanding>> call = mNasaDataService.getMeteoriteLandings(50, mMeteoriteLandingList.size() - 1);
            call.enqueue(new Callback<List<MeteoriteLanding>>() {
                @Override
                public void onResponse(Call<List<MeteoriteLanding>> call, Response<List<MeteoriteLanding>> response) {
                    mMeteoriteLandingList.remove(mMeteoriteLandingList.size() - 1);
                    mMeteoriteListAdapter.notifyItemRemoved(mMeteoriteLandingList.size());

                    List<MeteoriteLanding> list = response.body();
                    if (list != null) {
                        mMeteoriteLandingList.addAll(list);
                        mMeteoriteListAdapter.notifyDataSetChanged();
                    }
                    mMeteoriteListAdapter.setLoaded();

                }

                @Override
                public void onFailure(Call<List<MeteoriteLanding>> call, Throwable t) {
                    Log.e(TAG, "onFailure: An error was thrown during data fetching", t);
                }
            });

        }
    }

    //
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }
//
//    /**
//     * This interface must be implemented by activities that contain this
//     * fragment to allow an interaction in this fragment to be communicated
//     * to the activity and potentially other fragments contained in that
//     * activity.
//     * <p>
//     * See the Android Training lesson <a href=
//     * "http://developer.android.com/training/basics/fragments/communicating.html"
//     * >Communicating with Other Fragments</a> for more information.
//     */
//    public interface OnFragmentInteractionListener {
//        void onFragmentInteraction(Uri uri);
//    }
}
