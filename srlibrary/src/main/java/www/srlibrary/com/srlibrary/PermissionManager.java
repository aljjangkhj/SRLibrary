package www.srlibrary.com.srlibrary;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

/**
 * Created by seeroo_dev on 2018. 6. 28..
 */

public class PermissionManager {
    private static final String TAG = PermissionManager.class.getSimpleName();

    public interface PERMISSION_REQUEST_CODE{
        int MY_PERMISSIONS_REQUEST_ALL                      = 0x0001;
        int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE         = 0x0002;
        int MY_PERMISSIONS_REQUEST_CAMERA                   = 0x0003;
        int MY_PERMISSIONS_REQUEST_GET_ACCOUNTS             = 0x0004;
        int MY_PERMISSIONS_REQUEST_RECEIVE_SMS              = 0x0005;
        int MY_PERMISSIONS_REQUEST_SEND_SMS                 = 0x0006;
        int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE   = 0x0007;
        int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE    = 0x0008;

        //add dangerous permission request code

    }

    private String[] reqAllPermissionStr = new String[]{
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.CAMERA,
            Manifest.permission.GET_ACCOUNTS,
            Manifest.permission.RECEIVE_SMS,
            Manifest.permission.SEND_SMS,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CALL_PHONE

            // add dangerous permission request
    };


    /**
     * 권한 필요 -> 권한 체크 -> 권한 요청 -> CANCEL (BY USER)
     * -> 권한 필요 -> 권한 체크 -> 권한 체크의 필요성 알림(NOTICE) or 권한 요청 -> OK (BY USER)
     * 권한을 요청하는 Activity에 onRequestPermissionsResult()가 override되어 있어야 한다.
     */


    private PermissionManager() {
    }


    private static PermissionManager mInstance;

    public synchronized static PermissionManager getInstance() {
        if (mInstance == null) {
            mInstance = new PermissionManager();
        }

        return mInstance;
    }


    public boolean checkPermission(Context context, String checkPermission) {
        // Here, thisActivity is the current activity
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (context.checkSelfPermission(checkPermission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }

        return true;
    }

    public void reqPermission(Activity context, String reqPermission, int reqCode){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            context.requestPermissions(new String[]{reqPermission}, reqCode);
        }
    }

    public boolean allCheckPermission(Activity context){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (String aReqAllPermissionStr : reqAllPermissionStr) {
                if (context.checkSelfPermission(aReqAllPermissionStr) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }

        return true;
    }

    public void allReqPermission(Activity context){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            context.requestPermissions(reqAllPermissionStr, PERMISSION_REQUEST_CODE.MY_PERMISSIONS_REQUEST_ALL);
        }
    }


//    @Override
//    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
//    {
//        switch (requestCode) {
//            case PERMISSION_REQUEST_CODE.MY_PERMISSIONS_REQUEST_ALL:
//                for (int i = 0; i < permissions.length; i++)
//                {
//                    String permission = permissions[i];
//                    int grantResult = grantResults[i];
//                    if (/*permission.equals(Manifest.permission.CAMERA) || */permission.equals(Manifest.permission.READ_PHONE_STATE)) {
//                        if(grantResult == PackageManager.PERMISSION_GRANTED) {
//                            //권한이 있을때
//
//                        } else {
//                            //권한이 없을때
//                        }
//                    }
//                }
//                break;
//        }
//    }

}
