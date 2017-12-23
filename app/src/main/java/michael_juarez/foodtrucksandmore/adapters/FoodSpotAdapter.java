package michael_juarez.foodtrucksandmore.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import michael_juarez.foodtrucksandmore.R;
import michael_juarez.foodtrucksandmore.data.FoodSpot;
import michael_juarez.foodtrucksandmore.utilities.GlideApp;


/**
 * Created by user on 11/8/2017.
 */

public class FoodSpotAdapter extends RecyclerView.Adapter<FoodSpotAdapter.ViewHolder> {
    private static final String KEYSTATE_ITEMS = "FoodSpotAdapter.KEY_STATE_ITEMS";

    private ArrayList<FoodSpot> mFoodSpotList;
    private Context mContext;
    private ListItemClickListener mListItemClickListener; //If list item clicked, send info to next screen

    private boolean mShouldAnimate;
    private boolean mIsHorizontal;
    private boolean mIsGridView;


    public FoodSpotAdapter(ArrayList<FoodSpot> foodSpotList, Context context, ListItemClickListener listItemClickListener, boolean shouldAnimate, boolean isHorizontal, boolean isGridView) {
        if (mFoodSpotList == null)
            mFoodSpotList = new ArrayList<>();
        mFoodSpotList = foodSpotList;
        mShouldAnimate = shouldAnimate;
        mContext = context;
        mListItemClickListener = listItemClickListener;
        mIsHorizontal = isHorizontal;
        mIsGridView = isGridView;
    }

    public interface ListItemClickListener {
        void onListItemClick(int itemClicked, String link, ImageView imageView);
    }

    public void updateAdapter(ArrayList<FoodSpot> foodSpotList) {
        mFoodSpotList = foodSpotList;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = null;
        if (!mIsGridView) {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_foodspot_listview, parent, false);
            return new ViewHolder(v);
        }

        if (!mIsHorizontal) {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_foodspot_vertical, parent, false);
        }
        else {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_foodspot_horizontal, parent, false);
        }

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        FoodSpot listItem = mFoodSpotList.get(position);

        Typeface custom_font = Typeface.createFromAsset(mContext.getAssets(), "fonts/app_font.ttf");
        holder.mNameTextView.setTypeface(custom_font);

        GlideApp.with(mContext)
                .load(listItem.getImage_location())
                .into(holder.mFoodSpotImageView);
        holder.mNameTextView.setText(listItem.getName());
        holder.mStyleTextView.setText(listItem.getFood_type());
        holder.mDistanceTextView.setText(listItem.getDistanceText());
        if (mShouldAnimate == true)
            setAnimation(holder.itemView, position);
    }

    private void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.fade_in);
        animation.setDuration(1500);
        viewToAnimate.startAnimation(animation);

/*        if (position > lastPosition && !first3)
        {
            Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.fade_in);
            animation.setDuration(1000);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
            SLIDE_DURATION += SLIDE_INCREMENT;
            if (SLIDE_DURATION > SLIDE_LIMIT)
                first3 = TRUE;
        }*/
    }

    @Override
    public int getItemCount() {
        return mFoodSpotList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView mFoodSpotImageView;
        public TextView mNameTextView;
        public TextView mStyleTextView;
        public TextView mDistanceTextView;
        @BindView(R.id.list_item_foodspot_container)
        CardView container;

        public ViewHolder(View itemView) {
            super(itemView);
            mFoodSpotImageView = (ImageView) itemView.findViewById(R.id.list_item_foodspot_iv);
            mNameTextView = (TextView) itemView.findViewById(R.id.list_item_foodspot_name);
            mStyleTextView = (TextView) itemView.findViewById(R.id.list_item_foodspot_style_tv);
            mDistanceTextView = (TextView) itemView.findViewById(R.id.list_item_foodspot_distance_tv);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            String link = mFoodSpotList.get(getAdapterPosition()).getImage_location();
            mListItemClickListener.onListItemClick(getAdapterPosition(), link, mFoodSpotImageView);
        }
    }

    public ArrayList<FoodSpot> getFoodSpotList() {
        return mFoodSpotList;
    }

}
