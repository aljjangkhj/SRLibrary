package www.srlibrary.com.srlibrary.http;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import static www.srlibrary.com.srlibrary.http.HttpSetting.SERVER_IP;

public class HttpGetSend extends AsyncTask<String, Void, String>
{
    Context mContext = null;
    JSONObject json = null;
    public OnResponse CallBack = null;

    public boolean show_progress = true;

    public String SEND_URL = "";
    public String ParamString = "";

    static boolean m_session = false;
    static String m_cookies = "";

    public abstract interface OnResponse
    {
        public abstract void Response(String result);
    }

    public HttpGetSend(Context context)
    {
        mContext = context;
    }

    @Override
    protected void onPreExecute()
    {
        if(show_progress)
        {
            //서버 통신 중 로딩 창
//            dialog = new AppPosProgressDialog(mContext);
//            dialog.show();
        }
    }

    @Override
    protected String doInBackground(String... params)
    {
        // TODO Auto-generated method stub

        String result = "";

        result = HttpGetConn(params[0]);

        Log.e("HttpGetSend", "HttpGetConn result : " + result);

        return result;
    }

    @Override
    protected void onPostExecute(final String json)
    {
        closeProgress();

        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                if(json.contains("Error.0"))
                {
                    ShowFailedDialog(json);
                }
                else
                {
                    if(CallBack != null)
                    {
                        CallBack.Response(json);
                    }
                }
            }
        }, 100);
    }

    private void ShowFailedDialog(String Error)
    {
//        String msg = mContext.getResources().getString(R.string.data_failed);
//
//        if(Config.RELEASE == false)
//        {
//            String Server = Config.SERVER_IP + SEND_URL + ParamString;
//            msg = msg + "\n"
//                    + Server + "\n" + Error;
//        }
//
//        final Popup popup = new Popup(mContext, "통신오류", msg, "앱종료", "다시시도");
//        popup.Cancel_Click = new Popup.onClick()
//        {
//            @Override
//            public void onClick()
//            {
//                ((BaseActivity)mContext).APP_END();
//            }
//        };
//        popup.OK_Click = new Popup.onClick()
//        {
//            @Override
//            public void onClick()
//            {
//                HttpGetSend HttpSend = new HttpGetSend(mContext);
//                HttpSend.CallBack =  HttpGetSend.this.CallBack;
//                HttpSend.ParamString = HttpGetSend.this.ParamString;
//                HttpSend.execute(SEND_URL);
//            }
//        };
//        popup.show();
    }

    private void closeProgress()
    {
//        try
//        {
//            if(dialog != null)
//            {
//                if (dialog.isShowing())
//                {
//                    dialog.dismiss();
//                }
//            }
//        }
//        catch (Exception e)
//        {
//            Log.e(Config.TAG,"HttpGetSend closeProgress error : " + e.getMessage());
//        }
    }

    public void saveCookie( HttpURLConnection conn)
    {
        Map<String, List<String>> imap = conn.getHeaderFields( ) ;

        if(imap.containsKey( "Set-Cookie" ) )
        {
            List<String> lString = imap.get( "Set-Cookie" ) ;
            for( int i = 0 ; i < lString.size() ; i++ ) {
                m_cookies += lString.get( i ) ;
            }

            m_session = true ;

            Log.e("HttpGetSend", "HttpGetConn saveCookie true : " + m_cookies);
        }
        else
        {
            m_session = false ;
            Log.e("HttpGetSend", "HttpGetConn saveCookie false : " + m_cookies);
        }
    }

    public void addParam(String name, String value)
    {
        String vName = "";
        String vValue = "";

        if(name == null)
        {
            return;
        }

        if(value == null)
        {
            value = "";
        }

        if(value.matches(".*[ㄱ-ㅎㅏ-ㅣ가-힣]+.*"))
        {
            // 한글이 포함된 문자열
            try
            {
                vName = name;
                vValue = URLEncoder.encode(value, "UTF-8");
            }
            catch(Exception e)
            {
                vName =name;
                vValue =value;
            }
        }
        else
        {
            // 한글이 포함되지 않은 문자열
            vName =name;
            vValue =value;
        }

        if (ParamString.equals(""))
        {
            ParamString = ParamString + "?"+  vName + "=" + vValue;
        }
        else
        {
            ParamString = ParamString + "&"+  vName + "=" + vValue;
        }
    }

    public String HttpGetConn(String cmd)
    {
        HttpURLConnection httpcon;

        String result = null;
        SEND_URL = cmd;
        String Server = "";

        try
        {
            Server = SERVER_IP + cmd + ParamString;
            Log.e("HttpGetSend", "HttpGetConn URL : " + Server);
            URL ServerURL = new URL(Server);

            httpcon = (HttpURLConnection)ServerURL.openConnection();

            httpcon.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

            if(m_session)
            {
                httpcon.setRequestProperty("Cookie", m_cookies );
                Log.e("HttpGetSend", "HttpGetConn SEND  Cookie: " + m_cookies);
            }

            httpcon.setUseCaches(true);
            httpcon.setDoInput(true);
            httpcon.setConnectTimeout(10000);
            httpcon.setReadTimeout(20000);
            httpcon.setRequestMethod("GET");
            httpcon.connect();
            saveCookie(httpcon);

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
            result = "Error.001 : " + e.getMessage();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            result = "Error.002 : " + e.getMessage();
        }
        catch(Exception e)
        {
            result = "Error.003 : " + e.getMessage();
        }

        return result;
    }
}

