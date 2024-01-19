export interface SignalStrengthPlugin {
  getdBm(): Promise<any>;
  getPercentage(): Promise<any>;
  getLevel(): Promise<any>;
}
