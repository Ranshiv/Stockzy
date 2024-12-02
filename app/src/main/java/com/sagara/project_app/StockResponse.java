package com.sagara.project_app;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class StockResponse {
    @SerializedName("Global Quote")
    private Map<String, String> globalQuote;

    public Map<String, String> getGlobalQuote() {
        return globalQuote;
    }
}