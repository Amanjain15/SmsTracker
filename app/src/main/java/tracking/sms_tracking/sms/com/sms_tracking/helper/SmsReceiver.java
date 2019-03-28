package tracking.sms_tracking.sms.com.sms_tracking.helper;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import tracking.sms_tracking.sms.com.sms_tracking.home.models.RetrofitSmsProvider;
import tracking.sms_tracking.sms.com.sms_tracking.home.presenter.SmsPresenter;
import tracking.sms_tracking.sms.com.sms_tracking.home.presenter.SmsPresenterImpl;
import tracking.sms_tracking.sms.com.sms_tracking.home.view.SmsView;

/**
 * Created by aman on 28/6/18.
 */

public class SmsReceiver extends BroadcastReceiver implements SmsView {

    public static final String SMS_URI = "content://sms";

    public static final String ADDRESS = "address";
    public static final String PERSON = "person";
    public static final String DATE = "date";
    public static final String READ = "read";
    public static final String STATUS = "status";
    public static final String TYPE = "type";
    public static final String BODY = "body";
    public static final String SEEN = "seen";

    public static final int MESSAGE_TYPE_INBOX = 1;
    public static final int MESSAGE_TYPE_SENT = 2;

    public static final int MESSAGE_IS_NOT_READ = 0;
    public static final int MESSAGE_IS_READ = 1;

    public static final int MESSAGE_IS_NOT_SEEN = 0;
    public static final int MESSAGE_IS_SEEN = 1;

    private SmsPresenter smsPresenter;

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle myBundle = intent.getExtras();
        SmsMessage[] messages = null;

        initialize();
        if (myBundle != null)
        {
            Object [] smsExtra = (Object[]) myBundle.get("pdus");

            ContentResolver contentResolver = context.getContentResolver();

            messages = new SmsMessage[smsExtra.length];

            for (int i = 0; i < messages.length; i++)
            {
                String strMessage = "";
                messages[i] = SmsMessage.createFromPdu((byte[]) smsExtra[i]);

                String body = messages[i].getMessageBody();
                String address = messages[i].getOriginatingAddress();

                strMessage += "SMS From: " + address;
                strMessage += " :\n";
                strMessage += body + "\n" +" SMS Tracking";
                smsPresenter.getSmsData(address, body);
                Toast.makeText(context, strMessage, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void putSmsToDatabase( ContentResolver contentResolver, SmsMessage sms )
    {
        // Create SMS row
        ContentValues values = new ContentValues();
        values.put( ADDRESS, sms.getOriginatingAddress() );
        values.put( DATE, sms.getTimestampMillis() );
        values.put( READ, MESSAGE_IS_NOT_READ );
        values.put( STATUS, sms.getStatus() );
        values.put( TYPE, MESSAGE_TYPE_INBOX );
        values.put( SEEN, MESSAGE_IS_NOT_SEEN );
        values.put( BODY, sms.getMessageBody() );

        // Push row into the SMS table
        contentResolver.insert( Uri.parse( SMS_URI ), values );
    }

    @Override
    public void showMessage(String msg) {
        Log.d("SmsService",msg);
//        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void initialize(){
        smsPresenter = new SmsPresenterImpl(this, new RetrofitSmsProvider());
    }
}
