import Foundation

struct RCTCard: Codable {
    let number, securityCode, expiryMonth, expiryYear, holder: String?
}
