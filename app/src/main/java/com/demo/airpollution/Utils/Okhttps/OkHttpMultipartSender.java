package com.demo.airpollution.Utils.Okhttps;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.demo.airpollution.MyLog;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.ConnectionSpec;
import okhttp3.HttpUrl;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.Context.WIFI_SERVICE;

public class OkHttpMultipartSender  {

    protected String m_sApiUri = "";
    //private String API_AIRPOLLUTION = "https://data.epa.gov.tw/api/v1/aqx_p_432";
    String mReturnString = "";
    private String API_AIRPOLLUTION = "https://data.epa.gov.tw/api/v1/aqx_p_432?limit=1000&api_key=9be7b239-557b-4c10-9775-78cadfc555e9&sort=ImportDate%20desc&format=json";
    public int mConnectErrCode = 0;
    public boolean isSuccess = false;
    protected Handler m_hCallbackHandler = null;
    protected int m_nReturnCmd;
    protected int m_nReturnErrorCmd = -1;
    protected int m_nCmd ;
    protected int m_nConnectRetryCnt = 1;

    protected int m_nTimeOut = 30;   //secand
    static public String exceptionMessage = "";
    //API功能代號
    static public final int API_POLLUTION = 0;


    public void setParam(int cmd) {
        m_nCmd = cmd;
        //HttpPostCallback = callback;
        switch (m_nCmd) {
            case API_POLLUTION:
                m_sApiUri = API_AIRPOLLUTION;
                break;
            default:
                break;
        }
    }
    private void sendCallbackMessage()
    {
        Message message;
        message = m_hCallbackHandler.obtainMessage(m_nReturnCmd, mReturnString);
        m_hCallbackHandler.sendMessage(message);
    }

    private Runnable mUploadThread = new Runnable() {
        public void run() {
            send();

            if(m_hCallbackHandler != null)
                sendCallbackMessage();

        }
    };
    public void sendRequest() {
        Thread thread = new Thread(mUploadThread);
        thread.start();
    }
    public boolean isSuccess(){
        return isSuccess;
    }
    public String send()
    {
        int nTryCnt = 0;
        boolean bSuccess = false;

        for (nTryCnt = 0;nTryCnt < m_nConnectRetryCnt ;nTryCnt++) {
            isSuccess = post();
        }

        if(!isSuccess)
            m_nReturnCmd = m_nReturnErrorCmd;

        return mReturnString;
    }
    //金亞太版本 沒開wifi選像會閃退  所以send之前需先確認
    public boolean isWifiOpen(Context ctx){
        WifiManager wifiManager = (WifiManager) ctx.getSystemService(WIFI_SERVICE);
        if(wifiManager.isWifiEnabled()) return true;
        else return false;
    }
    protected boolean httpGet()
    {
        boolean bSuccess = false;
        OkHttpClient okHttpClient =new OkHttpClient()
                .newBuilder()
                .connectTimeout(m_nTimeOut,TimeUnit.SECONDS)
                .writeTimeout(m_nTimeOut,TimeUnit.SECONDS)
                .readTimeout(m_nTimeOut,TimeUnit.SECONDS)
                .build();

        HttpUrl.Builder builder = HttpUrl.parse(m_sApiUri).newBuilder();
        String UrlString = builder.toString();
        Request request = new Request.Builder()
                .url(UrlString)
                .get()
                .build();

        Call call = okHttpClient.newCall(request);
        try {
            Response response = call.execute();
            //System.out.println(response.body().string());

            if(response.code() == 200) {
                mReturnString = response.body().string();
                bSuccess = true;
            }
            else {
            //    bSuccess = true;
            //    mReturnString = "{\"version\": \"1.0.1.0412\", \"apk\": \"http://w7.papagoinc.com/Pakka_20180411_release.apk\"}";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bSuccess;
    }

    protected boolean post(){
        String result = "";
        boolean bSuccess = false;
        boolean bFileReady = false;
        exceptionMessage = "";
        mConnectErrCode = 0;
        mReturnString = "";

        bFileReady = true;

        OkHttpClient okHttpClient =new OkHttpClient()
                .newBuilder()
                .connectTimeout(m_nTimeOut,TimeUnit.SECONDS)
                .writeTimeout(m_nTimeOut,TimeUnit.SECONDS)
                .readTimeout(m_nTimeOut,TimeUnit.SECONDS)
                //.addInterceptor(new NetInterceptor())
                //.retryOnConnectionFailure(true)
                .connectionSpecs(Arrays.asList(
                        ConnectionSpec.MODERN_TLS,
                        ConnectionSpec.COMPATIBLE_TLS,
                        ConnectionSpec.CLEARTEXT))
                .build();

        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        //欄位Demo寫死
        builder.addFormDataPart("api_key", "9be7b239-557b-4c10-9775-78cadfc555e9");
        builder.addFormDataPart("limit", "1000");
        builder.addFormDataPart("sort", "ImportDate desc");
        builder.addFormDataPart("format", "json");
        RequestBody body = builder.build();

        try {
            Request request = new Request.Builder()
                    .url(m_sApiUri)
                    .header("Content-Type", "multipart/form-data")
                    .header("Accept", "application/json")
                    .post(body)
                    .build();

            Call call = okHttpClient.newCall(request);
            Response response = call.execute();
            //System.out.println(response.body().string());
            if(response.code() == 200) {
                mReturnString = response.body().string();
                bSuccess = true;
            } else {
                mReturnString = response.body().string();
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            mConnectErrCode = 5;
    } catch(SocketTimeoutException e){
            e.printStackTrace();
        }catch (IOException e) {
            MyLog.e("okHttp","okHttpClient  IOException ");
            e.printStackTrace();
            exceptionMessage = e.getMessage();
            mConnectErrCode = 1;
            if(exceptionMessage != null) {
                if (exceptionMessage.toLowerCase().contains("no address associated with hostname"))
                    mConnectErrCode = 1;
                else if (exceptionMessage.toLowerCase().contains("timed out"))
                    mConnectErrCode = 2;
                else
                    mConnectErrCode = 3;
                MyLog.e("okHttp", exceptionMessage);
            }
            else
                mConnectErrCode = 4;

        }
        return bSuccess;
    }

}
