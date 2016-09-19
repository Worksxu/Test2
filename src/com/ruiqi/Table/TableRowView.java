package com.ruiqi.Table;

import com.ruiqi.utils.DensityUtils;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


/**
 * 表格布局
 * @author Administrator
 *
 */
public class TableRowView extends LinearLayout{
	public TableRowView(Context context, TabRow tableRow,int odd,int height) {     
        super(context);     
             
        this.setOrientation(LinearLayout.HORIZONTAL);     
        for (int i = 0; i < tableRow.getSize(); i++) {//逐个格单元添加到行    
        	
            TableCell tableCell = tableRow.getCellValue(i); 
            
         // 获得屏幕宽度
         	WindowManager wm = (WindowManager) getContext().getSystemService(
         			Context.WINDOW_SERVICE);
         	
         	int width = wm.getDefaultDisplay().getWidth();
            
         // 按照格单元指定的大小设置空间 //50dp  // 宽度为屏幕size
         	LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(

         	//tableCell.width, tableCell.height);
         			
         	(width - DensityUtils.dp2px(getContext(), 20)) / tableRow.getSize(),height);
            
            layoutParams.setMargins(1, 1, 1, 1);//预留空隙制造边框      
            
            if (tableCell.type == TableCell.STRING) {//如果格单元是文本内容      
            	
                TextView textCell = new TextView(context);  
                
                textCell.setLines(2); 
                textCell.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
                
                textCell.setGravity(Gravity.CENTER); 
                layoutParams.gravity = LinearLayout.VERTICAL;
                textCell.setText(String.valueOf(tableCell.value)); 
                if(odd==0){//第一行
                	textCell.setBackgroundColor(Color.parseColor("#cccccc"));
                }else{
                	textCell.setBackgroundColor(Color.WHITE);//背景   白色  
                }
                
                  
                
                
                addView(textCell, layoutParams);     
            } else if (tableCell.type == TableCell.IMAGE) {//如果格单元是图像内容      
                ImageView imgCell = new ImageView(context);  
                
                imgCell.setBackgroundColor(Color.WHITE);//背景灰色   
                
                imgCell.setImageResource((Integer) tableCell.value); 
                
                addView(imgCell, layoutParams);     
            }     
        }     
        this.setBackgroundColor(Color.parseColor("#dddddd"));//背景白色，利用空隙来实现边框      
    }     
}
