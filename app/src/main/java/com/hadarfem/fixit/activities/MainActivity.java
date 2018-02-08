package com.hadarfem.fixit.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hadarfem.fixit.R;
import com.hadarfem.fixit.cache.IPictureCache;
import com.hadarfem.fixit.cache.MemoryPictureCache;
import com.hadarfem.fixit.dal.interfaces.IProblemDao;
import com.hadarfem.fixit.dal.interfaces.IBidDao;
import com.hadarfem.fixit.dal.interfaces.IUpdater;
import com.hadarfem.fixit.dal.models.Bid;
import com.hadarfem.fixit.dal.models.Problem;
import com.hadarfem.fixit.dal.problem.FirebaseProblemDao;
import com.hadarfem.fixit.dal.bid.FirebaseBidDao;
import com.hadarfem.fixit.extensions.AnimationHandler;
import com.hadarfem.fixit.fragments.BidsBoardFragment;
import com.hadarfem.fixit.fragments.ProblemsBoardFragment;
import com.hadarfem.fixit.fragments.ProblemFragment;
import com.hadarfem.fixit.interfaces.IBidsBoardActivity;
import com.hadarfem.fixit.interfaces.IProblemActivity;
import com.hadarfem.fixit.models.User;
import com.hadarfem.fixit.tasks.DownloadImageTask;
import com.hadarfem.fixit.userProvider.FacebookUserProvider;
import com.hadarfem.fixit.userProvider.IUserProvider;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements IBidsBoardActivity, IProblemActivity {
    private ProblemsBoardFragment problemsBoardFragment;
    private ProblemFragment problemFragment;
    private BidsBoardFragment bidsBoardFragment;
    private Fragment currentFragment;
    private List<MenuItem> menuItems;
    private int selectedMenuItemIndex;
    private AnimationHandler animationHandler;
    private IBidDao bidDao;
    private IProblemDao problemDao;
    private IUserProvider userProvider;
    private IPictureCache pictureCache;
    private User loggedUser;
    private boolean isMenuEnabled = true;

    public MainActivity() {
        animationHandler = new AnimationHandler();
        userProvider = new FacebookUserProvider();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setToolbar();

        loggedUser = (User) getIntent().getExtras().getSerializable(
                getString(R.string.intent_logged_user));
        injectDependencies();
        ImageView userImageView = (ImageView) findViewById(R.id.userPicture);
        TextView userNameView = (TextView) findViewById(R.id.userLabel);

        if (userImageView != null) {
            new DownloadImageTask(userImageView, pictureCache, (int) getResources().getDimension(
                    R.dimen.user_picture_width), (int) getResources().getDimension(
                    R.dimen.user_picture_height)).execute(loggedUser.getProfilePictureUrl());
        }

        if (userNameView != null) {
            userNameView.setText(loggedUser.getName());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tab_menu, menu);

        menuItems = Arrays.asList(
                menu.findItem(R.id.problemsBoardTab),
                menu.findItem(R.id.problemTab),
                menu.findItem(R.id.bidsBoardTab));

        onOptionsItemSelected(menuItems.get(0));

        return true;
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();

        if (fragmentManager.getBackStackEntryCount() > 0) {

            if (!isMenuEnabled) {
                setEnabledMenu(true);
            }

            String entryName = fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount() - 1).getName();
            fragmentManager.popBackStackImmediate();
            int ItemID = Integer.parseInt(entryName);
            selectedMenuItemIndex = ItemID;
            MenuItem item = menuItems.get(selectedMenuItemIndex);
            changeSelectedToolbarIcon(item);
            Fragment prevFragment = getFragment(item);
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragmentPlaceholder, prevFragment);
            fragmentTransaction.show(prevFragment);
            fragmentTransaction.commit();
            currentFragment = prevFragment;
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int oldMenuItemIndex = selectedMenuItemIndex;

        changeSelectedToolbarIcon(item);

        int newSelectionIndex = menuItems.indexOf(item);

        AnimationHandler.Animation animation =
                animationHandler.getAnimation(selectedMenuItemIndex, newSelectionIndex, menuItems.size());

        selectedMenuItemIndex = newSelectionIndex;

        Fragment requestedFragment = getFragment(item);

        if (requestedFragment == null || requestedFragment == currentFragment) {
            return super.onOptionsItemSelected(item);
        }

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        if (animation == AnimationHandler.Animation.EnterFromRight) {
            fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,
                    R.anim.enter_from_left, R.anim.exit_to_right);
        } else if (animation == AnimationHandler.Animation.EnterFromLeft) {
            fragmentTransaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right,
                    R.anim.enter_from_right, R.anim.exit_to_left);
        }

        fragmentTransaction.replace(R.id.fragmentPlaceholder, requestedFragment);

        if (currentFragment != null) {
            fragmentTransaction.addToBackStack((String.valueOf(oldMenuItemIndex)));
        }

        fragmentTransaction.show(requestedFragment);
        fragmentTransaction.commit();

        currentFragment = requestedFragment;

        return true;
    }

    private void changeSelectedToolbarIcon(MenuItem item) {
        for (MenuItem menuItem : menuItems) {
            if (menuItem == item) {
                menuItem.getIcon().setColorFilter(ResourcesCompat.getColor(getResources(),
                        R.color.selectedMenuItem, null), PorterDuff.Mode.MULTIPLY);
            } else {
                menuItem.getIcon().clearColorFilter();
            }
        }
    }

    @Override
    public void savePost(Bid bid) {
        bidDao.addBid(bid);
    }

    @Override
    public void registerForBids(IUpdater<Bid> updater) {
        bidDao.registerForUpdates(updater);
    }

    @Override
    public void saveProblem(Problem problem) {
        problemDao.addProblem(problem);
    }

    public void logout(View v) {
        userProvider.logout();

        Intent loginScreen = new Intent(MainActivity.this, FacebookLoginActivity.class);

        // Setting the flags needed to zero the activities task stack in order no to press back and get inside the app after logout
        loginScreen.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        startActivity(loginScreen);

        finish();
    }

    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private Fragment getFragment(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.problemsBoardTab: {
                if (problemsBoardFragment == null) {
                    problemsBoardFragment = ProblemsBoardFragment.newInstance(this, loggedUser);
                }

                return problemsBoardFragment;
            }
            case R.id.problemTab: {
                if (problemFragment == null) {
                    problemFragment = ProblemFragment.newInstance(this, loggedUser);
                }

                return problemFragment;
            }
            case R.id.bidsBoardTab: {
                if (bidsBoardFragment == null) {
                    bidsBoardFragment = BidsBoardFragment.newInstance(this, loggedUser);
                }

                return bidsBoardFragment;
            }
            default: {
                return null;
            }
        }
    }

    private void setEnabledMenu(boolean status) {
        for (int i = 0; i < menuItems.size(); i++) {
            menuItems.get(i).setEnabled(status);

            if (status == false) {
                menuItems.get(i).getIcon().setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
            } else {
                menuItems.get(i).getIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
            }
        }
        isMenuEnabled = status;
    }

    private void injectDependencies() {
        bidDao = new FirebaseBidDao(loggedUser.getName());
        problemDao = new FirebaseProblemDao();
        pictureCache = new MemoryPictureCache();
    }
}
