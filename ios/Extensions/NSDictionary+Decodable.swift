import Foundation

extension NSDictionary {
    func decoded<T: Decodable>() throws -> T {
        return try JSONDecoder().decode(T.self, from: JSONSerialization.data(withJSONObject: self))
    }
}
