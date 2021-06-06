package com.example.home_module;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.lifecycle.ViewModelStore;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.annotation.JRouterAnno;
import com.example.base_lib.db.DBHelper;
import com.example.home_module.callback.DataCallback;
import com.example.home_module.callback.DataProgressCallback;
import com.example.home_module.model.RecommendItem;
import com.example.home_module.viewmodel.HomeViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@JRouterAnno(path = "/home_module/HomeActivity")
public class HomeActivity extends AppCompatActivity {

    private HomeViewModel homeViewModel;
    RecyclerView recyclerView;
    View contentView;
    View loadingView;
    View emptyView;
    private RecommendAdapter recommendAdapter;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        recyclerView = findViewById(R.id.recyclerview);
        contentView = findViewById(R.id.content_view);
        progressBar = findViewById(R.id.progress_bar_h);
        loadingView = findViewById(R.id.loading_view);
        emptyView = findViewById(R.id.empty_view);
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        homeViewModel.getLiveData().observe(this, new Observer<List<RecommendItem>>() {
            @Override
            public void onChanged(List<RecommendItem> recommendItems) {
                //数据源更新
                if (recommendAdapter != null) {
                    recommendAdapter.updateDataList(recommendItems);
                }
            }
        });
        homeViewModel.initDataFromDB("123", new DataCallback<List<RecommendItem>>() {
            @Override
            public void onSuccess(List<RecommendItem> data) {
                if (data == null || data.isEmpty()) {
                    showLoadingView();
                    homeViewModel.initDataFromRemote("123", remoteDataCallback);
                } else {
                    initView(data);
                }
            }

            @Override
            public void onFailed(String msg) {

            }
        });
    }

    RemoteDataCallback remoteDataCallback = new RemoteDataCallback();

    //从网络抓取一批数据，并更新到缓存和本地数据库
    public void fetchFromRemote(View view) {
        homeViewModel.fetchDataFromRemote(new DataProgressCallback<List<RecommendItem>>() {


            @Override
            public void onProgress(float total, float cur) {
                progressBar.setVisibility(View.VISIBLE);
                progressBar.setMax(100);
                progressBar.setProgress((int) ((cur * 1.0f / total) * 100));
            }

            @Override
            public void onSuccess(List<RecommendItem> data) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "共加载" + data.size() + "条数据", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailed(String msg) {

            }
        });
    }

    public void deleteDBdData(View view) {
        Dispatcher.getInstance().dispatch(new Runnable() {
            @Override
            public void run() {
                DBHelper.getInstance(HomeModule.sContext).deleteAllRecommends();
            }
        });
    }

    class RemoteDataCallback implements DataCallback<List<RecommendItem>> {

        @Override
        public void onSuccess(List<RecommendItem> data) {
            if (data == null || data.isEmpty()) {
                showEmptyView();
            } else {
                initView(data);
            }
        }

        @Override
        public void onFailed(String msg) {
            showErrorView(msg);
        }
    }

    private void showErrorView(String msg) {

    }

    private void showLoadingView() {
        loadingView.setVisibility(View.VISIBLE);
        contentView.setVisibility(View.GONE);
        emptyView.setVisibility(View.GONE);
    }

    private void showEmptyView() {
        emptyView.setVisibility(View.VISIBLE);
        contentView.setVisibility(View.GONE);
        loadingView.setVisibility(View.GONE);
    }

    private void initView(List<RecommendItem> recommendItems) {
        contentView.setVisibility(View.VISIBLE);
        loadingView.setVisibility(View.GONE);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recommendAdapter = new RecommendAdapter(recommendItems);
        recyclerView.setAdapter(recommendAdapter);
    }


    public void addOne(View view) {
        homeViewModel.addOneData();
    }

    public void removeOne(View view) {
        homeViewModel.removeOneData();
    }

    class RecommendAdapter extends RecyclerView.Adapter {
        //适配器需要的数据源
        List<RecommendItem> dataList;

        public RecommendAdapter(List<RecommendItem> recommendItems) {
            //初始化数据源
            dataList = recommendItems;
        }

        //使用新的数据源
        public void updateDataList(List<RecommendItem> recommendItems) {
            dataList = recommendItems;
            notifyDataSetChanged();
        }

        @NonNull
        @NotNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup viewGroup, int i) {
            TextView textView = new TextView(HomeActivity.this);
            RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 60);
            layoutParams.topMargin = 20;
            textView.setGravity(Gravity.CENTER);
            textView.setBackgroundColor(Color.BLUE);
            textView.setLayoutParams(layoutParams);
            return new RecyclerView.ViewHolder(textView) {

            };
        }

        @Override
        public void onBindViewHolder(@NonNull @NotNull RecyclerView.ViewHolder viewHolder, int position) {
            ((TextView) viewHolder.itemView).setText(position + "");
        }

        @Override
        public int getItemCount() {
            return dataList.size();
        }
    }
}
