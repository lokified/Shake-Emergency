package com.loki.credo.utils;

import android.view.View;
import android.widget.ImageView;

import androidx.cardview.widget.CardView;

import com.loki.credo.models.Contacts;

public interface ContactClickListener {

    void onClick(View cardView, Contacts contacts);
}
