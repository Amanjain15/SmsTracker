package tracking.sms_tracking.sms.com.sms_tracking.home.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import tracking.sms_tracking.sms.com.sms_tracking.helper.Urls;
import tracking.sms_tracking.sms.com.sms_tracking.home.models.data.SmsData;

public interface SmsApi {
    @GET(Urls.REQUEST_SMS_DETAILS)
    Call<SmsData> getSmsResponse(   @Query("mobile") String mobile,
                                    @Query("data") String data
                                 );
}
