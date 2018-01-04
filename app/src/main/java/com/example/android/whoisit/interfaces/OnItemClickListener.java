package com.example.android.whoisit.interfaces;

import android.view.View;

/**
 * Created by lottejespers on 29/12/17.
 */

public interface OnItemClickListener {
    void onItemClick(View view, int position);
    void onLongClick(View view, int position);
}