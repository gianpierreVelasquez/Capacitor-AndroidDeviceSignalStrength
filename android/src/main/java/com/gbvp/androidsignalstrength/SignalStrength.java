package com.gbvp.androidsignalstrength;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.LocationManager;
import android.os.Build;
import android.telephony.CellInfo;
import android.telephony.CellInfoCdma;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoWcdma;
import android.telephony.CellSignalStrengthCdma;
import android.telephony.CellSignalStrengthGsm;
import android.telephony.CellSignalStrengthLte;
import android.telephony.CellSignalStrengthWcdma;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import androidx.core.location.LocationManagerCompat;

import com.getcapacitor.JSObject;

import java.util.List;

public class SignalStrength {

    class MyPhoneStateListener extends PhoneStateListener {
        @Override
        public void onSignalStrengthsChanged(android.telephony.SignalStrength signalStrength) {
            super.onSignalStrengthsChanged(signalStrength);
            asulevel = signalStrength.getGsmSignalStrength();
        }
    }

    public int asulevel = -1;
    public int asulevelmax = 31;
    public int dBmlevel = 0;
    public String signalLevel;
    private Context context;

    public SignalStrength(Context context) {
        this.context = context;
    }

    @SuppressLint("MissingPermission")
    public Boolean isPhoneStateEnabled() {
        Boolean phoneStateEnabled = false;
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            phoneStateEnabled = tm.isDataEnabled();
        }

        return phoneStateEnabled;
    }

    @SuppressLint("MissingPermission")
    public Boolean isLocationServicesEnabled() {
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return LocationManagerCompat.isLocationEnabled(lm);
    }

    @SuppressLint("MissingPermission")
    public JSObject getInfo(TelephonyManager tm) {
        JSObject ret = new JSObject();
        List<CellInfo> cellInfoList = tm.getAllCellInfo();
        // Checking if list values are not null
        if (cellInfoList != null) {
            for (final CellInfo info : cellInfoList) {
                if (info instanceof CellInfoGsm) {
                    //GSM Network
                    CellSignalStrengthGsm cellSignalStrength = ((CellInfoGsm) info).getCellSignalStrength();
                    ret.put("dBmlevel", cellSignalStrength.getDbm());
                    ret.put("asulevel", cellSignalStrength.getAsuLevel());
                    ret.put("signalLevel", cellSignalStrength.getLevel() + "");
                    ret.put("asulevelmax", 31);
                } else if (info instanceof CellInfoCdma) {
                    //CDMA Network
                    CellSignalStrengthCdma cellSignalStrength = ((CellInfoCdma) info).getCellSignalStrength();
                    ret.put("dBmlevel", cellSignalStrength.getDbm());
                    ret.put("asulevel", cellSignalStrength.getAsuLevel());
                    ret.put("signalLevel", cellSignalStrength.getLevel() + "");
                    ret.put("asulevelmax", 97);
                } else if (info instanceof CellInfoLte) {
                    //LTE Network
                    CellSignalStrengthLte cellSignalStrength = ((CellInfoLte) info).getCellSignalStrength();
                    ret.put("dBmlevel", cellSignalStrength.getDbm());
                    ret.put("asulevel", cellSignalStrength.getAsuLevel());
                    ret.put("signalLevel", cellSignalStrength.getLevel() + "");
                    ret.put("asulevelmax", 97);
                } else if (info instanceof CellInfoWcdma) {
                    //WCDMA Network
                    CellSignalStrengthWcdma cellSignalStrength = ((CellInfoWcdma) info).getCellSignalStrength();
                    ret.put("dBmlevel", cellSignalStrength.getDbm());
                    ret.put("asulevel", cellSignalStrength.getAsuLevel());
                    ret.put("signalLevel", cellSignalStrength.getLevel() + "");
                    ret.put("asulevelmax", 31);
                } else {
                    throw new IllegalArgumentException("Unknown type of cell signal.");
                }
            }
        } else {
            //Mostly for Samsung devices, after checking if the list is indeed empty.
            try {
                MyPhoneStateListener myPhoneStateListener = new SignalStrength.MyPhoneStateListener();
                tm.listen(myPhoneStateListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
                int cc = 0;
                while (asulevel == -1) {
                    Thread.sleep(200);
                    if (cc++ >= 5) {
                        break;
                    }
                }
                ret.put("asulevelmax", 31);
                ret.put("dBmlevel", -113 + 2 * asulevel);
                tm.listen(myPhoneStateListener, PhoneStateListener.LISTEN_NONE);
                ret.put("signalLevel", String.format("%.0g%n", 1.0 * asulevel / asulevelmax * 4));
                ret.put("asulevel", asulevel);
            } catch (Exception e) {
                throw new IllegalArgumentException(e);
            }
        }

        return ret;
    }

    @SuppressWarnings("MissingPermission")
    public TelephonyManager getPhoneState() {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm;
    }
}
