import { registerPlugin } from '@capacitor/core';
const SignalStrength = registerPlugin('SignalStrength', {
    web: () => import('./web').then(m => new m.SignalStrengthWeb()),
});
export * from './definitions';
export { SignalStrength };
//# sourceMappingURL=index.js.map