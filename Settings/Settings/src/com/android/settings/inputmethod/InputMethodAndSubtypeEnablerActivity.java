/*
 * Copyright (C) 2011 The Android Open Source Project
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

import android.app.Fragment;
import android.content.Intent;
import android.preference.PreferenceActivity;




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







import com.android.settings.ChooseLockPassword.ChooseLockPasswordFragment;

public class InputMethodAndSubtypeEnablerActivity extends PreferenceActivity {
    @Override
    public Intent getIntent() {
        final Intent modIntent = new Intent(super.getIntent());
        if (!modIntent.hasExtra(EXTRA_SHOW_FRAGMENT)) {
            modIntent.putExtra(EXTRA_SHOW_FRAGMENT, InputMethodAndSubtypeEnabler.class.getName());
            modIntent.putExtra(EXTRA_NO_HEADERS, true);
        }
        return modIntent;
    }

    @Override
    protected boolean isValidFragment(String fragmentName) {
        if (InputMethodAndSubtypeEnabler.class.getName().equals(fragmentName)) return true;
        return false;
    }

	@Override
    protected void onCreate(Bundle savedInstanceState) {
		
		 super.onCreate(savedInstanceState);
		 	
         this.getWindow().setBackgroundDrawable(new ColorDrawable(Color.rgb(0x36, 0x4b, 0x60))); 

		

       
        
       

			
       

        
        //Override up navigation for multi-pane, since we handle it in the fragment breadcrumbs
        if( onIsMultiPane() ){
            getActionBar().setDisplayHomeAsUpEnabled(false);
            getActionBar().setHomeButtonEnabled(false);
        }
        //getActionBar().setDisplayHomeAsUpEnabled(true);
        //getActionBar().setHomeButtonEnabled(true);        
    }




	

}
