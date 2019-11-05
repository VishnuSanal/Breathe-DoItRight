package phone.vishnu.breathe_doitright.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.Calendar;

public class Prefs {
    private final SharedPreferences preferences;

    public Prefs(Activity activity) {
        this.preferences = activity.getPreferences(Context.MODE_PRIVATE);
    }

    public void setBreaths(int breaths) {
        preferences.edit().putInt("breaths", breaths).apply();
    }

    public int getBreaths() {
        return preferences.getInt("breaths", 0);
    }

    public void setSessions(int session) {
        preferences.edit().putInt("sessions", session).apply();
    }

    public int getSessions() {
        return preferences.getInt("sessions", 0);
    }

    public void setDate(long millis) {
        preferences.edit().putLong("seconds", millis).apply();
    }

    public String getDate() {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(preferences.getLong("seconds", 0));

        return ("Last session at " + c.get(Calendar.HOUR) + ":" + c.get(Calendar.MINUTE) + " " + ((c.get(Calendar.AM_PM)) == (Calendar.AM) ? "AM" : "PM"));
    }
}
