package uk.co.dmott.mysupershopper2.helper;

import android.support.v7.widget.RecyclerView;

/**
 * Created by david on 23/03/18.
 */

public interface OnStartDragListener {


    /**
     * Called when a view is requesting a start of a drag.
     *
     * @param viewHolder The holder of the view to drag.
     */
    void onStartDrag(RecyclerView.ViewHolder viewHolder);
}
