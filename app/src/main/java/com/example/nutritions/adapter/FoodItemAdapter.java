package com.example.nutritions.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.nutritions.R;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Adapter for showing food list
 */
public class FoodItemAdapter extends RecyclerView.Adapter<FoodItemAdapter.ViewHolder> {

    private ArrayList<String> mFoodItems;
    private ItemClickListener mItemClickListener;

    // Provide a reference to the views for each data item
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {

        public Button btn;
        public View mView;

        public ViewHolder(View itemView) {
            super(itemView);

            btn = itemView.findViewById(R.id.btnFood);
            mView = itemView;
        }
    }

    public FoodItemAdapter(ArrayList<String> foodItems, ItemClickListener clickListener) {
        mFoodItems = foodItems;
        mItemClickListener = clickListener;
    }

    @Override
    public FoodItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View bucketListView = inflater.inflate(R.layout.food_item, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(bucketListView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(FoodItemAdapter.ViewHolder holder, final int position) {
        // Get the data model based on position
        String foodItem = mFoodItems.get(position);

        // Set item views based on your views and data model
        final Button btn = holder.btn;
        btn.setText(foodItem);
        holder.btn.setOnClickListener(view -> mItemClickListener.onItemClick(position));
    }

    @Override
    public int getItemCount() {
        return mFoodItems.size();
    }

    public interface ItemClickListener {
        void onItemClick(int position);
    }
}
