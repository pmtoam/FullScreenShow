package net.pmtoam.showdate;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class CoreReceiver extends BroadcastReceiver
{

	private final String TAG = "CoreReceiver";

	@Override
	public void onReceive(Context context, Intent intent)
	{
		Logger.e(TAG, "Action = " + intent.getAction());
		context.startService(new Intent(context, CoreService.class));
	}

}
