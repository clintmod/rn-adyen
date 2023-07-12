import { NativeModules } from 'react-native';
import type {
  ActionData,
  Amount,
  AndroidBcmcData,
  AndroidCardData,
  AndroidIdealData,
  ComponentPaymentMethod,
  EncryptRequest,
  EncryptResult,
  IOSBcmcData,
  IOSCardData,
  IOSIdealData,
  RedirectData
} from './types/AdyenTypes';

const Adyen = NativeModules.Adyen;

export const encrypt = (
  publicKey: string,
  data: EncryptRequest
): Promise<EncryptResult> => {
  return Adyen.encrypt(publicKey, data);
};

export const handleComponent = (
  clientKey: string,
  environment: string,
  data: ComponentPaymentMethod,
  amount: Amount
): Promise<
  | AndroidBcmcData
  | AndroidCardData
  | AndroidIdealData
  | IOSBcmcData
  | IOSCardData
  | IOSIdealData
> => {
  return Adyen.handleComponent(clientKey, environment, data, amount);
};

export const handleAction = (
  clientKey: string,
  environment: string,
  data: ActionData
): Promise<RedirectData> => {
  return Adyen.handleAction(clientKey, environment, data);
};
