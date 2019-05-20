package com.SriSaiKrishnConstruction.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.SriSaiKrishnConstruction.R;
import com.SriSaiKrishnConstruction.api.URL;
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

public class Expense_Add extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String MY_PREFS_NAME = "SriSaiKrishnapref";

    private String mParam1;
    private String mParam2;
    String token, getexpense_headId;
    ListView expenselist;
    TextView title,issue_datetext;
    Button expense_name,submit_expenses;
    EditText expense_head,persons,amount,remark;
    CalendarView calendar_issue;
    ArrayList expense_ids = new ArrayList();
    ArrayList expense_names = new ArrayList();
    String issue_datetexts,expense_heads,personss,amounts,remarks;

    private OnFragmentInteractionListener mListener;

    public Expense_Add() {
    }

    public static Expense_Add newInstance(String param1, String param2) {
        Expense_Add fragment = new Expense_Add();
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
        View view= inflater.inflate(R.layout.expense_details, container, false);

        SharedPreferences prefs = getContext().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        token = prefs.getString("keyid", null);
        ExpenseList();
        title=(TextView)view.findViewById(R.id.title);
        expense_head=(EditText)view.findViewById(R.id.expense_head);
        persons=(EditText)view.findViewById(R.id.persons);
        amount=(EditText)view.findViewById(R.id.amount);
        remark=(EditText)view.findViewById(R.id.remark);
        expense_name=(Button)view.findViewById(R.id.expense_name);
        submit_expenses=(Button)view.findViewById(R.id.submit_expenses);
        calendar_issue=(CalendarView)view.findViewById(R.id.calendar_issue);
        issue_datetext=(TextView)view.findViewById(R.id.issue_datetext);

        calendar_issue.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                String date=String.valueOf(dayOfMonth)+"/"+String.valueOf(month+1)+"/"+String.valueOf(year);
                issue_datetext.setText(date);
            }
        });

        expense_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showExpense(v);
            }
        });
        submit_expenses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AttemptExpenses();
            }
        });

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
    private void AttemptExpenses() {
        issue_datetexts = issue_datetext.getText().toString();
        expense_heads = expense_head.getText().toString();
        personss = persons.getText().toString();
        amounts = amount.getText().toString();
        remarks = remark.getText().toString();
        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(issue_datetexts)) {
            issue_datetext.setError(getString(R.string.error_field_required));
            focusView = issue_datetext;
            cancel = true;
        }
        if (TextUtils.isEmpty(expense_heads)) {
            expense_head.setError(getString(R.string.error_field_required));
            focusView = expense_head;
            cancel = true;
        }
        if (TextUtils.isEmpty(personss)) {
            persons.setError(getString(R.string.error_field_required));
            focusView = persons;
            cancel = true;
        }
        if (TextUtils.isEmpty(amounts)) {
            amount.setError(getString(R.string.error_field_required));
            focusView = amount;
            cancel = true;
        }
        if (cancel) {

            focusView.requestFocus();

        }else {
            Submit_Data();
        }
    }

    public void ExpenseList(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL.URL_EXPENSEHEADS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (obj.getBoolean("status")) {
                                JSONArray userJson = obj.getJSONArray("expenseheads");

                                for(int i=0; i<userJson.length(); i++) {

                                    JSONObject itemslist = userJson.getJSONObject(i);
                                    String id = itemslist.getString("id");
                                    expense_ids.add(id);
                                    String expense_head = itemslist.getString("expense_head");
                                    expense_names.add(expense_head);
                                    String status = itemslist.getString("status");
                                }
                            }
                        } catch (JSONException e) {
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
    private void showExpense(View view) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.expense_list_dialog);
        expenselist= (ListView) dialog.findViewById(R.id.expense_list);
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_list_item_1, expense_names);
        expenselist.setAdapter(adapter);
        expenselist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                expense_head.setText(expense_names.get(position).toString());
                getexpense_headId=expense_ids.get(position).toString();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

      public void Submit_Data() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL.URL_EXPENSE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if(obj.getBoolean("status")) {

                                Toast.makeText(getActivity(), obj.getString("message"), Toast.LENGTH_SHORT).show();

                                 issue_datetext.setText("");
                                 expense_head.setText("");
                                 getexpense_headId="";
                                 persons.setText("");
                                 amount.setText("");
                                 remark.setText("");

                            }else {
                                Toast.makeText(getActivity(), "Error",Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("token",token);
                params.put("date",issue_datetexts);
                params.put("expense_head_id",getexpense_headId);
                params.put("name",personss);
                params.put("amount",amounts);
                params.put("remarks",remarks);
                return params;
            }
        };
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);

    }
}
