package com.furqoncreative.submission5.scheduler;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.furqoncreative.submission5.R;
import com.furqoncreative.submission5.model.movie.Movie;
import com.furqoncreative.submission5.model.movie.MoviesResponse;
import com.furqoncreative.submission5.util.ApiUtils;
import com.furqoncreative.submission5.view.activity.MainActivity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.furqoncreative.submission5.BuildConfig.API_KEY;

public class MovieReminder extends BroadcastReceiver {
    public static final String TYPE_RELEASE = "ReleaseReminder";
    public static final String TYPE_DAILY = "DailyReminder";
    private static final String EXTRA_TITLE = "title";
    private static final String EXTRA_MESSAGE = "message";
    private static final String EXTRA_TYPE = "type";
    private final static String GROUP_KEY_RELEASE = "group_key_emails";
    private static final String TIME_FORMAT = "HH:mm";
    private final static int ID_DAILY = 1;
    private final static int MAX_RELEASE = 15;
    private static final int ID_RELEASE = 0;
    private int MIN_RELEASE = 0;

    @Override
    public void onReceive(Context context, Intent intent) {
        String type = intent.getStringExtra(EXTRA_TYPE);
        String message = intent.getStringExtra(EXTRA_MESSAGE);
        String title = intent.getStringExtra(EXTRA_TITLE);

        int notifId = Objects.requireNonNull(type).equalsIgnoreCase(TYPE_RELEASE) ? ID_RELEASE : ID_DAILY;

        if (Objects.requireNonNull(message).equals(context.getResources().getString(R.string.release_reminder_message))) {
            getReleaseToday(context, title, message);
        } else {
            showDailyNotification(context, title, message, notifId);
        }
    }

    private void showDailyNotification(Context context, String title, String message, int notifId) {
        String CHANNEL_ID = "Channel_01";
        String CHANNEL_NAME = "daily_channel";

        NotificationManager notificationManagerCompat = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, ID_DAILY, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_movie)
                .setContentTitle(title)
                .setContentText(message)
                .setContentIntent(pendingIntent)
                .setColor(ContextCompat.getColor(context, android.R.color.transparent))
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setSound(alarmSound);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT);

            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{1000, 1000, 1000, 1000, 1000});

            builder.setChannelId(CHANNEL_ID);

            if (notificationManagerCompat != null) {
                notificationManagerCompat.createNotificationChannel(channel);
            }
        }

        Notification notification = builder.build();

        if (notificationManagerCompat != null) {
            notificationManagerCompat.notify(notifId, notification);
        }

    }

    private void showReleaseNotification(Context context, String title, String movieTitle, String message) {
        String CHANNEL_ID = "channel_02";
        String CHANNEL_NAME = "release_channel";


        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Bitmap largeIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_movie);
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, ID_RELEASE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder mBuilder;


        if (MIN_RELEASE < MovieReminder.MAX_RELEASE) {
            mBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setContentTitle("\"" + movieTitle + "\"" + " " + title)
                    .setContentText(message)
                    .setSmallIcon(R.drawable.ic_movie)
                    .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                    .setLargeIcon(largeIcon)
                    .setGroup(GROUP_KEY_RELEASE)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);
        } else {

            NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle()
                    .addLine(message)
                    .setBigContentTitle("\"" + movieTitle + "\"")
                    .setSummaryText("New Movie " + title);
            mBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setContentTitle("\"" + movieTitle + "\"")
                    .setContentText(message)
                    .setSmallIcon(R.drawable.ic_movie)
                    .setGroup(GROUP_KEY_RELEASE)
                    .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                    .setGroupSummary(true)
                    .setContentIntent(pendingIntent)
                    .setStyle(inboxStyle)
                    .setAutoCancel(true);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT);

            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{1000, 1000, 1000, 1000, 1000});

            mBuilder.setChannelId(CHANNEL_ID);

            if (mNotificationManager != null) {
                mNotificationManager.createNotificationChannel(channel);
            }
        }

        Notification notification = mBuilder.build();

        if (mNotificationManager != null) {
            mNotificationManager.notify(MIN_RELEASE, notification);
        }
    }


    public void setRepeatingAlarm(Context context, String type, String time, String title, String message) {

        if (isDateInvalid(time, TIME_FORMAT)) return;

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, MovieReminder.class);
        intent.putExtra(EXTRA_MESSAGE, message);
        intent.putExtra(EXTRA_TITLE, title);
        intent.putExtra(EXTRA_TYPE, type);

        String[] timeArray = time.split(":");

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArray[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(timeArray[1]));
        calendar.set(Calendar.SECOND, 0);

        int requestCode = type.equalsIgnoreCase(TYPE_RELEASE) ? ID_RELEASE : ID_DAILY;
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0);
        if (alarmManager != null) {
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        }
        if (requestCode == ID_DAILY) {
            Toast.makeText(context, context.getResources().getString(R.string.daily_reminder_enable), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, context.getResources().getString(R.string.release_reminder_enable), Toast.LENGTH_SHORT).show();
        }
    }

    public void cancelAlarm(Context context, String type) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, MovieReminder.class);
        int requestCode = type.equalsIgnoreCase(TYPE_RELEASE) ? ID_RELEASE : ID_DAILY;
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0);
        Objects.requireNonNull(alarmManager).cancel(pendingIntent);
        if (requestCode == ID_DAILY) {
            Toast.makeText(context, context.getResources().getString(R.string.daily_reminder_disable), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, context.getResources().getString(R.string.release_reminder_disable), Toast.LENGTH_SHORT).show();
        }
    }

    private void getReleaseToday(final Context context, String title, String message) {
        final DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String today = sdf.format(date);
        new ApiUtils();
        Call<MoviesResponse> call = ApiUtils.getApi().getReleaseToday(API_KEY, today, today);
        call.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                if (response.isSuccessful()) {
                    ArrayList<Movie> movies = new ArrayList<>(Objects.requireNonNull(response.body()).getMovies());
                    List<Movie> listMovies = response.body().getMovies();
                    for (int i = 0; i < listMovies.size(); i++) {
                        String movieTitle = listMovies.get(i).getTitle();
                        //   int movieId = listMovies.get(i).getId();
                        showReleaseNotification(context, title, movieTitle, message);
                        MIN_RELEASE++;
                    }
                    Log.d("Movie", "success loading from API");

                } else {
                    int statusCode = response.code();
                }
            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {

            }

        });
    }


    private boolean isDateInvalid(String date, String format) {
        try {
            DateFormat df = new SimpleDateFormat(format, Locale.getDefault());
            df.setLenient(false);
            df.parse(date);
            return false;
        } catch (ParseException e) {
            return true;
        }
    }
}
