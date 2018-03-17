package com.chateau86.xmovingmap.Activities.search;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chateau86.xmovingmap.R;
import com.chateau86.xmovingmap.model.Navaid;

import java.util.List;

/**
 * Created by chateau86 on 16-Mar-18.
 */

public class NavaidAdapter extends RecyclerView.Adapter<NavaidAdapter.ViewHolder> {
    private List<Navaid> mNavaidList;
    private double lat;
    private double lon;
    private static final double EARTH_RAD_NMI = 3440.069;
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTextView;

        public ViewHolder(TextView v) {
            super(v);
            mTextView = v;
        }
    }
    public NavaidAdapter(List<Navaid> list_in) {
        mNavaidList = list_in;
        lat = 0;
        lon = 0;
    }

    public void updateData(List<Navaid> list_in, double lat, double lon){
        mNavaidList = list_in;
        this.lat = lat;
        this.lon = lon;
        this.notifyDataSetChanged();
    }
    // Create new views (invoked by the layout manager)
    @Override
    public NavaidAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
        // create a new view
        TextView v = (TextView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.navaid_list_item_view, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;

    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        Navaid n = mNavaidList.get(position);
        double dist = EARTH_RAD_NMI*Math.acos(
                    (n.sin_lat*Math.sin(Math.toRadians(lat)))
                    + (n.cos_lat*Math.cos(Math.toRadians(lat))*Math.cos(Math.toRadians(n.lon)-Math.toRadians(lon))) );
        holder.mTextView.setText(String.format("%1.2f", dist)+" nmi: " + n.toString());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mNavaidList.size();
    }

}