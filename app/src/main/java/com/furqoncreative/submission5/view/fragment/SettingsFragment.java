package com.furqoncreative.submission5.view.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;
import androidx.preference.SwitchPreference;

import com.furqoncreative.submission5.R;

import butterknife.BindString;
import butterknife.ButterKnife;

public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener, Preference.OnPreferenceClickListener {
    @BindString(R.string.key_daily)
    String daily_remainder;
    @BindString(R.string.key_relase)
    String release_remainder;
    @BindString(R.string.key_language)
    String change_language;
    private SwitchPreference dailySwitch;
    private SwitchPreference releaseSwitch;

    public SettingsFragment() {
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        ButterKnife.bind(this, getActivity());
        addPreferencesFromResource(R.xml.preferences);
        init();
        setSummaries();
    }

    private void init() {
        dailySwitch = (SwitchPreference) findPreference(daily_remainder);
        releaseSwitch = (SwitchPreference) findPreference(release_remainder);
        findPreference(change_language).setOnPreferenceClickListener(this);

    }

    private void setSummaries() {
        SharedPreferences sh = getPreferenceManager().getSharedPreferences();
        dailySwitch.setChecked(sh.getBoolean(daily_remainder, false));
        releaseSwitch.setChecked(sh.getBoolean(release_remainder, false));
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(daily_remainder)) {
            Boolean daily = sharedPreferences.getBoolean(daily_remainder, false);
            dailySwitch.setChecked(daily);
            if(daily){
                Toast.makeText(getContext(), "daily : "+ true, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "daily : "+ false, Toast.LENGTH_SHORT).show();
            }
        }

        if (key.equals(release_remainder)) {
            Boolean release = sharedPreferences.getBoolean(release_remainder, false);
            releaseSwitch.setChecked(release);
            if(release){
                Toast.makeText(getContext(), "release : "+ true, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "release : "+ false, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        String key = preference.getKey();

        if (key.equals(change_language)) {
            Intent intent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
            startActivity(intent);
            getActivity().finish();

            return true;
        }

        return false;
    }
}
