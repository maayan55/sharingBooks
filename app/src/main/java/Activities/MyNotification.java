package Activities;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class MyNotification extends Application {
    public static final String CHANNEL_CUSTOMER_ORDER = "channelCustomerOrder";



    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannels();
    }

    private void createNotificationChannels() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel updatePayment = new NotificationChannel(CHANNEL_CUSTOMER_ORDER, "tttttttttt", NotificationManager.IMPORTANCE_DEFAULT);
            updatePayment.setDescription("ttttttttttt");
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(updatePayment);
        }
    }
}