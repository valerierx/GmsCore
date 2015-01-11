package com.google.android.gms.common;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.IntentSender;
import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Contains all possible error codes for when a client fails to connect to Google Play services.
 * These error codes are used by {@link GoogleApiClient.OnConnectionFailedListener}.
 */
public class ConnectionResult {
    /**
     * The connection was successful.
     */
    public static final int SUCCESS = 0;
    /**
     * Google Play services is missing on this device. The calling activity should pass this error
     * code to {@link GooglePlayServicesUtil#getErrorDialog(int, Activity, int)} to get a localized
     * error dialog that will resolve the error when shown.
     */
    public static final int SERVICE_MISSING = 1;
    /**
     * The installed version of Google Play services is out of date. The calling activity should
     * pass this error code to {@link GooglePlayServicesUtil#getErrorDialog(int, Activity, int)} to
     * get a localized error dialog that will resolve the error when shown.
     */
    public static final int SERVICE_VERSION_UPDATE_REQUIRED = 2;
    /**
     * The installed version of Google Play services has been disabled on this device. The calling
     * activity should pass this error code to
     * {@link GooglePlayServicesUtil#getErrorDialog(int, Activity, int)} to get a localized error
     * dialog that will resolve the error when shown.
     */
    public static final int SERVICE_DISABLED = 3;
    /**
     * The client attempted to connect to the service but the user is not signed in. The client may
     * choose to continue without using the API or it may call
     * {@link #startResolutionForResult(Activity, int)} to prompt the user to sign in. After the
     * sign in activity returns with {@link Activity#RESULT_OK} further attempts to connect should
     * succeed.
     */
    public static final int SIGN_IN_REQUIRED = 4;
    /**
     * The client attempted to connect to the service with an invalid account name specified.
     */
    public static final int INVALID_ACCOUNT = 5;
    /**
     * Completing the connection requires some form of resolution. A resolution will be available
     * to be started with {@link #startResolutionForResult(Activity, int)}. If the result returned
     * is {@link Activity#RESULT_OK}, then further attempts to connect should either complete or
     * continue on to the next issue that needs to be resolved.
     */
    public static final int RESOLUTION_REQUIRED = 6;
    /**
     * A network error occurred. Retrying should resolve the problem.
     */
    public static final int NETWORK_ERROR = 7;
    /**
     * An internal error occurred. Retrying should resolve the problem.
     */
    public static final int INTERNAL_ERROR = 8;
    /**
     * The version of the Google Play services installed on this device is not authentic.
     */
    public static final int SERVICE_INVALID = 9;
    /**
     * The application is misconfigured. This error is not recoverable and will be treated as
     * fatal. The developer should look at the logs after this to determine more actionable
     * information.
     */
    public static final int DEVELOPER_ERROR = 10;
    /**
     * The application is not licensed to the user. This error is not recoverable and will be
     * treated as fatal.
     */
    public static final int LICENSE_CHECK_FAILED = 11;
    /**
     * The client canceled the connection by calling {@link GoogleApiClient#disconnect()}.
     * Only returned by {@link GoogleApiClient#blockingConnect()}.
     */
    public static final int CANCELED = 13;
    /**
     * The timeout was exceeded while waiting for the connection to complete. Only returned by
     * {@link GoogleApiClient#blockingConnect()}.
     */
    public static final int TIMEOUT = 14;
    /**
     * An interrupt occurred while waiting for the connection complete. Only returned by
     * {@link GoogleApiClient#blockingConnect()}.
     */
    public static final int INTERRUPTED = 15;
    /**
     * One of the API components you attempted to connect to is not available. The API will not
     * work on this device, and updating Google Play services will not likely solve the problem.
     * Using the API on the device should be avoided.
     */
    public static final int API_UNAVAILABLE = 16;

    /**
     * The Drive API requires external storage (such as an SD card), but no external storage is
     * mounted. This error is recoverable if the user installs external storage (if none is
     * present) and ensures that it is mounted (which may involve disabling USB storage mode,
     * formatting the storage, or other initialization as required by the device).
     * <p/>
     * This error should never be returned on a device with emulated external storage. On devices
     * with emulated external storage, the emulated "external storage" is always present regardless
     * of whether the device also has removable storage.
     */
    @Deprecated
    public static final int DRIVE_EXTERNAL_STORAGE_REQUIRED = 1500;

    private final PendingIntent pendingIntent;
    private final int statusCode;

    /**
     * Creates a connection result.
     *
     * @param statusCode    The status code.
     * @param pendingIntent A pending intent that will resolve the issue when started, or null.
     */
    public ConnectionResult(int statusCode, PendingIntent pendingIntent) {
        this.statusCode = statusCode;
        this.pendingIntent = pendingIntent;
    }

    /**
     * Indicates the type of error that interrupted connection.
     *
     * @return the error code, or {@link #SUCCESS} if no error occurred.
     */
    public int getErrorCode() {
        return this.statusCode;
    }

    /**
     * A pending intent to resolve the connection failure. This intent can be started with
     * {@link Activity#startIntentSenderForResult(IntentSender, int, Intent, int, int, int)} to
     * present UI to solve the issue.
     *
     * @return The pending intent to resolve the connection failure.
     */
    public PendingIntent getResolution() {
        return this.pendingIntent;
    }

    /**
     * Returns {@code true} if calling {@link #startResolutionForResult(Activity, int)} will start
     * any intents requiring user interaction.
     *
     * @return {@code true} if there is a resolution that can be started.
     */
    public boolean hasResolution() {
        return statusCode != 0 && pendingIntent != null;
    }

    /**
     * Returns {@code true} if the connection was successful.
     *
     * @return {@code true} if the connection was successful, {@code false} if there was an error.
     */
    public boolean isSuccess() {
        return this.statusCode == 0;
    }

    /**
     * Resolves an error by starting any intents requiring user interaction. See
     * {@link #SIGN_IN_REQUIRED}, and {@link #RESOLUTION_REQUIRED}.
     *
     * @param activity    An Activity context to use to resolve the issue. The activity's
     *                    {@link Activity#onActivityResult} method will be invoked after the user
     *                    is done. If the resultCode is {@link Activity#RESULT_OK}, the application
     *                    should try to connect again.
     * @param requestCode The request code to pass to {@link Activity#onActivityResult}.
     * @throws IntentSender.SendIntentException If the resolution intent has been canceled or is no
     *                                          longer able to execute the request.
     */
    public void startResolutionForResult(Activity activity, int requestCode) throws
            IntentSender.SendIntentException {
        if (hasResolution()) {
            activity.startIntentSenderForResult(pendingIntent.getIntentSender(), requestCode, null,
                    0, 0, 0);
        }
    }
}
