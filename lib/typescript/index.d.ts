import type { ActionData, Amount, AndroidBcmcData, AndroidCardData, AndroidIdealData, ComponentPaymentMethod, EncryptRequest, EncryptResult, IOSBcmcData, IOSCardData, IOSIdealData, RedirectData } from './types/AdyenTypes';
export type { ActionData, Amount, AndroidBcmcData, AndroidCardData, AndroidIdealData, ComponentPaymentMethod, EncryptRequest, EncryptResult, IOSBcmcData, IOSCardData, IOSIdealData, RedirectData } from './types/AdyenTypes';
export declare const encrypt: (publicKey: string, data: EncryptRequest) => Promise<EncryptResult>;
export declare const handleComponent: (clientKey: string, environment: string, data: ComponentPaymentMethod, amount: Amount) => Promise<AndroidBcmcData | AndroidCardData | AndroidIdealData | IOSBcmcData | IOSCardData | IOSIdealData>;
export declare const handleAction: (clientKey: string, environment: string, data: ActionData) => Promise<RedirectData>;
