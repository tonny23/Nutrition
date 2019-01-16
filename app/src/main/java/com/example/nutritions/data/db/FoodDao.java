package com.example.nutritions.data.db;

import com.example.nutritions.data.model.Food;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface FoodDao {

    @Query("SELECT * from food")
    LiveData<List<Food>> getAllFood();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Food food);

    @Delete
    void delete(Food food);

    @Update
    void update(Food food);

    @Query("DELETE FROM food")
    void deleteAll();
}
