package welge.safe;

import welge.ui.SettingItemLayout;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class SettingActivity extends Activity{
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.setting);
		final SharedPreferences sp = getSharedPreferences("config",MODE_PRIVATE);
		final boolean update = sp.getBoolean("update", false);
		final SettingItemLayout m_update = (SettingItemLayout) findViewById(R.id.m_s1);
		if(update){
			m_update.setDesc("自动升级已开启");
		}else{
			m_update.setDesc("自动升级已关闭");
		}
		m_update.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				final boolean update = sp.getBoolean("update", false);
				Editor edit = sp.edit();
				if(update){
					edit.putBoolean("update", false);
					m_update.setChecked(false);
					m_update.setDesc("自动升级已开启");
				}
				else{
					edit.putBoolean("update", true);
					m_update.setChecked(true);
					m_update.setDesc("自动升级已关闭");
				}
				edit.commit();
			}
		});
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		System.out.println("SettingActivity关闭了");
	}
}
