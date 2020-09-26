package com.Aahan.wefix.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.Aahan.wefix.Api.RetrofitClient;
import com.Aahan.wefix.ui.LogDetailsActivity;
import com.Aahan.wefix.R;
import com.Aahan.wefix.model.Category;
import com.Aahan.wefix.model.Category1Response;
import com.Aahan.wefix.model.Logs;
import com.Aahan.wefix.model.Service2Response;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LogHistoryAdapter extends RecyclerView.Adapter<LogHistoryAdapter.LogViewHolder> implements Filterable {

    private Context mContext;
    private List<Logs> logsList;
    private List<Logs> logsListFull;

    public LogHistoryAdapter(Context mContext, List<Logs> logsList) {
        this.mContext = mContext;
        this.logsList = logsList;
        logsListFull = new ArrayList<>(logsList);
    }

    @NonNull
    @Override
    public LogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.log_history, parent, false);
        return new LogViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull LogViewHolder holder, int position) {

        Logs logs = logsList.get(position);

        holder.id.setText(String.valueOf(logs.getCallLogId()));
        holder.date.setText(logs.getCallLogDate());
//        holder.charge.setText(String.valueOf(logs.getAmount()));
        holder.company.setText(logs.getProductCompany());

        Call<Service2Response> call1 = RetrofitClient
                .getInstance()
                .getApi()
                .getServiceByID(logs.getRefServiceId());

        call1.enqueue(
                new Callback<Service2Response>() {
                    @Override
                    public void onResponse(@NonNull Call<Service2Response> call, @NonNull Response<Service2Response> response) {
                        if (response.isSuccessful()) {
                            assert response.body() != null;
                            holder.service.setText(response.body().getService().getTbl_services_name());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Service2Response> call, @NonNull Throwable t) {

                    }
                }
        );

        int tbl_category_id = logs.getRefCatId();

        Call<Category1Response> call = RetrofitClient
                .getInstance()
                .getApi()
                .getCategoryByID(tbl_category_id);

        call.enqueue(
                new Callback<Category1Response>() {
                    @Override
                    public void onResponse(@NonNull Call<Category1Response> call, @NonNull Response<Category1Response> response) {
                        if (response.isSuccessful()) {
                            assert response.body() != null;
                            Category category = response.body().getCategory();
                            holder.name.setText(category.getTbl_category_name());
                            holder.name1.setText(category.getTbl_category_name());
                            Picasso.get().load("http://weservice.in/product/" + category.getTbl_category_image()).into(holder.image);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Category1Response> call, @NonNull Throwable t) {
                        Toast.makeText(mContext, t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
        );

        holder.itemView.setOnClickListener(
                v -> {
                    Intent intent = new Intent(mContext, LogDetailsActivity.class);
                    intent.putExtra("logs", logsList.get(position));
                    mContext.startActivity(intent);
                }
        );
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Logs> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(logsListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Logs item : logsListFull) {
                    if (item.getClientName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                    if (String.valueOf(item.getCallLogId()).startsWith(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            logsList.clear();
            logsList.addAll((ArrayList) results.values);
            notifyDataSetChanged();
        }
    };

    @Override
    public int getItemCount() {
        return logsList.size();
    }

    public static class LogViewHolder extends RecyclerView.ViewHolder {

        TextView date, id, name;
        ImageView image;
        TextView name1, company, service;

        public LogViewHolder(@NonNull View itemView) {
            super(itemView);

            date = itemView.findViewById(R.id.date);
            id = itemView.findViewById(R.id.id);
            name = itemView.findViewById(R.id.name);
            image = itemView.findViewById(R.id.image);
            name1 = itemView.findViewById(R.id.name1);
            company = itemView.findViewById(R.id.company);
            service = itemView.findViewById(R.id.service);

        }
    }

}
