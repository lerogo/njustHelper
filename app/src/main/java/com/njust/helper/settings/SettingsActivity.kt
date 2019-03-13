package com.njust.helper.settings

import android.os.Bundle
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.njust.helper.R
import com.njust.helper.activity.BaseActivity

class SettingsActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().replace(R.id.holder, HolderFragment()).commit()
        }
    }

    override fun layoutRes(): Int {
        return R.layout.activity_settings
    }

    class HolderFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            addPreferencesFromResource(R.xml.preference)

            val modePreference = findPreference("mode") as ListPreference
            setListModeSummary(modePreference, modePreference.value)
            modePreference.setOnPreferenceChangeListener { preference, newValue ->
                setListModeSummary(preference, newValue as String)
                CourseAlarms.registerCourseAlarm(context!!)
                true
            }

            val timePreference = findPreference("course_time") as TimeSettingPreference
            timePreference.setOnPreferenceChangeListener { _, _ ->
                CourseAlarms.registerCourseAlarm(context!!)
                true
            }
        }

        private fun setListModeSummary(preference: Preference, index: String) {
            preference.summary = resources.getStringArray(R.array.pref_mode_entry)[Integer.parseInt(index)]
        }
    }
}
