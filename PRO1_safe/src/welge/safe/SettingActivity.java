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
			m_update.setDesc("�Զ������ѿ���");
		}else{
			m_update.setDesc("�Զ������ѹر�");
		}
		m_update.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				final boolean update = sp.getBoolean("update", false);
				Editor edit = sp.edit();
				if(update){
					edit.putBoolean("update", false);
					m_update.setChecked(false);
					m_update.setDesc("�Զ������ѿ���");
				}
				else{
					edit.putBoolean("update", true);
					m_update.setChecked(true);
					m_update.setDesc("�Զ������ѹر�");
				}
				edit.commit();
			}
		});
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		System.out.println("SettingActivity�ر���");
	}
}
