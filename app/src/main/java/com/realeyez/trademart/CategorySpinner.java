package com.realeyez.trademart;

import android.view.View;
import android.widget.AdapterView;

public class CategorySpinner extends CreateJobActivity implements AdapterView.OnItemSelectedListener {

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        parent.getItemAtPosition(pos);
    }

    public void onNothingSelected(AdapterView<?> parent) {
    }
}
