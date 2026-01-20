package com.gbvp.androidsignalstrength;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.telephony.CellInfo;
import android.telephony.CellInfoCdma;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoNr;
import android.telephony.CellInfoWcdma;
import android.telephony.CellSignalStrengthCdma;
import android.telephony.CellSignalStrengthGsm;
import android.telephony.CellSignalStrengthLte;
import android.telephony.CellSignalStrengthNr;
import android.telephony.CellSignalStrengthWcdma;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;

import androidx.annotation.NonNull;

import com.getcapacitor.JSObject;

import java.util.List;

public class SignalStrength {

    private final Context context;

    public SignalStrength(@NonNull Context context) {
        this.context = context;
    }

    @SuppressLint("MissingPermission")
    private Integer getActiveDataSubscriptionId() {
        SubscriptionManager sm = (SubscriptionManager) context.getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE);

        if (sm == null) return null;

        SubscriptionInfo active = null;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            active = sm.getActiveSubscriptionInfo(SubscriptionManager.getDefaultDataSubscriptionId());
        }

        return active != null ? active.getSubscriptionId() : null;
    }

    @SuppressLint("MissingPermission")
    public JSObject getSignalInfo() {
        JSObject ret = new JSObject();

        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (tm == null) {
            ret.put("error", "TelephonyManager unavailable");
            return ret;
        }

        Integer activeId = getActiveDataSubscriptionId();
        if (activeId == null) {
            ret.put("error", "No active data SIM");
            return ret;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tm = tm.createForSubscriptionId(activeId);
        }

        List<CellInfo> cells = tm.getAllCellInfo();
        
        if (cells == null || cells.isEmpty()) {
            ret.put("error", "No cell info available");
            return ret;
        }

        for (CellInfo info : cells) {
            if (!info.isRegistered()) continue;

            // --- GSM ---
            if (info instanceof CellInfoGsm) {
                CellSignalStrengthGsm ss = ((CellInfoGsm) info).getCellSignalStrength();
                return makeResponse("GSM", ss.getDbm(), ss.getAsuLevel(), ss.getLevel(), 31);
            }

            // --- CDMA ---
            if (info instanceof CellInfoCdma) {
                CellSignalStrengthCdma ss = ((CellInfoCdma) info).getCellSignalStrength();
                return makeResponse("CDMA", ss.getDbm(), ss.getAsuLevel(), ss.getLevel(), 97);
            }

            // --- WCDMA ---
            if (info instanceof CellInfoWcdma) {
                CellSignalStrengthWcdma ss = ((CellInfoWcdma) info).getCellSignalStrength();
                return makeResponse("WCDMA", ss.getDbm(), ss.getAsuLevel(), ss.getLevel(), 31);
            }

            // --- LTE ---
            if (info instanceof CellInfoLte) {
                CellSignalStrengthLte ss = ((CellInfoLte) info).getCellSignalStrength();
                return makeResponse("LTE", ss.getDbm(), ss.getAsuLevel(), ss.getLevel(), 97);
            }

            // --- NR / 5G ---
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && info instanceof CellInfoNr) {
                CellSignalStrengthNr ss = (CellSignalStrengthNr) ((CellInfoNr) info).getCellSignalStrength();
                return makeResponse("NR", ss.getDbm(), ss.getAsuLevel(), ss.getLevel(), 97);
            }
        }

        ret.put("error", "No registered cell found");
        return ret;
    }

    private JSObject makeResponse(String type, int dBm, int asu, int level, int asuMax) {
        JSObject o = new JSObject();
        o.put("type", type);
        o.put("dBm", dBm);
        o.put("asu", asu);
        o.put("level", level);
        o.put("asuMax", asuMax);
        return o;
    }
}
