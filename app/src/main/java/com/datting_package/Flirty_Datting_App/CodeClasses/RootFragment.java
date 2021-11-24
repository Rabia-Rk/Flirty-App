package com.datting_package.Flirty_Datting_App.CodeClasses;

import androidx.fragment.app.Fragment;

/**
 * Created by datting_package on 3/30/2018.
 */

public class RootFragment extends Fragment implements OnBackPressListener {

    @Override
    public boolean onBackPressed() {
        return new BackPressImplimentation(this).onBackPressed();
    }
}