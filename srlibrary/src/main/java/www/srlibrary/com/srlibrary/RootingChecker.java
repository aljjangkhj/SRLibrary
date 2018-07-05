package www.srlibrary.com.srlibrary;

import android.os.Environment;

import java.io.File;

public class RootingChecker
{
    public static final String TAG = RootingChecker.class.getSimpleName();

    private static final String ROOT_PATH = Environment.getExternalStorageDirectory() + "";
    private static final String ROOTING_PATH_1 = "/system/bin/su";
    private static final String ROOTING_PATH_2 = "/system/xbin/su";
    private static final String ROOTING_PATH_3 = "/system/app/SuperUser.apk";
    private static final String ROOTING_PATH_4 = "/data/data/com.noshufou.android.su";

    public interface RESULT_ROOTING_FLAG
    {
        int UNKNOWN_ERROR   = 0x000;
        int ROOTING         = 0x0001;
        int NOT_ROOTING     = 0x0002;
    }

    public RootingChecker(){
        ;
    }


    public static int checkRooting()
    {
        //시스템 권한있는지 체크
        if(checkSystemRootPermission())
            return RESULT_ROOTING_FLAG.ROOTING;

        //루팅 파일들이 존재 하는지 체크
        if(checkRootingFiles(createFiles(RootFilesPath)))
            return RESULT_ROOTING_FLAG.ROOTING;


        //정상 단말
        return RESULT_ROOTING_FLAG.NOT_ROOTING;
    }


    private static String[] RootFilesPath = new String[]{
            ROOT_PATH + ROOTING_PATH_1 ,
            ROOT_PATH + ROOTING_PATH_2 ,
            ROOT_PATH + ROOTING_PATH_3 ,
            ROOT_PATH + ROOTING_PATH_4
    };


    /**
     * 시스템 권한을 사용할 수 있는지 체크
     * @return TRUE : 루팅, FALSE : 정상
     */
    private static boolean checkSystemRootPermission()
    {
        boolean ret;
        try
        {
            Runtime.getRuntime().exec("su");
            ret = true;
        } catch ( Exception e) {
            // Exception 일경우 루팅 false;
            ret = false;
        }

        return ret;
    }

    /**
     * 루팅파일 의심 Path를 가진 파일들을 생성 한다.
     */
    private static File[] createFiles(String[] sfiles)
    {
        File[] rootingFiles = new File[sfiles.length];
        for(int i=0 ; i < sfiles.length; i++){
            rootingFiles[i] = new File(sfiles[i]);
        }
        return rootingFiles;
    }

    /**
     * 루팅파일 여부를 확인 한다.
     */
    private static boolean checkRootingFiles(File... file)
    {
        boolean result = false;
        for(File f : file){
            if(f != null && f.exists() && f.isFile()){
                result = true;
                break;
            }else{
                result = false;
            }
        }
        return result;
    }

    private boolean rootingCheck() {

        int retRooting = RootingChecker.checkRooting();

        if (retRooting == RootingChecker.RESULT_ROOTING_FLAG.ROOTING){
            return true;
        }else if (retRooting == RootingChecker.RESULT_ROOTING_FLAG.NOT_ROOTING){
            return false;
        }else{
            return true;
        }
    }
}
