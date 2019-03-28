package tracking.sms_tracking.sms.com.sms_tracking;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.ArrayList;

import tracking.sms_tracking.sms.com.sms_tracking.helper.SmsReceiver;

public class Home extends AppCompatActivity implements View.OnClickListener,
                                AdapterView.OnItemClickListener {

    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        button = this.findViewById(R.id.UpdateList);
        final Context context  = this;
        final Activity activity = this;
        button.setEnabled(false);
        checkPermissions(activity);
        button.setOnClickListener(this);
    }

    ArrayList<String> smsList = new ArrayList<String>();

    public void onItemClick( AdapterView<?> parent, View view, int pos, long id )
    {
        String[] splitted = smsList.get( pos ).split("\n");
        String sender = splitted[0];
        try
        {
            String encryptedData = "";
            for ( int i = 1; i < splitted.length; ++i )
            {
                encryptedData += splitted[i];
            }
            String data = sender + "\n" + encryptedData;
            Toast.makeText( this, data, Toast.LENGTH_SHORT ).show();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void onClick( View v )
    {
        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query( Uri.parse( "content://sms/inbox" ), null, null, null, null);

        int indexBody = cursor.getColumnIndex( SmsReceiver.BODY );
        int indexAddr = cursor.getColumnIndex( SmsReceiver.ADDRESS );

        if ( indexBody < 0 || !cursor.moveToFirst() ) return;

        smsList.clear();

        do
        {
            String str = "Sender: " + cursor.getString( indexAddr ) + "\n" + cursor.getString( indexBody );
            smsList.add( str );
        }
        while( cursor.moveToNext() );


        ListView smsListView = (ListView) findViewById( R.id.SMSList );
        smsListView.setAdapter( new ArrayAdapter<String>( this, android.R.layout.simple_list_item_1, smsList) );
        smsListView.setOnItemClickListener( this );
    }

    void checkPermissions(Activity activity){
        Dexter.withActivity(activity)
            .withPermission(Manifest.permission.READ_SMS)
            .withListener(new PermissionListener() {
                @Override
                public void onPermissionGranted(PermissionGrantedResponse response) {
                    button.setEnabled(true);
                }
                @Override
                public void onPermissionDenied(PermissionDeniedResponse response) {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    Uri uri = Uri.fromParts("package",getPackageName(),null);
                    intent.setData(uri);
                    startActivity(intent);

                }

                @Override
                public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                    token.continuePermissionRequest();
                }
            })
            .check();
    }
}
