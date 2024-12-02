package com.sagara.project_app;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private static final String BASE_URL = "https://www.alphavantage.co/";
    private static final String API_KEY = "TPMWFV093KQDDN9E"; // Replace with your API key
    private TextView stockSymbolTextView, openTextView, highTextView, lowTextView, priceTextView,
            volumeTextView, latestTradingDayTextView, previousCloseTextView, changeTextView, changePercentTextView;
    private EditText stockSymbolEditText;
    private Button fetchStockButton;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        stockSymbolTextView = findViewById(R.id.stockSymbolTextView);
        openTextView = findViewById(R.id.openTextView);
        highTextView = findViewById(R.id.highTextView);
        lowTextView = findViewById(R.id.lowTextView);
        priceTextView = findViewById(R.id.priceTextView);
        volumeTextView = findViewById(R.id.volumeTextView);
        latestTradingDayTextView = findViewById(R.id.latestTradingDayTextView);
        previousCloseTextView = findViewById(R.id.previousCloseTextView);
        changeTextView = findViewById(R.id.changeTextView);
        changePercentTextView = findViewById(R.id.changePercentTextView);
        stockSymbolEditText = findViewById(R.id.stockSymbolEditText);
        fetchStockButton = findViewById(R.id.fetchStockButton);
        progressBar = findViewById(R.id.progressBar);

        // Initialize Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        StockApiService stockApiService = retrofit.create(StockApiService.class);

        // Fetch stock data when button is clicked
        fetchStockButton.setOnClickListener(v -> {
            String symbol = stockSymbolEditText.getText().toString().trim();
            if (!symbol.isEmpty()) {
                progressBar.setVisibility(View.VISIBLE);  // Show progress bar
                fetchStockData(stockApiService, symbol);
            } else {
                stockSymbolTextView.setText("Please enter a stock symbol.");
            }
        });
    }

    // Fetch stock data from API
    private void fetchStockData(StockApiService stockApiService, String symbol) {
        Call<StockResponse> call = stockApiService.getStockData("GLOBAL_QUOTE", symbol, API_KEY);
        call.enqueue(new Callback<StockResponse>() {
            @Override
            public void onResponse(Call<StockResponse> call, Response<StockResponse> response) {
                progressBar.setVisibility(View.GONE);  // Hide progress bar once data is received
                if (response.isSuccessful() && response.body() != null) {
                    StockResponse stockResponse = response.body();
                    updateStockUI(stockResponse);
                } else {
                    Log.e("StockApp", "Response unsuccessful");
                    stockSymbolTextView.setText("Error fetching data. Please check the stock symbol.");
                }
            }

            @Override
            public void onFailure(Call<StockResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);  // Hide progress bar on failure
                Log.e("StockApp", "Error: " + t.getMessage());
                stockSymbolTextView.setText("Failed to fetch data. Please try again.");
            }
        });
    }

    // Update UI with stock data
    private void updateStockUI(StockResponse stockResponse) {
        stockSymbolTextView.setText("Stock Symbol: " + stockResponse.getSymbol());
        openTextView.setText("Open: " + stockResponse.getOpen());
        highTextView.setText("High: " + stockResponse.getHigh());
        lowTextView.setText("Low: " + stockResponse.getLow());
        priceTextView.setText("Price: " + stockResponse.getPrice());
        volumeTextView.setText("Volume: " + stockResponse.getVolume());
        latestTradingDayTextView.setText("Latest Trading Day: " + stockResponse.getLatestTradingDay());
        previousCloseTextView.setText("Previous Close: " + stockResponse.getPreviousClose());
        changeTextView.setText("Change: " + stockResponse.getChange());
        changePercentTextView.setText("Change Percent: " + stockResponse.getChangePercent() + "%");
    }

}
