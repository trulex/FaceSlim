package org.indywidualni.fblite.util;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatTextView;

import org.indywidualni.fblite.R;

public final class DownloadManagerResolver {

    private static final String DOWNLOAD_MANAGER_PACKAGE_NAME = "com.android.providers.downloads";

    /**
     * Resolve whether the DownloadManager is enable in current devices.
     *
     * @return true if DownloadManager is enable, false otherwise.
     */
    public static boolean resolve(Context context) {
        boolean enable = resolveEnable(context);
        if (!enable) {
            AlertDialog alertDialog = createDialog(context);
            alertDialog.show();
        }
        return enable;
    }

    /**
     * Resolve whether the DownloadManager is enable in current devices.
     *
     * @param context
     * @return true if DownloadManager is enable, false otherwise.
     */
    private static boolean resolveEnable(Context context) {
        int state = context.getPackageManager()
                .getApplicationEnabledSetting(DOWNLOAD_MANAGER_PACKAGE_NAME);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR2) {
            return !(state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED ||
                    state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED_USER
                    || state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED_UNTIL_USED);
        } else {
            return !(state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED ||
                    state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED_USER);
        }
    }

    private static AlertDialog createDialog(final Context context) {
        AppCompatTextView messageTextView = new AppCompatTextView(context);
        messageTextView.setTextSize(16f);
        messageTextView.setText(context.getString(R.string.download_manager_disabled));
        return new AlertDialog.Builder(context)
                .setView(messageTextView, 50, 30, 50, 30)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        enableDownloadManager(context);
                    }
                })
                .setCancelable(false)
                .create();
    }

    /**
     * Start activity to enable DownloadManager in Settings.
     */
    private static void enableDownloadManager(Context context) {
        try {
            // open the specific App Info page
            Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.parse("package:" + DOWNLOAD_MANAGER_PACKAGE_NAME));
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();

            // open the generic Apps page
            Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS);
            context.startActivity(intent);
        }
    }

}
