package www.srlibrary.com.srlibrary.http;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
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
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import static www.srlibrary.com.srlibrary.http.HttpSetting.SERVER_IP;


public class HttpsPostSend extends AsyncTask<String, Void, String>
{
    Context mContext = null;
    JSONObject json = null;
    public OnResponse CallBack = null;

    public boolean show_progress = true;
//    private TransparentProgressDialog dialog = null;

    public String SEND_URL = "";

    static boolean m_session = false;
    static String m_cookies = "";

    public abstract interface OnResponse
    {
        public abstract void Response(String result);
    }

    public HttpsPostSend(Context context)
    {
        mContext = context;
    }

    @Override
    protected void onPreExecute()
    {
        if(show_progress)
        {
            //통신 중 로딩
//            dialog = new TransparentProgressDialog(mContext, R.drawable.loading1);
//            dialog.show();
        }
    }

    @Override
    protected String doInBackground(String... params)
    {
        // TODO Auto-generated method stub

        String result = "";

        result = HttpsPostConn(params[0], params[1]);

        Log.e("HttpsPostSend", "HttpsPostConn result : " + result);

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

    public void saveCookie( HttpURLConnection conn)
    {
        Map<String, List<String>> imap = conn.getHeaderFields( ) ;

        if( imap.containsKey( "Set-Cookie" ) )
        {
            List<String> lString = imap.get( "Set-Cookie" ) ;
            for( int i = 0 ; i < lString.size() ; i++ ) {
                m_cookies += lString.get( i ) ;
            }

            m_session = true ;

            Log.e("HttpsPostSend", "HttpsPostConn saveCookie true : " + m_cookies);
        }
        else
        {
            m_session = false ;
            Log.e("HttpsPostSend", "HttpsPostConn saveCookie false : " + m_cookies);
        }
    }


    private void closeProgress()
    {
        //통신 로딩창 닫기
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
//            Log.e(Config.TAG,"HttpsPostSend closeProgress error : " + e.getMessage());
//        }
    }

    public String HttpsPostConn(String cmd, String data)
    {
        HttpsURLConnection httpcon;

        String result = null;
        SEND_URL = cmd;
        String Server = "";

        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() {
                        X509Certificate[] myTrustedAnchors = new X509Certificate[0];
                        return myTrustedAnchors;
                    }

                    @Override
                    public void checkClientTrusted(X509Certificate[] certs, String authType) {
                    }

                    @Override
                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
                    }
                }
        };



        SSLSocketFactoryExtended factory = null;

        try {
            factory = new SSLSocketFactoryExtended();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }

        final SSLSocketFactoryExtended finalFactory = factory;

        try
        {
            Server = SERVER_IP + cmd;
            Log.e("HttpsPostSend", "HttpsPostConn URL : " + Server);
            URL ServerURL = new URL(Server);

            httpcon = (HttpsURLConnection)ServerURL.openConnection();

            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new SecureRandom());
            httpcon.setDefaultSSLSocketFactory(sc.getSocketFactory());
            httpcon.setDefaultHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String arg0, SSLSession arg1) {
                    return true;
                }
            });

//            // Install the all-trusting trust manager
            httpcon.setSSLSocketFactory(finalFactory);

            httpcon.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

            if(m_session)
            {
                httpcon.setRequestProperty("Cookie", m_cookies );
                Log.e("HttpsPostSend", "HttpsPostConn SEND  Cookie: " + m_cookies);
            }

            httpcon.setUseCaches(true);
            httpcon.setDoInput(true);
            httpcon.setConnectTimeout(10000);
            httpcon.setReadTimeout(20000);
            httpcon.setRequestMethod("POST");
            httpcon.connect();
            saveCookie(httpcon);

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



    private void ShowFailedDialog(String Error)
    {
//        String msg = mContext.getResources().getString(R.string.data_failed);
//
//        if(Config.RELEASE == false)
//        {
//            String Server = Config.SERVER_IP + SEND_URL;
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
//                HttpsPostSend HttpSend = new HttpsPostSend(mContext);
//                HttpSend.CallBack =  HttpsPostSend.this.CallBack;
//                HttpSend.execute(SEND_URL);
//            }
//        };
//        popup.show();
        String msg = "서버와 통신에 실패 했습니다.";
        String Server = SERVER_IP + SEND_URL;
        msg = msg + "\n" + Server + "\n" + Error;

        Log.e("HttpsPostSend", "통신오류: " + msg);
    }

    public void ClearTop()
    {
//        for(int i = 0; i < Config.actList.size(); i++)
//        {
//            Config.actList.get(i).finish();
//        }
    }

    public static void clearCookie() {
        if(m_session) {
            m_session = false;
            m_cookies = null;
        }
    }
}

