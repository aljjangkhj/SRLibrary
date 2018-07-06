package www.srlibrary.com.srlibrary.http;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by cgw on 2016-07-12.
 */

public class HttpFCMPost extends AsyncTask<String, Void, String>
{
    Context mContext = null;
    JSONObject json = null;
    public OnResponse CallBack = null;
    private ProgressDialog dialog = null;

    String serverkey = "AIzaSyCZDufEKBDA4GzlGxkrsEQT4Mr26SXFtro";

    public abstract interface OnResponse
    {
        public abstract void Response(String result);
    }

    public HttpFCMPost(Context context)
    {
        mContext = context;
    }

    @Override
    protected void onPreExecute()
    {
        dialog = ProgressDialog.show(mContext, "", "서버 통신중입니다.", true);
    }

    @Override
    protected String doInBackground(String... params)
    {
        // TODO Auto-generated method stub
        String result = HttpPostConn(params[0]);
        return result;
    }

    @Override
    protected void onPostExecute(String json)
    {
        if (dialog.isShowing())
        {
            dialog.dismiss();
        }

        if(CallBack != null)
        {
            CallBack.Response(json);
        }
    }

    public String HttpPostConn(String data)
    {
        Log.e("HttpPostConn", "data: " + data);
        HttpURLConnection httpcon;


        String result = null;
        try
        {
            String Server = "https://fcm.googleapis.com/fcm/send";
            URL ServerURL = new URL(Server);
            //Connect
            httpcon = (HttpURLConnection)ServerURL.openConnection();
            httpcon.setDoOutput(true);
            httpcon.setRequestProperty("Content-Type", "application/json");
            httpcon.setRequestProperty("Authorization","key=" + serverkey);
            httpcon.setRequestMethod("POST");
            httpcon.connect();

            //Write
            OutputStream os = httpcon.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(data);
            writer.close();
            os.close();

            //Read
            BufferedReader br = new BufferedReader(new InputStreamReader(httpcon.getInputStream(),"UTF-8"));

            String line = null;
            StringBuilder sb = new StringBuilder();

            while ((line = br.readLine()) != null)
            {
                sb.append(line);
            }

            br.close();
            result = sb.toString();
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
            result = "[UnsupportedEncodingException] : " + e.getMessage();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            result = "[IOException] : " + e.getMessage();
        }

        return result;
    }

}