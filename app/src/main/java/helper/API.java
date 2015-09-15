package helper;

/**
 * Created by amirfazwan on 9/9/15.
 */
public class API {
    //public static String HOST = "http://192.168.1.29/";
    //public static String HOST = "http://192.168.1.32/";
    public static String HOST = "http://dev.rootmybox.com/";
    public static String DIRECTORY = "qittoalert/";
    public static String API = "API/";
    public static String FILE_PATH = "api.php?";
    public static String URL = HOST + DIRECTORY + API + FILE_PATH;
    //public static String STATISTIC_2015 = "statistic_geocode_2015.json";
    //http://dev.rootmybox.com/qittoalert/API/api.php?getData&tahun=2015
    //public static String URL_STATISTICS =  HOST + DIRECTORY + STATISTIC_2015;
    public static String FORECAST = "http://api.openweathermap.org/data/2.5/forecast?q=kajang,my&mode=json&units=metric&cnt=1";
    public static String WEATHER_ICON = "http://openweathermap.org/img/w/";
    public static String ICON_EXT = ".png";

}
