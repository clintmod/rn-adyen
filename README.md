# `react-native-adyen`

React Native Adyen Module for iOS and Android. Adyen allows developers to collect payment details utilizing native components or via asymetric encryption. Native components can be broken down into Card Components, Redirect Component and 3DS2 Component. Native asymetric encryption encrypts card number, expiry date and cvc/cvv number.

Currently CCI, 3DS, 3DS2, Scheme Card Component, BCMC Card Component, iDEAL Card Component and EPS Card Component are supported. Currently Drop-in integration is not supported.



Since **react-native 0.60** and higher, [autolinking](https://github.com/react-native-community/cli/blob/master/docs/autolinking.md) makes the installation process simpler.

## Configuration

### iOS

Edit the `Podfile` to update the iOS platform to 11.0 - `platform :ios, '11.0'`. Save the `Podfile` and install the pods.

```sh
pod install
```

#### Redirect / 3D Secure

To complete the Adyen redirect and properly receive the redirect result on iOS the deeplink URL will need to be listened to on the native delegate `application:openUrl:options`. Add a native file that contains the following:

```swift
import Foundation
import Adyen

@objc(RCTAdyenExample)
class RCTAdyenExample: NSObject {

  @objc(applicationDidOpen:)
  static func applicationDidOpen(_ url: URL) -> Bool {
     let adyenHandled = RedirectComponent.applicationDidOpen(from : url)

     return adyenHandled
  }
}
```

When the Adyen URL is received, execute the function `RCTAdyenExample.applicationDidOpen(url)` - the Adyen delegate function will be executed and the redirect result will be recieved through the pending promise.

### Android

#### Redirect / 3D Secure

To complete the Adyen redirect and properly receive the redirect result on Android the expected Adyen scheme and host must be registered in the Android Manifest.

```
<intent-filter>
    <action android:name="android.intent.action.VIEW" />
    <category android:name="android.intent.category.DEFAULT" />
    <category android:name="android.intent.category.BROWSABLE" />
    <data
        android:host="${applicationId}"
        android:scheme="adyencheckout" />
</intent-filter>
```

When the Adyen URL is received, the awaited promise will receive the Redirect result.

## Usage

### Example

```javascript
import React from 'react';
import {
  encrypt,
  handleAction,
  handleComponent,
} from 'react-native-adyen';

const adyen = () => {
  const cciRes = await encrypt('10001|...', {
    number: '4111111111111111',
    expiryMonth: '03',
    expiryYear: '30',
    securityCode: '737',
  });

  const actionRes = await handleAction(
    'test_',
    'test',
    PaymentMethods.redirect
  );

  const componentRes = await handleComponent(
    'test_',
    'test',
    paymentMethod,
    {
      currencyCode: 'EUR',
      value: 1000,
      countryCode: 'NL',
    }
  );

  .
  .
  .
};
```


## Methods

### Summary

- [`encrypt`](#enrypt)
- [`handleAction`](#handleAction)
- [`handleComponent`](#handleComponent)

---

#### `encrypt()`

```javascript
const cciRes = await encrypt(publicKey, paymentDetails);
```

Adyen provides custom card integration via asymmetric encryption through the this function with providing the client public key and payment details.

**Parameters:**

| Name           | Type   | Required | Description |
| -------------- | ------ | -------- | ----------- |
| publicKey      | string | Yes      | See below.  |
| paymentDetails | object | Yes      | See below.  |

Supported options:

- `publicKey` (string) - 	A public key linked to your API credential, used for [client-side authentication](https://docs.adyen.com/development-resources/client-side-authentication).
- `paymentDetails` (object) - The credit card number, expiry month, expiry year and cvc/cvv numbers.

---

#### `handleAction()`

```javascript
const actionRes = await handleAction(clientKey, environment, action);
```

Adyen provides a redirect component for redirect actions / payment methods i.e. 3D Secure, 3D Secure 2, Redirect Payemnts.

**Parameters:**

| Name        | Type   | Required | Description |
| ----------- | ------ | -------- | ----------- |
| clientKey   | string | Yes      | See below.  |
| environment | string | Yes      | See below.  |
| action      | object | Yes      | See below.  |

Supported options:

- `clientKey` (string) - 	A public key linked to your API credential, that the SDK Components use for client-side authentication.
- `environment` (string) - Use test. When you're ready to accept live payments, change the value to one of our [live environments](https://adyen.github.io/adyen-ios/Docs/Structs/Environment.html). 
- `action` (object) - Payment object for some payment methods require additional action from the shopper such as: to authenticate a payment with 3D Secure, or to switch to another app to complete the payment.

---

#### `handleComponent()`

```javascript
const componentRes = await handleComponent(clientKey, environment, paymentMethod, amount);
```

Adyen provides native component integration - Adyen takes care of the ui and controller logic and provides the developer with the resulting details.

**Parameters:**

| Name          | Type   | Required | Description |
| ------------- | ------ | -------- | ----------- |
| clientKey     | string | Yes      | See below.  |
| environment   | string | Yes      | See below.  |
| paymentMethod | object | Yes      | See below.  |
| amount        | object | Yes      | See below.  |

Supported options:

- `clientKey` (string) - 	A public key linked to your API credential, that the SDK Components use for client-side authentication.
- `environment` (string) - Use test. When you're ready to accept live payments, change the value to one of our [live environments](https://adyen.github.io/adyen-ios/Docs/Structs/Environment.html). 
- `paymentMethod` (object) - Decoded from the /paymentMethods response with the PaymentMethods structure. Find the payment method object for the Component that you want to instantiate.

---

## Contributing

See the [contributing guide](CONTRIBUTING.md) to learn how to contribute to the repository and the development workflow.

## License

UNLICENSED
