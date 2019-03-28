package tracking.sms_tracking.sms.com.sms_tracking.home.models;

import tracking.sms_tracking.sms.com.sms_tracking.home.SmsCallBack;

public interface SmsProvider {

    void getSmsData(String mobile, String data, SmsCallBack smsCallBack);
}
