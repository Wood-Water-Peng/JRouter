package com.example.home_module.datasource;

import com.example.base_lib.db.DBHelper;
import com.example.base_lib.db.RecommendItemEntity;
import com.example.home_module.Dispatcher;
import com.example.home_module.HomeModule;
import com.example.home_module.R;
import com.example.home_module.callback.DataProgressCallback;
import com.example.home_module.model.RecommendItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RemoteDataSource implements HomeDataSource {
    @Override
    public List<RecommendItem> getRecommendItem() {

        return generateFakeData();
    }

    public void fetchFakeData(DataProgressCallback<List<RecommendItem>> callback) {
        int total = new Random().nextInt(10);
        List<RecommendItem> list = new ArrayList<>(total);

        Dispatcher.getInstance().dispatch(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < total; i++) {
                    try {
                        Thread.sleep(new Random().nextInt(2000) + 200);
                        list.add(new RecommendItem());
                        int finalI = i;
                        Dispatcher.getInstance().runOnUIThread(new Runnable() {
                            @Override
                            public void run() {
                                callback.onProgress(total, finalI + 1);
                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Dispatcher.getInstance().runOnUIThread(new Runnable() {
                    @Override
                    public void run() {
                        callback.onSuccess(list);
                    }
                });
            }
        });

    }

    public List<RecommendItem> generateFakeData() {
        List<RecommendItem> list = new ArrayList<>();
        int nextInt = new Random().nextInt(10);
        for (int i = 0; i < nextInt; i++) {
            list.add(new RecommendItem());
        }
        Dispatcher.getInstance().dispatch(new Runnable() {
            @Override
            public void run() {
                DBHelper instance = DBHelper.getInstance(HomeModule.sContext);
                for (int i = 0; i < list.size(); i++) {
                    instance.insertEvent(new RecommendItemEntity());
                }
            }
        });
        return list;
    }
}
