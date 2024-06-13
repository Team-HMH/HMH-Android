package datastore

import android.content.SharedPreferences
import androidx.core.content.edit
import com.hmh.hamyeonham.usagestats.datastore.HMHDeletedAppUsagePreference
import org.json.JSONArray
import org.json.JSONException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultHMHDeletedAppUsagePreference @Inject constructor(
    private val preferences: SharedPreferences,
) : HMHDeletedAppUsagePreference {
    override var totalUsage: Long
        get() = preferences.getLong("total_usage", 0)
        set(value) {
            preferences.edit(commit = true) {
                putLong("total_usage", value)
            }
        }
    override var deletedPackageName: List<String>
        get() {
            val json = preferences.getString("deleted_package_name", "").orEmpty()
            val arrayList = ArrayList<String>()
            if (json != null) {
                try {
                    val jsonArray = JSONArray(json)
                    for (i in 0 until jsonArray.length()) arrayList.add(jsonArray.optString(i))
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
            return arrayList
        }
        set(values) {
            val jsonArray = JSONArray()
            for (value in values) {
                jsonArray.put(value)
            }
            preferences.edit(commit = true) {
                if (values.isNotEmpty()) {
                    putString("deleted_package_name", jsonArray.toString())
                } else {
                    putString("deleted_package_name", null)
                }
            }
        }

    override fun clear() {
        preferences.edit(commit = true) {
            clear()
        }
    }
}
