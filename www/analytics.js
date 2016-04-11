var exec = require("cordova/exec"),
    channel = require("cordova/channel");

// Called after "deviceready" event
channel.deviceready.subscribe(function () {
    // Device is ready now, the listeners are registered
    // and all queued events can be executed.
    exec(null, null, "AnalyticsPlugin", "deviceReady", []);
});