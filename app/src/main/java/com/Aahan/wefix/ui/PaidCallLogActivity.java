package com.Aahan.wefix.ui;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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

public class PaidCallLogActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private ProgressBar progressBar;
    private int refDelearID;
    private List<PaidLog> paidLogList;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paid_call_log);

        mSwipeRefreshLayout = findViewById(R.id.layout);
        mSwipeRefreshLayout.setOnRefreshListener(this);

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

    @Override
    public void onRefresh() {
        if (paidLogList != null)
            paidLogList.clear();
        getPayment();
        if (paidLogList != null) {
            Toast.makeText(PaidCallLogActivity.this, "Refresh", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(() -> mSwipeRefreshLayout.setRefreshing(false), 1000);
        }
    }
}