package com.example.stephanie.feedthekitty;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Jack on 11/27/2017.
 */

public class WePay {

    private static final String TAG = "WePay";

    private static String userURI = "https://stage.wepayapi.com/v2/user/register";
    private static String createAccountURI = "https://stage.wepayapi.com/v2/account/create";
    private static String checkoutURI = "https://stage.wepayapi.com/v2/checkout/create";
    private static String userSendConfirmationURI = "https://stage.wepayapi.com/v2/user/send_confirmation";
    private static String checkoutStatusURI = "https://stage.wepayapi.com/v2/checkout";

    //  ClientID and Client Secret belong to the master account, which is only used for creating new users
    static int clientID = 197615;
    static String clientSecret = "938072abaf";

    // This creates the account used by a user to receive payments
    public static void createUser(final Context context, final String email, final String first_name, final String last_name){
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest sr = new StringRequest(Request.Method.POST, userURI, new Response.Listener<String>() {

            // NOTE: Repeating this function with the same email address will give the same result
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject responseJSON = new JSONObject(response);

                    String accessToken = responseJSON.getString("access_token");
                    String userID = responseJSON.getString("user_id");


                    Log.i(TAG, "User ID: " + userID);
                    Log.i(TAG, "Access Token: " + accessToken);

                    SharedPreferences sharedPref = context.getSharedPreferences("com.example.stephanie.FeedTheKitty", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("AccessToken", accessToken);
                    editor.putString("UserID", userID);
                    editor.putString("UserName", first_name + " " + last_name);
                    editor.commit();

                    // Send email to a user to confirm the creation of their account
                    sendConfirmation(context, accessToken);
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
    // The time is the time of the event stored as the epoch time
    // TODO: The account will be added to the firebase along with a description of the event
    public static void createAccount (final Context context, final String accountName, final String accountDescription, final long time, final String imageURI, final String accessToken, final String goal){
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest sr = new StringRequest(Request.Method.POST, createAccountURI, new Response.Listener<String>() {

            // TODO: Store the account_id on users device as a list of my events
            // TODO: Sore the account_id along with accessToken, description, time, name, and image in our firebase so other users can get information about this event
            // account_ids are associated with users so they know where to make payments
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject responseJSON = new JSONObject(response);
                    String account_id = responseJSON.getString("account_id");
                    Log.i(TAG, "Account ID: " + account_id);

                    SharedPreferences sharedPref = context.getSharedPreferences("com.example.stephanie.FeedTheKitty", Context.MODE_PRIVATE);

                    // Add this event to the list of events hosted on this device
                    Set<String> myEvents = sharedPref.getStringSet("hosting_events", new HashSet<String>());
                    myEvents.add(account_id);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putStringSet("hosting_events", myEvents);
                    editor.commit();

                    DatabaseReference eventNameRef = FirebaseDatabase.getInstance()
                            .getReferenceFromUrl("https://feedthekitty-a803d.firebaseio.com");

                    DatabaseReference eventIDRef = eventNameRef.child(account_id);
                    DatabaseReference eventFundGoal = eventIDRef.child("Fund Goal");
                    DatabaseReference eventTime = eventIDRef.child("Time");
                    DatabaseReference eventName = eventIDRef.child("Name");
                    DatabaseReference eventDescription = eventIDRef.child("Description");
                    DatabaseReference eventAccessToken = eventIDRef.child("AccessToken");

                    eventFundGoal.setValue(goal);
                    eventTime.setValue(time);
                    eventName.setValue(accountName);
                    eventDescription.setValue(accountDescription);
                    eventAccessToken.setValue(accessToken);

                    Intent intent = new Intent(context, EventDetailsActivity.class);
                    intent.putExtra("EVENT_ID", account_id);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);

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
    // payer is a description of the payment. It will be the name of the person making the payment so they can be given credit
    // amount is the amount of the payment in dollars
    public static void checkout (final Context context, final int accountID, final String payer, final float amount, final String accessToken){
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest sr = new StringRequest(Request.Method.POST, checkoutURI, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject responseJSON = new JSONObject(response);
                    // TODO create an intent to fulfill the checkout uri. The checkout uri takes care of the user paying
                    // TODO add a this checkout to the locally stored list of pending payments for this user
                    String url = responseJSON.getJSONObject("hosted_checkout").getString("checkout_uri");

                    SharedPreferences sharedPref = context.getSharedPreferences("com.example.stephanie.FeedTheKitty", Context.MODE_MULTI_PROCESS);
                    String checkout_id = responseJSON.getString("checkout_id");

                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("pending_checkout", checkout_id);
                    editor.commit();

                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    context.startActivity(i);

                    Log.i(TAG, url);
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
                params.put("short_description", payer);
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

    // Check the status of a pending checkout to see if it has been made or cancelled
    public static void getCheckoutStatus (final Context context, final int checkoutID, final String accessToken){
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest sr = new StringRequest(Request.Method.POST, checkoutStatusURI, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject responseJSON = new JSONObject(response);
                    String accountId = responseJSON.getString("account_id");
                    String state = responseJSON.getString("state");
                    String payer = responseJSON.getString("short_description");
                    Double amount = responseJSON.getDouble("amount");

                    Log.i(TAG, "State of checkout " + checkoutID + " of amount " + amount + " from " + payer + " to account " + accountId + ": " + state);

                    // TODO: If the state of the payment is authorized, captured, or released, then add the payment to the account in the firebase
                    // If the state of the payment is still new, do nothing. If it is anything else, you can delete it from the pending payments
                    // For reference: https://developer.wepay.com/api/api-calls/checkout#states

                    if (state.equals("authorized") || state.equals("captured") || state.equals("released")) {
                        Log.i(TAG, "Checkout: " + checkoutID + " added to event");
                        DatabaseReference eventNameRef = FirebaseDatabase.getInstance()
                                .getReferenceFromUrl("https://feedthekitty-a803d.firebaseio.com");
                        DatabaseReference event = eventNameRef.child(accountId);
                        DatabaseReference checkouts = event.child("Checkout");
                        DatabaseReference checkout = checkouts.child(Integer.toString(checkoutID));
                        DatabaseReference amountPaidRef = checkout.child("Amount");
                        DatabaseReference payerRef = checkout.child("Payer");
                        amountPaidRef.setValue(amount);
                        payerRef.setValue(payer);
                    }

                } catch (JSONException e) {
                    Log.i(TAG, "JSON Exception");
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
                params.put("checkout_id", Integer.toString(checkoutID));
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

    public static void updatePendingCheckouts(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences("com.example.stephanie.FeedTheKitty", Context.MODE_MULTI_PROCESS);
        String pendingCheckouts = sharedPref.getString("pending_checkout", null);
        String accessToken = sharedPref.getString("AccessToken", null);

        if (pendingCheckouts != null) {
            getCheckoutStatus(context, Integer.parseInt(pendingCheckouts), accessToken);
        }
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
