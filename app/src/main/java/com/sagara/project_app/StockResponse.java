package com.sagara.project_app;

import com.google.gson.annotations.SerializedName;
import java.util.Map;

public class StockResponse {
    @SerializedName("Global Quote")
    private Map<String, String> globalQuote;

    // Getter for globalQuote
    public Map<String, String> getGlobalQuote() {
        return globalQuote;
    }

    // Add a method to retrieve the stock symbol from the map
    public String getSymbol() {
        return globalQuote.get("01. symbol"); // "01. symbol" is the key for stock symbol in the response
    }

    public String getOpen() {
        return globalQuote.get("02. open");
    }

    public String getHigh() {
        return globalQuote.get("03. high");
    }

    public String getLow() {
        return globalQuote.get("04. low");
    }

    public String getPrice() {
        return globalQuote.get("05. price");
    }

    public String getVolume() {
        return globalQuote.get("06. volume");
    }

    public String getLatestTradingDay() {
        return globalQuote.get("07. latest trading day");
    }

    public String getPreviousClose() {
        return globalQuote.get("08. previous close");
    }

    public String getChange() {
        return globalQuote.get("09. change");
    }

    public String getChangePercent() {
        return globalQuote.get("10. change percent");
    }
}
