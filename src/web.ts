import { WebPlugin } from '@capacitor/core';

import type { SignalStrengthPlugin } from './definitions';

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
}
