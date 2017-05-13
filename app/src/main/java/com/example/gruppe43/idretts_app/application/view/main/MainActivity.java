package com.example.gruppe43.idretts_app.application.view.main;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.example.gruppe43.idretts_app.R;
import com.example.gruppe43.idretts_app.application.controll.DataBaseHelperA;
import com.example.gruppe43.idretts_app.application.controll.DataBaseHelperB;
import com.example.gruppe43.idretts_app.application.controll.DatabaseHelperC;
import com.example.gruppe43.idretts_app.application.helper_classes.PrefferencesClass;
import com.example.gruppe43.idretts_app.application.interfaces.FragmentActivityInterface;
import com.example.gruppe43.idretts_app.application.view.fragments.Login;
import com.example.gruppe43.idretts_app.application.view.fragments.Messages;
import com.example.gruppe43.idretts_app.application.view.fragments.PlayerActivityRegistration;
import com.example.gruppe43.idretts_app.application.view.fragments.TrainerActivityRegistration;
import com.example.gruppe43.idretts_app.application.view.fragments.Player;
import com.example.gruppe43.idretts_app.application.view.fragments.ProfileView;
import com.example.gruppe43.idretts_app.application.view.fragments.Tabs;
import com.example.gruppe43.idretts_app.application.view.fragments.Team;
import com.example.gruppe43.idretts_app.application.view.fragments.Trainer;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import static com.example.gruppe43.idretts_app.application.view.fragments.PlayerActivityRegistration.par;
import static com.example.gruppe43.idretts_app.application.view.fragments.TrainerActivityRegistration.nar;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, FragmentActivityInterface {
    private DrawerLayout mDrawerLayout;
    private FragmentManager mFragmentManager;
    private Boolean trainerIsShowing;
    private Boolean playerIsShowing;
    private FloatingActionButton fab;
    private Boolean isPlayerSignedIn;
    private Boolean isTrainerSignedIn;
    private Boolean editActivityIsShowing;
    private FirebaseAuth mAuth;
    private boolean onNewActivityRegisterPage;
    private ActionBar actionBar;
    private PrefferencesClass prefs;
    private boolean isBackButtonPressedFromEditFragment;
    private static MainActivity mainContext;


    public MainActivity() {
        //init only if persistant not set already
        if (FirebaseApp.getApps(this).isEmpty()) {
            FirebaseApp.initializeApp(this);
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }
    }

    @Override
    public MainActivity getContext() {
        return this;
    }

    public FloatingActionButton getFab() {
        return fab;
    }

    public void setEditActivityIsShowing(Boolean editActivityIsShowing) {
        this.editActivityIsShowing = editActivityIsShowing;
    }

    @Override
    public void setIsOnNewActivityRegisterPage(boolean onActivityRegisterPage) {
        onNewActivityRegisterPage = onActivityRegisterPage;
    }

    public static MainActivity getMainContext() {
        return mainContext;
    }

    public FragmentManager getmFragmentManager() {
        return mFragmentManager;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFragmentManager = getSupportFragmentManager();
        isBackButtonPressedFromEditFragment = false;
        fab = (FloatingActionButton) findViewById(R.id.fab);
        mAuth = FirebaseAuth.getInstance();
        prefs = new PrefferencesClass(this);
        //dbhb.runPlayerPostMaintenance();
        fab.hide();

        fab.setRippleColor(Color.GREEN);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onNewActivityRegisterPage && isTrainerSignedIn) {
                    nar.registerActivity();
                    onNewActivityRegisterPage = false;
                } else if (onNewActivityRegisterPage && isPlayerSignedIn) {
                    par.registerPlayerActivity();
                    onNewActivityRegisterPage = false;
                } else if (!onNewActivityRegisterPage) {
                    showFragmentOfGivenCondition();
                    onNewActivityRegisterPage = true;
                }
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        NavigationView view = (NavigationView) findViewById(R.id.nav_view);
        view.setNavigationItemSelectedListener(this);
        setSupportActionBar(toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        if (actionBar == null) {
            actionBar = getSupportActionBar();
        }

        String restore = prefs.loadSharedPrefData("isAdmin");//after loggin credential it did not go in here!
        if (restore.equals("false")) {
            isPlayerSignedIn = true;
            isTrainerSignedIn = false;
            actionBar.show();
        } else if (restore.equals("true")) {
            isPlayerSignedIn = false;
            isTrainerSignedIn = true;
            actionBar.show();
        }

        if (isTrainerSignedIn != null || isPlayerSignedIn != null) {//isOnSignedInState
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.containerView, new Tabs()).commit();
        } else {//isOnSignedOutState
            onSignOut();
        }
        DatabaseHelperC dbhc = new DatabaseHelperC(this);
        dbhc.initiateDataInRecyclerViewForTeam();
    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("//////////// resume");
    }

    //navigate to appropriate fragment after registration.
    @Override
    public void showFragmentOfGivenCondition() {
        if (isTrainerSignedIn && trainerIsShowing) {
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            fragmentTransaction.addToBackStack("");
            fragmentTransaction.replace(R.id.containerView, new TrainerActivityRegistration()).commit();
            currentShowingFragment("editActivity");
        } else if (isPlayerSignedIn && playerIsShowing) {
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            fragmentTransaction.addToBackStack("");
            fragmentTransaction.replace(R.id.containerView, new PlayerActivityRegistration()).commit();
            currentShowingFragment("editActivity");
        } else if (isPlayerSignedIn && editActivityIsShowing) {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    Tabs.viewPager.setCurrentItem(1);
                }
            });
            hideKeyboard();
            currentShowingFragment("player");
            mFragmentManager.popBackStack();
        } else if (isTrainerSignedIn && editActivityIsShowing) {
            clearBackStack();
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.containerView, new Tabs()).commit();
            currentShowingFragment("trainer");
            editActivityIsShowing = false;
            hideKeyboard();
            //mFragmentManager.popBackStack();
        }
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
        getMenuInflater().inflate(R.menu.main_menu, menu);
        // initPreLogin();//hide menus
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.toolbar_home) {
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            mFragmentManager.popBackStack();
            fragmentTransaction.replace(R.id.containerView, new Tabs()).commit();
            return true;
        }
        if (id == R.id.toolbar_messages) {
            FragmentTransaction xfragmentTransaction = mFragmentManager.beginTransaction();
            xfragmentTransaction.addToBackStack("messages");
            xfragmentTransaction.replace(R.id.containerView, new Messages()).commit();
            currentShowingFragment("messaging");
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.nav_signout) {
            onSignOut();
        } else if (menuItem.getItemId() == R.id.nav_profile) {
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            fragmentTransaction.addToBackStack("");//TODO goes to only current logged in and EDIT is enabled
            fragmentTransaction.replace(R.id.containerView, new ProfileView()).commit();
            fragmentTransaction.addToBackStack("");
            currentShowingFragment("");
        } else if (menuItem.getItemId() == R.id.nav_messages) {
            FragmentTransaction xfragmentTransaction = mFragmentManager.beginTransaction();
            xfragmentTransaction.addToBackStack("");
            mFragmentManager.popBackStack();
            xfragmentTransaction.replace(R.id.containerView, new Messages()).commit();
        } else if (menuItem.getItemId() == R.id.nav_trainer) {
            FragmentTransaction xfragmentTransaction = mFragmentManager.beginTransaction();
            xfragmentTransaction.addToBackStack("");
            xfragmentTransaction.replace(R.id.containerView, new Trainer()).commit();
        } else if (menuItem.getItemId() == R.id.navn_player) {
            FragmentTransaction xfragmentTransaction = mFragmentManager.beginTransaction();
            xfragmentTransaction.addToBackStack("");
            mFragmentManager.popBackStack();
            xfragmentTransaction.replace(R.id.containerView, new Player()).commit();
        } else if (menuItem.getItemId() == R.id.nav_Team) {
            FragmentTransaction xfragmentTransaction = mFragmentManager.beginTransaction();
            xfragmentTransaction.addToBackStack("");
            xfragmentTransaction.replace(R.id.containerView, new Team()).commit();
        } else if (menuItem.getItemId() == R.id.registrationRequests) {
            DataBaseHelperB dbhb = new DataBaseHelperB(this);
            dbhb.checkOutdatedTrainerPosts();
            //TODO
        } else if (menuItem.getItemId() == R.id.savePlayersData) {
//TODO
        } else if (menuItem.getItemId() == R.id.resetApp) {
//TODO
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return false;//returning true keeps the item selected, selected.
    }
    /*////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////*/

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
    }

    //set current fragment
    @Override
    public void currentShowingFragment(String tabId) {
        if (tabId.equals("trainer")) {
            trainerIsShowing = true;
            playerIsShowing = false;
            editActivityIsShowing = false;
            fab.setBackgroundTintList(ColorStateList.valueOf(ResourcesCompat.getColor(getResources(), R.color.primary_darker, null)));
        } else if (tabId.equals("player")) {
            trainerIsShowing = false;
            playerIsShowing = true;
            editActivityIsShowing = false;
            fab.setBackgroundTintList(ColorStateList.valueOf(ResourcesCompat.getColor(getResources(), R.color.jet, null)));
        } else if (tabId.equals("editActivity")) {
            trainerIsShowing = false;
            playerIsShowing = false;
            editActivityIsShowing = true;
        } else {
            trainerIsShowing = false;
            playerIsShowing = false;
            editActivityIsShowing = false;
        }
        if (isTrainerSignedIn != null && isPlayerSignedIn != null) {
            updatesWhileSwiping();//update states
        }
    }

    //update changes
    private void updatesWhileSwiping() {
        if (trainerIsShowing && isTrainerSignedIn) {
            fab.setImageResource(R.drawable.add24dp);
            fab.show();
            //Toast.makeText(this, "hide", Toast.LENGTH_LONG).show();
        } else if (playerIsShowing && isPlayerSignedIn) {
            fab.setImageResource(R.drawable.add24dp);
            fab.show();
        } else if (editActivityIsShowing) {
            fab.show();
            fab.setImageResource(R.drawable.check24dp);
        } else {
            fab.hide();
        }
        //if no one is signed in dont show the actionbar menu.
    }

    //init things that should be initialyzed after a successful sign in.
    @Override
    public void initAfterLogin(String userType) {
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.containerView, new Tabs()).commit();
        currentShowingFragment("trainer");
        onNewActivityRegisterPage = false;
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        if (userType.equals("admin")) {
            isTrainerSignedIn = true;
            isPlayerSignedIn = false;
            prefs.saveSharedPrefData("isAdmin", "true");
        } else {
            isTrainerSignedIn = false;
            isPlayerSignedIn = true;
            prefs.saveSharedPrefData("isAdmin", "false");
        }
        actionBar.show();
        DataBaseHelperA dbha = new DataBaseHelperA(this);
        dbha.saveSignedInUserNameToCache();
    }

    //set state sign out!
    @Override
    public void onSignOut() {
        prefs.saveSharedPrefData("isAdmin", "default");
        onNewActivityRegisterPage = false;
        actionBar.hide();
        fab.hide();
        isTrainerSignedIn = null;
        isPlayerSignedIn = null;
        if (mAuth.getCurrentUser() != null) {
            mAuth.signOut();
        }
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.containerView, new Login()).commit();
    }

    @Override
    public void hideKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }

    @Override
    public void clearBackStack() {
        for (int i = 0; i < mFragmentManager.getBackStackEntryCount(); i++) {
            mFragmentManager.popBackStack();
        }
    }


    @Override//imageCroper..A
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ProfileView.GALLERY_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();

            CropImage.activity(imageUri).setGuidelines(CropImageView.Guidelines.ON).setAspectRatio(1, 1).start(this);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                ProfileView.imageUri = result.getUri();
                ProfileView.profImageIV.setImageURI(ProfileView.imageUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
}