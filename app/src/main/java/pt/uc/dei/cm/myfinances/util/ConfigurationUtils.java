package pt.uc.dei.cm.myfinances.util;


import android.content.Context;
import com.firebase.ui.auth.AuthUI;

import androidx.annotation.NonNull;
import pt.uc.dei.cm.myfinances.myfinances.R;

public final class ConfigurationUtils {

    private ConfigurationUtils() {
        throw new AssertionError("No instance for you!");
    }

    public static boolean isGoogleMisconfigured(@NonNull Context context) {
        return AuthUI.UNCONFIGURED_CONFIG_VALUE.equals(
                context.getString(R.string.default_web_client_id));
    }
}
