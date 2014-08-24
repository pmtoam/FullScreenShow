package net.pmtoam.showdate;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ToggleButton;

public class MainActivity extends Activity
{

	private Context context = this;
	private static final String TAG = "MainActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main2);

		// startService(new Intent(this, MainService.class));
		startService(new Intent(this, CoreService.class));

		final EditText et = (EditText) findViewById(R.id.et);
		et.setText(CommonUtil.getCustomsContent(context));
		Button btn_save = (Button) findViewById(R.id.btn_save);
		btn_save.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				CommonUtil.setCustomsContent(context, et.getText().toString()
						.trim());
			}
		});
		
		ToggleButton toggleButton_gprs = (ToggleButton) findViewById(R.id.toggleButton_gprs);
		toggleButton_gprs.setChecked(CommonUtil.isEnableGprs(context)); 
		toggleButton_gprs.setOnCheckedChangeListener(new OnCheckedChangeListener() 
		{
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) 
			{
				Logger.e(TAG, "toggleButton_gprs onCheckedChanged arg1 = " + arg1);
				CommonUtil.setEnableGprs(arg1, context);
			}
		});
		
		ToggleButton toggleButton_show = (ToggleButton) findViewById(R.id.toggleButton_show);
		toggleButton_show.setChecked(CommonUtil.isShowView(context)); 
		toggleButton_show.setOnCheckedChangeListener(new OnCheckedChangeListener() 
		{
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) 
			{
				Logger.e(TAG, "toggleButton_show onCheckedChanged arg1 = " + arg1);
				CommonUtil.setShowView(arg1, context);
			}
		});
		
		try
		{
			Intent service = new Intent();
			service.setClassName("com.dooioo.corestartservice", "com.dooioo.corestartservice.CoreService");
			startService(service);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

}
