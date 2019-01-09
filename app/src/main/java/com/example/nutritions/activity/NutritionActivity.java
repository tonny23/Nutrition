package com.example.nutritions.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.nutritions.R;
import com.example.nutritions.api.ApiClient;
import com.example.nutritions.api.ApiService;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NutritionActivity extends AppCompatActivity {

    public static final int READ_XML_START = 1000;
    private ProgressBar mProgressBar;
    private TextView tvName;
    private TextView tvNotFound;
    private ImageView mImageView;
    private String name;
    private static final String TAG = "NutritionActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nutrition);
        mProgressBar = findViewById(R.id.progressBar);
        mImageView = findViewById(R.id.imageView);
        tvName = findViewById(R.id.tvName);
        tvNotFound = findViewById(R.id.tvNotFound);
        Log.d(TAG, "onCreate: " + getIntent().getStringExtra("name"));
        name = getIntent().getStringExtra("name");
        tvName.setText(name);
        name += " nutrition facts";

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getNutrition();
    }

    public void getNutrition() {
        mProgressBar.setVisibility(View.VISIBLE);
        // Create a very simple REST adapter which points the API endpoint.
        ApiService client = ApiClient.getClient().create(ApiService.class);
        // Fetch a list of the Github repositories.
        Call<String> call = client.getNutrition(name);
        // Execute the call asynchronously. Get a positive or negative callback.
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.d(TAG, "onResponse: do call");
                Log.d(TAG, "onResponse: " + response.raw());
                // The network call was a success and we got a response
                if (response.isSuccessful() && response != null && response.body() != null) {
                    Log.d(TAG, "onResponse: " + response.body());
                    setNutrition(response.body());
                } else {
                    Log.e(TAG, "onResponse: not successful");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                // the network call was a failure
                Toast.makeText(getApplicationContext(), "Error, check internet connection", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setNutrition(String nutritionXml) {
        //pattern to find image URL
        Pattern pattern = Pattern.compile("src=\'(.*?)\'");
        Matcher matcher = pattern.matcher(nutritionXml);
        if (nutritionXml.length() > READ_XML_START && matcher.find(READ_XML_START)) {

            Log.d(TAG, "setNutrition: " + matcher.group(1));
            Glide.with(this)
                    // remove amp; from URL otherwise image cannot be loaded
                    .load(matcher.group(1).trim().replace("amp;", ""))
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            // log exception
                            Log.e("TAG", "Error loading image", e);
                            mProgressBar.setVisibility(View.GONE);
                            return false; // important to return false so the error placeholder can be placed
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            mProgressBar.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .into(mImageView);
        } else {
            tvNotFound.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Log.e(TAG, "onOptionsItemSelected: " + getIntent().getAction());
            if (getIntent().getAction() != null){
                switch (getIntent().getAction()) {
                    case (Intent.ACTION_MAIN):
                        startActivity(new Intent(this, MainActivity.class));
                        break;
                    case (Intent.ACTION_SEARCH):
                        startActivity(new Intent(this, SearchHistoryActivity.class));
                        break;
                }
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
