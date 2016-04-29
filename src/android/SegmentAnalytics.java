package org.apache.cordova.plugin;

import android.content.Context;

import com.segment.analytics.*;
import com.segment.analytics.android.integrations.amplitude.AmplitudeIntegration;
import com.segment.analytics.android.integrations.google.analytics.GoogleAnalyticsIntegration;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;


/**
 * This class echoes a string called from JavaScript.
 */
public class SegmentAnalytics extends CordovaPlugin {

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        Context context=this.cordova.getActivity().getApplicationContext();

        if (action.equals("load")) {
            String message = args.getString(0);
            this.load(message, callbackContext, context);
            return true;
        }else if (action.equals("page")) {
            String message = args.getString(0);
            this.page(message, callbackContext, context);
            return true;
        }else if (action.equals("track")) {
            this.track(args, callbackContext, context);
            return true;
        }else if (action.equals("identify")) {
            this.identify(args, callbackContext, context);
            return true;
        }
        return false;
    }

    public static Properties parseProperties(JSONObject json , Properties out) throws JSONException{
        Iterator<String> keys = json.keys();
        while(keys.hasNext()){
            String key = keys.next();
            String val = null;
            try{
                JSONObject value = json.getJSONObject(key);
                parseProperties(value,out);
            }catch(Exception e){
                val = json.getString(key);
            }

            if(val != null){
                out.putValue(key,val);
            }
        }
        return out;
    }
    public static Traits parseTraits(JSONObject json , Traits out) throws JSONException{
        Iterator<String> keys = json.keys();
        while(keys.hasNext()){
            String key = keys.next();
            String val = null;
            try{
                JSONObject value = json.getJSONObject(key);
                parseTraits(value,out);
            }catch(Exception e){
                val = json.getString(key);
            }

            if(val != null){
                out.putValue(key,val);
            }
        }
        return out;
    }
    private void track(JSONArray args, CallbackContext callbackContext, Context context) throws JSONException {
        String message = args.getString(0);
        JSONObject jsonProps = args.getJSONObject(1);

        if (message != null && message.length() > 0) {
            if(jsonProps != null){
                Properties props = new Properties();
                props = parseProperties(jsonProps, props);
                Analytics.with(context).track(message, props);
            }else{
                Analytics.with(context).track(message);
            }

            callbackContext.success(message);
        } else {
            callbackContext.error("Expected one non-empty string argument.");
        }
    }

    private void identify(JSONArray args, CallbackContext callbackContext, Context context) throws JSONException {
        String userId = args.getString(0);
        JSONObject jsonProps = args.getJSONObject(1);

        if (userId != null && userId.length() > 0) {
            Analytics.with(context).identify(userId);
            if(jsonProps != null){
                Traits traits = new Traits();
                traits = parseTraits(jsonProps, traits);
                Analytics.with(context).identify(traits);
            }

            callbackContext.success();
        } else {
            callbackContext.error("Expected one non-empty string argument.");
        }
    }

    private void page(String pageName, CallbackContext callbackContext, Context context) {
        if (pageName != null && pageName.length() > 0) {
            Analytics.with(context).screen("Page", pageName);
            callbackContext.success(pageName);
        } else {
            callbackContext.error("Expected one non-empty string argument.");
        }
    }

    private void load(String key, CallbackContext callbackContext, Context context) {
        if (key != null && key.length() > 0) {

            Analytics analytics = new Analytics.Builder(context, key)
                    .use(GoogleAnalyticsIntegration.FACTORY)
                    .use(AmplitudeIntegration.FACTORY)
                    .build();
            Analytics.setSingletonInstance(analytics);
            callbackContext.success(key);
        } else {
            callbackContext.error("Expected one non-empty string argument.");
        }
    }
}