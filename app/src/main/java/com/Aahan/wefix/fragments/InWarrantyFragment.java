package com.Aahan.wefix.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.Aahan.wefix.Api.RetrofitClient;
import com.Aahan.wefix.MessageActivity;
import com.Aahan.wefix.R;
import com.Aahan.wefix.model.Company;
import com.Aahan.wefix.model.CompanyResponse;
import com.Aahan.wefix.model.MyResponse;
import com.Aahan.wefix.storage.SharedPrefManager;

import java.io.ByteArrayOutputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

public class InWarrantyFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private static final int IMAGE_REQUEST = 222;
    private int user_id;

    private ProgressDialog progressBar;

    List<Company> companyList;
    ArrayList<String> companyNames;

    Bitmap bitmap;

    int txt_company = 0;
    String txt_image;

    private Button add, cancel, uploadTaxInvoice, upload;
    private ImageView image;
    private Spinner company;
    private EditText address;
    private EditText zip_code;
    private EditText phone_number;
    private EditText email_id;
    private EditText model;
    private EditText serial;
    private EditText problem_des;
    private EditText purchase;
    private EditText name;
    private EditText category;

    SwipeRefreshLayout mSwipeRefreshLayout;

    public InWarrantyFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_in_warranty, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mSwipeRefreshLayout = view.findViewById(R.id.container);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        company = view.findViewById(R.id.company);

        getCompany();

        name = view.findViewById(R.id.name);
        address = view.findViewById(R.id.address);
        zip_code = view.findViewById(R.id.zip_code);
        phone_number = view.findViewById(R.id.phone_number);
        email_id = view.findViewById(R.id.email_id);
        problem_des = view.findViewById(R.id.problem_des);
        model = view.findViewById(R.id.model);
        serial = view.findViewById(R.id.serial);
        purchase = view.findViewById(R.id.purchase);
        category = view.findViewById(R.id.category);

        image = view.findViewById(R.id.image);

        add = view.findViewById(R.id.add);
        cancel = view.findViewById(R.id.cancel);
        uploadTaxInvoice = view.findViewById(R.id.upload_image);
        upload = view.findViewById(R.id.upload);

        cancel.setVisibility(View.GONE);
        upload.setVisibility(View.GONE);
        image.setVisibility(View.GONE);
        uploadTaxInvoice.setVisibility(View.VISIBLE);

        uploadTaxInvoice.setOnClickListener(
                v -> {
                    image.setVisibility(View.VISIBLE);
                    getImage();
                    uploadTaxInvoice.setVisibility(View.GONE);
                }
        );

        cancel.setOnClickListener(
                v -> {
                    cancel.setVisibility(View.GONE);
                    upload.setVisibility(View.GONE);
                    uploadTaxInvoice.setVisibility(View.VISIBLE);
                    image.setVisibility(View.GONE);
                }
        );

        upload.setOnClickListener(
                v -> {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
                    builder.setMessage("Are you want upload it?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", (dialog, id) -> uploadImage())
                            .setNegativeButton("No", (dialog, id) -> dialog.cancel());
                    AlertDialog alert = builder.create();
                    alert.show();
                }
        );

//        email_id.setText(SharedPrefManager.getInstance(getActivity()).getDelear().getEmail());
//        email_id.setFocusable(false);

        company.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String a = parent.getItemAtPosition(position).toString();
                        if (companyList != null) {
                            for (Company c : companyList) {
                                if (c.getTblCompanyName().equals(a)) {
                                    txt_company = c.getTblCompanyId();
//                                    Toast.makeText(getActivity(), txt_company + " ??", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                }
        );

        add.setVisibility(View.GONE);

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
                                String txt_model = model.getText().toString();
                                String txt_serial = serial.getText().toString();
                                String txt_purchase = purchase.getText().toString();
                                String txt_category = category.getText().toString();
                                user_id = SharedPrefManager.getInstance(getActivity()).getDelear().getTblDelearId();

                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
                                String currentDate = sdf.format(new Date());

                                SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm:ss");
                                String currentTime = sdf1.format(new Date());

                                if (TextUtils.isEmpty(txt1_address) || TextUtils.isEmpty(txt_billing_name) || TextUtils.isEmpty(txt_zip_code) ||
                                        TextUtils.isEmpty(txt_phone_number) || TextUtils.isEmpty(txt_email_id) || TextUtils.isEmpty(txt_problem_des) ||
                                        TextUtils.isEmpty(txt_model) || TextUtils.isEmpty(txt_serial) || TextUtils.isEmpty(txt_purchase) || txt_company == 0 || TextUtils.isEmpty(txt_image) || TextUtils.isEmpty(txt_category)) {
                                    Toast.makeText(getActivity(), "All Field are required", Toast.LENGTH_LONG).show();
                                    progressBar.dismiss();
                                } else {
                                    Call<MyResponse> call = RetrofitClient
                                            .getInstance()
                                            .getApi()
                                            .addWarrantyCallLog(currentDate, "APP", txt_billing_name, txt1_address, txt_zip_code, txt_phone_number, txt_email_id, txt_company, txt_model, txt_serial, txt_purchase, txt_image, txt_amount, "POS", txt_problem_des, currentTime, "OPEN", ipAddress(), user_id, txt_category);

                                    call.enqueue(
                                            new Callback<MyResponse>() {
                                                @Override
                                                public void onResponse(@NonNull Call<MyResponse> call, @NonNull Response<MyResponse> response) {
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
                                                        name.setText("");
                                                        model.setText("");
                                                        serial.setText("");
                                                        purchase.setText("");
                                                        category.setText("");

                                                    } else {
                                                        Toast.makeText(getActivity(), "Failed To Add Call Log Something went wrong Try again!", Toast.LENGTH_LONG).show();
                                                    }
                                                    progressBar.dismiss();
                                                }

                                                @Override
                                                public void onFailure(@NonNull Call<MyResponse> call, @NonNull Throwable t) {
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

    private void uploadImage() {

        ProgressDialog progressBar1 = new ProgressDialog(getActivity());
        progressBar1.show();
        progressBar1.setContentView(R.layout.progress_dialog);
        Objects.requireNonNull(progressBar1.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);

        String id = SharedPrefManager.getInstance(getActivity()).getDelear().tblDelearId + "a" + new Random().nextInt(20000000);

        txt_image = "myImage" + id + ".jpeg";
        String txt_location = "images/" + txt_image;

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);

        byte[] imageInByte = byteArrayOutputStream.toByteArray();

        String encodedImage = Base64.encodeToString(imageInByte, Base64.DEFAULT);

//        Toast.makeText(getActivity(), encodedImage, Toast.LENGTH_SHORT).show();
        Call<MyResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .uploadImage(txt_image, txt_location, encodedImage);

        call.enqueue(
                new Callback<MyResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<MyResponse> call, @NonNull Response<MyResponse> response) {
                        assert response.body() != null;
                        if (response.isSuccessful()) {
                            if (!response.body().getError()) {
//                                progressBar1.dismiss();
//                                Toast.makeText(getActivity(), "Successfully Uploaded", Toast.LENGTH_SHORT).show();
//                                upload.setVisibility(View.GONE);
//                                cancel.setVisibility(View.GONE);
//                                uploadTaxInvoice.setVisibility(View.GONE);
//                                image.setVisibility(View.VISIBLE);
//                                add.setVisibility(View.VISIBLE);
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<MyResponse> call, @NonNull Throwable t) {
//                        Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );

        upload.setVisibility(View.GONE);
        cancel.setVisibility(View.GONE);
        uploadTaxInvoice.setVisibility(View.GONE);
        image.setVisibility(View.VISIBLE);
        add.setVisibility(View.VISIBLE);
        progressBar1.dismiss();

    }

    private void getImage() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMAGE_REQUEST);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            Uri imageUri = data.getData();

            try {
                bitmap = MediaStore.Images.Media.getBitmap(Objects.requireNonNull(getActivity()).getContentResolver(), imageUri);
                image.setImageBitmap(bitmap);
                upload.setVisibility(View.VISIBLE);
                cancel.setVisibility(View.VISIBLE);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            uploadTaxInvoice.setVisibility(View.VISIBLE);
        }

    }

    private void getCompany() {

        companyNames = new ArrayList<>();
        companyNames.add("Select From Below");

        Call<CompanyResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .getCompany();

        call.enqueue(
                new Callback<CompanyResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<CompanyResponse> call, @NonNull Response<CompanyResponse> response) {
                        if (response.isSuccessful()) {
                            assert response.body() != null;
                            companyList = response.body().getCompany();
                            for (Company c : companyList) {
                                companyNames.add(c.getTblCompanyName());
                            }
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(Objects.requireNonNull(getActivity()), R.layout.support_simple_spinner_dropdown_item, companyNames);
                            company.setAdapter(adapter);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<CompanyResponse> call, @NonNull Throwable t) {

                    }
                }
        );

    }

    @Override
    public void onRefresh() {
        getCompany();
        if (companyList != null) {
            Toast.makeText(getActivity(), "Refresh", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(() -> mSwipeRefreshLayout.setRefreshing(false), 1000);
        }
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
}