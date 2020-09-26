package com.Aahan.wefix.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
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

public class CloseLogFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    RecyclerView recyclerView;
    List<Logs> logsList;
    private ProgressBar progressBar;
    TextView noRecord;
    SwipeRefreshLayout mSwipeRefreshLayout;
    private LogHistoryAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_close_log, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SearchView searchView = view.findViewById(R.id.search_view);
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (adapter != null)
                    adapter.getFilter().filter(newText);
                return false;
            }
        });

        mSwipeRefreshLayout = view.findViewById(R.id.container);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        progressBar = view.findViewById(R.id.progress_bar);

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

        int client_ref_id = SharedPrefManager.getInstance(getActivity()).getDelear().getTblDelearId();

        Call<LogResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .getCallLogForDelear(client_ref_id);

        call.enqueue(
                new Callback<LogResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<LogResponse> call, @NonNull Response<LogResponse> response) {
//                        logsList.clear();
                        if (response.isSuccessful()) {
//                            progressBar.dismiss();
                            assert response.body() != null;
                            logsList = response.body().getLog();
                            List<Logs> logs = new ArrayList<>();
                            for (Logs logs1 : logsList) {
                                if (logs1.getCallLogStatus().equals("CANCEL") || logs1.getCallLogStatus().equals("CLOSE") || logs1.getCallLogStatus().equals("REJECT") || logs1.getCallLogStatus().equals("COMPLETE")) {
                                    logs.add(logs1);
                                }
                            }
                            recyclerView.setItemViewCacheSize(logs.size());
                            if (!logs.isEmpty()) {
                                adapter = new LogHistoryAdapter(getActivity(), logs);
                                adapter.setHasStableIds(true);
                                adapter.notifyDataSetChanged();
                                recyclerView.setAdapter(adapter);
                            } else {
                                noRecord.setVisibility(View.VISIBLE);
                            }
                        } else {
//                            progressBar.dismiss();
                            Toast.makeText(getActivity(), "Something went wrong try Again", Toast.LENGTH_LONG).show();
                        }
                        progressBar.setVisibility(View.GONE);

                    }

                    @Override
                    public void onFailure(@NonNull Call<LogResponse> call, @NonNull Throwable t) {
                        Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
//                        progressBar.dismiss();
                        progressBar.setVisibility(View.GONE);

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