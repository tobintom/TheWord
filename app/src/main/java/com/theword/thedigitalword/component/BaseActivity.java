package com.theword.thedigitalword.component;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.theword.thedigitalword.HomeActivity;
import com.theword.thedigitalword.R;

public abstract class BaseActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    protected BottomNavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());

        navigationView = (BottomNavigationView) findViewById(R.id.bottomNav);
        navigationView.setOnNavigationItemSelectedListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateNavigationBarState();
    }

    // Remove inter-activity transition to avoid screen tossing on tapping bottom navigation items
    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        navigationView.postDelayed(() -> {
            int itemId = item.getItemId();
            if (itemId == R.id.action_today) {
                //startActivity(new Intent(this, HomeActivity.class));
            } else if (itemId == R.id.action_read) {
                startActivity(new Intent(this, HomeActivity.class));
            } else if (itemId == R.id.action_today) {
                //startActivity(new Intent(this, NotificationsActivity.class));
            } else if (itemId == R.id.action_me) {
               // startActivity(new Intent(this, MoreActivity.class));
            }else if (itemId==R.id.action_search){
                // startActivity(new Intent(this, MoreActivity.class));
            }else if( itemId==R.id.action_more){
                // startActivity(new Intent(this, MoreActivity.class));
            }
            finish();
        }, 300);
        return true;
    }

    private void updateNavigationBarState() {
        int actionId = getNavigationMenuItemId();
        selectBottomNavigationBarItem(actionId);
    }

    void selectBottomNavigationBarItem(int itemId) {
        MenuItem item = navigationView.getMenu().findItem(itemId);
        item.setChecked(true);
    }

    public abstract int getContentViewId(); // this is to return which layout(activity) needs to display when clicked on tabs.

    public abstract int getNavigationMenuItemId();//Which menu item selected and change the state of that menu item
}
