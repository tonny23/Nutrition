package com.example.nutritions.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.example.nutritions.R;
import com.example.nutritions.activity.NutritionActivity;
import com.example.nutritions.adapter.FoodItemAdapter;
import com.example.nutritions.data.model.Nutrition;
import com.example.nutritions.data.model.NutritionViewModel;

import java.util.ArrayList;

import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * A fragment to show the predicted food in a list
 * Use the {@link FoodListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FoodListFragment extends DialogFragment implements FoodItemAdapter.ItemClickListener {
    
    private static final String ARG_FOOD = "food";
    public static final String ARG_NAME = "name";
    private ArrayList<String> foodList;
    private FoodItemAdapter mFoodItemAdapter;
    private RecyclerView mRecyclerView;
    private NutritionViewModel mNutritionViewModel;
    public static final double DIALOG_HEIGHT = 0.4;

    private static final String TAG = "FoodListFragment";

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param foodList list of food.
     * @return A new instance of fragment FoodListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FoodListFragment newInstance(ArrayList<String> foodList) {
        FoodListFragment fragment = new FoodListFragment();
        Bundle args = new Bundle();
        args.putStringArrayList(ARG_FOOD, foodList);
        Log.d(TAG, "newInstance: " + foodList.size());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            foodList = getArguments().getStringArrayList(ARG_FOOD);
        }
        mFoodItemAdapter = new FoodItemAdapter(foodList, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_food_list, container);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));

        mNutritionViewModel = ViewModelProviders.of(this).get(NutritionViewModel.class);

        mRecyclerView = view.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mFoodItemAdapter);
        Log.d(TAG, "onCreateView: " + mRecyclerView.getAdapter().getItemCount());
        return view;
    }

    @Override
    public void onItemClick(int position) {
        Intent nutrition = new Intent(getActivity(), NutritionActivity.class);
        nutrition.setAction(Intent.ACTION_MAIN);
        nutrition.putExtra(ARG_NAME, foodList.get(position));
        mNutritionViewModel.insert(new Nutrition(foodList.get(position)));
        startActivity(nutrition);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setCanceledOnTouchOutside(true);
        Window window = dialog.getWindow();
        assert window != null;
        WindowManager.LayoutParams lp = window.getAttributes();
        //set dialog to bottom
        lp.gravity = Gravity.BOTTOM;
        //set dialog animation
        lp.windowAnimations = R.style.DialogAnimation;
        window.setAttributes(lp);
        //set width and height of dialog
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = (int)(getResources().getDisplayMetrics().heightPixels*DIALOG_HEIGHT);
        dialog.getWindow().setLayout(width, height);

        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();

        // set dialog size
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = getResources().getDisplayMetrics().widthPixels;
            int height = (int)(getResources().getDisplayMetrics().heightPixels*DIALOG_HEIGHT);
            dialog.getWindow().setLayout(width, height);
        }
    }
}
