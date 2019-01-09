package com.example.nutritions.data.db;

import android.app.Application;
import android.os.AsyncTask;

import com.example.nutritions.data.model.Nutrition;

import java.util.List;

import androidx.lifecycle.LiveData;

public class NutritionRepository {
    private NutritionDao mNutritionDao;
    private LiveData<List<Nutrition>> mAllNutrition;

    public NutritionRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        mNutritionDao = db.nutritionDao();
        mAllNutrition = mNutritionDao.getAllNutrition();
    }

    public LiveData<List<Nutrition>> getAllNutrition() {
        return mAllNutrition;
    }

    public void insert (Nutrition nutrition) {
        new insertAsyncTask(mNutritionDao).execute(nutrition);
    }

    private static class insertAsyncTask extends AsyncTask<Nutrition, Void, Void> {

        private NutritionDao mAsyncTaskDao;

        insertAsyncTask(NutritionDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Nutrition... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    public void deleteAll() {
        new deleteAsyncTask(mNutritionDao).execute();
    }

    private static class deleteAsyncTask extends AsyncTask<Void, Void, Void> {

        private NutritionDao mAsyncTaskDao;

        deleteAsyncTask(NutritionDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Void... params) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }
}
