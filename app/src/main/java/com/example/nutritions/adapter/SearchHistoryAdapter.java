package com.example.nutritions.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.nutritions.R;
import com.example.nutritions.data.model.Nutrition;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Adapter for search history list
 */
public class SearchHistoryAdapter extends RecyclerView.Adapter<SearchHistoryAdapter.SearchHistoryHolder> {

    private final LayoutInflater mInflater;
    private List<Nutrition> mNutritions;
    private ItemClickListener mItemClickListener;

    public SearchHistoryAdapter(Context context, SearchHistoryAdapter.ItemClickListener clickListener) {
        mInflater = LayoutInflater.from(context);
        mItemClickListener = clickListener;
    }

    @Override
    public SearchHistoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.food_item, parent, false);
        return new SearchHistoryHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SearchHistoryHolder holder, int position) {
        if (mNutritions != null) {
            Nutrition current = mNutritions.get(position);
            holder.btnFood.setText(current.getName());
            holder.btnFood.setOnClickListener(view -> mItemClickListener.onItemClick(position));
        }
    }

    public void setNutritions(List<Nutrition> nutritions){
        mNutritions = nutritions;
        notifyDataSetChanged();
    }

    // getItemCount() is called many times, and when it is first called,
    // mNutritions has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        if (mNutritions != null)
            return mNutritions.size();
        else return 0;
    }

    class SearchHistoryHolder extends RecyclerView.ViewHolder {
        private final Button btnFood;

        private SearchHistoryHolder(View itemView) {
            super(itemView);
            btnFood = itemView.findViewById(R.id.btnFood);
        }
    }

    public interface ItemClickListener {
        void onItemClick(int position);
    }
}