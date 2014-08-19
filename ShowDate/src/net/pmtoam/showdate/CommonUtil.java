package net.pmtoam.showdate;

import android.content.Context;
import android.content.SharedPreferences;

public class CommonUtil
{
	public static final String TAG = "CommonUtil";
	public static final String PREFS_CACHE_ONE = "prefs_cache_one";

	// ---------------------------------------------------------------------------
	public static void setDownloading(boolean isDownloading, Context context)
	{
		SharedPreferences sp = context.getSharedPreferences(PREFS_CACHE_ONE, 0);
		SharedPreferences.Editor editor = sp.edit();
		editor.putBoolean("isDownloading", isDownloading);
		editor.commit();
	}

	public static boolean isDownloading(Context context)
	{
		SharedPreferences sp = context.getSharedPreferences(PREFS_CACHE_ONE, 0);
		return sp.getBoolean("isDownloading", false);
	}

	// ---------------------------------------------------------------------------
	public static void setWindowY(float y, Context context)
	{
		SharedPreferences sp = context.getSharedPreferences(PREFS_CACHE_ONE, 0);
		SharedPreferences.Editor editor = sp.edit();
		editor.putFloat("y", y);
		editor.commit();
	}

	public static float getWindowY(Context context)
	{
		SharedPreferences sp = context.getSharedPreferences(PREFS_CACHE_ONE, 0);
		return sp.getFloat("y", 460);
	}

	// ---------------------------------------------------------------------------
	public static void setCustomsContent(Context context, String value)
	{
		SharedPreferences sp = context.getSharedPreferences(
				CommonUtil.PREFS_CACHE_ONE, 0);
		SharedPreferences.Editor editor = sp.edit();
		editor.putString("CustomsContent", value);
		editor.commit();
	}

	public static String getCustomsContent(Context context)
	{
		SharedPreferences sp = context.getSharedPreferences(
				CommonUtil.PREFS_CACHE_ONE, 0);
		return sp.getString("CustomsContent", "");
	}

	// ---------------------------------------------------------------------------
	public static void setGetDooiooAllTime(long time, Context context)
	{
		SharedPreferences sp = context.getSharedPreferences(PREFS_CACHE_ONE, 0);
		SharedPreferences.Editor editor = sp.edit();
		editor.putLong("getDooiooAllTime", time);
		editor.commit();
	}

	public static long getGetDooiooAllTime(Context context)
	{
		SharedPreferences sp = context.getSharedPreferences(PREFS_CACHE_ONE, 0);
		return sp.getLong("getDooiooAllTime", 0);
	}
}
