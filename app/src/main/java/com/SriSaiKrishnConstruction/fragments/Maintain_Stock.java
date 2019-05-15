package com.SriSaiKrishnConstruction.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.SriSaiKrishnConstruction.R;
import com.SriSaiKrishnConstruction.model.TabAdapter;

public class Maintain_Stock extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;

    TabAdapter adapters;
    TabLayout tabLayouts;
    ViewPager viewPagers;

    public Maintain_Stock() {
    }

    public static Maintain_Stock newInstance(String param1, String param2) {
        Maintain_Stock fragment = new Maintain_Stock();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.stock_form, container, false);
        viewPagers = (ViewPager) view.findViewById(R.id.stockist);
        tabLayouts = (TabLayout) view.findViewById(R.id.stocktab);
        adapters = new TabAdapter(getActivity().getSupportFragmentManager());

        adapters.addFragment(new StockIn(), "Stock In");
        adapters.addFragment(new StockOut(), "Stock Out");
        viewPagers.setAdapter(adapters);
        tabLayouts.setupWithViewPager(viewPagers);
        return view;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
