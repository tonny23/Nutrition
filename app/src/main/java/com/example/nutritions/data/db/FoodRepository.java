package com.example.nutritions.data.db;

import android.app.Application;
import android.os.AsyncTask;

import com.example.nutritions.data.model.Food;

import java.util.List;

import androidx.lifecycle.LiveData;

public class FoodRepository {
    private FoodDao mFoodDao;
    private LiveData<List<Food>> mAllFood;

    public FoodRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        mFoodDao = db.nutritionDao();
        mAllFood = mFoodDao.getAllFood();
    }

    public LiveData<List<Food>> getAllFood() {
        return mAllFood;
    }

    public void insert (Food food) {
        new insertAsyncTask(mFoodDao).execute(food);
    }

    private static class insertAsyncTask extends AsyncTask<Food, Void, Void> {

        private FoodDao mAsyncTaskDao;

        insertAsyncTask(FoodDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Food... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    public void deleteAll() {
        new deleteAsyncTask(mFoodDao).execute();
    }

    private static class deleteAsyncTask extends AsyncTask<Void, Void, Void> {

        private FoodDao mAsyncTaskDao;

        deleteAsyncTask(FoodDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Void... params) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }
}
