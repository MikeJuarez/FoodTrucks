package michael_juarez.foodtrucksandmore.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import michael_juarez.foodtrucksandmore.R;
import michael_juarez.foodtrucksandmore.data.FoodSpot;
import michael_juarez.foodtrucksandmore.utilities.FavoritesDbUtility;
import michael_juarez.foodtrucksandmore.utilities.GlideApp;

import static michael_juarez.foodtrucksandmore.R.id.fragment_foodspotdetails_foodtruckmenu_tv;

/**
 * Created by user on 11/14/2017.
 */

public class



Fragment_FoodSpotDetails extends Fragment {
    private static final int LOADER_FAVORITE_CHECK = 0;
    public static final String KEY_IMAGE_LINK = "Fragment_FoodSpotDetails.KEY_IMAGE_LINK";
    public static final String KEY_FOODSPOT = "Fragment_FoodSpotDetails.KEY_FOODSPOT";
    public static final String KEY_FRAGMENT_FOODSPOTDETAILS_BUNDLE = "KEY_FRAGMENT_FOODSPOTDETAILS.BUNDLE";

    @BindView(R.id.fragment_foodspotdetails_details_image) ImageView mDetailsImageView;
    @BindView(R.id.fragment_foodspotdetails_foodtruckname_tv) TextView mFoodSpotNameTextView;
    @BindView(R.id.fragment_foodspotlist_foodtruckdetails_tv) michael_juarez.foodtrucksandmore.utilities.TextViewEx mFoodSpotDescriptionTextView;
    @BindView(R.id.fragment_foodspotdetails_foodtruckaddress_tv) michael_juarez.foodtrucksandmore.utilities.TextViewEx mFoodSpotAddressTextView;
    @BindView(R.id.fragment_foodspotdetails_foodtruckhours_tv) michael_juarez.foodtrucksandmore.utilities.TextViewEx mFoodSpotWebsiteTextView;
    @BindView(R.id.fragment_foodspotdetails_foodtruckwebsite_tv) michael_juarez.foodtrucksandmore.utilities.TextViewEx mFoodSpotHoursTextView;
    @BindView(fragment_foodspotdetails_foodtruckmenu_tv) ImageView mFoodSpotMenuImageView;

    private Unbinder unbinder;
    private String mImageUrlLink;
    private FoodSpot mFoodSpot;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_foodspotdetails, container, false);
        unbinder = ButterKnife.bind(this, view);

        Bundle bundle = getArguments();
        mFoodSpot = (FoodSpot) bundle.getSerializable(KEY_FOODSPOT);

        setupViews();

        return view;
    }

    private void setupViews() {
        //Set FoodSpot Image
        mImageUrlLink = mFoodSpot.getImage_location();
        GlideApp.with(getActivity())
                .load(mImageUrlLink)
                .into(mDetailsImageView);

        //Set FoodSpot Name
        mFoodSpotNameTextView.setText(mFoodSpot.getName());

        //Set Description
        mFoodSpotDescriptionTextView.setText(mFoodSpot.getDescription(), true);

        //Set Address
        mFoodSpotAddressTextView.setText(mFoodSpot.getAddress(), true);

        String hoursText = "";

        //Set Hours
        for (int i = 0; i < mFoodSpot.getOpen_time().size(); i++) {
            String dayOfWeek = "";
            switch (i) {
                case 0: dayOfWeek = "Monday:"; break;
                case 1: dayOfWeek = "Tuesday:"; break;
                case 2: dayOfWeek = "Wednesday:"; break;
                case 3: dayOfWeek = "Thursday:"; break;
                case 4: dayOfWeek = "Friday:"; break;
                case 5: dayOfWeek = "Saturday:"; break;
                case 6: dayOfWeek = "Sunday:"; break;
            }
            hoursText += dayOfWeek + "\t\t\t\t\t" + mFoodSpot.getOpen_time().get(i) + " - " + mFoodSpot.getClose_time().get(i) + "\n";
        }

        mFoodSpotHoursTextView.setText(hoursText);

        //Set Website
        mFoodSpotWebsiteTextView.setText(mFoodSpot.getWebsite());

        GlideApp.with(getActivity())
                .load(mFoodSpot.getMenu_location())
                .into(mFoodSpotMenuImageView);
    }

    @OnClick(R.id.fragment_foodspotdetails_foodtruckmenu_tv)
    public void menuImageClicked() {
        Fragment_Menu fragment = new Fragment_Menu();
        Bundle bundle = new Bundle();
        bundle.putString(Fragment_Menu.KEY_MENU_IMAGEVIEW, mFoodSpot.getMenu_location());
        fragment.setArguments(bundle);

        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onCreateOptionsMenu(final Menu menu, final MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.foodspot_details_menu, menu);

        ActionBar actionbar = ((AppCompatActivity)getActivity()).getSupportActionBar();

        if (actionbar != null) {
            actionbar.setHomeButtonEnabled(true);
        }
        // isFavorite will return true if the user clicked the FoodSpot using the Favorites Menu
        // otherwise we need to check if this FoodSpot is in the Favorites List

        LoaderManager.LoaderCallbacks<Boolean> queryLoader = new LoaderManager.LoaderCallbacks<Boolean>() {

            @Override
            public Loader<Boolean> onCreateLoader(int id, Bundle args) {
                return new AsyncTaskLoader<Boolean>(getActivity()){

                    @Override
                    protected void onStartLoading() {
                        forceLoad();
                    }

                    @Override
                    public Boolean loadInBackground() {
                        try {
                            FavoritesDbUtility favoritesDbUtility = FavoritesDbUtility.getInstance(getActivity());
                            List<FoodSpot> favoriteList = favoritesDbUtility.getFavoriteList(getActivity(), false);

                            if (favoriteList == null)
                                return false;

                            for (FoodSpot fs : favoriteList) {
                                if (mFoodSpot.getKey_geofire().equals(fs.getKey_geofire()))
                                    return true;
                            }

                            return false;
                        } catch (Exception e) {
                            Log.e("FoodSpotDetails: ", "Failed to asynchronously load data.");
                            e.printStackTrace();
                            return false;
                        }
                    }
                };
            }

            @Override
            public void onLoadFinished(Loader<Boolean> loader, Boolean data) {
                if (data){
                    mFoodSpot.setFavorite(true);
                    MenuItem item = menu.getItem(0).setIcon(R.drawable.ic_favorite_checked);
                    item.setChecked(true);
                }
                getActivity().getSupportLoaderManager().destroyLoader(loader.getId());
            }

            @Override
            public void onLoaderReset(Loader<Boolean> loader) {

            }
        };

        LoaderManager loaderManager = getActivity().getSupportLoaderManager();
        Loader<Boolean> favoriteLoader = loaderManager.getLoader(LOADER_FAVORITE_CHECK);

        if (favoriteLoader == null)
            loaderManager.initLoader(LOADER_FAVORITE_CHECK,null,queryLoader);
        else
            loaderManager.restartLoader(LOADER_FAVORITE_CHECK,null,queryLoader);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().onBackPressed();
                return true;
            case R.id.menu_favorite:
                if (!item.isChecked()) {
                    item.setChecked(true);
                    item.setIcon(R.drawable.ic_favorite_checked);
                    mFoodSpot.setFavorite(true);
                    saveFavorite();
                } else {
                    item.setChecked(false);
                    item.setIcon(R.drawable.ic_favorite_unchecked);
                    removeFavorite();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveFavorite() {
        FavoritesDbUtility favoritesDbUtility = FavoritesDbUtility.getInstance(getActivity());
        boolean result = favoritesDbUtility.insertFavorite(getActivity(), mFoodSpot);
        if (result)
            Toast.makeText(getActivity(), mFoodSpot.getName() + " was succesfully added to your Favorites List", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(getActivity(), "There was an error adding " + mFoodSpot.getName() + " to your Favorites List.", Toast.LENGTH_SHORT).show();
    }

    private void removeFavorite() {
        FavoritesDbUtility favoritesDbUtility = FavoritesDbUtility.getInstance(getActivity());
        boolean result = favoritesDbUtility.removeFavorite(getActivity(), mFoodSpot);
        if (result)
            Toast.makeText(getActivity(), mFoodSpot.getName() + " was succesfully removed from your Favorites List", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(getActivity(), "There was an error removing " + mFoodSpot.getName() + " from your Favorites List.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }



}
