package com.example.home_module.data;

import com.example.home_module.Dispatcher;
import com.example.home_module.callback.DataProgressCallback;
import com.example.home_module.datasource.DBDataSource;
import com.example.home_module.datasource.RemoteDataSource;
import com.example.home_module.model.RecommendItem;

import java.util.List;

public class HomeRepository {
    DBDataSource dbDataSource;
    RemoteDataSource remoteDataSource;
    private static volatile HomeRepository instance;

    public static HomeRepository getInstance() {
        if (instance == null) {
            instance = new HomeRepository(new DBDataSource(), new RemoteDataSource());
        }
        return instance;
    }

    public HomeRepository(DBDataSource dbDataSource, RemoteDataSource remoteDataSource) {
        this.dbDataSource = dbDataSource;
        this.remoteDataSource = remoteDataSource;
    }

    public void getDataFromDB(String uid, CallBack callBack) {
        Dispatcher.getInstance().dispatch(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                    List<RecommendItem> recommendItem = dbDataSource.getRecommendItem();
                    if (callBack != null) {
                        Dispatcher.getInstance().runOnUIThread(new Runnable() {
                            @Override
                            public void run() {
                                callBack.onSuccess(recommendItem);
                            }
                        });
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public void getDataFromRemote(String uid, CallBack callBack) {
        Dispatcher.getInstance().dispatch(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                    List<RecommendItem> recommendItem = remoteDataSource.getRecommendItem();
                    if (callBack != null) {
                        Dispatcher.getInstance().runOnUIThread(new Runnable() {
                            @Override
                            public void run() {
                                callBack.onSuccess(recommendItem);
                            }
                        });
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 将过程返回
     *
     * @param callBack
     */
    public void fetchDataFromRemote(DataProgressCallback<List<RecommendItem>> callBack) {
        remoteDataSource.fetchFakeData(new DataProgressCallback<List<RecommendItem>>() {
            @Override
            public void onProgress(float total, float cur) {
                callBack.onProgress(total, cur);
            }

            @Override
            public void onSuccess(List<RecommendItem> data) {
                callBack.onSuccess(data);
                //将数据写入数据库中
                Dispatcher.getInstance().dispatch(new Runnable() {
                    @Override
                    public void run() {
                        dbDataSource.insertData(data);
                    }
                });
            }

            @Override
            public void onFailed(String msg) {

            }
        });
    }

    public interface CallBack {
        void onSuccess(List<RecommendItem> data);

        void onFailed(String msg);
    }
}
