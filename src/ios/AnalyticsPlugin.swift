import Segment_GoogleAnalytics

@objc(HWPAnalyticsPlugin) class AnalyticsPlugin: CDVPlugin {
    var analytics: SEGAnalytics!
    let appVersion = NSBundle.mainBundle().infoDictionary!["CFBundleShortVersionString"] as! String

    var userId: String!

    func load(command: CDVInvokedUrlCommand){
        let segConfig = SEGAnalyticsConfiguration(writeKey: command.arguments[0] as! String)
        segConfig.flushAt = 1
        SEGAnalytics.setupWithConfiguration(segConfig)
    }
    func page(command: CDVInvokedUrlCommand){
        let name = command.argumentAtIndex(0, withDefault: nil)
        if(name != nil && name as! String != ""){
            SEGAnalytics.sharedAnalytics().screen(name as! String)
        }
    }
    func track(command: CDVInvokedUrlCommand){
        let eventName = command.arguments[0] as! String
        let props = command.arguments[1] as? Dictionary<String, AnyObject>

        if(props != nil){
            SEGAnalytics.sharedAnalytics().track(eventName, properties: props!)
        }else{
            SEGAnalytics.sharedAnalytics().track(eventName)
        }
    }
    func identify(command: CDVInvokedUrlCommand){
        let userId = command.arguments[0] as? String
        var traits = command.arguments[1] as? Dictionary<String, AnyObject> ?? [:]

        traits["latest-ios-version"] = appVersion

        if(userId != nil){
            self.userId = userId
        }

        SEGAnalytics.sharedAnalytics().identify(self.getUserId(), traits: traits)
    }

    func getUserId() -> String{
        if(self.userId != nil){
            return self.userId
        }
        let uuid = NSUUID().UUIDString
        self.userId = uuid
        return self.userId
    }
}