package com.gbvp.androidsignalstrength;

import android.Manifest;

import com.getcapacitor.JSObject;
import com.getcapacitor.PermissionState;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.getcapacitor.annotation.Permission;
import com.getcapacitor.annotation.PermissionCallback;

@CapacitorPlugin(
        name = "SignalStrength",
        permissions = {
                @Permission(
                        alias = SignalStrengthPlugin.PHONE,
                        strings = {
                                Manifest.permission.READ_PHONE_STATE,
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                        }
                )
        }
)
public class SignalStrengthPlugin extends Plugin {

    static final String PHONE = "phone";

    private SignalStrength provider;

    @Override
    public void load() {
        provider = new SignalStrength(getContext());
    }

    @PluginMethod
    public void checkPermissions(PluginCall call) {
        JSObject result = new JSObject();
        JSObject status = new JSObject();

        PermissionState phoneState = getPermissionState(PHONE);
        status.put("phone", phoneState.toString());

        result.put("permissions", status);
        call.resolve(result);
    }

    @PluginMethod
    public void requestPermissions(PluginCall call) {
        if (getPermissionState(PHONE) == PermissionState.GRANTED) {
            checkPermissions(call);
            return;
        }

        requestPermissionForAlias(PHONE, call, "permissionsCallback");
    }


    @PermissionCallback
    private void permissionsCallback(PluginCall call) {
        checkPermissions(call);
    }

    @PluginMethod
    public void getdBm(PluginCall call) {
        if (getPermissionState(PHONE) != PermissionState.GRANTED) {
            requestPermissionForAlias(PHONE, call, "getdBmCallback");
            return;
        }

        JSObject o = provider.getSignalInfo();
        call.resolve(new JSObject().put("dBm", o.getInteger("dBm")));
    }

    @PermissionCallback
    private void getdBmCallback(PluginCall call) {
        getdBm(call);
    }


    @PluginMethod
    public void getLevel(PluginCall call) {
        if (getPermissionState(PHONE) != PermissionState.GRANTED) {
            requestPermissionForAlias(PHONE, call, "getLevelCallback");
            return;
        }

        JSObject o = provider.getSignalInfo();
        call.resolve(new JSObject().put("level", o.getInteger("level")));
    }

    @PermissionCallback
    private void getLevelCallback(PluginCall call) {
        getLevel(call);
    }

    @PluginMethod
    public void getPercentage(PluginCall call) {
        if (getPermissionState(PHONE) != PermissionState.GRANTED) {
            requestPermissionForAlias(PHONE, call, "getPercentageCallback");
            return;
        }

        JSObject o = provider.getSignalInfo();
        String conn = call.getString("connection");

        double percentage;

        if ("wifi".equals(conn)) {
            int dBm = o.getInteger("dBm");
            if (dBm <= -100) percentage = 0;
            else if (dBm >= -50) percentage = 1;
            else percentage = (2.0 * (dBm + 100)) / 100;
        } else {
            percentage = (double) o.getInteger("asu") / o.getInteger("asuMax");
        }

        call.resolve(new JSObject().put("percentage", percentage));
    }

    @PermissionCallback
    private void getPercentageCallback(PluginCall call) {
        getPercentage(call);
    }
}