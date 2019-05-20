package com.SriSaiKrishnConstruction.main;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.SriSaiKrishnConstruction.R;
import com.SriSaiKrishnConstruction.api.URL;
import com.SriSaiKrishnConstruction.model.Sites;
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

public class SiteActivity extends AppCompatActivity {

    CalendarView calendar_issue;
    TextView issue_datetext,logout;
    EditText jcb_start,jcb_close,total_working_hour,total_chain,vehicless,owner;
    Button vehicle_name,submit_stock;
    ListView material_list;
    String token;
    String issue_datetexts,jcb_starts,jcb_closes,total_working_hours,total_chains,vehiclesss,owners;
    ArrayList<Sites> sitess;
    String getids,data="";
    ListView Type;
    MaterialAdapter adapter;
    ArrayList vechile_ids = new ArrayList();
    ArrayList vechile_names= new ArrayList();
    ArrayList vechile_owner = new ArrayList();
    ProgressBar loader;
    private static final String SHARED_PREF_NAME = "SriSaiKrishnapref";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_site);

        SharedPreferences sp = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        token = sp.getString("keyid", "");

        Vehicle();

        calendar_issue=(CalendarView) findViewById(R.id.calendar_issue);
        issue_datetext=(TextView)findViewById(R.id.issue_datetext);
        jcb_close=(EditText)findViewById(R.id.jcb_close);
        jcb_start=(EditText)findViewById(R.id.jcb_start);
        loader=(ProgressBar)findViewById(R.id.loader);
        sitess=new ArrayList<Sites>();

        total_working_hour=(EditText)findViewById(R.id.total_working_hour);
        total_chain=(EditText)findViewById(R.id.total_chain);
        vehicless=(EditText)findViewById(R.id.vehicless);

        vehicle_name=(Button) findViewById(R.id.vehicle_name);
        vehicle_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showVehicle(v);
            }
        });

        logout=(TextView)findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = getSharedPreferences("SriSaiKrishnapref", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.apply();
                Intent intent=new Intent(getApplicationContext(),Login.class);
                startActivity(intent);
                finish();

            }
        });
        owner=(EditText)findViewById(R.id.owner);
        material_list=(ListView)findViewById(R.id.material_list);
        material_list.setDivider(null);
        LayoutInflater myinflater = getLayoutInflater();
        ViewGroup myHeader = (ViewGroup) myinflater.inflate(R.layout.material_header, material_list, false);
        material_list.addHeaderView(myHeader, null, false);
        submit_stock=(Button)findViewById(R.id.submit_stock);
        submit_stock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptSites();
            }
        });
        calendar_issue.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {

                String date=String.valueOf(dayOfMonth)+"/"+String.valueOf(month+1)+"/"+String.valueOf(year);
                issue_datetext.setText(date);

            }
        });

    }
    private void attemptSites() {
        issue_datetexts = issue_datetext.getText().toString();
        jcb_starts = jcb_start.getText().toString();
        jcb_closes = jcb_close.getText().toString();
        total_working_hours = total_working_hour.getText().toString();
        total_chains = total_chain.getText().toString();
        vehiclesss = vehicless.getText().toString();
        owners = owner.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(issue_datetexts)) {
            issue_datetext.setError(getString(R.string.error_field_required));
            focusView = issue_datetext;
            cancel = true;
        }
        if (TextUtils.isEmpty(jcb_starts)) {
            jcb_start.setError(getString(R.string.error_field_required));
            focusView = jcb_start;
            cancel = true;
        }
        if (TextUtils.isEmpty(jcb_closes)) {
            jcb_close.setError(getString(R.string.error_field_required));
            focusView = jcb_close;
            cancel = true;
        }
        if (TextUtils.isEmpty(total_working_hours)) {
            total_working_hour.setError(getString(R.string.error_field_required));
            focusView = total_working_hour;
            cancel = true;
        }
        if (TextUtils.isEmpty(total_chains)) {
            total_chain.setError(getString(R.string.error_field_required));
            focusView = total_chain;
            cancel = true;
        }
        if (TextUtils.isEmpty(vehiclesss)) {
            vehicless.setError(getString(R.string.error_field_required));
            focusView = vehicless;
            cancel = true;
        }
        if (TextUtils.isEmpty(owners)) {
            owner.setError(getString(R.string.error_field_required));
            focusView = owner;
            cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();

        }else {
            Submit_Data();
        }
    }
    private void showVehicle(View view) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.type_list_dialog);
        Type= (ListView) dialog.findViewById(R.id.List);
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1, vechile_names);
        Type.setAdapter(adapter);
        Type.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                vehicless.setText(vechile_names.get(position).toString());
                owner.setText(vechile_owner.get(position).toString());
                getids=vechile_ids.get(position).toString();
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    public void Vehicle(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL.URL_VEHICLES,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if(obj.getBoolean("status")) {

                                JSONArray userJson = obj.getJSONArray("vehicles");
                                for (int i = 0; i < userJson.length(); i++) {

                                    JSONObject itemslist = userJson.getJSONObject(i);
                                    String item_id = itemslist.getString("id");
                                    vechile_ids.add(item_id);
                                    String vehicle_no = itemslist.getString("vehicle_no");
                                    vechile_names.add(vehicle_no);
                                    String owner = itemslist.getString("owner");
                                    vechile_owner.add(owner);
                                }
                            }else {
                                Toast.makeText(SiteActivity.this,"No vehicle added",Toast.LENGTH_SHORT).show();
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
        VolleySingleton.getInstance(SiteActivity.this).addToRequestQueue(stringRequest);
        CallList();
    }

    public class MaterialAdapter extends BaseAdapter {
        private Context mContext;
        ArrayList<Sites> mylists = new ArrayList<>();
        String valueList2;
        JSONObject jObjectData;

        public MaterialAdapter(ArrayList<Sites> itemArray, Context mContext) {
            super();
            this.mContext = mContext;
            mylists = itemArray;
        }
        @Override
        public int getCount() {
            return mylists.size();
        }

        @Override
        public String getItem(int position) {
            return mylists.get(position).toString();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public class ViewHolder {
            public EditText fitting_num;
            public CheckBox tick;
            public TextView get_id,get_remain;
        }
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            ViewHolder view = null;
            LayoutInflater inflator = null;

            if (convertView == null) {
                view = new ViewHolder();
                try{
                    inflator=((Activity) mContext).getLayoutInflater();
                    convertView = inflator.inflate(R.layout.fitting_list, null);
                    view.fitting_num = (EditText) convertView.findViewById(R.id.fitting_number);
                    view.get_id = (TextView) convertView.findViewById(R.id.get_id);
                    view.get_remain = (TextView) convertView.findViewById(R.id.get_remain);
                    view.tick = (CheckBox) convertView.findViewById(R.id.fitting_checks);
                    view.tick.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            int getPosition = (Integer) buttonView.getTag();
                            mylists.get(getPosition).setChecked(buttonView.isChecked());
                            if (isChecked) {
                                try {
                                    jObjectData = new JSONObject();
                                    jObjectData.put("id", mylists.get(position).getProduct_id());
                                    jObjectData.put("value", valueList2);
                                } catch (Exception e) {
                                }
                                if (data.length() > 5) {
                                    data = data.concat(jObjectData.toString());
                                } else {
                                    data = jObjectData.toString();
                                }
                            }
                        }
                    });
                    convertView.setTag(view);
                }catch (NullPointerException e){
                    e.printStackTrace();
                }
            } else {
                view = (ViewHolder) convertView.getTag();
            }
            try{
                view.tick.setTag(position);
                view.tick.setText(mylists.get(position).getProduct_name());
                view.get_id.setText(mylists.get(position).getProduct_id());
                view.get_remain.setText(mylists.get(position).getRemaining());
                view.tick.setChecked(mylists.get(position).isChecked());
            }catch (NullPointerException e){
                e.printStackTrace();
            }
            view.fitting_num.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    valueList2 = s.toString();
                }
            });
            return convertView;
        }
    }
    public void CallList(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL.URL_SITEPRODUCTS,
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
                                    String remaining = itemslist.getString("remaining");

                                    Sites sites = new Sites(product_id, product_name,remaining,false);
                                    sitess.add(sites);
                                }

                            }else {
                                Toast.makeText(getApplicationContext(),obj.getString("message"),Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            loader.setVisibility(View.GONE);
                            adapter = new MaterialAdapter(sitess, SiteActivity.this);
                            material_list.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                            ListUtils.setDynamicHeight(material_list);
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
        VolleySingleton.getInstance(SiteActivity.this).addToRequestQueue(stringRequest);
    }

    public static class ListUtils {
        public static void setDynamicHeight(ListView mListView) {
            ListAdapter mListAdapter = mListView.getAdapter();
            if (mListAdapter == null) {
                return;
            }
            int height = 0;
            int desiredWidth = View.MeasureSpec.makeMeasureSpec(mListView.getWidth(), View.MeasureSpec.UNSPECIFIED);
            for (int i = 0; i < mListAdapter.getCount(); i++) {
                View listItem = mListAdapter.getView(i, null, mListView);
                listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
                height += listItem.getMeasuredHeight();
            }
            ViewGroup.LayoutParams params = mListView.getLayoutParams();
            params.height = height + (mListView.getDividerHeight() * (mListAdapter.getCount() - 1));
            mListView.setLayoutParams(params);
            mListView.requestLayout();
        }
    }

    public void Submit_Data() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL.URL_SITEENTRY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if(obj.getBoolean("status")) {

                                Toast.makeText(SiteActivity.this, obj.getString("message"),Toast.LENGTH_SHORT).show();
                                issue_datetext.setText("");
                                total_working_hour.setText("");
                                jcb_start.setText("");
                                jcb_close.setText("");
                                total_chain.setText("");
                                owner.setText("");
                                vehicless.setText("");
                                data="";

                            }else {
                                Toast.makeText(SiteActivity.this, "Error",Toast.LENGTH_SHORT).show();
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

                params.put("token", token);
                params.put("jcb_start", jcb_starts);
                params.put("jcb_end", jcb_closes);
                params.put("hours", total_working_hours);
                params.put("total_chain", total_chains);
                params.put("vehicle_id", getids);
                params.put("owner", owners);
                params.put("used_products", data);

                return params;
            }
        };
        VolleySingleton.getInstance(SiteActivity.this).addToRequestQueue(stringRequest);
    }

}
