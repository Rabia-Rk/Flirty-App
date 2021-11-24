package com.datting_package.Flirty_Datting_App.CodeClasses;

import android.view.View;

public interface AdapterClickListener {

    void onItemClick(int postion, Object model, View view);
    void onLongItemClick(int postion, Object model, View view);
}
