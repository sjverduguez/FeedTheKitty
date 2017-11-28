package com.example.stephanie.feedthekitty;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Jack on 11/27/2017.
 */

public class WePay {

    private static final String TAG = "WePay";

    private static String userURI = "https://stage.wepayapi.com/v2/user/register";
    private static String createAccountURI = "https://stage.wepayapi.com/v2/account/create";
    private static String checkoutURI = "https://stage.wepayapi.com/v2/checkout/create";
    private static String userSendConfirmationURI = "https://stage.wepayapi.com/v2/user/send_confirmation";

    //  ClientID and Client Secret belong to the master account, which is only used for creating new users
    static int clientID = 197615;
    static String clientSecret = "938072abaf";

    // This creates the account used by a user to receive payments
    public static void createUser(final Context context, final String email, final String first_name, final String last_name){
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest sr = new StringRequest(Request.Method.POST, userURI, new Response.Listener<String>() {

            // TODO: Store the user id, access token, and access type in our database or locally on the users device
            // NOTE: Repeating this function with the same email address will give the same result
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject responseJSON = new JSONObject(response);


                    Log.i(TAG, "User ID: " + responseJSON.getString("user_id"));
                    Log.i(TAG, "Access Token: " + responseJSON.getString("access_token"));
                    Log.i(TAG, "Token Type: " + responseJSON.getString("token_type"));

                    sendConfirmation(context, responseJSON.getString("access_token"));
                    createAccount(context, "New Event", "Description about event", null, responseJSON.getString("access_token"));
                } catch (JSONException e) {

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "Error response " + error.toString());
            }
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("client_id", Integer.toString(clientID));
                params.put("client_secret", clientSecret);
                params.put("email", email);
                params.put("scope", "manage_accounts,collect_payments,view_user,send_money,preapprove_payments");
                params.put("first_name", first_name);
                params.put("last_name", last_name);
                params.put("original_ip", getIPAddress());
                params.put("original_device", "Android Device");
                params.put("tos_acceptance_time", Long.toString(System.currentTimeMillis()/1000));

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };
        queue.add(sr);
    }

    // User gets an email to finish setting up the account we made them
    // Access token was generated in when registering user
    public static void sendConfirmation (Context context, final String accessToken){
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest sr = new StringRequest(Request.Method.POST, userSendConfirmationURI, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject responseJSON = new JSONObject(response);
                    Log.i(TAG, "User ID: " + responseJSON.getString("user_id"));
                } catch (JSONException e) {

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "Error response " + error.toString());
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                params.put("Authorization", "Bearer " + accessToken);
                return params;
            }
        };
        queue.add(sr);
    }

    // Create an account for the user.
    // Access token was generated in when registering user
    // accountName is the name of the new event that the user is creating
    // Account Desciption is the description of the event if any
    // TODO: Decide if we want 1 account per user or per fundraiser
    public static void createAccount (Context context, final String accountName, final String accountDescription, final String imageURI, final String accessToken){
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest sr = new StringRequest(Request.Method.POST, createAccountURI, new Response.Listener<String>() {

            // TODO: Store the account_id in our database or locally on the users device
            // account_ids are associated with users so they know where to make payments
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject responseJSON = new JSONObject(response);
                    Log.i(TAG, "Account ID: " + responseJSON.getString("account_id"));
                } catch (JSONException e) {

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "Error response " + error.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> params = new HashMap<String, String>();
                params.put("name", accountName);
                params.put("description", accountDescription);
                params.put("image_uri", imageURI);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                params.put("Authorization", "Bearer " + accessToken);
                return params;
            }
        };
        queue.add(sr);
    }

    // Account ID is the person being paid
    // accessToken is for the person paying
    // description is a description of the payment. It will include the name of the person making the payment so they can be given credit
    // amount is the amount of the payment in dollars
    public static void checkout (Context context, final int accountID, final String description, final float amount, final String accessToken){
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest sr = new StringRequest(Request.Method.POST, checkoutURI, new Response.Listener<String>() {

            // TODO: Store the account_id in our database or locally on the users device
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject responseJSON = new JSONObject(response);
                    //TODO create an intent to fulfill the checkout uri. The checkout uri takes care of the user paying
                    // We will need a way later on to track how much has been paid to events
                    String checkoutURI = responseJSON.getJSONObject("hosted_checkout").getString("checkout_uri");

                    Log.i(TAG, checkoutURI);
                } catch (JSONException e) {

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "Error response " + error.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> params = new HashMap<String, String>();
                params.put("account_id", Integer.toString(accountID));
                params.put("short_description", description);
                params.put("type", "personal");
                params.put("amount", Float.toString(amount));
                params.put("currency", "USD");
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                params.put("Authorization", "Bearer " + accessToken);
                return params;
            }
        };
        queue.add(sr);
    }


    public static String getIPAddress() {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress();
                        //boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                        boolean isIPv4 = sAddr.indexOf(':')<0;

                        if (isIPv4)
                            return sAddr;

                    }
                }
            }
        } catch (Exception ex) { } // for now eat exceptions
        return "";
    }
}
