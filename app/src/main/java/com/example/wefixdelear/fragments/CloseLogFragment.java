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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.wefixdelear.Api.RetrofitClient;
import com.example.wefixdelear.R;
import com.example.wefixdelear.adapter.LogHistoryAdapter;
import com.example.wefixdelear.model.LogResponse;
import com.example.wefixdelear.model.Logs;
import com.example.wefixdelear.storage.SharedPrefManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CloseLogFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    RecyclerView recyclerView;
    List<Logs> logsList;
    ProgressDialog progressBar;

    SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_close_log, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mSwipeRefreshLayout = view.findViewById(R.id.container);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        getLog();

    }

    public void getLog() {
        progressBar = new ProgressDialog(getActivity());
        progressBar.show();
        progressBar.setContentView(R.layout.progress_dialog);
        Objects.requireNonNull(progressBar.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);

        int client_ref_id = SharedPrefManager.getInstance(getActivity()).getDelear().getTblDelearId();

        Call<LogResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .getCallLogForDelear(client_ref_id, "app");

        call.enqueue(
                new Callback<LogResponse>() {
                    @Override
                    public void onResponse(Call<LogResponse> call, Response<LogResponse> response) {
//                        logsList.clear();
                        if (response.isSuccessful()) {
                            progressBar.dismiss();
                            assert response.body() != null;
                            logsList = response.body().getLog();
                            List<Logs> logs = new ArrayList<>();
                            for (Logs logs1 : logsList) {
                                if (logs1.getCallLogStatus().equals("TRANSFER")) {
                                    logs.add(logs1);
                                }
                            }
                            LogHistoryAdapter adapter = new LogHistoryAdapter(getActivity(), logs);
                            recyclerView.setAdapter(adapter);
                        } else {
                            progressBar.dismiss();
                            Toast.makeText(getActivity(), "Something went wrong try Again", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<LogResponse> call, Throwable t) {
                        Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                        progressBar.dismiss();
                    }
                }
        );

    }

    @Override
    public void onRefresh() {
        getLog();
        if (logsList != null) {
            Toast.makeText(getActivity(), "Refresh", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(() -> mSwipeRefreshLayout.setRefreshing(false), 1000);
        }
    }
}