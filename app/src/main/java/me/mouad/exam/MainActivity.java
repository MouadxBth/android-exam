package me.mouad.exam;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.concurrent.atomic.AtomicReference;

public class MainActivity extends AppCompatActivity {

    private final String URL = "https://cdn.jsdelivr.net/gh/fawazahmed0/currency-api@1/latest/currencies/";
    private final AtomicReference<CryptoResult> cryptoResultAtomicReference = new AtomicReference<>();

    private String currentCurrency = "btc";

    private String getBasedOnCurrency(String currency) {

        switch (currency) {
            case "Ethereum":
                return "eth.json";
            case "Ripple":
                return "rpl.json";
            case "Litecoin":
                return "ltc.json";
            default:
                return "btc.json";
        }
    }

    private JsonObjectRequest configureRequest(String currency) {
        return new JsonObjectRequest(URL + currency, response -> {
            try {
                JSONObject resultObject = response.getJSONObject(currency.split("\\.")[0]);

                CryptoResult current = new CryptoResult(resultObject.getDouble("usd"),
                        resultObject.getDouble("eur"),
                        resultObject.getDouble("mad"));

                cryptoResultAtomicReference.set(current);

                displayResult(currentCurrency);
            } catch (Exception e) {
                Log.e("Error", e.getMessage() != null ? e.getMessage() : "Something bad happened");
            }
        }, e -> {
            Log.e("Error", e.getMessage() != null ? e.getMessage() : "Something bad happened");
        });
    }

    private void configureSpinner(RequestQueue queue) {
        final Spinner spinner = (Spinner) findViewById(R.id.crypto_names);

        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.crypto_names,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long rowId) {
                final JsonObjectRequest request = configureRequest(getBasedOnCurrency(spinner.getSelectedItem().toString()));
                queue.add(request);
            }


            @Override
            public void onNothingSelected(AdapterView<?> view) {}
        });

    }

    private void configureButtons() {


        final Button usdButton = findViewById(R.id.usd);
        final Button eurButton = findViewById(R.id.eur);
        final Button madButton = findViewById(R.id.mad);

        usdButton.setOnClickListener(view -> {
            currentCurrency = "usd";
            displayResult(currentCurrency);
        });

        eurButton.setOnClickListener(view -> {
            currentCurrency = "eur";
            displayResult(currentCurrency);
        });

        madButton.setOnClickListener(view -> {
            currentCurrency = "mad";
            displayResult(currentCurrency);
        });
    }

    @SuppressLint("SetTextI18n")
    private void displayResult(String currency) {
        final TextView textView = findViewById(R.id.result);
        final TextView currentView = findViewById(R.id.current);

        String valueResult = "Result: ";
        String currentResult = "Current: ";

        switch (currency) {
            case "eur":
                currentView.setText(currentResult + currentCurrency);
                textView.setText(valueResult + cryptoResultAtomicReference.get().getEur());
                break;
            case "mad":
                currentView.setText(currentResult + currentCurrency);
                textView.setText(valueResult + cryptoResultAtomicReference.get().getMad());
                break;
            default:
                currentView.setText(currentResult + currentCurrency);
                textView.setText(valueResult + cryptoResultAtomicReference.get().getUsd());
                break;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cryptoResultAtomicReference.set(new CryptoResult(0.0, 0.0, 0.0));

        final RequestQueue queue = Volley.newRequestQueue(this);

        configureSpinner(queue);
        configureButtons();


    }
}