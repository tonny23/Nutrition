package com.example.nutritions.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieProperty;
import com.airbnb.lottie.model.KeyPath;
import com.airbnb.lottie.value.LottieValueCallback;
import com.camerakit.CameraKitView;
import com.example.nutritions.R;
import com.example.nutritions.fragment.FoodListFragment;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import clarifai2.api.ClarifaiBuilder;
import clarifai2.api.ClarifaiClient;
import clarifai2.dto.input.ClarifaiInput;
import clarifai2.dto.model.output.ClarifaiOutput;
import clarifai2.exception.ClarifaiException;
import okhttp3.OkHttpClient;

public class MainActivity extends AppCompatActivity {

    private CameraKitView cameraKitView;
    private Button btnCapture;
    private LottieAnimationView mLottieAnimationView;
    private ArrayList<String> foodList;
    public static final String API_KEY_CLARIFAI = "c329566b744e4e13bb6b2a3b7e0454dc";
    public static final int READ_TIME_OUT = 10;

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cameraKitView = findViewById(R.id.camera);
        btnCapture = findViewById(R.id.btnCapture);
        mLottieAnimationView = findViewById(R.id.animation_view);
        mLottieAnimationView.setVisibility(View.INVISIBLE);
        //change animation color to white
        mLottieAnimationView.addValueCallback(new KeyPath("**"),
                LottieProperty.COLOR_FILTER,
                new LottieValueCallback<>(new PorterDuffColorFilter(Color.WHITE, PorterDuff.Mode.ADD)));
        btnCapture.setOnClickListener(v -> capture());
    }

    /**
     * Capture picture to send to the API
     */
    public void capture() {
        cameraKitView.captureImage((cameraKitView, capturedImage) -> {
            predictPhoto(capturedImage);
        });
    }

    /**
     * Send picture to the API and retrieve prediction results
     *
     * @param capturedImage Image captured to predict
     */
    public void predictPhoto(final byte[] capturedImage) {
        foodList = new ArrayList<>();
        // start loading animation
        mLottieAnimationView.setVisibility(View.VISIBLE);
        final ClarifaiClient client = new ClarifaiBuilder(API_KEY_CLARIFAI)
                .client(new OkHttpClient.Builder()
                        .readTimeout(READ_TIME_OUT, TimeUnit.SECONDS) // Increase timeout for poor mobile networks
                        .build())
                .buildSync();
        client.getModelByID(client.getDefaultModels().foodModel().id()).executeAsync(
                model -> model.predict()
                        .withInputs(ClarifaiInput.forImage(capturedImage))
                        .executeAsync(
                                outputs -> {
                                    // get list of results
                                    showFoodList(outputs.get(0));
                                    // hide animation
                                    mLottieAnimationView.setVisibility(View.INVISIBLE);
                                }
                        ),
                code -> Log.e(TAG, "Error code: " + code + ". Error msg: "),
                e -> {
                    throw new ClarifaiException(e);
                }
        );
    }

    /**
     * Show predicted food in a fragment
     *
     * @param outputList the predicted list retrieved from the API
     */
    public void showFoodList(ClarifaiOutput<?> outputList) {
        extractFoodNames(outputList);
        showDialog();
    }

    /**
     * Show dialogfragment with a list of food
     */
    private void showDialog() {
        FragmentManager fm = getSupportFragmentManager();
        FoodListFragment foodFragment = FoodListFragment.newInstance(foodList);
        foodFragment.show(fm, "fragment_select_food");
    }

    /**
     * Extract the food list from the results from API
     *
     * @param outputList the predicted list retrieved from the API
     */
    public void extractFoodNames(ClarifaiOutput<?> outputList) {
        for (int i = 0; i < outputList.data().size(); i++) {
            foodList.add(outputList.data().get(i).asConcept().name());
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        cameraKitView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        cameraKitView.onResume();
    }

    @Override
    protected void onPause() {
        cameraKitView.onPause();
        super.onPause();
    }

    @Override
    protected void onStop() {
        cameraKitView.onStop();
        super.onStop();
    }

    /**
     * Ask for camera permission
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        cameraKitView.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent searchHistory = new Intent(this, SearchHistoryActivity.class);
            searchHistory.setAction(Intent.ACTION_SEARCH);
            startActivity(searchHistory);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
