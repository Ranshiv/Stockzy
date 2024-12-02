package com.sagara.project_app;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
    private TextView stockTextView;
    private EditText stockSymbolEditText;
    private Button fetchStockButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        stockTextView = findViewById(R.id.stockTextView);
        stockSymbolEditText = findViewById(R.id.stockSymbolEditText);
        fetchStockButton = findViewById(R.id.fetchStockButton);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        StockApiService stockApiService = retrofit.create(StockApiService.class);

        fetchStockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String symbol = stockSymbolEditText.getText().toString().trim();
                if (!symbol.isEmpty()) {
                    fetchStockData(stockApiService, symbol);
                } else {
                    stockTextView.setText("Please enter a stock symbol.");
                }
            }
        });
    }

    private void fetchStockData(StockApiService stockApiService, String symbol) {
        Call<StockResponse> call = stockApiService.getStockData("GLOBAL_QUOTE", symbol, API_KEY);
        call.enqueue(new Callback<StockResponse>() {
            @Override
            public void onResponse(Call<StockResponse> call, Response<StockResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String stockData = response.body().getGlobalQuote().toString();
                    stockTextView.setText(stockData);
                } else {
                    Log.e("StockApp", "Response unsuccessful");
                    stockTextView.setText("Error fetching data. Please check the stock symbol.");
                }
            }

            @Override
            public void onFailure(Call<StockResponse> call, Throwable t) {
                Log.e("StockApp", "Error: " + t.getMessage());
                stockTextView.setText("Failed to fetch data. Please try again.");
            }
        });
    }
}