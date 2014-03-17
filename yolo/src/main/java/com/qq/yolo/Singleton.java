package com.qq.yolo;
import java.util.Calendar;
import java.util.TimeZone;

public class Singleton {

    public static void main(String args[]){
        Singleton single = Singleton.getInstance();

        try {
            single.login("q","q");
            single.sendData("adsf");
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }


    }

    public void whoami(){
        String uu = Url + "whoami";
        try {
            http.sendGet(uu);
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            System.out.println(e);
        }
    }

    private String getDatetime(){
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.clear();
        calendar.set(2011, Calendar.OCTOBER, 1);
        long secondsSinceEpoch = calendar.getTimeInMillis() / 1000L;
        return String.valueOf(secondsSinceEpoch);
    }

    private String Url = "http://146.185.131.151/api/device/";
    private static Singleton instance;
    private Singleton(){}
    MyHttpURLConnection http = new MyHttpURLConnection();

    public void setUrl(String url){
        Url = url;
    }

    public void login(String user, String pass) throws Exception {
        String url = Url + "login";
        String body = String.format("{\"login\":\"%s\",\"password\":\"%s\"}", user, pass);
        http.sendPost(body, url);

    }
    public void logout() throws Exception {
        String url = Url + "logout";
        http.sendPost("{}", url);
    }

    public void sendData(String params) throws Exception {
        String url = Url + "geodatas";
        // {"login":"t","password":"t"}
        params = "{\"when\": 13950509538, \"coords\": {\"accuracy\": 1, \"latitude\": 69.3, \"longitude\": 27.2}}";
        http.sendPost(params, url);
    }




    public static Singleton getInstance()
    {
        if (instance == null)
        {
            instance = new Singleton();
        }
        return instance;
    }
}
