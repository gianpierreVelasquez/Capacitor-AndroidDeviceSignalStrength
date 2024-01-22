package com.gbvp.androidsignalstrength;

import android.Manifest;
import android.telephony.TelephonyManager;

import com.getcapacitor.JSObject;
import com.getcapacitor.PermissionState;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.getcapacitor.annotation.Permission;
import com.getcapacitor.annotation.PermissionCallback;

import java.util.List;

@CapacitorPlugin(
        name = "SignalStrength",
        permissions = {
                @Permission(alias = SignalStrengthPlugin.PHONE_STATE, strings = {Manifest.permission.READ_PHONE_STATE}),
                @Permission(alias = SignalStrengthPlugin.LOCATION, strings = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}),
                @Permission(alias = SignalStrengthPlugin.COARSE_LOCATION, strings = { Manifest.permission.ACCESS_COARSE_LOCATION })
        }
)
public class SignalStrengthPlugin extends Plugin {

    public static final String PHONE_STATE = "phone_state";
    public static final String LOCATION = "location";
    public static final String COARSE_LOCATION = "coarse_Location";
    private SignalStrength implementation;
    private TelephonyManager tm;

    @Override
    public void load() {
        implementation = new SignalStrength(getContext());
        tm = implementation.getPhoneState();
    }

    @PluginMethod
    public void getdBm(final PluginCall call) {
        if (getPermissionState(SignalStrengthPlugin.PHONE_STATE) != PermissionState.GRANTED && getPermissionState(SignalStrengthPlugin.LOCATION) != PermissionState.GRANTED && getPermissionState(SignalStrengthPlugin.COARSE_LOCATION) != PermissionState.GRANTED) {
            String[] aliases = getAliases(call);
            requestPermissionForAliases(aliases, call, "requestPermissionsDBmCallback");
        } else {
            JSObject obj = implementation.getInfo(tm);
            call.resolve(getJSObjectForDBmInfo(obj.getInteger("dBmlevel")));
        }
    }

    @PluginMethod
    public void getPercentage(final PluginCall call) {
        if (getPermissionState(SignalStrengthPlugin.PHONE_STATE) != PermissionState.GRANTED && getPermissionState(SignalStrengthPlugin.LOCATION) != PermissionState.GRANTED && getPermissionState(SignalStrengthPlugin.COARSE_LOCATION) != PermissionState.GRANTED) {
            String[] aliases = getAliases(call);
            requestPermissionForAliases(aliases, call, "requestPermissionsPercentageCallback");
        } else {
            JSObject obj = implementation.getInfo(tm);
            String status = call.getString("connection");
            call.resolve(getJSObjectForPercentageInfo(status, obj));
        }
    }

    @PluginMethod
    public void getLevel(final PluginCall call) {
        if (getPermissionState(SignalStrengthPlugin.PHONE_STATE) != PermissionState.GRANTED && getPermissionState(SignalStrengthPlugin.LOCATION) != PermissionState.GRANTED && getPermissionState(SignalStrengthPlugin.COARSE_LOCATION) != PermissionState.GRANTED) {
            String[] aliases = getAliases(call);
            requestPermissionForAliases(aliases, call, "requestPermissionsLevelCallback");
        } else {
            JSObject obj = implementation.getInfo(tm);
            call.resolve(getJSObjectForLevelInfo(obj.getInteger("signalLevel")));
        }
    }

    @Override
    @PluginMethod
    public void checkPermissions(PluginCall call) {
        if (implementation.isPhoneStateEnabled() && implementation.isLocationServicesEnabled()) {
            super.checkPermissions(call);
        } else {
            call.reject("Permissions are not granted.");
        }
    }

    @Override
    @PluginMethod
    public void requestPermissions(PluginCall call) {
        if (implementation.isPhoneStateEnabled() && implementation.isLocationServicesEnabled()) {
            super.requestPermissions(call);
        } else {
            call.reject("Permissions are not granted.");
        }
    }

    @PermissionCallback
    private void requestPermissionsDBmCallback(PluginCall call) {
        if (getPermissionState(SignalStrengthPlugin.PHONE_STATE) == PermissionState.GRANTED && getPermissionState(SignalStrengthPlugin.LOCATION) == PermissionState.GRANTED && getPermissionState(SignalStrengthPlugin.COARSE_LOCATION) == PermissionState.GRANTED) {
            JSObject obj = implementation.getInfo(tm);
            call.resolve(getJSObjectForDBmInfo(obj.getInteger("dBmlevel")));
        } else {
            call.reject("Failed to retrieve signal strength.");
        }
    }

    @PermissionCallback
    private void requestPermissionsPercentageCallback(PluginCall call) {
        if (getPermissionState(SignalStrengthPlugin.PHONE_STATE) == PermissionState.GRANTED && getPermissionState(SignalStrengthPlugin.LOCATION) == PermissionState.GRANTED && getPermissionState(SignalStrengthPlugin.COARSE_LOCATION) == PermissionState.GRANTED) {
            JSObject obj = implementation.getInfo(tm);
            String status = call.getString("connection");
            call.resolve(getJSObjectForPercentageInfo(status, obj));
        } else {
            call.reject("Failed to retrieve signal strength.");
        }
    }

    @PermissionCallback
    private void requestPermissionsLevelCallback(PluginCall call) {
        if (getPermissionState(SignalStrengthPlugin.PHONE_STATE) == PermissionState.GRANTED && getPermissionState(SignalStrengthPlugin.LOCATION) == PermissionState.GRANTED && getPermissionState(SignalStrengthPlugin.COARSE_LOCATION) == PermissionState.GRANTED) {
            JSObject obj = implementation.getInfo(tm);
            call.resolve(getJSObjectForLevelInfo(obj.getInteger("signalLevel")));
        } else {
            call.reject("Failed to retrieve signal strength.");
        }
    }

    private JSObject getJSObjectForDBmInfo(int dBmlevel) {
        JSObject ret = new JSObject();
        ret.put("dBm", dBmlevel);

        return ret;
    }

    // SIGNAL STRENGTH INFO
    /* **********************
                 MIN     MAX
    CDMA (2g)
        dBm =   -100    -75
        asu =   0       97
    LTE (4g)
        dBm =   -140    -44
        asu =   0(99)   97
    GSM (3g)
        dBm =   -120    -50
        asu =   0(99)   31
    WCDMA(3g)
        dBm =   -115    -50
        asu =   0(99)   31

    LINEAR =        100 * (1 - (((-dBmmax) - (-dBmlevel))/((-dBmmax) - (-dBmmin))));
    NOT LINEAR =    (2.0 * (dBmlevel + 100))/100

    ********************* */
    private JSObject getJSObjectForPercentageInfo(String status, JSObject obj) {
        JSObject ret = new JSObject();
        String result;

        if (status == "wifi") {
            if (obj.getInteger("dBmlevel") <= -100) {
                result = String.format("%.2f", 0);
            } else if (obj.getInteger("dBmlevel") >= -50) {
                result = String.format("%.2f", 1);
            } else {
                result = String.format("%.2f", (2.0 * (obj.getInteger("dBmlevel") + 100)) / 100);
            }
        } else {
            result = String.format("%.2f", 1.0 * obj.getInteger("asulevel") / obj.getInteger("asulevelmax"));
        }

        ret.put("percentage", result);
        return ret;
    }

    private JSObject getJSObjectForLevelInfo(int signalLevel) {
        JSObject ret = new JSObject();
        ret.put("level", signalLevel);

        return ret;
    }

    private String[] getAliases(PluginCall call) {
        String[] aliases = {SignalStrengthPlugin.PHONE_STATE, SignalStrengthPlugin.LOCATION, SignalStrengthPlugin.COARSE_LOCATION};
        return aliases;
    }
}
