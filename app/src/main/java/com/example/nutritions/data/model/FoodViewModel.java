package com.example.nutritions.data.model;

import android.app.Application;

import com.example.nutritions.data.db.FoodRepository;

import java.util.List;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class FoodViewModel extends AndroidViewModel {

    private FoodRepository mRepository;

    private LiveData<List<Food>> mAllFood;

    public FoodViewModel(Application application) {
        super(application);
        mRepository = new FoodRepository(application);
        mAllFood = mRepository.getAllFood();
    }

    public LiveData<List<Food>> getAllFood() { return mAllFood; }

    public void insert(Food food) { mRepository.insert(food); }
    public void deleteAll() { mRepository.deleteAll(); }

}