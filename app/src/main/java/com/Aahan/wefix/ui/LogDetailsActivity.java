package com.Aahan.wefix.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.Aahan.wefix.Api.RetrofitClient;
import com.Aahan.wefix.R;
import com.Aahan.wefix.model.Category;
import com.Aahan.wefix.model.Category1Response;
import com.Aahan.wefix.model.Logs;
import com.Aahan.wefix.model.Service;
import com.Aahan.wefix.model.Service1Response;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LogDetailsActivity extends AppCompatActivity {

    private TextView category, workType;
    private ProgressDialog progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_details);

        progressBar = new ProgressDialog(this);
        progressBar.show();
        progressBar.setContentView(R.layout.progress_dialog);
        Objects.requireNonNull(progressBar.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Logs Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent i = getIntent();

        Logs logs = (Logs) i.getSerializableExtra("logs");

        TextView date = findViewById(R.id.date);
        TextView name = findViewById(R.id.name);
        TextView email = findViewById(R.id.email);
        TextView phone = findViewById(R.id.phone);
        TextView address = findViewById(R.id.address);
        category = findViewById(R.id.category);
        TextView company = findViewById(R.id.company);
        TextView status = findViewById(R.id.status);
        TextView pinCode = findViewById(R.id.pin);
        TextView problemDes = findViewById(R.id.problem_des);
        workType = findViewById(R.id.service);

        assert logs != null;
        getCategory(logs.getRefCatId());
        if (logs.getRefServiceId() == 37) {
            workType.setText("Only Visit");
        } else {
            getService(logs.getRefServiceId(), logs.getRefCatId());
        }

        name.setText(logs.getClientName());
        address.setText(logs.getClientAddress());
        pinCode.setText(logs.getClientPin());
        email.setText(logs.getClientEmail());
        phone.setText(logs.getClientMb());
        date.setText(logs.getCallLogDate());
        company.setText(logs.getProductCompany());
        problemDes.setText(logs.getProblem());
        status.setText(logs.getCallLogStatus());

    }

    private void getService(int refServiceId, int refCatId) {

        final String[] serviceName = new String[1];

        Call<Service1Response> call = RetrofitClient
                .getInstance()
                .getApi()
                .getService(refCatId);

        call.enqueue(
                new Callback<Service1Response>() {
                    @Override
                    public void onResponse(Call<Service1Response> call, Response<Service1Response> response) {
                        if (response.isSuccessful()) {
                            assert response.body() != null;
                            List<Service> serviceList = response.body().getService();
                            for (Service s : serviceList) {
                                if (s.getTbl_services_id() == refServiceId) {
                                    serviceName[0] = s.getTbl_services_name();
                                }
//                                serviceItem.add(s.getTbl_services_name());
                            }
//                            ArrayAdapter<String> adapter = new ArrayAdapter<>(LogHistoryDetailsActivity.this, R.layout.support_simple_spinner_dropdown_item, serviceItem);
                            workType.setText(serviceName[0]);

                        }
                    }

                    @Override
                    public void onFailure(Call<Service1Response> call, Throwable t) {

                    }
                }
        );

    }

    private void getCategory(int refCatId) {

        Call<Category1Response> call = RetrofitClient
                .getInstance()
                .getApi()
                .getCategoryByID(refCatId);

        call.enqueue(
                new Callback<Category1Response>() {
                    @Override
                    public void onResponse(@NonNull Call<Category1Response> call, @NonNull Response<Category1Response> response) {
                        if (response.isSuccessful()) {
                            assert response.body() != null;
                            Category cat = response.body().getCategory();
                            category.setText(cat.getTbl_category_name());
                        }
                        progressBar.dismiss();
                    }

                    @Override
                    public void onFailure(@NonNull Call<Category1Response> call, @NonNull Throwable t) {
                        progressBar.dismiss();
                    }
                }
        );
    }

}