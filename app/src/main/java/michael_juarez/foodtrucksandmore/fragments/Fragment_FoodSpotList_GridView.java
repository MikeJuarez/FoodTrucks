package michael_juarez.foodtrucksandmore.fragments;

import android.os.Bundle;
import android.support.constraint.Guideline;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
    private Guideline mLandscapeGuideline;

    private boolean mGridViewActive = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_foodspotlist_gridview, container, false);
        unbinder = ButterKnife.bind(this, view);

        mLandscapeGuideline = view.findViewById(R.id.gridview_landscape_guideline);

        mFoodSpotList = (ArrayList<FoodSpot>) getArguments().getSerializable(KEY_FOODSPOT_LIST);

        setupRecyclerView();

        return view;
    }

    private void setupRecyclerView() {
        if (mLandscapeGuideline == null) {
            mFoodSpotAdapter = new FoodSpotAdapter(mFoodSpotList, getActivity(), this, false, false, mGridViewActive);
            if (mGridViewActive)
                mGridLayoutManager = new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false);
            else
                mGridLayoutManager = new GridLayoutManager(getActivity(), 1, GridLayoutManager.VERTICAL, false);
        }
        else {
            mFoodSpotAdapter = new FoodSpotAdapter(mFoodSpotList, getActivity(), this, false, true, true);
            mGridLayoutManager = new GridLayoutManager(getActivity(), 2, GridLayoutManager.HORIZONTAL, false);
        }

        mFoodSpotRecyclerView.setAdapter(mFoodSpotAdapter);
        mFoodSpotRecyclerView.setLayoutManager(mGridLayoutManager);
    }


    @Override
    public void onListItemClick(int itemClicked, String link, ImageView imageView) {

    }


    @Override
    public void onCreateOptionsMenu(final Menu menu, final MenuInflater menuInflater) {
        //Don't show ListView if in LandScape Orientation
        if (mLandscapeGuideline == null)
            menuInflater.inflate(R.menu.foodspot_gridview, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.fragment_gridview_menu_gridViewButton:
                mGridViewActive = !mGridViewActive;
                if (!mGridViewActive) {
                    item.setIcon(R.drawable.ic_grid);
                    setupRecyclerView();
                } else {
                    item.setIcon(R.drawable.ic_listview_blue);
                    setupRecyclerView();
                }
            break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
