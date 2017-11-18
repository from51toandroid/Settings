/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.settings.inputmethod;

import com.android.internal.inputmethod.InputMethodUtils;
import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;
import android.text.TextUtils;
import android.util.Log;
import android.view.inputmethod.InputMethodInfo;
import android.view.inputmethod.InputMethodManager;
import android.view.inputmethod.InputMethodSubtype;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;



import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.OnAccountsUpdateListener;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.INetworkManagementService;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.UserHandle;
import android.os.UserManager;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.text.TextUtils;
import android.util.Log;


import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;


import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleAdapter;



import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.RelativeLayout;

import android.app.ActionBar;



import com.android.internal.util.ArrayUtils;
import com.android.settings.accessibility.AccessibilitySettings;
import com.android.settings.accessibility.ToggleAccessibilityServicePreferenceFragment;
import com.android.settings.accessibility.ToggleCaptioningPreferenceFragment;
import com.android.settings.accounts.AccountSyncSettings;
import com.android.settings.accounts.AuthenticatorHelper;
import com.android.settings.accounts.ManageAccountsSettings;
import com.android.settings.applications.AppOpsSummary;
import com.android.settings.applications.ManageApplications;
import com.android.settings.applications.ProcessStatsUi;
import com.android.settings.bluetooth.BluetoothEnabler;
import com.android.settings.bluetooth.BluetoothSettings;
import com.android.settings.deviceinfo.Memory;
import com.android.settings.deviceinfo.UsbSettings;
import com.android.settings.fuelgauge.PowerUsageSummary;
import com.android.settings.inputmethod.InputMethodAndLanguageSettings;
import com.android.settings.inputmethod.KeyboardLayoutPickerFragment;
import com.android.settings.inputmethod.SpellCheckersSettings;
import com.android.settings.inputmethod.UserDictionaryList;
import com.android.settings.location.LocationSettings;
import com.android.settings.nfc.AndroidBeam;
import com.android.settings.nfc.PaymentSettings;
import com.android.settings.print.PrintJobSettingsFragment;
import com.android.settings.print.PrintServiceSettingsFragment;
import com.android.settings.print.PrintSettingsFragment;
import com.android.settings.tts.TextToSpeechSettings;
import com.android.settings.users.UserSettings;
import com.android.settings.vpn2.VpnSettings;
import com.android.settings.wfd.WifiDisplaySettings;
import com.android.settings.wifi.AdvancedWifiSettings;
import com.android.settings.wifi.WifiEnabler;
import com.android.settings.wifi.WifiSettings;
import com.android.settings.wifi.p2p.WifiP2pSettings;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import android.graphics.Color;
import android.widget.LinearLayout;
import android.view.Gravity;


import android.content.res.Resources;  
import android.graphics.drawable.Drawable; 

import android.graphics.Color;
import android.widget.LinearLayout;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ListAdapter;
import android.widget.ListView;







public class InputMethodAndSubtypeEnabler extends SettingsPreferenceFragment {
    private static final String TAG =InputMethodAndSubtypeEnabler.class.getSimpleName();
    private AlertDialog mDialog = null;
    private boolean mHaveHardKeyboard;
    final private HashMap<String, List<Preference>> mInputMethodAndSubtypePrefsMap =
            new HashMap<String, List<Preference>>();
    final private HashMap<String, CheckBoxPreference> mSubtypeAutoSelectionCBMap =
            new HashMap<String, CheckBoxPreference>();
    private InputMethodManager mImm;
    private List<InputMethodInfo> mInputMethodProperties;
    private String mInputMethodId;
    private String mTitle;
    private String mSystemLocale = "";
    private Collator mCollator = Collator.getInstance();

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        Log.w(TAG, "in onCreate.");

		


		

		
        mImm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        final Configuration config = getResources().getConfiguration();
        mHaveHardKeyboard = (config.keyboard == Configuration.KEYBOARD_QWERTY);

        final Bundle arguments = getArguments();
        // Input method id should be available from an Intent when this preference is launched as a
        // single Activity (see InputMethodAndSubtypeEnablerActivity). It should be available
        // from a preference argument when the preference is launched as a part of the other
        // Activity (like a right pane of 2-pane Settings app)
        mInputMethodId = getActivity().getIntent().getStringExtra(
                android.provider.Settings.EXTRA_INPUT_METHOD_ID);
        if (mInputMethodId == null && (arguments != null)) {
            final String inputMethodId =
                    arguments.getString(android.provider.Settings.EXTRA_INPUT_METHOD_ID);
            if (inputMethodId != null) {
                mInputMethodId = inputMethodId;
            }
        }
        mTitle = getActivity().getIntent().getStringExtra(Intent.EXTRA_TITLE);
        if (mTitle == null && (arguments != null)) {
            final String title = arguments.getString(Intent.EXTRA_TITLE);
            if (title != null) {
                mTitle = title;
            }
        }

        final Locale locale = config.locale;
        mSystemLocale = locale.toString();
        mCollator = Collator.getInstance(locale);
        onCreateIMM();
        setPreferenceScreen(createPreferenceHierarchy());

		 
		
    }

    @Override
    public void onActivityCreated(Bundle icicle) {
        super.onActivityCreated(icicle);
        if (!TextUtils.isEmpty(mTitle)) {
            getActivity().setTitle(mTitle);
        }
		Log.w(TAG, "in onActivityCreated.");

		getListView().setDivider(new ColorDrawable(Color.GRAY));
		getListView().setDividerHeight(1);  

		
		
    }

    @Override
    public void onResume() {
        super.onResume();

		
		Log.w(TAG, "in onResume.");
        // Refresh internal states in mInputMethodSettingValues to keep the latest
        // "InputMethodInfo"s and "InputMethodSubtype"s
        InputMethodSettingValuesWrapper
                .getInstance(getActivity()).refreshAllInputMethodAndSubtypes();
        InputMethodAndSubtypeUtil.loadInputMethodSubtypeList(
                this, getContentResolver(), mInputMethodProperties, mInputMethodAndSubtypePrefsMap);
        updateAutoSelectionCB();
    }

    @Override
    public void onPause() {

	
        super.onPause();

		
		Log.w(TAG, "in onPause.");
        // Clear all subtypes of all IMEs to make sure
        clearImplicitlyEnabledSubtypes(null);
        InputMethodAndSubtypeUtil.saveInputMethodSubtypeList(this, getContentResolver(),
                mInputMethodProperties, mHaveHardKeyboard);
    }

    @Override
    public boolean onPreferenceTreeClick( PreferenceScreen preferenceScreen, Preference preference ) {
        Log.w(TAG, "in onPreferenceTreeClick.");
        if( preference instanceof CheckBoxPreference ){
            final CheckBoxPreference chkPref = (CheckBoxPreference) preference;
            Log.w( TAG, "chkPref = " + chkPref );
            for (String imiId: mSubtypeAutoSelectionCBMap.keySet()) {
			  Log.w( TAG, "imiId = " + imiId );
			  Log.w( TAG, "mSubtypeAutoSelectionCBMap.get(imiId) = " + mSubtypeAutoSelectionCBMap.get(imiId) );
                if (mSubtypeAutoSelectionCBMap.get(imiId) == chkPref) {
                    // We look for the first preference item in subtype enabler.
                    // The first item is used for turning on/off subtype auto selection.
                    // We are in the subtype enabler and trying selecting subtypes automatically.
                    Log.w( TAG, "imiId = " + imiId );
                    Log.w( TAG, "chkPref.isChecked() = " + chkPref.isChecked() );
					
                    setSubtypeAutoSelectionEnabled(imiId, chkPref.isChecked());
                    return super.onPreferenceTreeClick(preferenceScreen, preference);
                }
            }

            final String id = chkPref.getKey();
            Log.w( TAG, "id = " + id );
            if (chkPref.isChecked()) {
                Log.w( TAG, "isChecked1");				
                InputMethodInfo selImi = null;
                final int N = mInputMethodProperties.size();
                Log.w( TAG, "N = " + N );
                for (int i = 0; i < N; i++) {
                    InputMethodInfo imi = mInputMethodProperties.get(i);
                    if (id.equals(imi.getId())) {
                        selImi = imi;
                        if (InputMethodUtils.isSystemIme(imi)) {
                            InputMethodAndSubtypeUtil.setSubtypesPreferenceEnabled(
                                    this, mInputMethodProperties, id, true);
                            // This is a built-in IME, so no need to warn.
                            return super.onPreferenceTreeClick(preferenceScreen, preference);
                        }
                        break;
                    }
                }
                Log.w( TAG, "selImi = " + selImi );
                if (selImi == null) {
                    return super.onPreferenceTreeClick(preferenceScreen, preference);
                }
                chkPref.setChecked(false);
                if (mDialog == null) {
                    mDialog = (new AlertDialog.Builder(getActivity()))
                            .setTitle(android.R.string.dialog_alert_title)
                            .setIconAttribute(android.R.attr.alertDialogIcon)
                            .setCancelable(true)
                            .setPositiveButton(android.R.string.ok,
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            chkPref.setChecked(true);
                                            InputMethodAndSubtypeUtil.setSubtypesPreferenceEnabled(
                                                    InputMethodAndSubtypeEnabler.this,
                                                    mInputMethodProperties, id, true);
                                        }

                            })
                            .setNegativeButton(android.R.string.cancel,
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                        }

                            })
                            .create();
                } else {
                    if (mDialog.isShowing()) {
                        mDialog.dismiss();
                    }
                }
                mDialog.setMessage(getResources().getString(
                        R.string.ime_security_warning,
                        selImi.getServiceInfo().applicationInfo.loadLabel(getPackageManager())));
                mDialog.show();
            } else {
                Log.w( TAG, "isChecked0");	             
                InputMethodAndSubtypeUtil.setSubtypesPreferenceEnabled(
                        this, mInputMethodProperties, id, false);
                updateAutoSelectionCB();
            }
        }
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

		Log.w(TAG, "in onDestroy.");
		
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }
    }

    private void onCreateIMM() {
        InputMethodManager imm = (InputMethodManager) getSystemService(
                Context.INPUT_METHOD_SERVICE);
      
	  Log.w(TAG, "in onCreateIMM.");
        // TODO: Change mInputMethodProperties to Map
        mInputMethodProperties = imm.getInputMethodList();
    }

    private PreferenceScreen createPreferenceHierarchy() {
        // Root
        final PreferenceScreen root = getPreferenceManager().createPreferenceScreen(getActivity());
        final Context context = getActivity();


		
		Log.w(TAG, "in createPreferenceHierarchy.");

        int N = (mInputMethodProperties == null ? 0 : mInputMethodProperties.size());

        for (int i = 0; i < N; ++i) {
            final InputMethodInfo imi = mInputMethodProperties.get(i);
            final int subtypeCount = imi.getSubtypeCount();
            if (subtypeCount <= 1) continue;
            final String imiId = imi.getId();
            // Add this subtype to the list when no IME is specified or when the IME of this
            // subtype is the specified IME.
            if (!TextUtils.isEmpty(mInputMethodId) && !mInputMethodId.equals(imiId)) {
                continue;
            }
            final PreferenceCategory keyboardSettingsCategory = new PreferenceCategory(context);
            root.addPreference(keyboardSettingsCategory);
            final PackageManager pm = getPackageManager();
            final CharSequence label = imi.loadLabel(pm);

            keyboardSettingsCategory.setTitle(label);
            keyboardSettingsCategory.setKey(imiId);
            // TODO: Use toggle Preference if images are ready.
            final CheckBoxPreference autoCB = new CheckBoxPreference(context);
            mSubtypeAutoSelectionCBMap.put(imiId, autoCB);
            keyboardSettingsCategory.addPreference(autoCB);

            final PreferenceCategory activeInputMethodsCategory = new PreferenceCategory(context);
            activeInputMethodsCategory.setTitle(R.string.active_input_method_subtypes);
            root.addPreference(activeInputMethodsCategory);

            boolean isAutoSubtype = false;
            CharSequence autoSubtypeLabel = null;
            final ArrayList<Preference> subtypePreferences = new ArrayList<Preference>();
            if (subtypeCount > 0) {
                for (int j = 0; j < subtypeCount; ++j) {
                    final InputMethodSubtype subtype = imi.getSubtypeAt(j);
                    final CharSequence subtypeLabel = subtype.getDisplayName(context,
                            imi.getPackageName(), imi.getServiceInfo().applicationInfo);
                    if (subtype.overridesImplicitlyEnabledSubtype()) {
                        if (!isAutoSubtype) {
                            isAutoSubtype = true;
                            autoSubtypeLabel = subtypeLabel;
                        }
                    } else {
                        final CheckBoxPreference chkbxPref = new SubtypeCheckBoxPreference(
                                context, subtype.getLocale(), mSystemLocale, mCollator);
                        chkbxPref.setKey(imiId + subtype.hashCode());
                        chkbxPref.setTitle(subtypeLabel);
                        subtypePreferences.add(chkbxPref);
                    }
                }
                Collections.sort(subtypePreferences);
                for (int j = 0; j < subtypePreferences.size(); ++j) {
                    activeInputMethodsCategory.addPreference(subtypePreferences.get(j));
                }
                mInputMethodAndSubtypePrefsMap.put(imiId, subtypePreferences);
            }
            if (isAutoSubtype) {
                if (TextUtils.isEmpty(autoSubtypeLabel)) {
                    Log.w(TAG, "Title for auto subtype is empty.");
                    autoCB.setTitle("---");
                } else {
                    autoCB.setTitle(autoSubtypeLabel);
                }
            } else {
                autoCB.setTitle(R.string.use_system_language_to_select_input_method_subtypes);
            }
        }
        return root;
    }

    private boolean isNoSubtypesExplicitlySelected(String imiId) {
        boolean allSubtypesOff = true;
        final List<Preference> subtypePrefs = mInputMethodAndSubtypePrefsMap.get(imiId);

		
		Log.w(TAG, "in isNoSubtypesExplicitlySelected.");


        for (Preference subtypePref: subtypePrefs) {
            if (subtypePref instanceof CheckBoxPreference
                    && ((CheckBoxPreference)subtypePref).isChecked()) {
                allSubtypesOff = false;
                break;
            }
        }
        return allSubtypesOff;
    }

    private void setSubtypeAutoSelectionEnabled(String imiId, boolean autoSelectionEnabled) {
        CheckBoxPreference autoSelectionCB = mSubtypeAutoSelectionCBMap.get(imiId);
        Log.w( TAG, "autoSelectionCB = " + autoSelectionCB );
        Log.w( TAG, "autoSelectionEnabled = " + autoSelectionEnabled );
        if (autoSelectionCB == null) return;
        autoSelectionCB.setChecked(autoSelectionEnabled);
        final List<Preference> subtypePrefs = mInputMethodAndSubtypePrefsMap.get(imiId);

		//Log.w( TAG, "subtypePrefs = " + subtypePrefs );
		
        for (Preference subtypePref: subtypePrefs) {
            if (subtypePref instanceof CheckBoxPreference) {
                // When autoSelectionEnabled is true, all subtype prefs need to be disabled with
                // implicitly checked subtypes. In case of false, all subtype prefs need to be
                // enabled.
                //Log.w( TAG, "subtypePref = " + subtypePref );
				
                subtypePref.setEnabled(!autoSelectionEnabled);
                if (autoSelectionEnabled) {
                    ((CheckBoxPreference)subtypePref).setChecked(false);
                }
            }
        }
        if (autoSelectionEnabled) {
            InputMethodAndSubtypeUtil.saveInputMethodSubtypeList(this, getContentResolver(),
                    mInputMethodProperties, mHaveHardKeyboard);
            setCheckedImplicitlyEnabledSubtypes(imiId);
        }
    }

    private void setCheckedImplicitlyEnabledSubtypes(String targetImiId) {
        updateImplicitlyEnabledSubtypes(targetImiId, true);
    }

    private void clearImplicitlyEnabledSubtypes(String targetImiId) {
        updateImplicitlyEnabledSubtypes(targetImiId, false);
    }

    private void updateImplicitlyEnabledSubtypes(String targetImiId, boolean check) {
        // When targetImiId is null, apply to all subtypes of all IMEs
        for (InputMethodInfo imi: mInputMethodProperties) {
            String imiId = imi.getId();
            if (targetImiId != null && !targetImiId.equals(imiId)) continue;
            final CheckBoxPreference autoCB = mSubtypeAutoSelectionCBMap.get(imiId);
            // No need to update implicitly enabled subtypes when the user has unchecked the
            // "subtype auto selection".
            if (autoCB == null || !autoCB.isChecked()) continue;
            final List<Preference> subtypePrefs = mInputMethodAndSubtypePrefsMap.get(imiId);
            final List<InputMethodSubtype> implicitlyEnabledSubtypes =
                    mImm.getEnabledInputMethodSubtypeList(imi, true);
            if (subtypePrefs == null || implicitlyEnabledSubtypes == null) continue;
            for (Preference subtypePref: subtypePrefs) {
                if (subtypePref instanceof CheckBoxPreference) {
                    CheckBoxPreference cb = (CheckBoxPreference)subtypePref;
                    cb.setChecked(false);
                    if (check) {
                        for (InputMethodSubtype subtype: implicitlyEnabledSubtypes) {
                            String implicitlyEnabledSubtypePrefKey = imiId + subtype.hashCode();
                            if (cb.getKey().equals(implicitlyEnabledSubtypePrefKey)) {
                                cb.setChecked(true);
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    private void updateAutoSelectionCB() {
        for (String imiId: mInputMethodAndSubtypePrefsMap.keySet()) {
            setSubtypeAutoSelectionEnabled(imiId, isNoSubtypesExplicitlySelected(imiId));
        }
        setCheckedImplicitlyEnabledSubtypes(null);
    }

    private static class SubtypeCheckBoxPreference extends CheckBoxPreference {
        private final boolean mIsSystemLocale;
        private final boolean mIsSystemLanguage;
        private final Collator mCollator;

        public SubtypeCheckBoxPreference(
                Context context, String subtypeLocale, String systemLocale, Collator collator) {
            super(context);
            if (TextUtils.isEmpty(subtypeLocale)) {
                mIsSystemLocale = false;
                mIsSystemLanguage = false;
            } else {
                mIsSystemLocale = subtypeLocale.equals(systemLocale);
                mIsSystemLanguage = mIsSystemLocale
                        || subtypeLocale.startsWith(systemLocale.substring(0, 2));
            }
            mCollator = collator;
        }

        @Override
        public int compareTo(Preference p) {
            if (p instanceof SubtypeCheckBoxPreference) {
                final SubtypeCheckBoxPreference pref = ((SubtypeCheckBoxPreference)p);
                final CharSequence t0 = getTitle();
                final CharSequence t1 = pref.getTitle();
                if (TextUtils.equals(t0, t1)) {
                    return 0;
                }
                if (mIsSystemLocale) {
                    return -1;
                }
                if (pref.mIsSystemLocale) {
                    return 1;
                }
                if (mIsSystemLanguage) {
                    return -1;
                }
                if (pref.mIsSystemLanguage) {
                    return 1;
                }
                if (TextUtils.isEmpty(t0)) {
                    return 1;
                }
                if (TextUtils.isEmpty(t1)) {
                    return -1;
                }
                return mCollator.compare(t0.toString(), t1.toString());
            } else {
                Log.w(TAG, "Illegal preference type.");
                return super.compareTo(p);
            }
        }
    }
}
