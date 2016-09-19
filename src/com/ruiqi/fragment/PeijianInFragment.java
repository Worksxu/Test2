package com.ruiqi.fragment;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import android.app.ProgressDialog;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.ruiqi.Table.OrderTable;
import com.ruiqi.Table.TabAdapterNoTitle;
import com.ruiqi.Table.TabRow;
import com.ruiqi.bean.OutIn;
import com.ruiqi.bean.PeiJian;
import com.ruiqi.bean.PeiJianTypeMoney;
import com.ruiqi.bean.TableInfo;
import com.ruiqi.db.Pj;
import com.ruiqi.fragment.OutInForFragment.MyAdapter;
import com.ruiqi.fragment.OutInForFragment.MyAdapter.ViewHolder;
import com.ruiqi.utils.CurrencyUtils;
import com.ruiqi.utils.HttpUtil;
import com.ruiqi.utils.RequestUrl;
import com.ruiqi.utils.SPUtils;
import com.ruiqi.utils.SPutilsKey;
import com.ruiqi.works.ApplyActivity;
import com.ruiqi.works.R;

/**
 * 配件入库Fragment
 * @author Administrator
 *
 */
public class PeijianInFragment extends BaseFragment{
	
	private List<TableInfo> mDatas;//子项的表格类数据
	public List<OutIn> list; //listview的数据
	
	public ProgressDialog pd;
	public MyAdapter mAdapter; //listview的适配器
	
	public ListView lv_all_content;
	
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			String result = (String) msg.obj;
			paraseData(result);
		}
	};

	public void initDataListView() {
		
		list = new ArrayList<OutIn>();
		pd.show();
		String shipper_id = (String) SPUtils.get(getContext(), SPutilsKey.SHIP_ID, "error");
		String shop_id = (String) SPUtils.get(getContext(), SPutilsKey.SHOP_ID, "error");
		RequestParams params = new RequestParams(RequestUrl.SHIPPERPRODUCT);
		params.addBodyParameter("shipper_id", shipper_id);
		params.addBodyParameter("shop_id", shop_id);
		HttpUtil.PostHttp(params, handler, pd);
	}
	private void paraseData(String result) {
		System.out.println("配件入库记录result="+result);
		try {
			JSONObject obj = new JSONObject(result);
			int resultCode = obj.getInt("resultCode");
			if(resultCode==1){
				JSONArray array = obj.getJSONArray("resultInfo");
				mDatas = new ArrayList<TableInfo>();
				for(int i=0;i<array.length();i++){
					JSONObject obj1 = array.getJSONObject(i);
					String product_no = obj1.getString("product_no");
					String good_name = obj1.getString("good_name");
					String good_num = obj1.getString("good_num");
					mDatas.add(new TableInfo(product_no,good_name, good_num));
					System.out.println("list="+list);
				}
				list.add(new OutIn("", "", mDatas));
				//list.add(new OutIn(product_no, "", mDatas));
				mAdapter = new MyAdapter();
				lv_all_content.setAdapter(mAdapter); 
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		
	}
	@Override
	public View initView() {
		View view = LayoutInflater.from(getContext()).inflate(R.layout.out_in_four_fragment, null);
		pd = new ProgressDialog(getContext());
		pd.setMessage("正在加载");
		lv_all_content = (ListView) view.findViewById(R.id.lv_all_content);
		initDataListView();
		return view;
	}

	/**
	 * 自定义适配器
	 * @author Administrator
	 *
	 */
	class MyAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder = null;
			if(convertView==null){
				viewHolder = new ViewHolder();
				convertView=LayoutInflater.from(getContext()).inflate(R.layout.lv_all_content_item, null);
				//初始化组件
				viewHolder.lv_type = (ListView) convertView.findViewById(R.id.lv_type);
				viewHolder.tv_num = (TextView) convertView.findViewById(R.id.tv_num);
				viewHolder.tv_sum = (TextView) convertView.findViewById(R.id.tv_sum);
				viewHolder.tv_sum.setVisibility(View.GONE);
			
				//设置标签
				convertView.setTag(viewHolder);
			}else{
				//取出标签
				viewHolder = (ViewHolder) convertView.getTag();
			}
			OutIn allOrder = list.get(position);
			//给子项表格listview设置数据
				viewHolder.tv_num.setVisibility(View.GONE);
				viewHolder.tv_num.setText(allOrder.getOrderNum());
				viewHolder.tv_sum.setText(allOrder.getTime());
				
			List<TableInfo> mData = allOrder.getmTableInfo();//得到瓶的类型集合
			
			List<TabRow> table = new OrderTable().addDataNoTitle( mData,3);
			TabAdapterNoTitle adapter = null;
				adapter = new TabAdapterNoTitle(getContext(), table,viewHolder.tv_num,null);
			
			viewHolder.lv_type.setAdapter(adapter);
			CurrencyUtils.setListViewHeightBasedOnChildren(viewHolder.lv_type);
			
			
			return convertView;
		}
		class ViewHolder{
			ListView lv_type;
			TextView tv_num;
			TextView tv_sum;
			}
		}
}