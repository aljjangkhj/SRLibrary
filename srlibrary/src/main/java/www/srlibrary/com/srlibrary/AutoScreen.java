package www.srlibrary.com.srlibrary;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

public class AutoScreen {

    static float ScreenRate = 0;
    static public int DEVICE_SCREEN_HEIGHT = 2560; //
    static public int DEVICE_SCREEN_WIDTH = 1440; //
    public final static int BaseWidth = 1440; // xml들이 작업된 기준 넓이
    public final static int BaseHeight = 2560; // xml들이 작업된 기준 높이.

    // option 1 = 높이기준으로 화면조절, 2 = 가로 기준으로 화면조절
    private static void Adjust(Context context, View view, int option)
    {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        DEVICE_SCREEN_HEIGHT = displayMetrics.heightPixels;
        DEVICE_SCREEN_WIDTH = displayMetrics.widthPixels;

        if(option == 1)
        {
            ScreenRate = (float)DEVICE_SCREEN_HEIGHT / (float)BaseHeight;
        }
        else
        {
            ScreenRate = (float)DEVICE_SCREEN_WIDTH / (float)BaseWidth;
        }

        setScreen(view);
    }

    // 루프 돌면서 뷰의 자식뷰까지 리사이즈함.
    private static void setScreen(View view)
    {
        if (view != null)
        {
            if (view instanceof ViewGroup)
            {
                ViewGroup vg = (ViewGroup) view;

                int len = vg.getChildCount();

                for (int i = 0; i < len; i++)
                {
                    View childview = vg.getChildAt(i);

                    adjustView(childview);

                    // 자식뷰도 리사이즈 해줌.
                    setScreen(childview);
                }
            }
        }
        else
        {
            Log.v("AutoScreen","ScreenAdjust setScreen setScreen view = null");
        }
    }

    // 가로, 세로 길이와 마진값을 늘리거나 줄여줌
    private static void adjustView(View v)
    {
        ViewGroup.LayoutParams par = v.getLayoutParams();

        if(par.width == ViewGroup.LayoutParams.WRAP_CONTENT)
        {

        }
        else if(par.width == ViewGroup.LayoutParams.MATCH_PARENT)
        {

        }
        else if(par.width == ViewGroup.LayoutParams.FILL_PARENT)
        {

        }
        else if(par.width <= 2)
        {
            // 2px이하는 리사이즈 안함
        }
        else
        {
            par.width = (int)((float)par.width * ScreenRate);
        }

        if(par.height == ViewGroup.LayoutParams.WRAP_CONTENT)
        {

        }
        else if(par.height == ViewGroup.LayoutParams.MATCH_PARENT)
        {

        }
        else if(par.height == ViewGroup.LayoutParams.FILL_PARENT)
        {

        }
        else if(par.height <= 2)
        {
            // 2px이하는 리사이즈 안함
        }
        else
        {
            par.height = (int)((float)par.height * ScreenRate);
        }

        try
        {
            ViewGroup.MarginLayoutParams margin = (ViewGroup.MarginLayoutParams)par;

            if(margin.leftMargin <= 2)
            {
                // 2px이하는 리사이즈 안함
            }
            else
            {
                margin.leftMargin = (int)((float)margin.leftMargin * ScreenRate);
            }

            if(margin.topMargin <= 2)
            {
                // 2px이하는 리사이즈 안함
            }
            else
            {
                margin.topMargin = (int)((float)margin.topMargin * ScreenRate);
            }

            if(margin.rightMargin <= 2)
            {
                // 2px이하는 리사이즈 안함
            }
            else
            {
                margin.rightMargin = (int)((float)margin.rightMargin * ScreenRate);
            }

            if(margin.bottomMargin <= 2)
            {
                // 2px이하는 리사이즈 안함
            }
            else
            {
                margin.bottomMargin = (int)((float)margin.bottomMargin * ScreenRate);
            }

            ((ViewGroup.MarginLayoutParams) par).setMargins(margin.leftMargin, margin.topMargin, margin.rightMargin, margin.bottomMargin);
        }
        catch (Exception e)
        {
            // 유튜브뷰는 마진값 조절이 안되었음. 귀찮아서 더 안알아봄.
            // 수정해 주시면 감사.
            Log.v("AutoScreen","ScreenAdjust adjustView 이 뷰는 마진값을 조절할 수 없음");
        }

        try
        {
            int padLeft = 0, padTop = 0, padRight = 0, padBottom = 0;
            if(v.getPaddingLeft() <= 2)
            {
                // 2px이하는 리사이즈 안함
            }
            else
            {
                padLeft = (int)((float)v.getPaddingLeft() * ScreenRate);
            }

            if(v.getPaddingTop() <= 2)
            {
                // 2px이하는 리사이즈 안함
            }
            else
            {
                padTop = (int)((float)v.getPaddingTop() * ScreenRate);
            }

            if(v.getPaddingRight() <= 2)
            {
                // 2px이하는 리사이즈 안함
            }
            else
            {
                padRight = (int)((float)v.getPaddingRight() * ScreenRate);
            }

            if(v.getPaddingBottom() <= 2)
            {
                // 2px이하는 리사이즈 안함
            }
            else
            {
                padBottom = (int)((float)v.getPaddingBottom() * ScreenRate);
            }

            v.setPadding(padLeft, padTop, padRight, padBottom);
        }
        catch (Exception e)
        {
            // 유튜브뷰는 마진값 조절이 안되었음. 귀찮아서 더 안알아봄.
            // 수정해 주시면 감사.
            Log.v("AutoScreen","ScreenAdjust adjustView 이 뷰는 마진값을 조절할 수 없음");
        }

        v.setLayoutParams(par);
    }

    public static void init_autoscreen(Context context, int option)
    {
        try
        {
            Adjust(context, ((Activity)context).getWindow().getDecorView(), option);
        }
        catch (Exception e)
        {
            Log.v("AutoScreen", "BaseActivity init_autoscreen Error : " + e.getMessage());
        }
    }

}
