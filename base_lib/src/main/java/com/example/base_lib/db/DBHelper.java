package com.example.base_lib.db;

import android.content.Context;

import androidx.room.Room;

import java.util.List;

/**
 * @Author jacky.peng
 * @Date 2021/4/14 9:26 AM
 * @Version 1.0
 */
public class DBHelper {
    private static volatile DBHelper sInstance;
    JRouterDatabase database;
    RecommendDao eventDao;


    public static DBHelper getInstance(Context context) {
        if (sInstance == null) {
            synchronized (DBHelper.class) {
                if (sInstance == null) {
                    sInstance = new DBHelper(context);
                }
            }
        }
        return sInstance;
    }

    public DBHelper(Context context) {
        database = Room.databaseBuilder(context.getApplicationContext(), JRouterDatabase.class, DBParams.DB_NAME) //new a database
                .build();
        eventDao = database.getEventDao();
    }

    /**
     * @param entity 插入新数据的行数
     * @return
     */
    public long insertEvent(RecommendItemEntity entity) {
        return eventDao.insertEvent(entity);
    }

    /**
     * @param entity 模拟将数据上传到服务器
     * @return
     */
    public long reportEvent(RecommendItemEntity entity) {
        return eventDao.insertEvent(entity);
    }

    /**
     * @param lastId 要删除的最后一行的id
     * @return 表中的count数
     */
    public long deleteEvents(int lastId) {
        eventDao.deleteEventsById(lastId);
        return eventDao.queryCount();
    }


    public void deleteAllRecommends() {
        eventDao.deleteAll();
    }


    /**
     * @param limit 最多获取的行数
     * @return 表中的count数
     */
    public List<RecommendItemEntity> queryEvents(int limit) {
        return eventDao.getLimitEvents(limit);
    }
}
