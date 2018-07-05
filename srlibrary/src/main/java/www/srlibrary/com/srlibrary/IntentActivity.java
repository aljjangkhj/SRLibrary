package www.srlibrary.com.srlibrary;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

public class IntentActivity extends AppCompatActivity{

    public void MoveToMain(Intent intent)
    {
        startActivity(intent);
        overridePendingTransition(R.anim.slide_center_from_right, R.anim.slide_left_from_center);
    }

    public void MoveToActivity(Intent intent)
    {
        startActivity(intent);
        overridePendingTransition(R.anim.slide_center_from_right, R.anim.slide_left_from_center);
    }

    public void BackToActivity(Intent intent)
    {
        startActivity(intent);
        overridePendingTransition(R.anim.slide_center_from_left, R.anim.slide_right_from_center);
    }

    public void FinishAnim()
    {
        finish();
        overridePendingTransition(R.anim.slide_center_from_left, R.anim.slide_right_from_center);
    }

}
