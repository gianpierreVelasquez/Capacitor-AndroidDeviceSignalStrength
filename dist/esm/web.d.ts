import { WebPlugin } from '@capacitor/core';
import type { PermissionStatus, SignalStrengthPlugin } from './definitions';
export declare class SignalStrengthWeb extends WebPlugin implements SignalStrengthPlugin {
    getdBm(): Promise<any>;
    getPercentage(): Promise<any>;
    getLevel(): Promise<any>;
    checkPermissions(): Promise<PermissionStatus>;
    requestPermissions(): Promise<PermissionStatus>;
}
