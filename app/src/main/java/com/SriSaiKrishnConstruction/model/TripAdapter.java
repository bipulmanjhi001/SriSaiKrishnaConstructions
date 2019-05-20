package com.SriSaiKrishnConstruction.model;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.SriSaiKrishnConstruction.R;
import java.util.ArrayList;

public class TripAdapter extends BaseAdapter {
    private Context mContext;
    ArrayList<TripList> mylist = new ArrayList<>();

    public TripAdapter(ArrayList<TripList> itemArray, Context mContext) {
        super();
        this.mContext = mContext;
        mylist = itemArray;
    }

    @Override
    public int getCount() {
        return mylist.size();
    }

    @Override
    public String getItem(int position) {
        return mylist.get(position).toString();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class ViewHolder {
        private TextView trip_total, trip_id, tript_date, trip_time, trip_vehicle,trip_owner;

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        final ViewHolder view;
        LayoutInflater inflator = null;
        if (convertView == null) {
            view = new ViewHolder();
            try {
                inflator = ((Activity) mContext).getLayoutInflater();

                convertView = inflator.inflate(R.layout.trip_list, null);
                view.trip_id = (TextView) convertView.findViewById(R.id.trip_id);
                view.tript_date = (TextView) convertView.findViewById(R.id.tript_date);
                view.trip_time = (TextView) convertView.findViewById(R.id.trip_time);
                view.trip_vehicle = (TextView) convertView.findViewById(R.id.trip_vehicle);
                view.trip_owner = (TextView) convertView.findViewById(R.id.trip_owner);
                view.trip_total = (TextView) convertView.findViewById(R.id.trip_total);

                convertView.setTag(view);
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        } else {
            view = (ViewHolder) convertView.getTag();
        }
        try {
            view.trip_id.setTag(position);
            view.trip_id.setText(mylist.get(position).getId());
            view.tript_date.setText(mylist.get(position).getDate());
            view.trip_time.setText(mylist.get(position).getTime());
            view.trip_vehicle.setText(mylist.get(position).getVehicle());
            view.trip_owner.setText(mylist.get(position).getOwner());
            view.trip_total.setText(mylist.get(position).getTrip());

        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        return convertView;
    }
}
