package com.demo.airpollution.Utils.Okhttps;

import android.os.AsyncTask;

import com.demo.airpollution.Interface.IHttpPost;
import com.demo.airpollution.MyLog;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Project : air demo
 * Class : HttpPostCtrl
 * Create by Ives 2022
 * 上層專門拿來做http 溝通的 class
 */

public class HttpPostCtrl extends AsyncTask<Void, Void, String>{
    String TAG = "HttpPostCtrl";
    int API_Number = -1;
    IHttpPost callback;
    OkHttpMultipartSender http;


    public HttpPostCtrl(int API,IHttpPost call){
        MyLog.i(TAG,"API = " + API);
        API_Number = API;
        callback = call;
    }

    @Override
    protected String doInBackground(Void... voids) {
        http = new OkHttpMultipartSender();
        http.setParam(API_Number);
        if (API_Number == OkHttpMultipartSender.API_POLLUTION) {
            //如果有參數可帶入
        }
        String result = http.send();
        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        //先將外層判斷處理起來
        MyLog.i(TAG,"result = " + result);
        boolean status = http.isSuccess;
        String msg = "";
        String data = "";
        String orders = "";
        String plantingList = "";
        if(!status){
            if (result.equals(""))   //沒有回傳200 一律都認為無網路
                msg = "請檢查網路連線";
            else
                msg = result;
            callback.onResult(status, data, msg);
        }else {
            try {
                JSONObject jsonobj = new JSONObject(result);
                if (jsonobj.has("records")) {
                    if (jsonobj.getString("records").length() == 0) {
                        MyLog.i(TAG, "無資料");
                    } else {
                        data = jsonobj.getJSONArray("records").toString();
                    }
                }
            } catch (JSONException e) {
                status = false;
                msg = "API-" + API_Number + " 格式錯誤";
                MyLog.e(TAG, msg + " : " + e.getMessage());
                callback.onResult(status, data, msg);
            }
            if (result.equals(""))   //沒有回傳200 一律都認為無網路
                msg = "請檢查網路連線";
            callback.onResult(status, data, msg);
        }
    }
}
