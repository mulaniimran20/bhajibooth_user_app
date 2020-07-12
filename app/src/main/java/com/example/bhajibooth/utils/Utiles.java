package com.example.bhajibooth.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.util.Log;

import com.example.bhajibooth.MyApplication;
import com.example.bhajibooth.model.ProductItem;

import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Utiles {
    public static boolean isRef=false;
    public static boolean isSelect=false;
    public static int seletAddress=0;
    public static int ISVARIFICATION =-1;
    public static boolean ISRATES =false;
    public static List<ProductItem> ProductDatum=new ArrayList<>();

    public static String getDate() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static String getNextDate() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date tomorrow = calendar.getTime();
        return dateFormat.format(tomorrow);
    }

    static String IMEI = "";
    public static String getIMEI(Context context) {

        String unique_id = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        Log.e("unique_id", "-->" + unique_id);
        return unique_id;
    }
    public static boolean internetChack() {
       return true;
    }

    public static void CopyStream(InputStream is, OutputStream os)
    {
        final int buffer_size=1024;
        try
        {
            byte[] bytes=new byte[buffer_size];
            for(;;)
            {
                int count=is.read(bytes, 0, buffer_size);
                if(count==-1)
                    break;
                os.write(bytes, 0, count);
            }
        }
        catch(Exception ex){
            ex.toString();
        }
    }
}
