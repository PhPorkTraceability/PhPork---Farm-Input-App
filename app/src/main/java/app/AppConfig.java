package app;

/**
 * Created by marmagno on 11/10/2015.
 */
public class AppConfig {
    // Server user login url
    //public static String IP = "10.0.4.225";
    //public static String IP = "192.168.5.173";
    public static String IP = "192.168.137.1";
    //public static String IP = "192.168.1.6";

    public static String URL_LOGIN = "http://" + IP + "/phpork/android_connect/login.php";

    public static String URL_GETPIGS = "http://" + IP + "/phpork/android_connect/getPigs.php";

    public static String URL_GETTAGS = "http://" + IP + "/phpork/android_connect/getTags.php";

    public static String URL_GETPENS = "http://" + IP + "/phpork/android_connect/getPens.php";

    public static String URL_GETALLDATA =
            "http://" + IP + "/phpork/android_connect/getTablesData.php";

    public static String URL_SENDNEWDATA =
            "http://" + IP + "/phpork/android_connect/insertNewData.php";

    public static String URL_SENDUPDATEDDATA =
            "http://" + IP + "/phpork/android_connect/updateTablesData.php";
}
