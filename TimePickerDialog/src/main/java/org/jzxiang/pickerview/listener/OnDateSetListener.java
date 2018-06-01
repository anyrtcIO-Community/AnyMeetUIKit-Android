package org.jzxiang.pickerview.listener;

import org.jzxiang.pickerview.TimePickerDialog;

/**
 * Created by jzxiang on 16/4/20.
 */
public interface OnDateSetListener {

    void onDateSet(TimePickerDialog timePickerView, long millseconds);
    void OnDissmiss();
}
