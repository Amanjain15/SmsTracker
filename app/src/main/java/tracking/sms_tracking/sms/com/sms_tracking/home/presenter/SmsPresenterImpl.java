package tracking.sms_tracking.sms.com.sms_tracking.home.presenter;

import tracking.sms_tracking.sms.com.sms_tracking.home.SmsCallBack;
import tracking.sms_tracking.sms.com.sms_tracking.home.models.SmsProvider;
import tracking.sms_tracking.sms.com.sms_tracking.home.models.data.SmsData;
import tracking.sms_tracking.sms.com.sms_tracking.home.view.SmsView;

public class SmsPresenterImpl implements  SmsPresenter {

    private SmsView smsView;
    private SmsProvider smsProvider;

    public SmsPresenterImpl(SmsView smsView, SmsProvider smsProvider) {
        this.smsView = smsView;
        this.smsProvider = smsProvider;
    }

    @Override
    public void getSmsData(String mobile, String data) {
        smsProvider.getSmsData(mobile, data, new SmsCallBack() {
            @Override
            public void onSuccess(SmsData smsData) {
                try {
                    smsView.showMessage(smsData.getMessage());
                }catch (NullPointerException e){
                    e.printStackTrace();
                    smsView.showMessage("Empty ResponseJson");
                }

            }

            @Override
            public void onFailure(String error) {
                smsView.showMessage(error);
            }
        });
    }
}
