package com.SriSaiKrishnConstruction.fragments;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import com.SriSaiKrishnConstruction.R;
import com.SriSaiKrishnConstruction.api.URL;
import com.SriSaiKrishnConstruction.model.Stockist;
import com.SriSaiKrishnConstruction.pref.VolleySingleton;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import static android.content.Context.MODE_PRIVATE;

public class Home extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    ProgressBar pr_at_list;
    String token;
    StockListAdpater adapter;
    ListView expense_list;
    ArrayList<Stockist> stockists;
    private static final String SHARED_PREF_NAME = "SriSaiKrishnapref";

    FloatingActionButton floatingActionButton;

    public Home() {
    }

    public static Home newInstance(String param1, String param2) {
        Home fragment = new Home();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_form, container, false);

        SharedPreferences sp = getActivity().getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        token = sp.getString("keyid", "");

        floatingActionButton=(FloatingActionButton)view.findViewById(R.id.view_salary_refresh);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stockists.clear();
                adapter.notifyDataSetChanged();
                CallList();
                ObjectAnimator.ofFloat(floatingActionButton, "rotation", 0f, 360f).setDuration(800).start();
            }
        });

        expense_list = (ListView) view.findViewById(R.id.view_salary_list);
        expense_list.setDivider(null);
        LayoutInflater myinflater = getLayoutInflater();
        ViewGroup myHeader = (ViewGroup) myinflater.inflate(R.layout.stock_header, expense_list, false);
        expense_list.addHeaderView(myHeader, null, false);
        pr_at_list = (ProgressBar) view.findViewById(R.id.view_salary_list_pro);
        stockists=new ArrayList<Stockist>();

        CallList();
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

    public void CallList(){
        pr_at_list.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL.URL_STOCKLIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (obj.getBoolean("status")) {
                            JSONArray userJson = obj.getJSONArray("products");

                            for(int i=0; i<userJson.length(); i++) {

                                JSONObject itemslist = userJson.getJSONObject(i);
                                String product_id = itemslist.getString("product_id");
                                String product_name = itemslist.getString("product_name");
                                String total = itemslist.getString("total");
                                String out = itemslist.getString("out");
                                String remaining = itemslist.getString("remaining");

                                Stockist expenseList = new Stockist(product_id, product_name, total, out, remaining);
                                stockists.add(expenseList);
                            }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            pr_at_list.setVisibility(View.GONE);
                            adapter = new StockListAdpater(stockists, getActivity());
                            expense_list.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        }catch (NullPointerException e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("token", token);

                return params;
            }
        };
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
