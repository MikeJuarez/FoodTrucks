package michael_juarez.foodtrucksandmore.fragments;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.location.Location;
import android.os.Bundle;
import android.support.constraint.Guideline;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import michael_juarez.foodtrucksandmore.R;
import michael_juarez.foodtrucksandmore.activities.Activity_Address;
import michael_juarez.foodtrucksandmore.activities.Activity_FoodSpotDetails;
import michael_juarez.foodtrucksandmore.activities.Activity_FoodSpotListGridView;
import michael_juarez.foodtrucksandmore.adapters.FoodSpotAdapter;
import michael_juarez.foodtrucksandmore.adapters.FoodStylesAdapter;
import michael_juarez.foodtrucksandmore.data.FoodSpot;
import michael_juarez.foodtrucksandmore.data.FoodSpotLatLng;
import michael_juarez.foodtrucksandmore.data.FoodStyle;
import michael_juarez.foodtrucksandmore.utilities.CurrentLocationUtility;
import michael_juarez.foodtrucksandmore.utilities.FavoritesDbUtility;
import michael_juarez.foodtrucksandmore.utilities.FoodSpotDb;
import michael_juarez.foodtrucksandmore.utilities.FoodStyleDb;
import michael_juarez.foodtrucksandmore.utilities.GenerateMarkersUtility;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;
import static michael_juarez.foodtrucksandmore.R.id.map;


/**
 * Created by user on 11/8/2017.
 */

public class Fragment_FoodSpotList extends Fragment implements FoodSpotDb.FoodSpotDbInterface, FoodSpotAdapter.ListItemClickListener, OnMapReadyCallback, CurrentLocationUtility.UpdateCurrentLocationInterface {
    public static final String KEY_COORDINATES = "Fragment_FoodSpotList.KEY_COORDINATES";

    private static final String KEY_STATE_FAVORITE_CLICKED = "Fragment_FoodSpotList.KEY_STATE_FAVORITE_CLICKED";
    private static final String KEY_STATE_FOODSPOT_LIST = "Fragment_FoodSpotList.KEY_STATE_FOODSPOT_LIST";

    private Unbinder unbinder;

    private FoodSpotAdapter mFoodSpotAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private FoodStylesAdapter mFoodStylesAdapter;
    private GridLayoutManager mFoodStyleGridLayoutManager;
    private GridLayoutManager mFoodSpotGridLayoutManager;

    private ArrayList<FoodSpot> mFoodSpotList;
    private ArrayList<FoodStyle> mFoodStyleList;

    private FoodSpotDb mFoodSpotData;
    private FoodStyleDb mFoodStyleData;
    private FavoritesDbUtility mFavoritesDbUtility;

    private String[] addressArr;
    private FoodSpotLatLng mOriginPoint;
    private GenerateMarkersUtility gmu;

    @BindView(R.id.fragment_foodspotlist_foodspot_rv)
    RecyclerView mRecyclerView;

    @BindView(R.id.fragment_foodspotlist_styles_rv)
    RecyclerView mStylesRecylerView;

    @BindView(R.id.fragment_foodspotlist_grid_button)
    ImageView mGridViewButton;

    @BindView(R.id.fragment_foodspotlist_favorite)
    ImageView mFavoriteButton;

    @BindView(R.id.details_view)
    CardView mCardDetailsView;

    private Guideline mGuidelineVertical50;

    private boolean filterButtonClicked;
    private ImageView truckFilter;
    private AnimatedVectorDrawable truckToFilter;
    private AnimatedVectorDrawable filterToTruck;
    private boolean truck = true;
    private boolean mFavoritesClicked;
    private int[] mCardMeasuredWidth;

    private SupportMapFragment mapFragment;

    private GoogleMap mMap;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_foodspotlist, container, false);
        unbinder = ButterKnife.bind(this, view);

        //Used to check for landscape orientation
        mGuidelineVertical50 = view.findViewById(R.id.guidelineVertical50);

        addressArr = getArguments().getStringArray(KEY_COORDINATES);
        if (addressArr != null)
            mOriginPoint = new FoodSpotLatLng(Double.parseDouble(addressArr[0]), Double.parseDouble(addressArr[1]));
        else
            Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.error_no_origin_point), Toast.LENGTH_LONG).show();

        // Check if Favorites Button was clicked to ensure Favorite list is loaded
        // This usually returns != null when orientation changes
        if (savedInstanceState != null) {
            mFavoritesClicked = savedInstanceState.getBoolean(KEY_STATE_FAVORITE_CLICKED);
            mFoodSpotList = (ArrayList<FoodSpot>) savedInstanceState.getSerializable(KEY_STATE_FOODSPOT_LIST);
        }

        //Prepare FoodSpot List Utility
        mFoodSpotData = FoodSpotDb.getInstance(mOriginPoint, this);

        //FoodStyle Filter
        mFoodStyleData = FoodStyleDb.getInstance();
        mFoodStyleList = mFoodStyleData.getFoodStyleList();

        setupFoodStyleRecyclerView();
        setupFilterButtonViews(view);

        //Device rotation check
        //If Favorites menu is open, then load favorite list
        //Else Try to load full list without reading from the network database
        if (mFavoritesClicked)
            setFavoritesView(true, true);

        //If this is first time, then do network call to get full list from network database
        //Else just set up the recyclerview with either favorite list or existing list
        if (mFoodSpotList == null && !mFavoritesClicked)
            mFoodSpotData.readFromDB(getActivity());
        else
            setupFoodSpotRecyclerView(true);

        return view;

        //Set GeoFire metadate in database
        //GeoFireSetLocations.setLocations(mOriginPoint, getActivity());
    }

    private void setupFoodSpotRecyclerView(boolean animateList) {
        //Ready to initialize the Map and it's markers.
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(map);
        mapFragment.getMapAsync(this);

        if (mGuidelineVertical50 == null) {
            mFoodSpotAdapter = new FoodSpotAdapter(mFoodSpotList, getActivity(), this, animateList,true, true);
            mFoodSpotGridLayoutManager = new GridLayoutManager(getActivity(), 1, GridLayoutManager.HORIZONTAL, false);
        }
        else {
            mFoodSpotAdapter = new FoodSpotAdapter(mFoodSpotList, getActivity(), this, animateList,false, true);
            mFoodSpotGridLayoutManager = new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false);
        }

        mRecyclerView.setAdapter(mFoodSpotAdapter);
        mRecyclerView.setLayoutManager(mFoodSpotGridLayoutManager);
    }

    private void setupFoodStyleRecyclerView() {
        if (mFoodStyleList == null) {
            //Generate the FoodStyle List
            FoodStyleDb foodStyleDb = FoodStyleDb.getInstance();
            foodStyleDb.generateList(getActivity());
            mFoodStyleList = foodStyleDb.getFoodStyleList();
        }

        mFoodStylesAdapter = new FoodStylesAdapter(mFoodStyleList, getActivity(), true);

        if (mGuidelineVertical50 == null)
            mFoodStyleGridLayoutManager = new GridLayoutManager(getActivity(), 2, GridLayoutManager.HORIZONTAL, true);
        else
            mFoodStyleGridLayoutManager = new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false);

        mStylesRecylerView.setAdapter(mFoodStylesAdapter);
        mStylesRecylerView.setLayoutManager(mFoodStyleGridLayoutManager);
    }

    public void setupFilterButtonViews(View view) {
        truckFilter = (ImageView) view.findViewById(R.id.fragment_foodspotlist_filter_button);
        truckToFilter = (AnimatedVectorDrawable) ContextCompat.getDrawable(getActivity(), R.drawable.avd_truck_to_filter);
        filterToTruck = (AnimatedVectorDrawable) ContextCompat.getDrawable(getActivity(), R.drawable.avd_filter_to_truck);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    //Interface callback method from FoodSpotDb
    @Override
    public void updateAdapters(ArrayList<FoodSpot> filteredFoodSpotList, boolean animateList) {
        if (mFavoritesClicked && (filteredFoodSpotList == null || filteredFoodSpotList.isEmpty())) {
            Log.d(TAG, getActivity().getResources().getString(R.string.error_no_favorite_foodspots));
            Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.error_no_favorite_foodspots), Toast.LENGTH_LONG).show();
            return;
        }

        //Threads are finished running, list is safe to pull
        if (filteredFoodSpotList == null || filteredFoodSpotList.isEmpty()) {
            Log.d(TAG, getActivity().getResources().getString(R.string.error_retrieving_list));
            Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.error_retrieving_list), Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getActivity(), Activity_Address.class);
            startActivity(intent);
        }

        mFoodSpotList = filteredFoodSpotList;

        setupFoodSpotRecyclerView(animateList);
    }

    @Override
    public void onListItemClick(int itemClicked, String link, ImageView imageView) {
        Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(
                getActivity(),
                imageView,
                imageView.getTransitionName())
                .toBundle();

        Intent intent = new Intent(getActivity(), Activity_FoodSpotDetails.class);

        intent.putExtra(Activity_FoodSpotDetails.KEY_ACTIVITY_FOODSPOTDETAILS_FOODSPOT, mFoodSpotList.get(itemClicked));
        startActivity(intent, bundle);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        mMap = googleMap;


        int width = mCardDetailsView.getMeasuredWidth();
        int height = mCardDetailsView.getMeasuredHeight();
        int padding = (int) (height * 0.10);

        mCardMeasuredWidth = new int[3];
        mCardMeasuredWidth[0] = width;
        mCardMeasuredWidth[1] = height;
        mCardMeasuredWidth[2] = padding;

        if (mFoodSpotList != null)
            generateMarkers();
    }

    private void generateMarkers() {
        //gmu = new GenerateMarkersUtility(mMap, mOriginPoint, mCardDetailsView, getActivity(), mFoodSpotList);
        gmu = new GenerateMarkersUtility(mMap, mOriginPoint, mCardMeasuredWidth, getActivity(), mFoodSpotList);
        gmu.getMarkerList();
    }

    public void animate(View view) {
        AnimatedVectorDrawable drawable = truck ? filterToTruck : truckToFilter;
        truckFilter.setImageDrawable(drawable);
        drawable.start();
        truck = !truck;
    }

    private void updateMarkerList() {
        //gmu = new GenerateMarkersUtility(mMap, mOriginPoint, mCardDetailsView, getActivity(), mFoodSpotList);
        gmu = new GenerateMarkersUtility(mMap, mOriginPoint, mCardMeasuredWidth, getActivity(), mFoodSpotList);
        gmu.updateMarkerList(mFoodSpotList);
    }

    private void setFavoritesView(boolean clicked, boolean animateList) {
        if (!clicked) {
            updateAdapters(mFoodSpotData.getFilteredList(true), animateList);
            mFavoriteButton.setImageResource(R.drawable.ic_favorite_unchecked);
            return;
        }

        mFavoritesDbUtility = FavoritesDbUtility.getInstance(getActivity());
        mFoodSpotList = (ArrayList<FoodSpot>) mFavoritesDbUtility.getFavoriteList(getActivity(), true);
        if (mFoodSpotList != null) {
            updateAdapters(mFoodSpotList, animateList);
            updateMarkerList();
            mFavoriteButton.setImageResource(R.drawable.ic_favorite_checked);
        } else {
            Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.error_no_favorite_foodspots), Toast.LENGTH_LONG).show();
            mFavoritesClicked = !mFavoritesClicked;
            mFavoriteButton.setImageResource(R.drawable.ic_favorite_unchecked);
            updateAdapters(mFoodSpotData.getFilteredList(false), animateList);
        }

    }

    //Interface callback method from CurrentLocationUtility.class
    @Override
    public void updateCurrentLocation(Location location) {
        mOriginPoint = new FoodSpotLatLng(location.getLatitude(), location.getLongitude());

        if (gmu != null && mMap != null) {
            gmu.updateOriginMaker(mOriginPoint);

            CameraUpdate center =
                    CameraUpdateFactory.newLatLng(new LatLng(mOriginPoint.getLat(), mOriginPoint.getLng()));
            CameraUpdate zoom = CameraUpdateFactory.zoomTo(17);

            mMap.moveCamera(center);
            mMap.animateCamera(zoom);
        }
    }

    @Override
    public void onCreateOptionsMenu(final Menu menu, final MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.foodspotlist_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.foodspotlist_menu_favorites:
                clickFavorite();
                break;
            case R.id.foodspotlist_menu_gridview:
                gridOnClick();
                break;
            case R.id.foodspotlist_menu_filter:
                filterOnClick();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @OnClick(R.id.fragment_foodspotlist_favorite)
    public void clickFavorite() {
        mFavoritesClicked = !mFavoritesClicked;

        if (mFavoritesClicked)
            setFavoritesView(true, true);
        else
            setFavoritesView(false, true);
    }

    @OnClick(R.id.fragment_foodspotlist_map_currentlocation_button)
    public void mapCurrentLocationButtonClick() {
        CurrentLocationUtility currentLocationUtility = new CurrentLocationUtility(getActivity(), this);
        currentLocationUtility.getLocation();
    }

    //Filter FoodSpot List
    @OnClick(R.id.fragment_foodspotlist_filter_button)
    public void filterOnClick() {

        animate(truckFilter);
        filterButtonClicked = !filterButtonClicked;

        //Filter Button Clicked
        if (filterButtonClicked) {

            mGridViewButton.setVisibility(View.INVISIBLE);

            Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_out_right);
            mRecyclerView.startAnimation(animation);

            Animation animation2 = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in_left);
            mStylesRecylerView.startAnimation(animation2);

            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    mRecyclerView.setAnimation(null);
                    mRecyclerView.setVisibility(View.INVISIBLE);

                    mStylesRecylerView.setAnimation(null);
                    mStylesRecylerView.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
        } else {
            //Filter Button UnClicked

            //Update FoodSpotList Adapter and Markers on Map
            if (mFavoritesClicked)
                mFoodSpotList = (ArrayList<FoodSpot>) mFavoritesDbUtility.getFavoriteList(getActivity(), true);
            else
                mFoodSpotList = mFoodSpotData.getFilteredList(true);

            mFoodSpotAdapter.updateAdapter(mFoodSpotList);
            updateMarkerList();

            Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in_right);
            mRecyclerView.startAnimation(animation);

            Animation animation2 = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_out_left);
            mStylesRecylerView.startAnimation(animation2);

            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {

                    mRecyclerView.setAnimation(null);
                    mRecyclerView.setVisibility(View.VISIBLE);

                    mStylesRecylerView.setAnimation(null);
                    mStylesRecylerView.setVisibility(View.INVISIBLE);

                    mGridViewButton.setVisibility(View.VISIBLE);


                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
        }
    }

    //GridView Button
    @OnClick(R.id.fragment_foodspotlist_grid_button)
    public void gridOnClick() {
        if (mFoodSpotList == null || mFoodSpotList.isEmpty() || mFoodSpotAdapter == null)
            return;

        mGridViewButton.setEnabled(false);

        Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(
                getActivity(),
                mRecyclerView,
                mRecyclerView.getTransitionName())
                .toBundle();

        Intent intent = new Intent(getActivity(), Activity_FoodSpotListGridView.class);

        intent.putExtra(Activity_FoodSpotListGridView.KEY_FOODSPOTLIST, mFoodSpotAdapter.getFoodSpotList());
        startActivity(intent, bundle);
    }

    @Override
    public void onSaveInstanceState(Bundle outstate) {
        super.onSaveInstanceState(outstate);
        outstate.putBoolean(KEY_STATE_FAVORITE_CLICKED, mFavoritesClicked);
        outstate.putSerializable(KEY_STATE_FOODSPOT_LIST, mFoodSpotList);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        // If coming back from FoodSpot_Details Activity, then refresh Favorites List in case
        // a favorite FoodSpot was removed.
        mGridViewButton.setEnabled(true);

        if (mFavoritesClicked) {
            mFoodSpotAdapter = null;
            setFavoritesView(true, false);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (gmu != null)
            gmu.cancelTask();
        if (mFoodSpotData != null)
            mFoodSpotData.stopTask();
    }
}

