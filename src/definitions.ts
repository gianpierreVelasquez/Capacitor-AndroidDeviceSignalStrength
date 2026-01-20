import type { PermissionState } from '@capacitor/core';

export type SignalStrengthPermissionType = 'phone';

export interface DBm {
  dBm: number;
}

export interface Level {
  level: number;
}

export interface Percentage {
  percentage: number;
}

export interface PermissionStatus {
  phone: PermissionState;
}

export type ConnectionType = 'wifi' | 'cellular' | 'none' | 'unknown';

export interface SignalStrengthPlugin {
  /*
   * Get the signal strength as dBm.
   * Returns a value Number instead of an object.
  */
  getdBm(): Promise<DBm>;

  /*
   * Retrievean abstract level value for the overall signal quality.
   * Returns a value Number instead of an object.
  */
  getLevel(): Promise<Level>;

  /*
   * Get the signal strength as a percentage.
   * Returns a value Number instead of an object.
  */
  getPercentage(connection: ConnectionType):  Promise<Percentage>;

  /*
   * Check the current permission status.
   * New in version 1.0.0
  */
  checkPermissions(): Promise<PermissionStatus>;

  /*
   * Request the necessary permissions.
   * New in version 1.0.0
  */
  requestPermissions(): Promise<PermissionStatus>;
}