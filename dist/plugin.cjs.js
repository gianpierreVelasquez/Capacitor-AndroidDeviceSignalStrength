'use strict';

Object.defineProperty(exports, '__esModule', { value: true });

var core = require('@capacitor/core');

const SignalStrength = core.registerPlugin('SignalStrength', {
    web: () => Promise.resolve().then(function () { return web; }).then(m => new m.SignalStrengthWeb()),
});

class SignalStrengthWeb extends core.WebPlugin {
    getdBm() {
        throw new Error('Method not implemented.');
    }
    getPercentage() {
        throw new Error('Method not implemented.');
    }
    getLevel() {
        throw new Error('Method not implemented.');
    }
    checkPermissions() {
        throw new Error('Method not implemented.');
    }
    requestPermissions() {
        throw new Error('Method not implemented.');
    }
}

var web = /*#__PURE__*/Object.freeze({
    __proto__: null,
    SignalStrengthWeb: SignalStrengthWeb
});

exports.SignalStrength = SignalStrength;
//# sourceMappingURL=plugin.cjs.js.map
