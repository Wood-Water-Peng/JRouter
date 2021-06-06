package com.example.base_lib.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

/**
 * @Author jacky.peng
 * @Date 2021/4/14 9:24 AM
 * @Version 1.0
 */
@Database(entities = {RecommendItemEntity.class}, version = 3, exportSchema = false)
public abstract class JRouterDatabase extends RoomDatabase {
    public abstract RecommendDao getEventDao();
}
