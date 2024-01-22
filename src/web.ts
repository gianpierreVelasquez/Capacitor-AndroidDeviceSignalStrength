import { WebPlugin } from '@capacitor/core';

import type { PermissionStatus, SignalStrengthPlugin } from './definitions';

export class SignalStrengthWeb
  extends WebPlugin
  implements SignalStrengthPlugin
{
  getdBm(): Promise<any> {
    throw new Error('Method not implemented.');
  }
  getPercentage(): Promise<any> {
    throw new Error('Method not implemented.');
  }
  getLevel(): Promise<any> {
    throw new Error('Method not implemented.');
  }
  checkPermissions(): Promise<PermissionStatus> {
    throw new Error('Method not implemented.');
  }
  requestPermissions(): Promise<PermissionStatus> {
    throw new Error('Method not implemented.');
  }
}
