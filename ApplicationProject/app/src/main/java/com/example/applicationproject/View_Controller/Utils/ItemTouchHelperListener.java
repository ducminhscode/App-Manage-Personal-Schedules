package com.example.applicationproject.View_Controller.Utils;

import androidx.recyclerview.widget.RecyclerView;

public interface ItemTouchHelperListener {
    void onSwipe(RecyclerView.ViewHolder viewHolder, int direction, int position);
}
