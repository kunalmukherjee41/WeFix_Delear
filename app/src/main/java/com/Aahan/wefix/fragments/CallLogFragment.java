package com.Aahan.wefix.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.Aahan.wefix.Api.RetrofitClient;
import com.Aahan.wefix.R;
import com.Aahan.wefix.adapter.LogHistoryAdapter;
import com.Aahan.wefix.model.LogResponse;
import com.Aahan.wefix.model.Logs;
import com.Aahan.wefix.storage.SharedPrefManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CallLogFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView recyclerView;
    private List<Logs> logsList;

//    ProgressDialog progressBar;

    TextView noRecord;

    SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_call_log, container, false);
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

    public void getLog() {

//        progressBar = new ProgressDialog(getActivity());
//        progressBar.show();
//        progressBar.setContentView(R.layout.progress_dialog);
//        Objects.requireNonNull(progressBar.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);

        int ref_technician_id = SharedPrefManager.getInstance(getActivity()).getDelear().getTblDelearId();

        Call<LogResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .getCallLogForDelear(ref_technician_id, "app");

        call.enqueue(
                new Callback<LogResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<LogResponse> call, @NonNull Response<LogResponse> response) {
                        if (response.isSuccessful()) {
                            assert response.body() != null;
                            logsList = response.body().getLog();
                            List<Logs> logs = new ArrayList<>();
                            for (Logs logs1 : logsList) {
                                if (!logs1.getCallLogStatus().equals("CANCEL") && !logs1.getCallLogStatus().equals("CLOSE") && !logs1.getCallLogStatus().equals("REJECT")) {
                                    logs.add(logs1);
                                }
                            }
                            recyclerView.setItemViewCacheSize(logs.size());
                            if (!logs.isEmpty()) {
                                LogHistoryAdapter adapter = new LogHistoryAdapter(getActivity(), logs);
                                adapter.setHasStableIds(true);
                                adapter.notifyDataSetChanged();
                                recyclerView.setAdapter(adapter);
//                                progressBar.dismiss();
                            } else {
                                noRecord.setVisibility(View.VISIBLE);
                            }
                        } else {
//                            progressBar.dismiss();
                            Toast.makeText(getActivity(), "Logs Not Loaded! Something went wrong try Again", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<LogResponse> call, @NonNull Throwable t) {
                        Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
//                        progressBar.dismiss();

                    }
                }
        );
    }

    @Override
    public void onRefresh() {
        if (logsList != null)
            logsList.clear();
        getLog();
        if (logsList != null) {
            Toast.makeText(getActivity(), "Refresh", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(() -> mSwipeRefreshLayout.setRefreshing(false), 1000);
        }
    }

}