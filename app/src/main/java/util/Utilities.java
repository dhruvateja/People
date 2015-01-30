package util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

/**
 * Created by Svaad on 29-01-2015.
 */
public class Utilities {
    public static Utilities mUtil;
    public static final String DATE_TIME_STAMP_PATTERN = "yyyy-MM-dd_HHmmss";
    private static String TAG = "Util";

    public static Utilities getInstance () {
        if (mUtil == null)
            mUtil = new Utilities();
        return mUtil;
    }
    public  boolean isLocationEnabled(Context context) {
        int locationMode = 0;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);

            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
            }

            return locationMode != Settings.Secure.LOCATION_MODE_OFF;

        }else{
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }


    }
    public void showLocationServiceDisabledAlertToUser(final Context context) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);
        alertDialogBuilder
                .setTitle("Location services are disabled")
                .setMessage(
                        "Hello needs access to your location.Please turn on location access.")
                .setCancelable(false)
                .setPositiveButton("Settings",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent callGPSSettingIntent = new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                context.startActivity(callGPSSettingIntent);
                            }
                        });
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }
    public void showGooglePlayServiceDisabledAlertToUser(final Context context,String text) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);
        alertDialogBuilder
                .setTitle("Google play services are disabled")
                .setMessage(
                        text)
                .setCancelable(false)
                .setPositiveButton("Settings",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                final String appPackageName = "com.google.android.gms"; // getPackageName() from Context or Activity object
                                try {
                                    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                                } catch (android.content.ActivityNotFoundException anfe) {
                                    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appPackageName)));
                                }
                            }
                        });
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }
    public String loadDataFromUrl(String url) {
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(url);
        InputStream is = null;
        String data = null;

        try {
            HttpResponse httpResponse = httpClient.execute(httpGet);
            int response = httpResponse.getStatusLine().getStatusCode();
            System.out.println("phani response code = " + response);
            if (response == 200) {
                HttpEntity httpEntity = httpResponse.getEntity();
                is = httpEntity.getContent();

                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
                StringBuilder sb = new StringBuilder();
                String line;
                if ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                is.close();
                data = sb.toString();
            } else if (response == 204) {
                data = "No Data Found";

                // is = httpEntity.getContent();

            } else {
                data = null;
            }

            System.out.println("phani api data " + data);
        } catch (UnsupportedEncodingException e) {
            //Logger.e("Parsing Error", "Error getting data", e);
            data = "Error:loading: Failed to load data";
        } catch (ClientProtocolException e) {
            //Logger.e("Parsing Error", "Error getting data", e);
            data = "Error:loading: Failed to load data";
        } catch (IOException e) {
            //Logger.e("Parsing Error", "Error getting data", e);
            data = "Error:loading: Failed to load data";
        }

        return data;
    }
}
