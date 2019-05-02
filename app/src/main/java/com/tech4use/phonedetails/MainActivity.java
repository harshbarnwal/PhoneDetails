package com.tech4use.phonedetails;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    TextView txt;
    Button btn;
    TelephonyManager tm;
    int READ_PHONE_STATE_CODE = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //checking for the requested permission
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "You have already granted this permission",
                    Toast.LENGTH_SHORT).show();
        } else {
            request_Read_Phone_State_permission();
        }

        txt = findViewById(R.id.textview);
        btn = findViewById(R.id.button);

        //getting instance of telephony manager
        tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
    }

    public void Click(View view) {

        //checking the permission
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
            }
        //calling the methods of the telephony to return the information
        String IMEI_Number = tm.getDeviceId();
        String subscriber_ID = tm.getSubscriberId();
        String SIMSerial_Number = tm.getSimSerialNumber();
        String networkCountry_ISO = tm.getNetworkCountryIso();
        String SIMCOuntry_ISO = tm.getSimCountryIso();
        String Software_version = tm.getDeviceSoftwareVersion();
        String VoiceMail_Number = tm.getVoiceMailNumber();

        //getting the phone type
        String strPhone_Type = "";
        int phoneType = tm.getPhoneType();
        switch (phoneType) {
            case (TelephonyManager.PHONE_TYPE_CDMA) :
                strPhone_Type = "CDMA";
                break;
            case (TelephonyManager.PHONE_TYPE_GSM) :
                strPhone_Type = "GSM";
                break;
            case (TelephonyManager.PHONE_TYPE_NONE) :
                strPhone_Type = "None";
                break;
        }

        //getting the information if the phone is roaming??
        boolean isRoaming = tm.isNetworkRoaming();

        //getting and setting the result
        String phone_info = "Phone Details :\n";
        phone_info+="\n IMEI Number = "+IMEI_Number;
        phone_info+="\n Subscriber ID = "+subscriber_ID;
        phone_info+="\n SIM Serial Number = "+SIMSerial_Number;
        phone_info+="\n Network Country ISO = "+networkCountry_ISO;
        phone_info+="\n Sim COuntry ISO = "+SIMCOuntry_ISO;
        phone_info+="\n Software Version = "+Software_version;
        phone_info+="\n Voice Mail Number = "+VoiceMail_Number;
        phone_info+="\n Phone Network Type = "+strPhone_Type;
        phone_info+="\n In Roaming  = "+isRoaming;

        txt.setText(phone_info);
    }

    /**requesting the needed permissions
     * here we only want one permission
     * read phone state permission
     */
    private void request_Read_Phone_State_permission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_PHONE_STATE)) {

            //creating the alert dialog
            new AlertDialog.Builder(this)
                    .setTitle("Permission Needed")
                    .setMessage("This permission is needed to read the data")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(MainActivity.this, new String[] {
                                    Manifest.permission.READ_PHONE_STATE}, READ_PHONE_STATE_CODE);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).create().show();
        } else {
            ActivityCompat.requestPermissions(this, new String[] {
                    Manifest.permission.READ_PHONE_STATE}, READ_PHONE_STATE_CODE);
        }
    }

    //now creating thw permission by overriding onrequestpermissionresult
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == READ_PHONE_STATE_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
