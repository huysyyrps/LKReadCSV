package com.example.lk_readcvs.Util;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.DecimalFormat;

public class MyValueFormatterX extends ValueFormatter {
    DecimalFormat mFormat = new DecimalFormat("0");
    @Override
    public String getPointLabel(Entry entry) {
        return mFormat.format(entry.getY());
    }

    @Override
    public String getAxisLabel(float value, AxisBase axis) {
        return mFormat.format(value);
    }
}
