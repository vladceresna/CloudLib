package com.rula.cloudlib;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class PrimaryActivity extends AppCompatActivity {

    BottomNavigationView bnv;
    MaterialToolbar topAppBar;

    MyPageFragment myPageFragment;
    ServicesPageFragment servicesPageFragment;
    PublicPageFragment publicPageFragment;
    FragmentTransaction fragmentTransaction;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_primary);

        topAppBar = findViewById(R.id.topAppBar);
        bnv = findViewById(R.id.bottom_navigation);

        myPageFragment = new MyPageFragment();
        servicesPageFragment = new ServicesPageFragment();
        publicPageFragment = new PublicPageFragment();
        setNewFragment(myPageFragment);
        bnv.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.page_1:
                        setNewFragment(myPageFragment);
                        break;
                    case R.id.page_2:
                        setNewFragment(servicesPageFragment);
                        break;
                    case R.id.page_3:
                        setNewFragment(publicPageFragment);
                        break;
                }
                return true;
            }
        });
        topAppBar.setOnMenuItemClickListener(new MaterialToolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch(item.getItemId()){
                    case R.id.pag_1:
                        startActivity(new Intent(PrimaryActivity.this, ProfileActivity.class));
                        break;
                    case R.id.pag_2:
                        break;
                    case R.id.pag_3:
                        break;
                }
                return false;
            }
        });

    }
    public void setNewFragment(Fragment fragment){
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}