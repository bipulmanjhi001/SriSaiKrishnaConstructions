package com.SriSaiKrishnConstruction.main;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;
import com.SriSaiKrishnConstruction.R;
import com.SriSaiKrishnConstruction.fragments.Home;
import com.SriSaiKrishnConstruction.fragments.Maintain_Stock;
import com.SriSaiKrishnConstruction.fragments.Profile;

public class StockActivity extends AppCompatActivity  {

    Fragment fragment = null;
    TextView title;

    private BottomNavigationView.OnNavigationItemSelectedListener
            mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {

                case R.id.navigation_home:
                    fragment = new Home();
                    loadFragment(fragment);
                    title.setText("Stock");
                    return true;

                case R.id.navigation_dashboard:
                    fragment = new Maintain_Stock();
                    loadFragment(fragment);
                    title.setText("Maintain Stock");
                    return true;

                case R.id.navigation_notifications:
                    fragment = new Profile();
                    loadFragment(fragment);
                    title.setText("Profile");
                    return true;

            }
                return false;
            }
        };

        private void loadFragment(Fragment fragment) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.dashboard, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        title=(TextView)findViewById(R.id.title);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.dashboard, new Home()).commit();
        title.setText("Stock");

    }
}
