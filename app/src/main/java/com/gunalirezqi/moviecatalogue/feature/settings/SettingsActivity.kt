package com.gunalirezqi.moviecatalogue.feature.settings

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.gunalirezqi.moviecatalogue.R
import com.gunalirezqi.moviecatalogue.broadcast.DailyReminder
import com.gunalirezqi.moviecatalogue.broadcast.ReleaseTodayReminder

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.settings, SettingsFragment())
            .commit()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    class SettingsFragment : PreferenceFragmentCompat() {

        private lateinit var dailyReminder: DailyReminder
        private lateinit var releaseTodayReminder: ReleaseTodayReminder

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)

            dailyReminder = DailyReminder()
            releaseTodayReminder = ReleaseTodayReminder()

            val changeLanguge = this.findPreference<Preference>("change_language")
            changeLanguge?.setOnPreferenceClickListener {
                val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
                startActivity(mIntent)
                return@setOnPreferenceClickListener true
            }

            val switchReleaseTodayReminder = this.findPreference<SwitchPreference>("release_reminder")
            switchReleaseTodayReminder?.setOnPreferenceClickListener {
                if (switchReleaseTodayReminder.isChecked) {
                    setReleaseTodayReminder()
                } else {
                    cancelReleaseTodayReminder()
                }
                return@setOnPreferenceClickListener true
            }

            val switchDailyReminder = this.findPreference<SwitchPreference>("daily_reminder")
            switchDailyReminder?.setOnPreferenceClickListener {
                if (switchDailyReminder.isChecked) {
                    setDailyReminder()
                } else {
                    cancelDailyReminder()
                }
                return@setOnPreferenceClickListener true
            }
        }

        private fun setReleaseTodayReminder() {
            releaseTodayReminder.setReminder(context)
        }

        private fun cancelReleaseTodayReminder() {
            releaseTodayReminder.cancelReminder(context)
        }

        private fun setDailyReminder() {
            dailyReminder.setReminder(context)
        }

        private fun cancelDailyReminder() {
            dailyReminder.cancelReminder(context)
        }
    }
}