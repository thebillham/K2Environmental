package nz.co.k2.k2e.data.local.prefs;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;

import nz.co.k2.k2e.data.DataManager;
import nz.co.k2.k2e.di.PreferenceInfo;
import nz.co.k2.k2e.utils.AppConstants;

// TODO Preferences section

public class AppPreferencesHelper implements PreferencesHelper {
    private static final String PREF_KEY_CURRENT_USER_EMAIL = "PREF_KEY_CURRENT_USER_EMAIL";

    private static final String PREF_KEY_CURRENT_USER_NAME = "PREF_KEY_CURRENT_USER_NAME";

    private static final String PREF_KEY_CURRENT_USER_PROFILE_PIC_URL = "PREF_KEY_CURRENT_USER_PROFILE_PIC_URL";

    private static final String PREF_KEY_CURRENT_USER_MY_JOB_NUMBERS = null;

    private final SharedPreferences mPrefs;
    private final SharedPreferences.Editor editor;

    //
    @Inject
    public AppPreferencesHelper(Context context, @PreferenceInfo String prefFileName) {
        mPrefs = context.getSharedPreferences(prefFileName, Context.MODE_PRIVATE);
        editor = mPrefs.edit();
    }
    @Override
    public String getCurrentUserEmail() {
        return mPrefs.getString(PREF_KEY_CURRENT_USER_EMAIL, null);
    }

    @Override
    public void setCurrentUserEmail(String email) {
        mPrefs.edit().putString(PREF_KEY_CURRENT_USER_EMAIL, email).apply();
    }

    @Override
    public String getCurrentUserName() {
        return mPrefs.getString(PREF_KEY_CURRENT_USER_NAME, null);
    }

    @Override
    public void setCurrentUserName(String userName) {
        mPrefs.edit().putString(PREF_KEY_CURRENT_USER_NAME, userName).apply();
    }

    @Override
    public String getCurrentUserProfilePicUrl() {
        return mPrefs.getString(PREF_KEY_CURRENT_USER_PROFILE_PIC_URL, null);
    }

    @Override
    public void setCurrentUserProfilePicUrl(String profilePicUrl) {
        mPrefs.edit().putString(PREF_KEY_CURRENT_USER_PROFILE_PIC_URL, profilePicUrl).apply();
    }

    @Override
    public Set<String> getCurrentUserMyJobNumbers() {
        return mPrefs.getStringSet(PREF_KEY_CURRENT_USER_MY_JOB_NUMBERS, new HashSet<String>());
    }

    @Override
    public void setCurrentUserMyJobNumbers(Set<String> myJobNumbers) {
        editor.putStringSet(PREF_KEY_CURRENT_USER_MY_JOB_NUMBERS, myJobNumbers);
        editor.commit();
    }

    // Preference string sets cannot be modified so we have to copy the set, modify it and then re-set it
    @Override
    public void deleteCurrentUserMyJobNumber(String myJobNumber) {
        Set<String> oldSet = getCurrentUserMyJobNumbers();
        Set<String> newSet = new HashSet<String>();
        newSet.addAll(oldSet);
        newSet.remove(myJobNumber);
        editor.putStringSet(PREF_KEY_CURRENT_USER_MY_JOB_NUMBERS, newSet);
        editor.commit();
    }

    @Override
    public void insertCurrentUserMyJobNumber(String myJobNumber) {
        Set<String> oldSet = getCurrentUserMyJobNumbers();
        Set<String> newSet = new HashSet<String>();
        newSet.add(myJobNumber);
        newSet.addAll(oldSet);
        editor.putStringSet(PREF_KEY_CURRENT_USER_MY_JOB_NUMBERS, newSet);
        editor.commit();
    }
}
