import Foundation
import Adyen

extension EncryptedCard {
    func toDictionary() -> [String: String] {
        var dictionary = [String:String]()
        
        if let number = number {
            dictionary["encryptedNumber"] = number
        }
        
        if let expiryMonth = expiryMonth {
            dictionary["encryptedExpiryMonth"] = expiryMonth
        }
        
        if let expiryYear = expiryYear {
            dictionary["encryptedExpiryYear"] = expiryYear
        }
        
        if let securityCode = securityCode {
            dictionary["encryptedSecurityCode"] = securityCode
        }
        
        return dictionary
    }
}
