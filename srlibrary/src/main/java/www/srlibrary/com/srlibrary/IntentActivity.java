package www.srlibrary.com.srlibrary;

import android.app.Activity;
import android.content.Intent;

public class IntentActivity {

    public static void MoveToMain(Activity context, Intent intent)
    {
        context.startActivity(intent);
        context.overridePendingTransition(R.anim.slide_center_from_right, R.anim.slide_left_from_center);
    }

    public static void MoveToActivity(Activity context, Intent intent)
    {
        context.startActivity(intent);
        context.overridePendingTransition(R.anim.slide_center_from_right, R.anim.slide_left_from_center);
    }

    public static void BackToActivity(Activity context, Intent intent)
    {
        context.startActivity(intent);
        context.overridePendingTransition(R.anim.slide_center_from_left, R.anim.slide_right_from_center);
    }

    public static void FinishAnim(Activity context)
    {
        context.finish();
        context.overridePendingTransition(R.anim.slide_center_from_left, R.anim.slide_right_from_center);
    }

}
