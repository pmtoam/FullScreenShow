package net.pmtoam.showdate;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity
{

	private Context context = this;

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
