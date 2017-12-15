package michael_juarez.foodtrucksandmore.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import michael_juarez.foodtrucksandmore.R;
import michael_juarez.foodtrucksandmore.adapters.FoodSpotAdapter;
import michael_juarez.foodtrucksandmore.data.FoodSpot;
import michael_juarez.foodtrucksandmore.data.FoodSpotLatLng;
import michael_juarez.foodtrucksandmore.utilities.FoodSpotDb;

/**
 * Created by user on 12/7/2017.
 */

public class Fragment_FoodSpotList_GridView extends Fragment implements FoodSpotAdapter.ListItemClickListener {
    public final static String KEY_FOODSPOT_LIST = "Fragment_FoodSpotList_GridView.KEY_FOODSPOT_LIST";
    public final static String KEY_ORIGINPOINT = "Fragment_FoodSpotList_GridView.KEY_ORIGINPOINT";
    public final static String KEY_GRID_BUTTON = "Fragment_FoodSpotList_GridView.KEY_GRID_BUTTON";

    public Unbinder unbinder;

    @BindView(R.id.fragment_foodspotlist_foodspot_rv_gridView)
    RecyclerView mFoodSpotRecyclerView;

    private FoodSpotAdapter mFoodSpotAdapter;
    private GridLayoutManager mGridLayoutManager;
    private ArrayList<FoodSpot> mFoodSpotList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_foodspotlist_gridview, container, false);
        unbinder = ButterKnife.bind(this, view);

        mFoodSpotList = (ArrayList<FoodSpot>) getArguments().getSerializable(KEY_FOODSPOT_LIST);

        setupRecyclerView();

        return view;
    }

    private void setupRecyclerView() {
        mFoodSpotAdapter = new FoodSpotAdapter(mFoodSpotList, getActivity(), this, false);
        mGridLayoutManager = new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false);

        mFoodSpotRecyclerView.setAdapter(mFoodSpotAdapter);
        mFoodSpotRecyclerView.setLayoutManager(mGridLayoutManager);
    }

    @Override
    public void onListItemClick(int itemClicked, String link, ImageView imageView) {

    }


    @OnClick(R.id.fragment_foodspotlist_grid_button_gridView)
    public void gridViewOnClick() {
        getActivity().onBackPressed();
    }

}
