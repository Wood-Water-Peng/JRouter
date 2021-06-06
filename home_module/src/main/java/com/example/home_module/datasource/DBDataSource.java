package com.example.home_module.datasource;

import com.example.base_lib.db.DBHelper;
import com.example.base_lib.db.RecommendItemEntity;
import com.example.home_module.HomeModule;
import com.example.home_module.model.RecommendItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DBDataSource implements HomeDataSource {
    @Override
    public List<RecommendItem> getRecommendItem() {
        return generateFakeData();
    }

    public List<RecommendItem> generateFakeData() {
        List<RecommendItemEntity> recommendItemEntities = DBHelper.getInstance(HomeModule.sContext).queryEvents(20);
        ArrayList<RecommendItem> list = new ArrayList<>();
        for (int i = 0; i < recommendItemEntities.size(); i++) {
            list.add(new RecommendItem());
        }
        return list;
    }

    public void insertData(List<RecommendItem> list) {
        for (int i = 0; i < list.size(); i++) {
            DBHelper.getInstance(HomeModule.sContext).insertEvent(new RecommendItemEntity());
        }
    }
}
