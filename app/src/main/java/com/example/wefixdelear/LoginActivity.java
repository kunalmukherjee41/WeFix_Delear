package com.example.wefixdelear;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wefixdelear.Api.RetrofitClient;
import com.example.wefixdelear.model.Delear;
import com.example.wefixdelear.model.DelearResponse;
import com.example.wefixdelear.storage.SharedPrefManager;
import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    Intent intent;

    EditText email, password;
    Button login;
    TextView forgotPassword;
    ProgressDialog progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);
        forgotPassword = findViewById(R.id.forgot_password);

        login.setOnClickListener(
                v -> {
                    progressBar = new ProgressDialog(LoginActivity.this);
                    progressBar.show();
                    progressBar.setContentView(R.layout.progress_dialog);
                    Objects.requireNonNull(progressBar.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);

                    login.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.custom_btn2, null));

                    String txt_email = email.getText().toString();
                    String txt_password = password.getText().toString();

                    if (TextUtils.isEmpty(txt_email)) {
                        Toast.makeText(LoginActivity.this, "Email required!", Toast.LENGTH_SHORT).show();
                        login.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.custom_btn, null));
                        email.setFocusable(true);
                        progressBar.dismiss();
                    } else if (!Patterns.EMAIL_ADDRESS.matcher(txt_email).matches()) {
                        Toast.makeText(LoginActivity.this, "Enter a Valid Email Address!", Toast.LENGTH_SHORT).show();
                        login.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.custom_btn, null));
                        email.setFocusable(true);
                        progressBar.dismiss();
                    } else if (TextUtils.isEmpty(txt_password)) {
                        Toast.makeText(LoginActivity.this, "Password Missing!", Toast.LENGTH_SHORT).show();
                        password.setFocusable(true);
                        login.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.custom_btn, null));
                        progressBar.dismiss();
                    } else {
                        delearLogin(txt_email, txt_password);
                    }
                }
        );
    }

    private void delearLogin(String txt_email, String txt_password) {

        Call<DelearResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .delearLogin(txt_email, txt_password);

        call.enqueue(
                new Callback<DelearResponse>() {
                    @Override
                    public void onResponse(Call<DelearResponse> call, Response<DelearResponse> response) {
                        if (response.isSuccessful()) {
                            assert response.body() != null;
                            if (response.body().getMessage().equals("Login Successful")) {
                                Delear delear = response.body().getDelear();
                                if (delear.getStatus().equals("ACTIVE")) {
                                    SharedPrefManager.getInstance(LoginActivity.this).saveDelear(delear);
                                    if (delear.getPlusMunber().equals("NO")) {
//                                    Toast.makeText(LoginActivity.this, delear.getTblDelearId() + "lbkjkv", Toast.LENGTH_SHORT).show();
                                        intent = new Intent(LoginActivity.this, MainActivity.class);
                                    } else {
                                        intent = new Intent(LoginActivity.this, DisplayActivity.class);
                                    }
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);

                                } else {
                                    Snackbar snackBar = Snackbar.make(findViewById(R.id.id), "You Are No Longer in this Company!", Snackbar.LENGTH_LONG);
                                    snackBar.setAction("Action Message", v -> snackBar.dismiss());
                                    snackBar.show();
                                }
                            } else {
                                Toast.makeText(LoginActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                            email.setText("");
                            password.setText("");
                            login.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.custom_btn, null));
                            progressBar.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<DelearResponse> call, Throwable t) {
                        Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                        login.setBackground(ContextCompat.getDrawable(LoginActivity.this, R.drawable.custom_btn));
                        password.setText("");
                        progressBar.dismiss();
                    }
                }
        );
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            if (SharedPrefManager.getInstance(this).getDelear().getPlusMunber().equals("NO")) {
                intent = new Intent(this, MainActivity.class);
            } else {
                intent = new Intent(this, DisplayActivity.class);
            }
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

    public void ShowHidePass(View view) {

        if (view.getId() == R.id.pass_btn) {
            if (password.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())) {
                ((ImageView) (view)).setImageResource(R.drawable.password_hide_asset);
                //Show Password
                password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            } else {
                ((ImageView) (view)).setImageResource(R.drawable.password_visible_asset);
                //Hide Password
                password.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        }
    }
}