package com.furqoncreative.submission5.view.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;

import com.furqoncreative.submission5.R;
import com.furqoncreative.submission5.scheduler.MovieReminder;

import java.util.Objects;

import butterknife.BindString;
import butterknife.ButterKnife;

public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener, Preference.OnPreferenceClickListener {
    private final MovieReminder movieReminder = new MovieReminder();
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
        ButterKnife.bind(this, Objects.requireNonNull(getActivity()));
        addPreferencesFromResource(R.xml.preferences);
        init();
        setupValue();
    }


    private void init() {
        dailySwitch = (SwitchPreference) findPreference(daily_remainder);
        releaseSwitch = (SwitchPreference) findPreference(release_remainder);
        findPreference(change_language).setOnPreferenceClickListener(this);

    }

    private void setupValue() {
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
            boolean daily = sharedPreferences.getBoolean(daily_remainder, false);
            dailySwitch.setChecked(daily);
            if (daily) {
                String timeDaily = "07:00";
                movieReminder.setRepeatingAlarm(getActivity(), MovieReminder.TYPE_DAILY, timeDaily, getString(R.string.daily_reminder_title), getString(R.string.daily_reminder_message));
            } else {
                movieReminder.cancelAlarm(getActivity(), MovieReminder.TYPE_DAILY);
            }
        }

        if (key.equals(release_remainder)) {
            boolean release = sharedPreferences.getBoolean(release_remainder, false);
            releaseSwitch.setChecked(release);
            if (release) {
                String timeRelease = "08:00";
                movieReminder.setRepeatingAlarm(getActivity(), MovieReminder.TYPE_RELEASE, timeRelease, getString(R.string.release_reminder_title), getString(R.string.release_reminder_message));
            } else {
                movieReminder.cancelAlarm(getActivity(), MovieReminder.TYPE_RELEASE);
            }
        }
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        String key = preference.getKey();

        if (key.equals(change_language)) {
            Intent intent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
            startActivity(intent);
            Objects.requireNonNull(getActivity()).finish();

            return true;
        }

        return false;
    }
}
