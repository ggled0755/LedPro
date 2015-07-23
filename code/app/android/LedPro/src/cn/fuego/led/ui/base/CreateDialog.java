/**   
* @Title: CreateDialog.java 
* @Package cn.fuego.led.ui.base 
* @Description: TODO
* @author Aether
* @date 2015-7-22 上午11:28:35 
* @version V1.0   
*/ 
package cn.fuego.led.ui.base;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import cn.fuego.common.util.validate.ValidatorUtil;
import cn.fuego.led.R;

/** 
 * @ClassName: CreateDialog 
 * @Description: TODO
 * @author Aether
 * @date 2015-7-22 上午11:28:35 
 *  
 */
public class CreateDialog extends Dialog
{
    
    private String txt_title;
    private String txt_hint;
    private OnConfirmListener confirmListener;
    EditText etName;
    private Context mContext;
	 //定义回调事件，用于dialog的点击事件
    public interface OnConfirmListener
    {
            public void confirmCallback(String name);
    }


    public CreateDialog(Context context,String title,String hint,OnConfirmListener confirmListener)
    {
            super(context);
            this.mContext=context;
            this.txt_title = title;
            this.txt_hint = hint;
            this.confirmListener = confirmListener;
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) 
    { 
            super.onCreate(savedInstanceState);
            setContentView(R.layout.dialog_create);
           
            Window dialogWindow = CreateDialog.this.getWindow();  
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();  
            dialogWindow.setGravity(Gravity.CENTER);  
            lp.width = LayoutParams.FILL_PARENT;  
            dialogWindow.setAttributes(lp); 
            //设置标题
            TextView title = (TextView) findViewById(R.id.dialog_title);
            title.setText(txt_title);
            etName = (EditText)findViewById(R.id.dialog_content);
            etName.setHint(txt_hint);
            
            Button btn_cancel = (Button) findViewById(R.id.dialog_cancel_btn);
            btn_cancel.setOnClickListener(clickListener);
            
            Button btn_confirm = (Button) findViewById(R.id.dialog_confirm_btn);
            btn_confirm.setOnClickListener(clickListener);
    }
    
    private View.OnClickListener clickListener = new View.OnClickListener() 
    {
            
            @Override
            public void onClick(View v) 
            {
            	if(v.getId()==R.id.dialog_cancel_btn)
            	{
            		CreateDialog.this.dismiss();
            	}
            	if(v.getId()==R.id.dialog_confirm_btn)
            	{
                	if(ValidatorUtil.isEmpty(etName.getText().toString().trim()))
                	{
                		Toast.makeText(mContext,mContext.getResources().getString(R.string.msg_input_null), Toast.LENGTH_SHORT).show();
                		return;
                	}
            		confirmListener.confirmCallback(etName.getText().toString().trim());
                    CreateDialog.this.dismiss();
            	}

            }
    };

}
