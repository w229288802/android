package welge.ui;

import java.lang.reflect.InvocationTargetException;

import welge.safe.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SettingItemLayout extends RelativeLayout{

	private CheckBox cb_status;
	private TextView tv_title;
	private TextView tv_desc;

	public SettingItemLayout(Context context) {
		super(context);
		this.initView(context);
		System.out.println("dadasdsd");
	}

	public SettingItemLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.initView(context);
	}

	public SettingItemLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.initView(context);
		final String attributeValue = attrs.getAttributeValue("http://schemas.android.com/apk/res/welge.safe", "dds");
		cb_status.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				try {
					SettingItemLayout.this.getClass().getMethod(attributeValue).invoke(SettingItemLayout.this);
				} catch (NoSuchMethodException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}
	private void initView(Context context){
		View.inflate(context, R.layout.setting_item_view, this);
		cb_status = (CheckBox) findViewById(R.id.cb_status);
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_desc = (TextView) findViewById(R.id.tv_desc);
		boolean update = context.getApplicationContext().getSharedPreferences("config", Context.MODE_PRIVATE).getBoolean("update", false);
		cb_status.setChecked(update);
	}
	
	public void setDesc(CharSequence text){
		tv_desc.setText(text);
	}
	public void setTitle(CharSequence text){
		tv_title.setText(text);
	}
	/**
	 * 校验组合控件是否选中
	 */
	
	public boolean isChecked(){
		return cb_status.isChecked();
	}
	
	/**
	 * 设置组合控件的状态
	 */
	
	public void setChecked(boolean checked){
		cb_status.setChecked(checked);
	}
}
	
