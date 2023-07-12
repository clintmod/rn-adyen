import Adyen
import Adyen3DS2

@objc(RCTAdyen)
class RCTAdyen: NSObject, ActionComponentDelegate, PresentationDelegate, PaymentComponentDelegate {
    var adyenActionComponent: AdyenActionComponent?
    var presentableComponent: PresentableComponent?
    var resolve: RCTPromiseResolveBlock?
    var reject: RCTPromiseRejectBlock?
    var threeDS2Component: ThreeDS2Component?
    var apiContext: APIContext?
    var environment: String = ""
    var clientKey: String = ""
    @objc
    func encrypt(_ publicKey: String, data: NSDictionary, resolve: RCTPromiseResolveBlock, reject: RCTPromiseRejectBlock) {
        do {
            let cardData = try data.decoded() as RCTCard
            let card = RCTCardBuilder()
                .withNumber(number: cardData.number)
                .withSecurityCode(securityCode: cardData.securityCode)
                .withExpiryMonth(expiryMonth: cardData.expiryMonth)
                .withExpiryYear(expiryYear: cardData.expiryYear)
                .withHolder(holder: cardData.holder)
                .build()
            
            let encryptedCard = try CardEncryptor.encrypt(card: card, with: publicKey)
            
            resolve(encryptedCard.toDictionary())
        } catch {
            reject("error", "RCTAdyen.encrypt", error)
        }
    }
    
    @objc
    func handleComponent(_ clientKey: String, environment: String, data: NSDictionary, amount: NSDictionary, resolve: @escaping RCTPromiseResolveBlock, reject: @escaping RCTPromiseRejectBlock) {
        do {
            self.resolve = resolve
            self.reject = reject
            
            let paymentMethod = try data.decoded() as RCTPaymentMethod
            let amountData = try amount.decoded() as RCTAmount
            
            let apiContext = APIContext(environment: RCTEnvironment(fromString: environment).getEnvironment(), clientKey: clientKey)
            let payment = Payment(amount: Amount(value:amountData.value, currencyCode: amountData.currencyCode), countryCode: amountData.countryCode)
            var color = UIColor(red: 0.03, green: 0.04, blue: 0.13, alpha: 1.00)
            var style = FormComponentStyle()
            style.tintColor = color
            style.mainButtonItem.button.cornerRounding = .none
            lazy var component: PresentableComponent? = {
                do {
                    switch paymentMethod.type {
                    case "bcmc":
                        let bcmcPaymentMethod = try data.decoded() as BCMCPaymentMethod
                        let bcmcComponent = BCMCComponent(paymentMethod: bcmcPaymentMethod, apiContext: apiContext, style: style)
                        bcmcComponent.delegate = self
                        bcmcComponent.payment = payment
                        
                        return bcmcComponent
                    case "ideal":
                        let issuerListPaymentMethod = try data.decoded() as IssuerListPaymentMethod
                        let idealComponent = IdealComponent(paymentMethod: issuerListPaymentMethod, apiContext: apiContext)
                        idealComponent.delegate = self
                        idealComponent.payment = payment
                        
                        return idealComponent
                    case "eps":
                        let issuerListPaymentMethod = try data.decoded() as IssuerListPaymentMethod
                        let epsComponent = EPSComponent(paymentMethod: issuerListPaymentMethod, apiContext: apiContext)
                        epsComponent.delegate = self
                        epsComponent.payment = payment
                        
                        return epsComponent
                    case "dotpay":
                        let issuerListPaymentMethod = try data.decoded() as IssuerListPaymentMethod
                        let dotpayComponent = DotpayComponent(paymentMethod: issuerListPaymentMethod, apiContext: apiContext)
                        dotpayComponent.delegate = self
                        dotpayComponent.payment = payment
                        
                        return dotpayComponent
                    case "scheme":
                        let cardPaymentMethod = try data.decoded() as CardPaymentMethod
                        let cardComponent = CardComponent(paymentMethod: cardPaymentMethod, apiContext: apiContext, style: style)
                        cardComponent.delegate = self
                        cardComponent.payment = payment
                        
                        return cardComponent
                    default:
                        reject("error", "RCTAdyen.handleComponent", nil)
                        return nil
                    }
                } catch {
                    reject("error", "RCTAdyen.handleComponent", error)
                    return nil
                }
            }()
            
            presentWithNav(component: component!)
            
        } catch {
            reject("error", "RCTAdyen.handleComponent", error)
        }
    }
    
    @objc
    func handleAction(_ clientKey: String, environment: String, data: NSDictionary, resolve: @escaping RCTPromiseResolveBlock, reject: @escaping RCTPromiseRejectBlock) {
        do {
            self.resolve = resolve
            self.reject = reject
            
            let apiContext = APIContext(environment: RCTEnvironment(fromString: environment).getEnvironment(), clientKey: clientKey)
            lazy var actionComponent: AdyenActionComponent = {
                let component = AdyenActionComponent(apiContext: apiContext)
                component.delegate = self
                component.presentationDelegate = self
                
                return component
            }()
            
            let action = try data.decoded() as Action
            self.adyenActionComponent = {
                do {
                    switch action {
                        case let .threeDS2Challenge(threeDS2ChallengeAction):
                            return self.adyenActionComponent
                        default:
                            return actionComponent
                    }
                } catch {
                    reject("error", "RCTAdyen.handleAction", error)
                    return nil
                }
            }()
            
            DispatchQueue.main.async {
                self.adyenActionComponent?.handle(action)
            }
        } catch {
            reject("error", "RCTAdyen.handleAction", error)
        }
    }
    
    @objc static func requiresMainQueueSetup() -> Bool {
        return true
    }
    
    func didProvide(_ data: ActionComponentData, from component: ActionComponent) {
        self.presentableComponent?.viewController.dismiss(animated: true) {
            self.resolve?(data.details.encodable.toDictionary())
        }
    }
    
    func didSubmit(_ data: PaymentComponentData, from component: PaymentComponent) {
        self.presentableComponent?.viewController.dismiss(animated: true) {
            var paymentMethod = data.paymentMethod.encodable.toDictionary()
            paymentMethod?["storePaymentMethod"] = data.storePaymentMethod
            self.resolve?(paymentMethod)
        }
    }
    
    func didFail(with error: Error, from component: PaymentComponent) {
        self.reject?("error", "RCTAdyen.handleAction.didFail", error)
    }
    
    func didComplete(from component: ActionComponent) {
        self.presentableComponent?.viewController.dismiss(animated: true, completion: nil)
    }
    
    func didFail(with error: Error, from component: ActionComponent) {
        self.reject?("cancel", "RCTAdyen.handleAction.didFail", error)
    }
    
    @objc func didCancel() {
        self.presentableComponent?.viewController.dismiss(animated: true, completion: nil)
    }

    func presentWithNav(component: PresentableComponent) {
        self.presentableComponent = component
        DispatchQueue.main.async {
            let keyWindow = UIApplication.shared.windows.filter {$0.isKeyWindow}.first
            if var topController = keyWindow?.rootViewController {
                while let presentedViewController = topController.presentedViewController {
                    topController = presentedViewController
                }
                let navigation = UINavigationController(rootViewController: component.viewController)
                component.viewController.navigationItem.leftBarButtonItem = .init(barButtonSystemItem: .cancel,
                                                                                   target: self,
                                                                                   action: #selector(self.didCancel))
                navigation.modalPresentationStyle = .popover
                topController.present(navigation, animated: true, completion: nil)
            }
        }
    }
    
    func present(component: PresentableComponent) {
        self.presentableComponent = component
        DispatchQueue.main.async {
            let keyWindow = UIApplication.shared.windows.filter {$0.isKeyWindow}.first
            if var topController = keyWindow?.rootViewController {
                while let presentedViewController = topController.presentedViewController {
                    topController = presentedViewController
                }
                topController.present(component.viewController, animated: true, completion: nil)
            }
        }
    }
}
