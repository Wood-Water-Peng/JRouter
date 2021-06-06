package com.example.home_module.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.home_module.HomeActivity;
import com.example.home_module.callback.DataCallback;
import com.example.home_module.callback.DataProgressCallback;
import com.example.home_module.data.HomeRepository;
import com.example.home_module.model.RecommendItem;

import java.util.List;

public class HomeViewModel extends ViewModel {
    HomeRepository repository = HomeRepository.getInstance();

    MutableLiveData<List<RecommendItem>> recommendList = new MutableLiveData<>();
    MutableLiveData<Boolean> noMoreDataEvent = new MutableLiveData<>();
    MutableLiveData<Boolean> loadingDataEvent = new MutableLiveData<>();
    MutableLiveData<Boolean> loadFailedEvent = new MutableLiveData<>();
    MutableLiveData<Boolean> emptyDataEvent = new MutableLiveData<>();
    MutableLiveData<Boolean> notInitEvent = new MutableLiveData<>();

    public MutableLiveData<List<RecommendItem>> getLiveData() {
        return recommendList;
    }

    public void initData(String uid) {
        repository.getDataFromRemote(uid, new HomeRepository.CallBack() {
            @Override
            public void onSuccess(List<RecommendItem> data) {
                recommendList.postValue(data);
            }

            @Override
            public void onFailed(String msg) {

            }
        });
    }

    public void initDataFromDB(String uid, DataCallback<List<RecommendItem>> callback) {
        repository.getDataFromDB(uid, new HomeRepository.CallBack() {
            @Override
            public void onSuccess(List<RecommendItem> data) {
                recommendList.setValue(data);
                callback.onSuccess(recommendList.getValue());
            }

            @Override
            public void onFailed(String msg) {

            }
        });
    }

    public void addOneData() {
        List<RecommendItem> list = recommendList.getValue();
        list.add(new RecommendItem());
        recommendList.setValue(list);
    }

    public void removeOneData() {
        List<RecommendItem> list = recommendList.getValue();
        if (list == null) {
            //还没有任何数据
        } else {
            if (!list.isEmpty()) {
                list.remove(list.size() - 1);
                recommendList.setValue(list);
            } else {
                //没有数据了

            }

        }
    }

    public void initDataFromRemote(String uid, DataCallback<List<RecommendItem>> remoteDataCallback) {
        repository.getDataFromRemote(uid, new HomeRepository.CallBack() {
            @Override
            public void onSuccess(List<RecommendItem> data) {
                remoteDataCallback.onSuccess(data);
            }

            @Override
            public void onFailed(String msg) {

            }
        });
    }

    public void fetchDataFromRemote(DataProgressCallback<List<RecommendItem>> remoteDataCallback) {
        repository.fetchDataFromRemote(new DataProgressCallback<List<RecommendItem>>() {
            @Override
            public void onProgress(float total, float cur) {
                remoteDataCallback.onProgress(total, cur);
            }

            @Override
            public void onSuccess(List<RecommendItem> data) {
                remoteDataCallback.onSuccess(data);
                if (data != null) {
                    recommendList.getValue().addAll(data);
                    recommendList.setValue(recommendList.getValue());
                }
            }

            @Override
            public void onFailed(String msg) {

            }
        });
    }
}
