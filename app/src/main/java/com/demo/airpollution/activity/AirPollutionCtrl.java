package com.demo.airpollution.activity;

import com.demo.airpollution.Constant.AirPollutionData;
import com.demo.airpollution.Interface.ICtrl;
import com.demo.airpollution.Interface.IHttpPost;
import com.demo.airpollution.Utils.Okhttps.HttpPostCtrl;
import com.demo.airpollution.Utils.Okhttps.OkHttpMultipartSender;

public class AirPollutionCtrl  implements IHttpPost {
    private ICtrl callback;   //回應訊息
    public void setCallback(ICtrl callback){
        this.callback = callback;
    }
    public void getDataFromAPI(){
        //call api to check login
        new HttpPostCtrl(OkHttpMultipartSender.API_POLLUTION,this).execute();
    }
    @Override
    public void onResult(boolean status, String result, String msg) {
        AirPollutionData.getInstance().parserData(result);
        callback.update();
    }
}
