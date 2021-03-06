package com.example.gruppe43.idretts_app.application.helper_classes;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

//Idretts-app bachelor oppgave 2017
//Ole-Kristian Steiro, Tasmia Faruque, Gebi Beshir

public class PrefferencesClass {
    private Context ctx;
    public PrefferencesClass(Context ctx) {
        this.ctx = ctx;
    }

    public void saveSharedPrefData(String key, String value) {
        SharedPreferences.Editor editor;
        try {
            editor = ctx.getSharedPreferences("IdrettsAppData", Context.MODE_PRIVATE).edit();
            editor.putString(key, value);
            editor.commit();
        } catch (NullPointerException npe) {
            Log.e("///////SHARED_PREF_SAVE", "isNullpointing");
        }
    }

    public String loadSharedPrefData(String key) {
        String data = "No name defined";
        try {
            SharedPreferences prefs = ctx.getSharedPreferences("IdrettsAppData", Context.MODE_PRIVATE);
            data = prefs.getString(key, "No name defined");
        } catch (NullPointerException npe) {
            Log.e("///////SHARED_PREF_LOAD", "isNullpointing");
        }
        return data;
    }

    public void saveLoggedInUserNameLocally(String firstName, String lastname){
        SharedPreferences.Editor editor;
        try {
            editor = ctx.getSharedPreferences("pname", Context.MODE_PRIVATE).edit();
            editor.putString("fname", firstName);
            editor.putString("lname", lastname);
            editor.commit();
        } catch (NullPointerException npe) {
            Log.e("///////SHARED_PREF_SAVE", "isNullpointing");
        }
    }

    public String[] loadUserName() {
        String[] name = new String[2];
        try {
            SharedPreferences prefs = ctx.getSharedPreferences("pname", Context.MODE_PRIVATE);
            name[0] = prefs.getString("fname", "No name defined");
            name[1] = prefs.getString("lname", "No name defined");
        } catch (NullPointerException npe) {
            Log.e("///////SHARED_PREF_LOAD", "isNullpointing");
        }
        return name;
    }


    public long loadLocallySavedTrainerChildCount() {
        long locallySavedChildCount = 0;
        try {
            SharedPreferences prefs = ctx.getSharedPreferences("trainerNodeNum", Context.MODE_PRIVATE);
            locallySavedChildCount = prefs.getLong("nTrainerPostKey",0);
        }catch (NullPointerException npe){
            Log.e("///////SHARED_PREF_LOAD", "isNullpointing");
        }
        return locallySavedChildCount;
    }

    public void saveUpdateOnTrainerChildCount(long trainerPostNodeCount) {
        SharedPreferences.Editor editor;
        try {
            editor = ctx.getSharedPreferences("trainerNodeNum", Context.MODE_PRIVATE).edit();
            editor.putLong("nTrainerPostKey", trainerPostNodeCount);
            editor.commit();
        } catch (NullPointerException npe) {
            Log.e("///////SHARED_PREF_SAVE", "isNullpointing");
        }
    }
}
