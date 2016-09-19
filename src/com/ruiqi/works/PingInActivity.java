package com.ruiqi.works;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import com.ruiqi.bean.OutIn;
import com.ruiqi.bean.TableInfo;
import com.ruiqi.bean.Weight;
import com.ruiqi.fragment.NotInFragment;
import com.ruiqi.fragment.NotOutFragment;
import com.ruiqi.fragment.NullInFragment;
import com.ruiqi.fragment.NullOutFragment;
import com.ruiqi.fragment.WeightInFragment;
import com.ruiqi.fragment.WeightOutFragment;
import com.ruiqi.utils.HttpUtil;
import com.ruiqi.utils.RequestUrl;
import com.ruiqi.utils.SPUtils;
import com.ruiqi.utils.SPutilsKey;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;


/**
 * 钢瓶入库界面
 * @author Administrator
 *
 */
public class PingInActivity extends BaseActivity implements OnCheckedChangeListener{
	
	private RadioGroup rg_select;
	private ProgressDialog pd;
	
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			String result = (String) msg.obj;
			parseData(result);
		}

	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ping_in);
		setTitle("钢瓶入库记录");
		pd = new ProgressDialog(this);
		pd.setMessage("正在加载......");
		init();
		initData();
		
	}
	private List<TableInfo> list;
	private void initData() {
		list = new ArrayList<TableInfo>();
		pd.show();
		String shop_id = (String) SPUtils.get(PingInActivity.this, SPutilsKey.SHOP_ID, "error");
		String ship_id = (String) SPUtils.get(PingInActivity.this, SPutilsKey.SHIP_ID, "error");
		RequestParams params = new RequestParams(RequestUrl.IN_PING);
		params.addBodyParameter("shop_id", shop_id);
		params.addBodyParameter("shipper_id", ship_id);
		HttpUtil.PostHttp(params, handler, pd);
		
	}
	private WeightInFragment wif;
	private void parseData(String result) {
		System.out.println("钢瓶入库result="+result);
		try {
			JSONObject obj = new JSONObject(result);
			int resultCode = obj.getInt("resultCode");
			if(resultCode==1){
				
				JSONObject jsob=obj.getJSONObject("resultInfo");
				JSONArray array = jsob.getJSONArray("bottle");
				for(int i=0;i<array.length();i++){
					JSONObject object = array.getJSONObject(i);
					String documentsn = object.getString("documentsn");
					String time = object.getString("ctime");
					
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm");
					Date date = new Date(Long.parseLong(time)*1000L);
					String dateString = format.format(date);
					
					String goods_num = object.getString("goods_num");
					String goods_name = object.getString("goods_name");
					String typename = object.getString("typename");
					
					list.add(new TableInfo(typename,goods_name, goods_num,dateString));
				}
				wif = new WeightInFragment();
				Bundle bundle = new Bundle();
				bundle.putSerializable("mData", (Serializable)list);
				wif.setArguments(bundle);
				getSupportFragmentManager().beginTransaction().replace(R.id.rl_out, wif).commit();
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	private void init() {
		rg_select = (RadioGroup) findViewById(R.id.rg_select);
		rg_select.setOnCheckedChangeListener(this);
		
	}

	@Override
	public Activity getActivity() {
		return this;
	}

	@Override
	public Handler[] initHandler() {
		return null;
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.rb_weight:
			getSupportFragmentManager().beginTransaction().replace(R.id.rl_out, wif).commit();
			break;
		case R.id.rb_null:
			getSupportFragmentManager().beginTransaction().replace(R.id.rl_out, new NullInFragment()).commit();
			break;
		case R.id.rb_not:
			getSupportFragmentManager().beginTransaction().replace(R.id.rl_out, new NotInFragment()).commit();
	break;

		default:
			break;
		}
	}

}