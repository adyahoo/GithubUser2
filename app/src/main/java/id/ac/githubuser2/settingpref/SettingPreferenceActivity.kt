package id.ac.githubuser2.settingpref

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import id.ac.githubuser2.R

class SettingPreferenceActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting_preference)

        supportFragmentManager.beginTransaction()
            .add(R.id.setting_holder, SettingPreferenceManager(this))
            .commit()

    }
}