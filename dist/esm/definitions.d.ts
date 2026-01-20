import type { PermissionState } from '@capacitor/core';
import type { ConnectionType } from '@capacitor/network';
export interface SignalStrengthPlugin {
    getdBm(): Promise<DBm>;
    getPercentage(options: {
        connection: ConnectionType;
    }): Promise<Percentage>;
    getLevel(): Promise<Level>;
    checkPermissions(): Promise<PermissionStatus>;
    requestPermissions(): Promise<PermissionStatus>;
}
export interface DBm {
    dBm: number;
}
export interface Percentage {
    percentage: string;
}
export interface Level {
    level: number;
}
export interface PermissionStatus {
    info: PermissionState;
}
