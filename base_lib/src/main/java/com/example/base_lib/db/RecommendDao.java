package com.example.base_lib.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/**
 * @Author jacky.peng
 * @Date 2021/4/14 9:21 AM
 * @Version 1.0
 */
@Dao
public interface RecommendDao {
    @Insert
    long insertEvent(RecommendItemEntity entity);


    @Insert
    long[] insertEvents(RecommendItemEntity... entities);

    @Update
    int updateWords(RecommendItemEntity... entities);

    @Query("DELETE FROM recommend_table")
    void deleteAll();

    @Query("DELETE FROM recommend_table WHERE id<=:id")
    void deleteEventsById(int id);

    @Query("SELECT * FROM recommend_table ORDER BY ID ASC LIMIT:limit")
    List<RecommendItemEntity> getLimitEvents(int limit);

    @Query("SELECT * FROM recommend_table ORDER BY ID ASC LIMIT :limit")
    RecommendItemEntity queryEntityByLimit(int limit);


    @Query("SELECT COUNT(*) FROM recommend_table")
    long queryCount();
}
