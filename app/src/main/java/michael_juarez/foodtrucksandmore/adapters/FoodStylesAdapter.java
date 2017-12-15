package michael_juarez.foodtrucksandmore.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import michael_juarez.foodtrucksandmore.R;
import michael_juarez.foodtrucksandmore.data.FoodStyle;
import michael_juarez.foodtrucksandmore.utilities.FoodStyleDb;
import michael_juarez.foodtrucksandmore.utilities.GlideApp;


/**
 * Created by user on 11/8/2017.
 */

public class FoodStylesAdapter extends RecyclerView.Adapter<FoodStylesAdapter.ViewHolder> {
    private ArrayList<FoodStyle> mFoodStyleList;
    private ListItemClickListener mListItemClickListener;
    private int lastPosition = -1;
    private boolean mAnimateList;

    //For ViewHolder animations
    private int SLIDE_DURATION = 1500;
    private static final int SLIDE_INCREMENT = 300;
    private static final int SLIDE_LIMIT = 3600;


    public FoodStylesAdapter(ArrayList<FoodStyle> foodStyleList, Context context, Boolean animateList) {
        mFoodStyleList = foodStyleList;
        mAnimateList = animateList;
    }

    public interface ListItemClickListener {
        void onListItemClick(int itemClicked, String link, ImageView imageView);
    }

    public void updateAdapter(ArrayList<FoodStyle> foodStyleList) {
        mFoodStyleList = foodStyleList;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_foodstyle, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        FoodStyle listItem = mFoodStyleList.get(position);

        Typeface custom_font = Typeface.createFromAsset(holder.itemView.getContext().getAssets(),  "fonts/app_font.ttf");
        holder.mStyleTextView.setTypeface(custom_font);

        GlideApp.with(holder.itemView.getContext())
                .load(listItem.getDrawableLocation())
                .into(holder.mFoodStyleImageView);
        holder.mStyleTextView.setText(listItem.getName());

        if (!mFoodStyleList.get(position).isSelected())
            holder.mCheckedImageView.setVisibility(View.INVISIBLE);

        if (mAnimateList == true);
            setAnimation(holder.itemView, position);
    }

    private void setAnimation(View viewToAnimate, int position)
    {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(viewToAnimate.getContext(), R.anim.slide_in_left);
            animation.setDuration(SLIDE_DURATION);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
            SLIDE_DURATION+=SLIDE_INCREMENT;
            if (SLIDE_DURATION > SLIDE_LIMIT)
                SLIDE_DURATION=1000;
        }
    }

    @Override
    public int getItemCount() {
        return mFoodStyleList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public ImageView mFoodStyleImageView;
        public TextView mStyleTextView;
        public ImageView mCheckedImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            mFoodStyleImageView = (ImageView) itemView.findViewById(R.id.list_item_foodstyle_iv);
            mStyleTextView = (TextView) itemView.findViewById(R.id.list_item_foodstyle_name);
            mCheckedImageView = (ImageView) itemView.findViewById(R.id.list_item_foodstyle_check);
            itemView.setOnClickListener(this);
        }


        // This code is when a FoodSpot Style Filter Item is clicked/unclicked
        @Override
        public void onClick(View view) {
            /*
                If a FoodSpot is unchecked, change it's selected setting to false
                Also change the check mark image to invisible
            */

            if (mCheckedImageView.getVisibility() == View.VISIBLE) {
                mFoodStyleList.get((getAdapterPosition())).setSelected(false);
                Animation animation = AnimationUtils.loadAnimation(view.getContext(), android.R.anim.fade_out);
                mCheckedImageView.setAnimation(animation);
                mCheckedImageView.setVisibility(View.INVISIBLE);
            }
            /*
                If a FoodSpot is checked, change it's selected setting to visible
                Also change the check mark image to visible
            */
            else {
                Animation animation = AnimationUtils.loadAnimation(view.getContext(), android.R.anim.fade_in);
                mFoodStyleList.get((getAdapterPosition())).setSelected(true);
                mCheckedImageView.setAnimation(animation);
                mCheckedImageView.setVisibility(View.VISIBLE);
            }
        }


    }
}
