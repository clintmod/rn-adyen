import Foundation

extension UIViewController {
    func presentAsync (viewController: UIViewController) {
        DispatchQueue.main.async {
            if #available(iOS 15.0, *) {
                viewController.sheetPresentationController?.detents = [.medium()]
            } else {
                
            }
            
            self.present(viewController, animated: true, completion: nil)
        }
    }
}
