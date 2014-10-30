package welge.safe;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import welge.safe.utils.StreamTools;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

public class LauncherActivity extends Activity {
	private static final int MSG_JSON_ERROR = 5;
	private static final int MSG_NET_ERROR = 1;
	private static final int MSG_PROTOCOL_ERROR = 2;
	private static final int MSG_URL_ERROR = 3;
	protected static final int MSG_UPDATE_APP = 4;
	private String filePath = null;
	private String description;
	private String downloadUrl;
	private String versionName;
	@SuppressLint("HandlerLeak")
	Handler handler =  new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
				case MSG_UPDATE_APP:
					openUpdateDialog();
					break;
				case MSG_JSON_ERROR:
					Log.e(getLocalClassName(),"JSON��ʽ���� ");
					break;
				case MSG_NET_ERROR:
					Log.e(getLocalClassName(), "�������");
					break;
				case MSG_PROTOCOL_ERROR:
					Log.e(getLocalClassName(),"Э�����");
					break;
				case MSG_URL_ERROR:
					Log.e(getLocalClassName(),"URL����");
					break;
				default:
					break;
			}
		}
		
	};
	private TextView tv_process;
	private TextView tv_version;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		//����window������ʾ
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.launcher);
		//�õ���ǰӦ�õİ汾
		versionName = getVersionName();
		//��ȡ����������
		SharedPreferences config = getSharedPreferences("config", MODE_PRIVATE);
		boolean update = config.getBoolean("update", false);
		if(update){
			//������
			checkUpdate("https://raw.githubusercontent.com/w229288802/android/master/PRO1_safe/src/info/update.html");
			
		}
		
		tv_version = (TextView) findViewById(R.id.tv_version);
		tv_process = (TextView) findViewById(R.id.tv_process);
		
		tv_version.setText("�汾��:"+versionName);
		
		//����������
		AlphaAnimation  animation = new AlphaAnimation(0.1f, 1.0f);
		RelativeLayout rl_launcher = (RelativeLayout) findViewById(R.id.rl_launcher);
		animation.setDuration(1000);
		rl_launcher.startAnimation(animation);
		if(!update){
			try {
				Thread.sleep(1);
				enterHome();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * �򿪸��¶Ի���
	 */
	private void openUpdateDialog(){
		AlertDialog.Builder updateDialog = new AlertDialog.Builder(this);
		updateDialog.setMessage(description);
		updateDialog.setCancelable(false);
		updateDialog.setPositiveButton("���ظ���", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				HttpUtils httpUtils = new HttpUtils();
				String target = Environment.getExternalStorageDirectory().getPath()+"/1.apk";
				filePath = target ;
				Toast.makeText(LauncherActivity.this, downloadUrl, 1).show();
				httpUtils.download(downloadUrl,target, new RequestCallBack<File>() {
					/**
					 * ��װAPK
					 * @param t
					 */
					private void installAPK(File t) {
					  Intent intent = new Intent();
					  intent.setAction("android.intent.action.VIEW");
					  intent.addCategory("android.intent.category.DEFAULT");
					  intent.setDataAndType(Uri.fromFile(t), "application/vnd.android.package-archive");
					  startActivity(intent);
					  
					}
					@Override
					public void onSuccess(ResponseInfo<File> arg0) {
						installAPK(new File(filePath));
					}
					
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						arg0.printStackTrace();
						Toast.makeText(LauncherActivity.this, arg1, Toast.LENGTH_LONG).show();
					}

					@Override
					public void onLoading(long total, long current,
							boolean isUploading) {
						super.onLoading(total, current, isUploading);
						tv_process.setText("����:"+current*100/total+"%");
					}
				});
			}
		});
		updateDialog.setNegativeButton("�´���˵", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				enterHome();
			}
		});
		updateDialog.show();
	}
	private void enterHome(){
		Intent intent = new Intent(LauncherActivity.this,HomeActivity.class);
		startActivity(intent);
		finish();
	}
	/**
	 * ������
	 */
	private void checkUpdate(final String netAddress){
		final Message msg = Message.obtain();
		new Thread(new Runnable() {
			
			long start = 0;
			@Override
			public void run() {
				try {
					long startTime = System.currentTimeMillis();
					URL url = new URL(netAddress);
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					conn.setConnectTimeout(10000);
					conn.setRequestMethod("GET");
					System.out.println("���糢������");
					start = System.currentTimeMillis();
					int responseCode = conn.getResponseCode();
					
					if(responseCode/2==100){
						System.out.println("�������ӳɹ�");
						//�õ�������
						InputStream inputStream = conn.getInputStream();
						//�õ���Ӧ����
						String json = StreamTools.readFromStream(inputStream);
						//����JSON
						JSONObject jsonObject = new JSONObject(json);
						//�汾����
						String version = (String) jsonObject.get("version");
						//������Ϣ
						description = (String) jsonObject.get("description");
						//���ص�ַ
						downloadUrl = (String) jsonObject.get("downloadUrl");
						System.out.println("���°汾��Ϊ��"+version);
						long endTime = System.currentTimeMillis();
						long totalTime = endTime -startTime;
						if(!version.equals(versionName)){
							msg.what = MSG_UPDATE_APP;
						}else{
							if(totalTime <1000){
								Thread.sleep(1000-totalTime);
							}
							enterHome();
						}
						versionName = version;
						
					}
				} catch (MalformedURLException e) {
					msg.what = MSG_URL_ERROR;
					e.printStackTrace();
				} catch (ProtocolException e) {
					msg.what = MSG_PROTOCOL_ERROR;
					e.printStackTrace();
				} catch (IOException e) {
					msg.what = MSG_NET_ERROR;
					System.out.println(System.currentTimeMillis()-start);
					e.printStackTrace();
				} catch (JSONException e) {
					msg.what = MSG_JSON_ERROR;
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				handler.sendMessage(msg);
			}
		}).start();
			
	}
	/**
	 * ��ð汾��
	 * 
	 * @return 
	 */
	private String getVersionName(){
		PackageInfo packageInfo = null;
		try {
			packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return packageInfo.versionName;
	}
}
