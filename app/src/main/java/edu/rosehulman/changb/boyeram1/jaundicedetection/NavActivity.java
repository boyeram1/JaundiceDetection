package edu.rosehulman.changb.boyeram1.jaundicedetection;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import edu.rosehulman.changb.boyeram1.jaundicedetection.adapters.ChildAdapter;
import edu.rosehulman.changb.boyeram1.jaundicedetection.adapters.TestResultAdapter;
import edu.rosehulman.changb.boyeram1.jaundicedetection.fragments.ChildListFragment;
import edu.rosehulman.changb.boyeram1.jaundicedetection.fragments.TestResultListFragment;
import edu.rosehulman.changb.boyeram1.jaundicedetection.modelObjects.Child;
import edu.rosehulman.changb.boyeram1.jaundicedetection.modelObjects.Family;
import edu.rosehulman.changb.boyeram1.jaundicedetection.modelObjects.TestResult;

public class NavActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, TestResultAdapter.Callback, ChildAdapter.NavActivityCallback {

    private String mKeyOfFamilyOfChild;
    private FloatingActionButton mFab;
    private Family mFamily;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        mFamily = intent.getParcelableExtra(LoginActivity.EXTRA_FAMILY);
        mKeyOfFamilyOfChild = mFamily.getKey();

        mFab = (FloatingActionButton) findViewById(R.id.fab_nav);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        if (savedInstanceState == null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            final ChildListFragment childListFragment = new ChildListFragment();
            childListFragment.setNavActivityCallback(this);
            mFab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    childListFragment.showAddEditDialog(null);
                }
            });
            mFab.setImageResource(R.drawable.ic_child_add);
            ft.add(R.id.fragment_container, childListFragment);
            ft.commit();
        }
        String familyName = mFamily.getName();
//        TextView textView = (TextView)findViewById(R.id.nav_family_name);
//        textView.setText(familyName);
        setTitle(getResources().getString(R.string.family_format, familyName));

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.nav, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        Fragment switchTo = null;
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_child_list:
                final ChildListFragment childListFragment = new ChildListFragment();
                childListFragment.setNavActivityCallback(this);
                switchTo = childListFragment;
                setTitle(getResources().getString(R.string.family_format, mFamily.getName()));
                mFab.show();
                mFab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        childListFragment.showAddEditDialog(null);
                    }
                });
                mFab.setImageResource(R.drawable.ic_child_add);
                break;
            case R.id.nav_test_list:
                switchTo = new TestResultListFragment();
                mFab.show();
                mFab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Snackbar.make(view, "Replace with adding test", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                });
                mFab.setImageResource(R.drawable.ic_library_add);
                break;
            case R.id.nav_settings:
                startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
                break;
            case R.id.nav_info:
                mFab.hide();
                break;
            case R.id.nav_logout:
                super.onBackPressed();
                break;
        }
        if (switchTo != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_container, switchTo);
            for (int i = 0; i < getSupportFragmentManager().getBackStackEntryCount(); i++) {
                getSupportFragmentManager().popBackStackImmediate();
            }
            ft.commit();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void showEditRemovePopup(Child child, View v, int position) {

    }

    @Override
    public String getKeyOfFamilyOfChild() {
        return mKeyOfFamilyOfChild;
    }

    @Override
    public void onTestSelected(TestResult testResult) {

    }
}
