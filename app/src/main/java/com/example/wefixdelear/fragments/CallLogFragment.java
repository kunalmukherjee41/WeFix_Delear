package com.example.wefixdelear.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class CallLogFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<Logs> logsList;

    ProgressDialog progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_call_log, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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

        int ref_technician_id = SharedPrefManager.getInstance(getActivity()).getDelear().getTblDelearId();

        Call<LogResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .getCallLogForDelear(ref_technician_id, "app");

        call.enqueue(
                new Callback<LogResponse>() {
                    @Override
                    public void onResponse(Call<LogResponse> call, Response<LogResponse> response) {
                        if (response.isSuccessful()) {
                            assert response.body() != null;
                            logsList = response.body().getLog();
                            List<Logs> logs = new ArrayList<>();
                            for (Logs logs1 : logsList) {
                                if (logs1.getCallLogStatus().equals("OPEN")) {
                                    logs.add(logs1);
                                }
                            }
                            LogHistoryAdapter adapter = new LogHistoryAdapter(getActivity(), logs);
                            recyclerView.setAdapter(adapter);
                            progressBar.dismiss();
                        } else {
                            progressBar.dismiss();
                            Toast.makeText(getActivity(), "Logs Not Loaded! Something went wrong try Again", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<LogResponse> call, Throwable t) {
                        Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
                        progressBar.dismiss();

                    }
                }
        );
    }

}