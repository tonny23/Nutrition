package com.example.nutritions.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.nutritions.R;
import com.example.nutritions.adapter.SearchHistoryAdapter;
import com.example.nutritions.data.model.Nutrition;
import com.example.nutritions.data.model.NutritionViewModel;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SearchHistoryActivity extends AppCompatActivity implements SearchHistoryAdapter.ItemClickListener {

    private NutritionViewModel mNutritionViewModel;
    private TextView tvNoHistory;
    private List<Nutrition> mNutritionList;
    public static final String ARG_NAME = "name";
    private static final String TAG = "SearchHistoryActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_history);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.history_title));

        tvNoHistory = findViewById(R.id.tvNoHistory);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        final SearchHistoryAdapter adapter = new SearchHistoryAdapter(this, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mNutritionViewModel = ViewModelProviders.of(this).get(NutritionViewModel.class);

        mNutritionViewModel.getAllNutrition().observe(this, nutritions -> {
            // Update the cached copy of the nutritions in the adapter.
            adapter.setNutritions(nutritions);
            mNutritionList = nutritions;
            if (nutritions.size() == 0) {
                tvNoHistory.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onItemClick(int position) {
        Intent nutrition = new Intent(this, NutritionActivity.class);
        nutrition.setAction(Intent.ACTION_SEARCH);
        nutrition.putExtra(ARG_NAME, mNutritionList.get(position).getName());
        startActivity(nutrition);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_history, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                Log.e(TAG, "onOptionsItemSelected: " + id + " " + android.R.id.home);
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.action_settings:
                Log.e(TAG, "onOptionsItemSelected: " + id + " " + R.id.action_settings);
                mNutritionViewModel.deleteAll();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
