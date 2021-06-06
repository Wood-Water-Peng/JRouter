package com.example.base_lib.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * @Author jacky.peng
 * @Date 2021/4/14 9:19 AM
 * @Version 1.0
 */
@Entity(tableName = DBParams.RECOMMEND_TABLE_NAME)
public class RecommendItemEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "event_name")
    private String name;

    @ColumnInfo(name = "create_time")
    private long create_time;

    @ColumnInfo(name = "data")
    private String data;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(long create_time) {
        this.create_time = create_time;
    }
}
