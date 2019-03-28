package tracking.sms_tracking.sms.com.sms_tracking.home.models;



import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import tracking.sms_tracking.sms.com.sms_tracking.helper.Urls;
import tracking.sms_tracking.sms.com.sms_tracking.home.SmsCallBack;
import tracking.sms_tracking.sms.com.sms_tracking.home.api.SmsApi;
import tracking.sms_tracking.sms.com.sms_tracking.home.models.data.SmsData;

public class RetrofitSmsProvider implements SmsProvider{

    private SmsApi smsApi;
    private Call<SmsData> smsDataCall;

    public RetrofitSmsProvider() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).connectTimeout(5, TimeUnit.MINUTES)
                .readTimeout(5, TimeUnit.MINUTES).build();

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Urls.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        this.smsApi = retrofit.create(SmsApi.class);
    }

    @Override
    public void getSmsData(String mobile, String data, final SmsCallBack smsCallBack) {
        smsDataCall = smsApi.getSmsResponse(mobile, data);

        smsDataCall.enqueue(new Callback<SmsData>() {
            @Override
            public void onResponse(Call<SmsData> call, Response<SmsData> response) {
                try {
                    smsCallBack.onSuccess(response.body());
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SmsData> call, Throwable t) {
                t.printStackTrace();
                smsCallBack.onFailure("No Internet Connection");
            }
        });
    }
}
