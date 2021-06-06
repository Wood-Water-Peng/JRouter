package com.example.home_module.datasource;

import com.example.home_module.model.RecommendItem;

import java.util.List;

public interface HomeDataSource {
    List<RecommendItem> getRecommendItem();
}
