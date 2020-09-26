package com.Aahan.wefix.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.Aahan.wefix.Api.RetrofitClient;
import com.Aahan.wefix.ui.MessageActivity;
import com.Aahan.wefix.R;
import com.Aahan.wefix.model.Category;
import com.Aahan.wefix.model.CategoryResponse;
import com.Aahan.wefix.model.Service;
import com.Aahan.wefix.model.Service1Response;
import com.Aahan.wefix.model.ServiceResponse;
import com.Aahan.wefix.storage.SharedPrefManager;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddLogFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private List<Service> serviceList;
    private List<Category> categoryList;
    private ArrayAdapter<String> adapter;

    private int service_id;
    private int category_id;
    private int user_id;

    private ProgressDialog progressBar;
    private ProgressDialog progressBar1;
    private String txt_category1, txt_service;
    private List<Service> s;

    private Spinner category1, service1;
    private EditText name, address, zip_code, phone_number, email_id, problem_des, company;

    SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_log, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mSwipeRefreshLayout = view.findViewById(R.id.container);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        //spinner
//        log_type = view.findViewById(R.id.log_type);
        company = view.findViewById(R.id.company);
        category1 = view.findViewById(R.id.category1);
        service1 = view.findViewById(R.id.service);

        //textView
        address = view.findViewById(R.id.address);
        name = view.findViewById(R.id.name);
        zip_code = view.findViewById(R.id.zip_code);
        phone_number = view.findViewById(R.id.phone_number);
        email_id = view.findViewById(R.id.email_id);
        problem_des = view.findViewById(R.id.problem_des);

        Button add = view.findViewById(R.id.add);

        //set all the spinner
        getCategory();
        getService();

//        email_id.setText(SharedPrefManager.getInstance(getActivity()).getDelear().getEmail());
//        email_id.setFocusable(false);

        //get the category from spinner
        category1.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        txt_category1 = parent.getItemAtPosition(position).toString();
                        for (Category category2 : categoryList) {
                            if (category2.getTbl_category_name().equals(txt_category1)) {
                                category_id = category2.getTbl_category_id();
                                ArrayList<String> nameList = new ArrayList<>();
                                nameList.add("Select From Below");
                                adapter = new ArrayAdapter<>(Objects.requireNonNull(getActivity()), R.layout.support_simple_spinner_dropdown_item, nameList);
                                service1.setAdapter(adapter);

                                getService1(category_id);
                                progressBar1 = new ProgressDialog(getActivity());
                                progressBar1.show();
                                progressBar1.setContentView(R.layout.progress_dialog);
                                Objects.requireNonNull(progressBar1.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
//                                Toast.makeText(getActivity(), category_id + " ??", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                }
        );

        //get service from spinner
        service1.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        txt_service = parent.getItemAtPosition(position).toString();
                        if (s != null) {
                            for (Service ser : s) {
                                if (ser.getTbl_services_name().equals(txt_service)) {
                                    service_id = ser.getTbl_services_id();
//                                    Toast.makeText(getActivity(), service_id + " ??", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                }
        );

        //set all the data to database in callLog
        add.setOnClickListener(
                v -> {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
                    builder.setMessage("Are you want Submit the Call Log?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", (dialog, id) -> {
                                progressBar = new ProgressDialog(getActivity());
                                progressBar.show();
                                progressBar.setContentView(R.layout.progress_dialog);
                                Objects.requireNonNull(progressBar.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);

                                String txt_billing_name = name.getText().toString();
                                String txt1_address = address.getText().toString();
                                String txt_zip_code = zip_code.getText().toString();
                                String txt_phone_number = phone_number.getText().toString();
                                String txt_email_id = email_id.getText().toString();
                                double txt_amount = 0;
                                String txt_problem_des = problem_des.getText().toString();
                                String txt_company = company.getText().toString();
                                user_id = SharedPrefManager.getInstance(getActivity()).getDelear().getTblDelearId();
//                                String txt_address = SharedPrefManager.getInstance(getActivity()).getDelear().getAddress();

                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
                                String currentDate = sdf.format(new Date());

                                SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm:ss");
                                String currentTime = sdf1.format(new Date());

                                if (TextUtils.isEmpty(txt1_address)) {
                                    progressBar.dismiss();
                                    address.setFocusable(true);
                                    Toast.makeText(getActivity(), "Address Missing! Enter Your Address", Toast.LENGTH_SHORT).show();
                                } else if (TextUtils.isEmpty(txt_billing_name)) {
                                    progressBar.dismiss();
                                    name.setFocusable(true);
                                    Toast.makeText(getActivity(), "Name Missing! Enter Your Name", Toast.LENGTH_SHORT).show();
                                } else if (TextUtils.isEmpty(txt_zip_code)) {
                                    progressBar.dismiss();
                                    zip_code.setFocusable(true);
                                    Toast.makeText(getActivity(), "Pin Code Missing! Enter Your Address Pin Code", Toast.LENGTH_SHORT).show();
                                } else if (TextUtils.isEmpty(txt_phone_number)) {
                                    progressBar.dismiss();
                                    phone_number.setFocusable(true);
                                    Toast.makeText(getActivity(), "Phone Number Missing! Enter Your Phone Number", Toast.LENGTH_SHORT).show();
                                } else if (TextUtils.isEmpty(txt_email_id)) {
                                    progressBar.dismiss();
                                    email_id.setFocusable(true);
                                    Toast.makeText(getActivity(), "Email Missing! Enter Your Email Address", Toast.LENGTH_SHORT).show();
                                } else if (TextUtils.isEmpty(txt_problem_des)) {
                                    progressBar.dismiss();
                                    problem_des.setFocusable(true);
                                    Toast.makeText(getActivity(), "Enter Your Problem Description", Toast.LENGTH_SHORT).show();
                                } else if (TextUtils.isEmpty(txt_service)) {
                                    progressBar.dismiss();
                                    Toast.makeText(getActivity(), "Select The Service", Toast.LENGTH_SHORT).show();
                                } else if (TextUtils.isEmpty(txt_category1)) {
                                    progressBar.dismiss();
                                    Toast.makeText(getActivity(), "Select The Category", Toast.LENGTH_SHORT).show();
                                } else if (TextUtils.isEmpty(txt_company)) {
                                    progressBar.dismiss();
                                    company.setFocusable(true);
                                    Toast.makeText(getActivity(), "Enter The Company Name", Toast.LENGTH_SHORT).show();
                                } else {
                                    Call<ResponseBody> call = RetrofitClient
                                            .getInstance()
                                            .getApi()
                                            .addCallLog(currentDate, "APP", user_id, txt_billing_name, txt1_address, txt_zip_code, txt_phone_number, txt_email_id, category_id, service_id, 0, txt_company, txt_amount, "POS", txt_problem_des, currentTime, "OPEN", ipAddress());

                                    call.enqueue(
                                            new Callback<ResponseBody>() {
                                                @Override
                                                public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                                                    if (response.isSuccessful()) {
                                                        Toast.makeText(getActivity(), "Successful", Toast.LENGTH_LONG).show();
                                                        Intent intent1 = new Intent(getActivity(), MessageActivity.class);
                                                        intent1.putExtra("string", "Thank you for submit Call Log");
                                                        startActivity(intent1);
                                                        name.setText("");
                                                        address.setText("");
                                                        zip_code.setText("");
                                                        phone_number.setText("");
                                                        problem_des.setText("");
                                                        email_id.setText("");
                                                        company.setText("");
                                                        getCategory();
                                                        getService();

                                                    } else {
                                                        Toast.makeText(getActivity(), "Failed To Add Call Log Something went wrong Try again!", Toast.LENGTH_LONG).show();
                                                    }
                                                    progressBar.dismiss();
                                                }

                                                @Override
                                                public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                                                    progressBar.dismiss();
                                                }
                                            }
                                    );
                                }
                            })
                            .setNegativeButton("No", (dialog, id) -> dialog.cancel());
                    AlertDialog alert = builder.create();
                    alert.show();
                }
        );

    }

    //fetch category from database

    private void getCategory() {

        ArrayList<String> nameList;
        nameList = new ArrayList<>();
        nameList.add("Select From Below");

        Call<CategoryResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .getCategory1();

        call.enqueue(
                new Callback<CategoryResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<CategoryResponse> call, @NonNull Response<CategoryResponse> response) {
                        if (response.isSuccessful()) {
                            assert response.body() != null;
                            categoryList = response.body().getCategory();
                            for (Category category2 : categoryList) {
                                nameList.add(category2.getTbl_category_name());
                            }

                            adapter = new ArrayAdapter<>(Objects.requireNonNull(getActivity()), R.layout.support_simple_spinner_dropdown_item, nameList);
                            category1.setAdapter(adapter);

                        } else {
                            Toast.makeText(getActivity(), "Category Not Load! Refresh The Page", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<CategoryResponse> call, @NonNull Throwable t) {

                    }
                }
        );

    }

    //when category get selected
    private void getService1(int position) {

        service1.clearFocus();
        service1.clearDisappearingChildren();

        Call<Service1Response> call = RetrofitClient
                .getInstance()
                .getApi()
                .getService(position);

        call.enqueue(
                new Callback<Service1Response>() {
                    @Override
                    public void onResponse(@NonNull Call<Service1Response> call, @NonNull Response<Service1Response> response) {
                        if (response.isSuccessful()) {
                            assert response.body() != null;
                            s = response.body().getService();
                            ArrayList<String> nameList = new ArrayList<>();
                            nameList.add("Select From Below");
                            for (Service ser : s) {
                                if (!nameList.contains(ser.getTbl_services_name()))
                                    nameList.add(ser.getTbl_services_name());
                            }
                            adapter = new ArrayAdapter<>(Objects.requireNonNull(getActivity()), R.layout.support_simple_spinner_dropdown_item, nameList);
                            service1.setAdapter(adapter);

                        } else {
                            Toast.makeText(getActivity(), "After Choice Category Something went wrong Try Again!", Toast.LENGTH_LONG).show();
                        }
                        progressBar1.dismiss();
                    }

                    @Override
                    public void onFailure(@NonNull Call<Service1Response> call, @NonNull Throwable t) {
                        Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
                        progressBar1.dismiss();
                    }
                }
        );

    }

    // fetch services name from database
    private void getService() {

        ArrayList<String> nameList;
        nameList = new ArrayList<>();
        nameList.add("Select From Below");

        Call<ServiceResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .getAllServices();

        call.enqueue(
                new Callback<ServiceResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<ServiceResponse> call, @NonNull Response<ServiceResponse> response) {
                        if (response.isSuccessful()) {
                            assert response.body() != null;
                            serviceList = response.body().getService();
                            for (Service ser : serviceList) {
                                if (!nameList.contains(ser.getTbl_services_name()))
                                    nameList.add(ser.getTbl_services_name());
                            }
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(Objects.requireNonNull(getActivity()), R.layout.support_simple_spinner_dropdown_item, nameList);
                            service1.setAdapter(adapter);
                        } else {
                            Toast.makeText(getActivity(), "All Service No Loaded!Something Went Wrong Try Again", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ServiceResponse> call, @NonNull Throwable t) {
                        Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
        );
    }

    public static String ipAddress() {
        try {
            for (final Enumeration<NetworkInterface> enumerationNetworkInterface = NetworkInterface.getNetworkInterfaces(); enumerationNetworkInterface.hasMoreElements(); ) {
                final NetworkInterface networkInterface = enumerationNetworkInterface.nextElement();
                for (Enumeration<InetAddress> enumerationInetAddress = networkInterface.getInetAddresses(); enumerationInetAddress.hasMoreElements(); ) {
                    final InetAddress inetAddress = enumerationInetAddress.nextElement();
                    final String ipAddress = inetAddress.getHostAddress();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        return ipAddress;
                    }
                }
            }
            return null;
        } catch (final Exception e) {
//            LogHelper.wtf(null, e);
            return null;
        }
    }

    @Override
    public void onRefresh() {
        getCategory();
        getService();
        if (serviceList != null) {
            Toast.makeText(getActivity(), "Refresh", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(() -> mSwipeRefreshLayout.setRefreshing(false), 1000);
        }
    }

}