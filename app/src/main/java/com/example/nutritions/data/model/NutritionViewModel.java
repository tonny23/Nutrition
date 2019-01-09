package com.example.nutritions.data.model;

import android.app.Application;

import com.example.nutritions.data.db.NutritionRepository;

import java.util.List;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class NutritionViewModel extends AndroidViewModel {

    private NutritionRepository mRepository;

    private LiveData<List<Nutrition>> mAllNutrition;

    public NutritionViewModel (Application application) {
        super(application);
        mRepository = new NutritionRepository(application);
        mAllNutrition = mRepository.getAllNutrition();
    }

    public LiveData<List<Nutrition>> getAllNutrition() { return mAllNutrition; }

    public void insert(Nutrition nutrition) { mRepository.insert(nutrition); }
    public void deleteAll() { mRepository.deleteAll(); }

}