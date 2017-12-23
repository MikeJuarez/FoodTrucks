package michael_juarez.foodtrucksandmore.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import michael_juarez.foodtrucksandmore.R;
import michael_juarez.foodtrucksandmore.utilities.GlideApp;

/**
 * Created by user on 12/3/2017.
 */

public class Fragment_Menu extends Fragment {

    private Unbinder unbinder;
    public static final String KEY_MENU_IMAGEVIEW = "DIALOGFRAGMENT_MENU.KEY_MENU_IMAGEVIEW";

    @BindView(R.id.fragment_dialog_menu_menuImageView) michael_juarez.foodtrucksandmore.utilities.TouchImageView mFoodSpotMenuImageView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        setHasOptionsMenu(true);

        String mImageUrlLink = getArguments().getString(KEY_MENU_IMAGEVIEW);

        View view = inflater.inflate(R.layout.fragment_dialog_menu, container, false);
        unbinder = ButterKnife.bind(this, view);

        GlideApp.with(getActivity())
                .load(mImageUrlLink)

                .into(mFoodSpotMenuImageView);


        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
