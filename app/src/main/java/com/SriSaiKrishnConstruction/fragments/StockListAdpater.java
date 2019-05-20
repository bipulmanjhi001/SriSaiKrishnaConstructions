package com.SriSaiKrishnConstruction.fragments;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.SriSaiKrishnConstruction.R;
import com.SriSaiKrishnConstruction.model.Stockist;

import java.util.ArrayList;

class StockListAdpater extends BaseAdapter {
    private Context mContext;
    ArrayList<Stockist> mylist = new ArrayList<>();

    public StockListAdpater(ArrayList<Stockist> itemArray, Context mContext) {
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
        private TextView id,stockproduct_name, total,out,stock_remaining;

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

                convertView = inflator.inflate(R.layout.stock_list, null);
                view.id = (TextView) convertView.findViewById(R.id.stock_id);
                view.stockproduct_name = (TextView) convertView.findViewById(R.id.stockproduct_name);
                view.total = (TextView) convertView.findViewById(R.id.total);
                view.out = (TextView) convertView.findViewById(R.id.out);
                view.stock_remaining = (TextView) convertView.findViewById(R.id.stock_remaining);


                convertView.setTag(view);
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        } else {
            view = (ViewHolder) convertView.getTag();
        }
        try {
            view.id.setTag(position);
            view.stockproduct_name.setText(mylist.get(position).getProduct());
            view.total.setText(mylist.get(position).getTotal());
            view.out.setText(mylist.get(position).getOut());
            view.stock_remaining.setText(mylist.get(position).getRemarks());

        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        return convertView;
    }
}
