package com.mp.hw2;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class SPManager {

    private Context context;
    SharedPreferences.Editor editor;
    private SharedPreferences preferences;

    public SPManager(Context context2) {
        context = context2;
        preferences = context2.getSharedPreferences("mypref", 0);
        editor = preferences.edit();
    }

    public long putL(String str, long j) {
        preferences= PreferenceManager.getDefaultSharedPreferences(context);
        editor = preferences.edit();
        editor.putLong(str, j);
        editor.commit();
        return 0;
    }

    public long getLong(String str) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getLong(str, 0);
    }

    public boolean putB(String str, boolean z) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);

        editor = preferences.edit();
        editor.putBoolean(str, z);
        editor.apply();
        return z;
    }

    public boolean getB(String str) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean(str, false);
    }

    public void addS(String str, String str2) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = preferences.edit();
        editor.putString(str, str2);
        editor.apply();
    }

    public String getS(String str) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(str, "");
    }

    public <T> void setList(String str, List<T> list) {
        set(str, new Gson().toJson((Object) list));

    }

    public <T> void setL(String str, List<T> list) {
        set(str, new Gson().toJson((Object) list));
    }

    public void set(String str, String str2) {
        System.out.println("set " + str2);
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = preferences.edit();
        editor.putString(str, str2);
        editor.commit();
    }

    public List<HighModel> getL(String str) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        Gson gson = new Gson();
        String string = preferences.getString(str, "");
        return (List) gson.fromJson(string, new TypeToken<List<HighModel>>() {
        }.getType());
    }
}
