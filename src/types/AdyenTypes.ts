export interface EncryptRequest {
  number?: string;
  securityCode?: string;
  expiryMonth?: string;
  expiryYear?: string;
  holder?: string;
}

export interface EncryptResult {
  encryptedNumber?: string;
  encryptedSecurityCode?: string;
  encryptedExpiryMonth?: string;
  encryptedExpiryYear?: string;
}

export interface ActionData {
  paymentMethodType?: string;
  url?: string;
  method?: string;
  type?: string;
}

export interface AndroidCardData {
  mPaymentComponentData?: MPaymentComponentData;
  mIsInputValid?: boolean;
  lastFourDigits?: string;
  cardType?: string;
  mIsReady?: boolean;
  binValue?: string;
}

export interface MPaymentComponentData {
  storePaymentMethod?: boolean;
  paymentMethod?: PaymentMethod;
}

export interface PaymentMethod {
  type?: string;
  threeDS2SdkVersion?: string;
  holderName?: string;
  encryptedExpiryYear?: string;
  encryptedCardNumber?: string;
  encryptedSecurityCode?: string;
  encryptedExpiryMonth?: string;
  issuer?: string;
}

export interface IOSCardData {
  encryptedCardNumber?: string;
  encryptedExpiryYear?: string;
  threeDS2SdkVersion?: string;
  storePaymentMethod?: boolean;
  encryptedSecurityCode?: string;
  brand?: string;
  type?: string;
  encryptedExpiryMonth?: string;
}

export interface IOSIdealData {
  type?: string;
  storePaymentMethod?: boolean;
  issuer?: string;
}

export interface IOSBcmcData {
  threeDS2SdkVersion?: string;
  encryptedExpiryYear?: string;
  storePaymentMethod?: boolean;
  encryptedCardNumber?: string;
  brand?: string;
  type?: string;
  encryptedExpiryMonth?: string;
}

export interface AndroidBcmcData {
  mPaymentComponentData?: MPaymentComponentData;
  mIsReady?: boolean;
  mIsInputValid?: boolean;
}

export interface AndroidIdealData {
  mPaymentComponentData?: MPaymentComponentData;
  mIsReady?: boolean;
  mIsInputValid?: boolean;
}

export interface ComponentPaymentMethod {
  details?: Detail[];
  name?: string;
  type?: string;
  brands?: string[];
}

export interface Detail {
  items?: Item[];
  key?: string;
  type?: string;
  optional?: boolean;
}

export interface Item {
  id?: string;
  name?: string;
}

export interface Amount {
  currencyCode?: string;
  countryCode?: string;
  value: number;
}

export interface RedirectData {
  redirectResult?: string;
}
