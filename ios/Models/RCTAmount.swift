import Foundation
import Adyen

struct RCTAmount: Codable {
    let currencyCode, countryCode: String
    let value: Int
}
