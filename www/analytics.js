var exec = require("cordova/exec"),
    channel = require("cordova/channel");

var is = {};
is.fn = is['function'] = function (value) {
    var isAlert = typeof window !== 'undefined' && value === window.alert;
    return isAlert || Object.prototype.toString.call(value) === '[object Function]';
};
is.object = function (value) {
    return Object.prototype.toString.call(value) === '[object Object]';
};

// Called after "deviceready" event
//channel.deviceready.subscribe(function () {
//    // Device is ready now, the listeners are registered
//    // and all queued events can be executed.
//    exec(null, null, "AnalyticsPlugin", "deviceReady", []);
//});

var Analytics = function(){

};

Analytics.prototype.load = function(key){
    exec(null, null, "AnalyticsPlugin", "load", [key]);
};

Analytics.prototype.page = function(pageName){
    exec(null, null, "AnalyticsPlugin", "page", [pageName]);
};

Analytics.prototype.track = function(message, properties){
    exec(null, null, "AnalyticsPlugin", "track", [message, properties]);
};

Analytics.prototype.identify = function(id, traits, options, fn) {
    // Argument reshuffling.
    /* eslint-disable no-unused-expressions, no-sequences */
    if (is.fn(options)) fn = options, options = null;
    if (is.fn(traits)) fn = traits, options = null, traits = null;
    if (is.object(id)) options = traits, traits = id, id = null;
    /* eslint-enable no-unused-expressions, no-sequences */

    // clone traits before we manipulate so we don't do anything uncouth, and take
    // from `user` so that we carryover anonymous traits
    //user.identify(id, traits);
    exec(null, null, "AnalyticsPlugin", "identify", [id, traits]);
    return this;
};


var analytics = new Analytics();
module.exports = analytics;