package com.hadarfem.fixit.userProvider;

import com.facebook.login.LoginManager;

/**
 * Created by Tzach & Nadav on 2/6/2018.
 */

public class FacebookUserProvider implements IUserProvider {
    @Override
    public void logout() {
        LoginManager.getInstance().logOut();
    }
}
