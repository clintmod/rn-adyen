import Foundation
import Adyen

class RCTEnvironment {
    let value: String

    init(fromString value: String) {
        self.value = value
    }
    
    func getEnvironment() -> Environment {
        switch self.value {
        case "test":
            return .test
        case "beta":
            return .beta
        case "live":
            return .live
        case "liveEurope":
            return .liveEurope
        case "liveAustralia":
            return .liveAustralia
        case "liveUnitedStates":
            return .liveUnitedStates
        default:
            return .test
        }
    }
}
