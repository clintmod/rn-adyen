//
//  RCTPaymentMethod.swift
//  RCTAdyen
//
//  Created by Christopher Herman on 6/15/22.
//  Copyright © 2022 Facebook. All rights reserved.
//

import Foundation
import Adyen

struct RCTPaymentMethod: PaymentMethod {
    var type: String
    
    var name: String
    
    func buildComponent(using builder: PaymentComponentBuilder) -> PaymentComponent? {
        return nil
    }
}
