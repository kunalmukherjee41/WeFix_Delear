package com.Aahan.wefix.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.Aahan.wefix.Api.RetrofitClient;
import com.Aahan.wefix.R;
import com.Aahan.wefix.adapter.PaymentLogAdapter;
import com.Aahan.wefix.model.PaidLog;
import com.Aahan.wefix.model.PaidLogResponse;
import com.Aahan.wefix.storage.SharedPrefManager;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaidCallLogActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private int refDelearID;
    private List<PaidLog> paidLogList;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paid_call_log);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Payment History");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressBar = findViewById(R.id.progress_bar);

        refDelearID = SharedPrefManager.getInstance(this).getDelear().getTblDelearId();
        getPayment();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

    }

    private void getPayment() {

        Call<PaidLogResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .getPaidCallLog(refDelearID);

        call.enqueue(
                new Callback<PaidLogResponse>() {
                    @Override
                    public void onResponse(Call<PaidLogResponse> call, Response<PaidLogResponse> response) {
                        assert response.body() != null;
                        if (!response.body().isError()) {
                            paidLogList = response.body().getPaidLogs();

                            recyclerView.setItemViewCacheSize(paidLogList.size());
                            PaymentLogAdapter adapter = new PaymentLogAdapter(PaidCallLogActivity.this, paidLogList);
                            adapter.setHasStableIds(true);
                            adapter.notifyDataSetChanged();
                            recyclerView.setAdapter(adapter);
                            progressBar.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onFailure(Call<PaidLogResponse> call, Throwable t) {

                    }
                }
        );
    }
}