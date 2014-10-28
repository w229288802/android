package welge.safe;

import welge.safe.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.SimpleAdapter;

public class HomeActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.home);
		GridView gv_list = (GridView) findViewById(R.id.gv_list);
		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
		gv_list.setAdapter(arrayAdapter);
	}

}
