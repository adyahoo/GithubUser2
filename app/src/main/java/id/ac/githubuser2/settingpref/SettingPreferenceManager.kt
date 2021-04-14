package id.ac.githubuser2.settingpref

import android.app.NotificationManager
import android.content.SharedPreferences
import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import id.ac.githubuser2.R
import id.ac.githubuser2.settingpref.alarmmanager.AlarmReceiver

class SettingPreferenceManager(val context: SettingPreferenceActivity) : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {
    private lateinit var reminder: String
    private lateinit var isSet: SwitchPreference
    private val alarmReceiver: AlarmReceiver = AlarmReceiver()
    companion object{
        private const val NOTIF_ID = 1
        private const val TIME = "09:00"
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.setting_preference)
        init()
        setSummaries()
    }

    private fun init() {
        reminder = resources.getString(R.string.key_reminder)
        isSet = findPreference<SwitchPreference>(reminder) as SwitchPreference
    }

    private fun setSummaries() {
        val sh = preferenceManager.sharedPreferences
        isSet.isChecked = sh.getBoolean(reminder, false)

    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        val switchStatus = sharedPreferences.getBoolean(reminder, false)
        if (switchStatus) {
            alarmReceiver.setRepeatAlarm(context)
        } else {
            alarmReceiver.cancelAlarm(context)
        }
    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }
}