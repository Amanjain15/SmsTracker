package tracking.sms_tracking.sms.com.sms_tracking.home.models.data;

public class SmsData {
    private boolean success;
    private String message;


    public SmsData(boolean success, String message) {
        this.success = success;
        this.message = message;
    }


    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}
