package com.excellence.skinloader.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.excellence.skinloader.R;

import org.qcode.qskinloader.sample.BaseActivity;

public class SkinByAPKFileActivity extends BaseActivity implements View.OnClickListener
{
	private Button mButton = null;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_apkskin);

		mButton = (Button) findViewById(R.id.button);
		mButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v)
	{
		Toast.makeText(this, "尚未示例，敬请期待！", Toast.LENGTH_SHORT).show();
	}
}
