package com.Aahan.wefix.storage;

import android.content.Context;
import android.content.SharedPreferences;

import com.Aahan.wefix.model.Delear;

public class SharedPrefManager {

    private static final String SHARED_PREF_NAME = "my_shared_preff";

    private static SharedPrefManager mInstance;
    private Context mCtx;

    private SharedPrefManager(Context mCtx) {
        this.mCtx = mCtx;
    }

    public static synchronized SharedPrefManager getInstance(Context mCtx) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(mCtx);
        }
        return mInstance;
    }

    public void saveDelear(Delear delear) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt("tbl_delear_id", delear.getTblDelearId());
        editor.putString("delear_name", delear.getDelearName());
        editor.putString("address", delear.getAddress());
        editor.putString("pin", delear.getPin());
        editor.putString("contact1", delear.getContact1());
        editor.putString("contact2", delear.getContact2());
        editor.putString("panno", delear.getPanno());
        editor.putString("gstin", delear.getGstin());
        editor.putString("email", delear.getEmail());
        editor.putString("website", delear.getWebsite());
        editor.putString("status", delear.getStatus());
        editor.putString("username", delear.getUsername());
        editor.putString("password", delear.getPassword());
        editor.putString("plus_member", delear.getPlusMunber());

        editor.apply();
    }

    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);

        return sharedPreferences.getInt("tbl_delear_id", -1) != -1;
    }

    public Delear getDelear() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);

        return new Delear(

                sharedPreferences.getInt("tbl_delear_id", -1),
                sharedPreferences.getString("delear_name", null),
                sharedPreferences.getString("address", null),
                sharedPreferences.getString("pin", null),
                sharedPreferences.getString("contact1", null),
                sharedPreferences.getString("contact2", null),
                sharedPreferences.getString("panno", null),
                sharedPreferences.getString("gstin", null),
                sharedPreferences.getString("email", null),
                sharedPreferences.getString("website", null),
                sharedPreferences.getString("status", null),
                sharedPreferences.getString("username", null),
                sharedPreferences.getString("password", null),
                sharedPreferences.getString("plus_member", null)

        );
    }

    public void clear() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.clear();
        editor.apply();

    }

}
