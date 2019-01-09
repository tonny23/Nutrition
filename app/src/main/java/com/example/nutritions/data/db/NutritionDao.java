package com.example.nutritions.data.db;

import com.example.nutritions.data.model.Nutrition;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface NutritionDao {

    @Query("SELECT * from nutrition ORDER BY id DESC")
    LiveData<List<Nutrition>> getAllNutrition();

    @Insert
    void insert(Nutrition nutrition);

    @Delete
    void delete(Nutrition nutrition);

    @Update
    void update(Nutrition nutrition);

    @Query("DELETE FROM nutrition")
    void deleteAll();
}
