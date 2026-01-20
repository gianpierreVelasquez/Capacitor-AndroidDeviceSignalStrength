# 📶 Capacitor Signal Strength (Android)

A **Capacitor Android-only plugin** to retrieve **cellular signal strength information** such as:

- **dBm**
- **Signal level**
- **Percentage**
- **5G / LTE / WCDMA / GSM awareness**

Designed with **Capacitor 5 → 8 compatibility** and **correct Android permission handling**.

---

## ✨ Features

- ✅ Android only (no web / iOS shims)
- ✅ Supports **LTE, WCDMA, GSM, CDMA, 5G (NR)**
- ✅ Returns:
  - Signal strength in **dBm**
  - Normalized **signal level**
  - **Percentage**
- ✅ Uses Capacitor’s **native permission system**
- ✅ Compatible with **Capacitor 5, 6, 7, 8**
- ✅ Safe permission lifecycle (no crashes after prompt)
- ✅ Works with `@capacitor/network` for connection type

---

## ⚠️ Platform Support

| Platform | Supported |
|--------|-----------|
| Android | ✅ Yes |
| iOS | ❌ No |
| Web | ❌ No |

---

## 📦 Installation

```bash
npm install capacitor-signal-strength
npx cap sync android
```

# API Reference

## SignalStrengthPlugin

### Methods

- `checkPermissions(): Promise<PermissionStatus>`
- `requestPermissions(options): Promise<PermissionStatus>`
- `getdBm(): Promise<{ dBm: number }>`
- `getLevel(): Promise<{ level: number }>`
- `getPercentage(connection: ConnectionType): Promise<{ percentage: number }>`

---

## PermissionStatus

```ts
{
  phone: 'granted' | 'denied' | 'prompt'
}
```

### Compatibility

- Capacitor: v5+
- Android: API 26 (Android 8.0) and above
- 5G signal metrics are available on Android 11+