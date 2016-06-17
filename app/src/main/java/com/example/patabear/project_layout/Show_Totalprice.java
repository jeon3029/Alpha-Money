package com.example.patabear.project_layout;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Patabear on 2016-06-10.
 */

public class Show_Totalprice extends Fragment {

    public Show_Totalprice() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.show_totalprice, container, false);
        return rootView;
    }
}