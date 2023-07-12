import { NativeModules } from 'react-native';
const Adyen = NativeModules.Adyen;
export const encrypt = (publicKey, data) => {
  return Adyen.encrypt(publicKey, data);
};
export const handleComponent = (clientKey, environment, data, amount) => {
  return Adyen.handleComponent(clientKey, environment, data, amount);
};
export const handleAction = (clientKey, environment, data) => {
  return Adyen.handleAction(clientKey, environment, data);
};
//# sourceMappingURL=index.js.map