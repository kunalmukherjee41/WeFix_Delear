package com.example.wefixdelear;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wefixdelear.Api.RetrofitClient;
import com.example.wefixdelear.model.Category;
import com.example.wefixdelear.model.Category1Response;
import com.example.wefixdelear.model.Logs;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LogDetailsActivity extends AppCompatActivity {

    TextView date, name, phone, email;
    TextView address, category;
    TextView company, status;
    Logs logs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_details);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Logs Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent i = getIntent();

        logs = (Logs) i.getSerializableExtra("logs");

        date = findViewById(R.id.date);
//        log_type = findViewById(R.id.log_type);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);
        address = findViewById(R.id.address);
        category = findViewById(R.id.category);
        company = findViewById(R.id.company);
//        amount = findViewById(R.id.amount);
        status = findViewById(R.id.status);

        getCategory(logs.getRefCatId());
        date.setText(logs.getCallLogDate());
        name.setText(logs.getClientName());
        email.setText(logs.getClientEmail());
        phone.setText(logs.getClientMb());
        address.setText(logs.getClientAddress());
        status.setText(logs.getCallLogStatus());
        company.setText(logs.getProductCompany());

    }

    private void getCategory(int refCatId) {

        Call<Category1Response> call = RetrofitClient
                .getInstance()
                .getApi()
                .getCategoryByID(refCatId, "aa");

        call.enqueue(
                new Callback<Category1Response>() {
                    @Override
                    public void onResponse(Call<Category1Response> call, Response<Category1Response> response) {
                        if (response.isSuccessful()) {
                            assert response.body() != null;
                            Category cat = response.body().getCategory();
                            category.setText(cat.getTbl_category_name());
                        }
                    }

                    @Override
                    public void onFailure(Call<Category1Response> call, Throwable t) {

                    }
                }
        );
    }

}