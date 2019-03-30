package project.ium.tweb.ium_tweb_android.callbacks;

import android.support.v7.widget.RecyclerView;

public interface SwipeCallback {
    void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position);
}
