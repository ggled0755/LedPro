/**   
* @Title: ModifyDialog.java 
* @Package cn.fuego.led.ui.base 
* @Description: TODO
* @author Aether
* @date 2015-7-29 下午5:17:32 
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
import android.widget.Toast;
import cn.fuego.common.util.validate.ValidatorUtil;
import cn.fuego.led.R;

/** 
 * @ClassName: ModifyDialog 
 * @Description: TODO
 * @author Aether
 * @date 2015-7-29 下午5:17:32 
 *  
 */
public class ModifyDialog extends Dialog
{
	private String txt_title;
	private String txt_content;
	private OnModifyConfirmListener confirmListener;
	private EditText etContent;
	private EditText etName;
	private Context mContext;
	 //定义回调事件，用于dialog的点击事件
    public interface OnModifyConfirmListener
    {
            public void confirmCallback(String name,String note);
    }


    public ModifyDialog(Context context,String title,String content)
    {
            super(context);
            this.mContext=context;
            this.txt_title = title;
            this.txt_content = content;
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) 
    { 
            super.onCreate(savedInstanceState);
            setContentView(R.layout.dialog_modify);
           
            Window dialogWindow = this.getWindow();  
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();  
            dialogWindow.setGravity(Gravity.CENTER);  
            lp.width = LayoutParams.FILL_PARENT;  
            dialogWindow.setAttributes(lp); 
            //设置标题
            etName = (EditText) findViewById(R.id.dialog_title);
            etName.setText(txt_title);
            
            etContent = (EditText)findViewById(R.id.dialog_content);
            etContent.setText(txt_content);
            
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
            	switch (v.getId())
				{
				case R.id.dialog_cancel_btn:
					dismiss();
					break;
				case R.id.dialog_confirm_btn:
					String name = etName.getText().toString().trim();
					String content = etContent.getText().toString().trim();
					if(ValidatorUtil.isEmpty(name))
                	{
                		Toast.makeText(mContext,mContext.getResources().getString(R.string.msg_input_null), Toast.LENGTH_SHORT).show();
                		return;
                	}
            		confirmListener.confirmCallback(name,content);
					dismiss();
					break;			
				default:
					break;
				}

            }
    };

	public void setConfirmListener(OnModifyConfirmListener confirmListener)
	{
		this.confirmListener = confirmListener;
	}


}
