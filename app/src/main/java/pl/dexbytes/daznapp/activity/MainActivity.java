package pl.dexbytes.daznapp.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import javax.inject.Inject;

import pl.dexbytes.daznapp.DaznApplication;
import pl.dexbytes.daznapp.R;
import pl.dexbytes.daznapp.adapter.MainPagerAdapter;
import pl.dexbytes.daznapp.dagger.scope.DaznApiScope;
import pl.dexbytes.daznapp.fragment.EventViewerFragment;
import pl.dexbytes.daznapp.fragment.ScheduleViewerFragment;
import pl.dexbytes.daznapp.fragment.ViewerFragment;
import pl.dexbytes.daznapp.net.DaznApi;

public class MainActivity extends AppCompatActivity {
    private static final int INTERNET_PERMISSION_REQUEST = 1;
    @Inject
    @DaznApiScope
    DaznApi mDaznApi;

    private ViewPager mViewPager;
    private BottomNavigationView mBottomNavigationMenu;
    private MenuItem mPrevMenuItem;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = item -> {
        switch (item.getItemId()) {
            case R.id.navigation_dashboard:
                mViewPager.setCurrentItem(0);
                return true;
            case R.id.navigation_notifications:
                mViewPager.setCurrentItem(1);
                return true;
            default:
                return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((DaznApplication)getApplication()).getNetworkComponent().inject(this);

        mViewPager = findViewById(R.id.view_pager);
        mBottomNavigationMenu = findViewById(R.id.navigation);
        mBottomNavigationMenu.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        setupViewPager();
    }

    private void setupViewPager() {
        if(checkPermissions()){
            MainPagerAdapter adapter = new MainPagerAdapter(getSupportFragmentManager());
            ViewerFragment eventFragment = new EventViewerFragment();
            ViewerFragment scheduleFragment = new ScheduleViewerFragment();

            adapter.addFragment(eventFragment);
            adapter.addFragment(scheduleFragment);

            mViewPager.setAdapter(adapter);
            mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    if(mPrevMenuItem != null){
                        mPrevMenuItem.setChecked(false);
                    } else {
                        mBottomNavigationMenu.getMenu().getItem(0).setChecked(false);
                    }
                    mBottomNavigationMenu.getMenu().getItem(position).setChecked(true);
                    mPrevMenuItem = mBottomNavigationMenu.getMenu().getItem(position);
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                }
            });
        } else {
            handleLackOfPermissions();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case INTERNET_PERMISSION_REQUEST:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    setupViewPager();
                } else {
                    MainActivity.this.finish();
                }
                break;
        }
    }

    private void handleLackOfPermissions() {
        // Permission is not granted
        // Should we show an explanation?
        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.INTERNET)) {
            // Show an explanation to the user *asynchronously* -- don't block
            // this thread waiting for the user's response! After the user
            // sees the explanation, try again to request the permission.
            new AlertDialog.Builder(MainActivity.this)
                    .setMessage(getString(R.string.permision_alert_dialog_explanation_title))
                    .setMessage(getString(R.string.permision_alert_dialog_explanation_message))
                    .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                    .create()
                    .show();
        } else {
            // No explanation needed; request the permission
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.READ_CONTACTS},
                    INTERNET_PERMISSION_REQUEST);
        }
    }

    private boolean checkPermissions() {
        return ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.INTERNET)
                == PackageManager.PERMISSION_GRANTED;
    }
}
