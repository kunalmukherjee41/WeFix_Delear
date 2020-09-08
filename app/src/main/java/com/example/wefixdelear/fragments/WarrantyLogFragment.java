package com.example.wefixdelear.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wefixdelear.Api.RetrofitClient;
import com.example.wefixdelear.R;
import com.example.wefixdelear.adapter.WarrantyLogHistoryAdapter;
import com.example.wefixdelear.model.Logs;
import com.example.wefixdelear.model.WarrantyLog;
import com.example.wefixdelear.model.WarrantyLogResponse;
import com.example.wefixdelear.storage.SharedPrefManager;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WarrantyLogFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    SwipeRefreshLayout mSwipeRefreshLayout;
    private List<WarrantyLog> warrantyLogList;
    //    ProgressDialog progressBar;
    RecyclerView recyclerView;
    TextView noRecord;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_warranty_log, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSwipeRefreshLayout = view.findViewById(R.id.container);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        noRecord = view.findViewById(R.id.no_record);
        noRecord.setVisibility(View.GONE);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        getLog();

    }

    private void getLog() {

//        progressBar = new ProgressDialog(getActivity());
//        progressBar.show();
//        progressBar.setContentView(R.layout.progress_dialog);
//        Objects.requireNonNull(progressBar.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);

        int tblDelearId = SharedPrefManager.getInstance(getActivity()).getDelear().getTblDelearId();

        Call<WarrantyLogResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .getWarrantyCallLog(tblDelearId, "APP");

        call.enqueue(
                new Callback<WarrantyLogResponse>() {
                    @Override
                    public void onResponse(Call<WarrantyLogResponse> call, Response<WarrantyLogResponse> response) {
                        if (response.isSuccessful()) {
                            assert response.body() != null;
                            warrantyLogList = response.body().getWarrantyLogList();
                            if (!warrantyLogList.isEmpty()) {
                                WarrantyLogHistoryAdapter adapter = new WarrantyLogHistoryAdapter(getActivity(), warrantyLogList);
                                recyclerView.setAdapter(adapter);
                            } else {
                                noRecord.setVisibility(View.VISIBLE);
                            }
//                            progressBar.dismiss();
                        } else {
//                            progressBar.dismiss();
                            Toast.makeText(getActivity(), "Logs Not Loaded! Something went wrong try Again", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<WarrantyLogResponse> call, Throwable t) {
//                        progressBar.dismiss();
                        Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
        );

    }

    @Override
    public void onRefresh() {
        getLog();
        if (warrantyLogList != null) {
            Toast.makeText(getActivity(), "Refresh", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(() -> mSwipeRefreshLayout.setRefreshing(false), 1000);
        }
    }

}