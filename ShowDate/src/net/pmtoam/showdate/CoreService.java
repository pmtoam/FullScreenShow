package net.pmtoam.showdate;

import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;

@SuppressLint("InflateParams")
public class CoreService extends Service
{

	private final String TAG = "CoreService";

	private BroadcastReceiver mReceiver;
	private WindowManager wm;
	private WindowManager.LayoutParams wmParams;
	private View layoutView;
	private TextView mTV;
	private Runnable mRunnable;
	private Handler handler;
	private String battery;
	private String screen_status = Intent.ACTION_SCREEN_ON;

	@Override
	public IBinder onBind(Intent intent)
	{
		return null;
	}

	@Override
	public void onCreate()
	{
		super.onCreate();
		Logger.e(TAG, "onCreate()");

		mRunnable = new Runnable()
		{

			@Override
			public void run()
			{
				if (mTV == null)
				{
					createView();
				}
				else
				{
					if (!TextUtils.isEmpty(battery))
						mTV.setText(getYearMonthDay()
								+ " "
								+ getWeeks()
								+ " "
								+ battery
								+ "\n"
								+ CommonUtil
										.getCustomsContent(getApplicationContext()));
					else
						mTV.setText(getYearMonthDay()
								+ " "
								+ getWeeks()
								+ "\n"
								+ CommonUtil
										.getCustomsContent(getApplicationContext()));
					
					if (CommonUtil.isShowView(getApplicationContext()))
					{
						mTV.setVisibility(View.VISIBLE);
					}
					else
					{
						mTV.setVisibility(View.GONE);
					}
				}
				
				if (!CommonUtil.isEnableGprs(getApplicationContext()))
				{
					try 
					{
						toggleGprs(false);
					} 
					catch (Exception e) 
					{
						e.printStackTrace();
					}
				}
				
				handler.postDelayed(mRunnable, 1000);
			}
		};

		handler = new Handler();
		mReceiver = new BroadcastReceiver()
		{

			@Override
			public void onReceive(Context context, Intent intent)
			{
				if (Intent.ACTION_SCREEN_ON.equals(intent.getAction()))
				{
					Logger.e(TAG, "--> handler.post(mRunnable)");
					handler.post(mRunnable);
					screen_status = Intent.ACTION_SCREEN_ON;
				}
				else if (Intent.ACTION_SCREEN_OFF.equals(intent.getAction()))
				{
					Logger.e(TAG, "--> handler.removeCallbacks(mRunnable)");
					handler.removeCallbacks(mRunnable);
					screen_status = Intent.ACTION_SCREEN_OFF;
				}

				if (Intent.ACTION_BATTERY_CHANGED.equals(intent.getAction())
						&& screen_status.equals(Intent.ACTION_SCREEN_ON))
				{
					int level = intent.getIntExtra("level", 0);
					int scale = intent.getIntExtra("scale", 100);
					battery = level * 100 / scale + "%";

					if (mTV == null)
						createView();
					else
						mTV.setText(getYearMonthDay()
								+ " "
								+ getWeeks()
								+ " "
								+ battery
								+ "\n"
								+ CommonUtil.getCustomsContent(getApplicationContext()));
				}
			}
		};

		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
		intentFilter.addAction(Intent.ACTION_SCREEN_ON);
		intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
		registerReceiver(mReceiver, intentFilter);

		if (screen_status.equals(Intent.ACTION_SCREEN_ON))
			handler.post(mRunnable);

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		Log.e(TAG, "onStartCommand(SHOW_DATE)");
		return START_STICKY;
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
		Logger.e(TAG, "--> onDestroy()");

		wm.removeView(layoutView);

		try
		{
			unregisterReceiver(mReceiver);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		if (handler != null && mRunnable != null)
			handler.removeCallbacks(mRunnable);

		startService(new Intent(this, CoreService.class));
	}

	private void createView()
	{
		Logger.e(TAG, "createView()");
		wm = (WindowManager) getApplicationContext().getSystemService("window");
		layoutView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.activity_main, null);
		mTV = (TextView) layoutView.findViewById(R.id.tv);
		if (CommonUtil.isShowView(getApplicationContext()))
		{
			mTV.setVisibility(View.VISIBLE);
		}
		else
		{
			mTV.setVisibility(View.GONE);
		}
		if (!TextUtils.isEmpty(battery))
			mTV.setText(getYearMonthDay() + " " + getWeeks() + " " + battery
					+ "\n"
					+ CommonUtil.getCustomsContent(getApplicationContext()));
		else
			mTV.setText(getYearMonthDay() + " " + getWeeks() + "\n"
					+ CommonUtil.getCustomsContent(getApplicationContext()));

		wmParams = new WindowManager.LayoutParams();
		wmParams.type = LayoutParams.TYPE_PHONE;
		wmParams.format = PixelFormat.RGBA_8888;
		wmParams.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL | LayoutParams.FLAG_NOT_TOUCHABLE
				| LayoutParams.FLAG_NOT_FOCUSABLE;
		wmParams.gravity = Gravity.CENTER_HORIZONTAL | Gravity.TOP;
		wmParams.x = 0;
		wmParams.y = 0;
		wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
		wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
		wm.addView(layoutView, wmParams);
	}

	@SuppressLint("SimpleDateFormat")
	private String getYearMonthDay()
	{
		SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
		Date d1 = new Date(System.currentTimeMillis());
		String chineseDate = "";
		try
		{
			chineseDate = Lunar.formatDate();
		}
		catch (ParseException e)
		{
			e.printStackTrace();
		}
		return format.format(d1) + " " + chineseDate;
	}

	private String getWeeks()
	{
		Calendar calendar = Calendar.getInstance();
		int day = calendar.get(Calendar.DAY_OF_WEEK);
		String today = "";
		if (day == 2)
			today = "周一";// MON
		else if (day == 3)
			today = "周二";// TUE
		else if (day == 4)
			today = "周三";// WED
		else if (day == 5)
			today = "周四";// THU
		else if (day == 6)
			today = "周五";// FRI
		else if (day == 7)
			today = "周六";// STA
		else if (day == 1)
			today = "周日";// SUN
		return today;
	}

	public void toggleGprs(boolean isEnable) throws Exception
	{    
		ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		Class<?> cmClass = connManager.getClass();    
		Class<?>[] argClasses = new Class[1];    
		argClasses[0] = boolean.class;    
		Method method = cmClass.getMethod("setMobileDataEnabled", argClasses);    
		method.invoke(connManager, isEnable);   
	} 
}
