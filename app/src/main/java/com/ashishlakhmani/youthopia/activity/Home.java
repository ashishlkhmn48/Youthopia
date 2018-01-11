package com.ashishlakhmani.youthopia.activity;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ashishlakhmani.youthopia.R;
import com.ashishlakhmani.youthopia.adapter.TabPager;
import com.ashishlakhmani.youthopia.fragment.AllEventsFragment;
import com.ashishlakhmani.youthopia.fragment.EventCommonFragment;
import com.ashishlakhmani.youthopia.fragment.EventTypeCommonFragment;
import com.ashishlakhmani.youthopia.fragment.FragmentList;
import com.ashishlakhmani.youthopia.fragment.ScheduleFragment;
import com.ashishlakhmani.youthopia.fragment.WebViewFragment;
import com.ashishlakhmani.youthopia.services.NotificationReceiver;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.miguelcatalan.materialsearchview.MaterialSearchView;


public class Home extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    Toolbar toolbar;
    MaterialSearchView searchView;
    DrawerLayout drawer;
    NavigationView navigationView;
    TabLayout tabLayout;
    ViewPager viewPager;
    FragmentList fragmentList;
    GoogleApiClient googleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        setContentView(R.layout.activity_home);


        //To ensure that only 1st time these things happen..
        SharedPreferences sp = getSharedPreferences("login", Context.MODE_PRIVATE);

        if (sp.getBoolean("isFirstTimeLogin", true)) {
            Toast.makeText(this, sp.getString("email", ""), Toast.LENGTH_SHORT).show();
            Toast.makeText(this, "Welcome " + sp.getString("name", "").toUpperCase(), Toast.LENGTH_LONG).show();
            SharedPreferences.Editor editor = sp.edit();
            editor.remove("isFirstTimeLogin");
            editor.putBoolean("isFirstTimeLogin", false);
            editor.apply();
        }

        //Google sign in task..
        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, signInOptions)
                .build();
        //

        initializeTask();

        //Navigation view sign out button..
        View header = navigationView.getHeaderView(0);
        TextView navName = (TextView) header.findViewById(R.id.nav_name_text);
        TextView navEmail = (TextView) header.findViewById(R.id.nav_email_text);
        Button signOut = (Button) header.findViewById(R.id.sign_out);
        navName.setText(sp.getString("name", "").toUpperCase());
        navEmail.setText(sp.getString("email", ""));
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOutTask();
            }
        });
        //

        tabTask();

        statusBarTask();

        toolBarTask();

        navigationDrawerTask();

        searchBarTask();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    @Override
    public void onBackPressed() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START) || searchView.isSearchOpen()) {
            drawer.closeDrawer(GravityCompat.START);
            searchView.closeSearch();
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        MenuItem item = menu.findItem(R.id.actionSearch);
        searchView.setMenuItem(item);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        WebViewFragment webViewFragment = new WebViewFragment();
        switch (item.getItemId()) {
            case R.id.actionSearch:
                return true;
            case R.id.nav_sponsors:
                webViewFragment.setDetailsLink("http://youthopia16.in/sponsors.html");
                webViewFragment.setToolbarHeading("SPONSORS");
                if (!isWebViewFragmentVisible("details_sponsors"))
                    loadFragment(webViewFragment, "details_sponsors");
                return true;
            case R.id.nav_contact:
                webViewFragment.setDetailsLink("http://youthopia16.in/contact.html");
                webViewFragment.setToolbarHeading("CONTACT US");
                if (!isWebViewFragmentVisible("details_contact"))
                    loadFragment(webViewFragment, "details_contact");
                return true;
            case R.id.nav_about:
                webViewFragment.setDetailsLink("http://youthopia16.in/about.html");
                webViewFragment.setToolbarHeading("ABOUT");
                if (!isWebViewFragmentVisible("details_about"))
                    loadFragment(webViewFragment, "details_about");
                return true;
            case R.id.nav_schedule:
                ScheduleFragment scheduleFragment = new ScheduleFragment();
                if (!isScheduleFragmentVisible("details_schedule"))
                    loadFragment(scheduleFragment, "details_schedule");
                return true;
            case R.id.nav_logout:
                signOutTask();
                return true;
        }
        return true;
    }

    private void initializeTask() {
        //Initialize all class fields..
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        searchView = (MaterialSearchView) findViewById(R.id.search_view);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        fragmentList = new FragmentList();
    }

    private void tabTask() {

        TabLayout.Tab tab1 = tabLayout.newTab();
        tabLayout.addTab(tab1);

        TabLayout.Tab tab2 = tabLayout.newTab();
        tabLayout.addTab(tab2);

        //link tab layout with viewpager
        tabLayout.setupWithViewPager(viewPager);

        PagerAdapter adapter = new TabPager
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        //ViewPager sets case 0 tab automatically..

        tab1.setIcon(R.drawable.registeredcheck);
        tab2.setIcon(R.drawable.home_dark);

        // addOnPageChangeListener event change the tab on slide
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }


    private void statusBarTask() {
        //To change color of status bar according to App Theme (Only API 21 & above)..
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorAccent));
        }
    }

    private void toolBarTask() {
        // This will set up our toolbar..
        setSupportActionBar(toolbar);
    }

    public void navigationDrawerTask() {
        //otherwise it also opens in retry page
        if (SplashScreen.jsonString != null) {
            // This will tie functionality of navigation drawer and toolbar...
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();

            //Action to perform when notification drawer items are clicked..
            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    int id = item.getItemId();
                    switch (id) {
                        case R.id.nav_cultual:
                            if (!isEventTypeFragmentVisible("cultural"))
                                callCultural();
                            break;
                        case R.id.nav_technical:
                            if (!isEventTypeFragmentVisible("technical"))
                                callTechnical();
                            break;
                        case R.id.nav_gaming:
                            if (!isEventTypeFragmentVisible("gaming"))
                                callGaming();
                            break;
                        case R.id.nav_informal:
                            if (!isEventTypeFragmentVisible("informal"))
                                callInformal();
                            break;
                        case R.id.nav_literary:
                            if (!isEventTypeFragmentVisible("literary"))
                                callLiterary();
                            break;
                        case R.id.nav_registered:
                            FragmentManager favFragment = getSupportFragmentManager();
                            for (int i = 0; i < favFragment.getBackStackEntryCount(); i++) {
                                favFragment.popBackStack();
                            }
                            viewPager.setCurrentItem(1, true);
                            break;
                        case R.id.nav_home:
                            FragmentManager homeFragment = getSupportFragmentManager();
                            for (int i = 0; i < homeFragment.getBackStackEntryCount(); i++) {
                                homeFragment.popBackStack();
                            }
                            viewPager.setCurrentItem(0, true);
                    }
                    //To close Navigation Drawer..
                    drawer.closeDrawer(GravityCompat.START);
                    return true;
                }
            });
        }
    }

    private void searchBarTask() {
        //Various search bar functionalities..
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(Home.this, "Please select from the list.", Toast.LENGTH_SHORT).show();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return fragmentList.filterTask(newText);
            }
        });

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {

            @Override
            public void onSearchViewShown() {
                loadFragment(fragmentList, "search");
            }

            @Override
            public void onSearchViewClosed() {
                removeThisFragment();
            }
        });
    }

    //To load the fragment..
    public void loadFragment(Fragment fragment, String TAG) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.replace(R.id.frameLayout, fragment, TAG);
        fragmentTransaction.addToBackStack(TAG);
        fragmentTransaction.commit(); // save the changes
    }

    //To remove the Current Fragment..
    private void removeThisFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.popBackStack();
    }

    public void changeConstraintLayout(String data, boolean picRemove) {
        TextView textView = (TextView) findViewById(R.id.toolbarText);
        ImageView imageView = (ImageView) findViewById(R.id.toolbarImage);
        if (picRemove) {
            textView.setText(data);
            textView.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.INVISIBLE);
        } else {
            textView.setVisibility(View.INVISIBLE);
            imageView.setVisibility(View.VISIBLE);
        }

    }

    //register button call
    public void onRegisterClick(View v) {
        loadFragment(new AllEventsFragment(), "allevents");
    }

    //Album button call
    public void onAlbumClick(View view) {
        Intent intent = new Intent(this, HomePicViewActivity.class);
        intent.putExtra("data", 0);
        startActivity(intent);
    }

    //Retry button call
    public void onRetryClick(View view) {
        Intent intent = new Intent(this, SplashScreen.class);
        startActivity(intent);
    }

    //Click events for buttons in AllEventsFragment
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cultural_button:
                callCultural();
                break;
            case R.id.technical_button:
                callTechnical();
                break;
            case R.id.gaming_button:
                callGaming();
                break;
            case R.id.informal_button:
                callInformal();
                break;
            case R.id.literary_button:
                callLiterary();
        }
    }

    //Schedule button call
    public void onScheduleClick(View view) {
        ScheduleFragment scheduleFragment = new ScheduleFragment();
        loadFragment(scheduleFragment, "details_schedule");
    }


    //to open facebook app
    public void onFacebookClick(View view) {
        String YourPageURL = "https://www.facebook.com/n/?dityouthopia2016";
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(YourPageURL));
        startActivity(browserIntent);
    }

    //to open instagram app
    public void onInstagramClick(View view) {
        startActivity(openInstagramIntent());
    }

    private Intent openInstagramIntent() {

        try {
            getPackageManager().getPackageInfo("com.instagram.android", 0);
            return new Intent(Intent.ACTION_VIEW, Uri.parse("http://instagram.com/_u/dityouthopia16"));
        } catch (Exception e) {
            return new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/dityouthopia16/"));
        }
    }


    //Calling various event types
    public void callCultural() {
        String heading = "CULTURAL EVENT";
        String quote = "Music is a piece of art that goes in the ears straight to the heart.";
        String toolbarHeading = "CULTURAL";
        EventTypeCommonFragment eventTypeCommonFragment = new EventTypeCommonFragment();
        Bundle bundle = new Bundle();
        bundle.putString("heading", heading);
        bundle.putString("quote", quote);
        bundle.putString("toolbarHeading", toolbarHeading);
        eventTypeCommonFragment.setArguments(bundle);
        loadFragment(eventTypeCommonFragment, toolbarHeading.toLowerCase());
    }

    public void callTechnical() {
        String heading = "TECHNO WAVE";
        String quote = "People who are crazy enough to think that they can change the world.. are the ones who do ! \n" +
                "- Steve jobs";
        String toolbarHeading = "TECHNICAL";

        EventTypeCommonFragment eventTypeCommonFragment = new EventTypeCommonFragment();
        Bundle bundle = new Bundle();
        bundle.putString("heading", heading);
        bundle.putString("quote", quote);
        bundle.putString("toolbarHeading", toolbarHeading);
        eventTypeCommonFragment.setArguments(bundle);
        loadFragment(eventTypeCommonFragment, toolbarHeading.toLowerCase());

    }

    public void callGaming() {
        String heading = "ARMAGEDDON";
        String quote = "I Don't need to \"Get A Life\"... I'm a gamer. I have LOTS of Lives.";
        String toolbarHeading = "GAMING";
        EventTypeCommonFragment eventTypeCommonFragment = new EventTypeCommonFragment();
        Bundle bundle = new Bundle();
        bundle.putString("heading", heading);
        bundle.putString("quote", quote);
        bundle.putString("toolbarHeading", toolbarHeading);
        eventTypeCommonFragment.setArguments(bundle);
        loadFragment(eventTypeCommonFragment, toolbarHeading.toLowerCase());
    }

    public void callInformal() {
        String heading = "EXHILARATE";
        String quote = "One way to get the most out of the life.. is to look upon it as an Adventure !\n" +
                "- William Fether";
        String toolbarHeading = "INFORMAL";
        EventTypeCommonFragment eventTypeCommonFragment = new EventTypeCommonFragment();
        Bundle bundle = new Bundle();
        bundle.putString("heading", heading);
        bundle.putString("quote", quote);
        bundle.putString("toolbarHeading", toolbarHeading);
        eventTypeCommonFragment.setArguments(bundle);
        loadFragment(eventTypeCommonFragment, toolbarHeading.toLowerCase());
    }

    public void callLiterary() {
        String heading = "AAKRITI";
        String quote = "If you hear a voice within you saying \"you are not a painter\" then by all means paint and that voice will be silenced \n- Vincent Van Gogh";
        String toolbarHeading = "LITERARY";
        EventTypeCommonFragment eventTypeCommonFragment = new EventTypeCommonFragment();
        Bundle bundle = new Bundle();
        bundle.putString("heading", heading);
        bundle.putString("quote", quote);
        bundle.putString("toolbarHeading", toolbarHeading);
        eventTypeCommonFragment.setArguments(bundle);
        loadFragment(eventTypeCommonFragment, toolbarHeading.toLowerCase());
    }

    //To check if fragment is already present on top of the stack or not and if not present then clear the stack.
    public boolean isEventFragmentVisible(String TAG) {
        EventCommonFragment fragment = (EventCommonFragment) getSupportFragmentManager().findFragmentByTag(TAG);
        if (fragment != null && fragment.isVisible())
            return true;
        else {
            //When we open any eventtype fragment, we want all other previously opened in backstack to be closed.
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.popBackStack();
            //
            return false;
        }
    }

    public boolean isEventTypeFragmentVisible(String TAG) {
        EventTypeCommonFragment fragment = (EventTypeCommonFragment) getSupportFragmentManager().findFragmentByTag(TAG);
        if (fragment != null && fragment.isVisible())
            return true;
        else {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.popBackStack();
            return false;
        }
    }

    public boolean isWebViewFragmentVisible(String TAG) {
        WebViewFragment fragment = (WebViewFragment) getSupportFragmentManager().findFragmentByTag(TAG);
        if (fragment != null && fragment.isVisible())
            return true;
        else {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.popBackStack();
            return false;
        }
    }

    public boolean isScheduleFragmentVisible(String TAG) {
        ScheduleFragment fragment = (ScheduleFragment) getSupportFragmentManager().findFragmentByTag(TAG);
        if (fragment != null && fragment.isVisible())
            return true;
        else {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.popBackStack();
            return false;
        }
    }


    public void loadFragmentBottomNavigation(Fragment fragment, String TAG) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_UNSET);
        fragmentTransaction.replace(R.id.content, fragment, TAG);
        fragmentTransaction.commit();
    }


    private void signOutTask() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("Sign Out");
        builder.setMessage("Do you want to Sign Out ?");

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        Intent i = new Intent(Home.this, NotificationReceiver.class);
                        PendingIntent pendingIntent1 = PendingIntent.getBroadcast(getApplicationContext(), 13, i, 0);
                        PendingIntent pendingIntent2 = PendingIntent.getBroadcast(getApplicationContext(), 4, i, 0);
                        AlarmManager alarmManager1 = (AlarmManager) getSystemService(ALARM_SERVICE);
                        AlarmManager alarmManager2 = (AlarmManager) getSystemService(ALARM_SERVICE);

                        if (alarmManager1 != null)
                            alarmManager1.cancel(pendingIntent1);
                        if (alarmManager2 != null)
                            alarmManager2.cancel(pendingIntent2);

                        SharedPreferences sp = getSharedPreferences("login", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.remove("name");
                        editor.remove("email");
                        editor.remove("isFirstTimeLogin");
                        editor.remove("notificationFlag");
                        editor.apply();
                        Intent intent = new Intent(Home.this, SplashScreen.class);
                        startActivity(intent);
                        finish();
                    }
                });
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}