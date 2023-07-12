import Foundation
import Adyen

class RCTCardBuilder {
    private var card: Card
    
    init() {
        card = Card()
    }
    
    func withNumber(number: String?) -> RCTCardBuilder {
        if let number = number {
            card.number = number
        }
        
        return self
    }
    
    func withSecurityCode(securityCode: String?) -> RCTCardBuilder {
        if let securityCode = securityCode {
            card.securityCode = securityCode
        }
        
        return self
    }
    
    func withExpiryMonth(expiryMonth: String?) -> RCTCardBuilder {
        if let expiryMonth = expiryMonth {
            card.expiryMonth = expiryMonth
        }
        
        return self
    }
    
    func withExpiryYear(expiryYear: String?) -> RCTCardBuilder {
        if let expiryYear = expiryYear {
            card.expiryYear = expiryYear
        }
        
        return self
    }
    
    func withHolder(holder: String?) -> RCTCardBuilder {
        if let holder = holder {
            card.holder = holder
        }
        
        return self
    }
    
    func build() -> Card {
        return card
    }
}
