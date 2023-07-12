import Foundation

extension Encodable {
    func toDictionary() -> [String: Any]?   {
        let encoder = JSONEncoder()
        let value = try! encoder.encode(self)
        
        return try! JSONSerialization.jsonObject(with: value) as? [String: Any]
    }
}
