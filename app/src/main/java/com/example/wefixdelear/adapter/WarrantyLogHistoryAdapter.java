package com.example.wefixdelear.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wefixdelear.Api.RetrofitClient;
import com.example.wefixdelear.R;
import com.example.wefixdelear.WarrantyLogHistoryActivity;
import com.example.wefixdelear.model.Company;
import com.example.wefixdelear.model.Company1Response;
import com.example.wefixdelear.model.CompanyResponse;
import com.example.wefixdelear.model.WarrantyLog;
import com.example.wefixdelear.model.WarrantyLogResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WarrantyLogHistoryAdapter extends RecyclerView.Adapter<WarrantyLogHistoryAdapter.LogViewHolder> {

    Context mContext;
    List<WarrantyLog> warrantyLogsList;
    Company company1;

    public WarrantyLogHistoryAdapter(Context mContext, List<WarrantyLog> warrantyLogsList) {
        this.mContext = mContext;
        this.warrantyLogsList = warrantyLogsList;
    }

    @NonNull
    @Override
    public LogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.warranty_recycler_view, parent, false);
        return new WarrantyLogHistoryAdapter.LogViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LogViewHolder holder, int position) {

        WarrantyLog warrantyLog = warrantyLogsList.get(position);

        Call<Company1Response> call = RetrofitClient
                .getInstance()
                .getApi()
                .getCompanyById(warrantyLog.getRefCompanyId(), "app");

        call.enqueue(
                new Callback<Company1Response>() {
                    @Override
                    public void onResponse(Call<Company1Response> call, Response<Company1Response> response) {
                        if (response.isSuccessful()) {
                            assert response.body() != null;
                            company1 = response.body().getCompany();
                            holder.problemDes.setText(warrantyLog.getProblem());
                            holder.name.setText(warrantyLog.getClientName());
                            assert company1 != null;
//                            Toast.makeText(mContext, company1.getTblCompanyName(), Toast.LENGTH_SHORT).show();
                            holder.company.setText(company1.getTblCompanyName());
                            holder.date.setText(warrantyLog.getCallLogDate());
                            holder.id.setText(String.valueOf(warrantyLog.getCallLogId()));
                        }
                    }

                    @Override
                    public void onFailure(Call<Company1Response> call, Throwable t) {

                    }
                }
        );

        holder.itemView.setOnClickListener(
                v -> {
                    WarrantyLog log = warrantyLogsList.get(position);
                    Intent intent = new Intent(mContext, WarrantyLogHistoryActivity.class);
                    intent.putExtra("log", log);
                    intent.putExtra("company", company1.getTblCompanyName());
                    mContext.startActivity(intent);
                }
        );

    }

    @Override
    public int getItemCount() {
        return warrantyLogsList.size();
    }

    public static class LogViewHolder extends RecyclerView.ViewHolder {

        TextView date, id, name, category, company, problemDes;

        public LogViewHolder(@NonNull View itemView) {
            super(itemView);

            date = itemView.findViewById(R.id.date);
            id = itemView.findViewById(R.id.id);
            name = itemView.findViewById(R.id.name);
//            category = itemView.findViewById(R.id.cancel);
            company = itemView.findViewById(R.id.company1);
            problemDes = itemView.findViewById(R.id.problem_des);

        }
    }
}
