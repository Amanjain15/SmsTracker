package tracking.sms_tracking.sms.com.sms_tracking.home;

import tracking.sms_tracking.sms.com.sms_tracking.home.models.data.SmsData;

public interface SmsCallBack {

    void onSuccess(SmsData smsData);
    void onFailure(String error);
}
