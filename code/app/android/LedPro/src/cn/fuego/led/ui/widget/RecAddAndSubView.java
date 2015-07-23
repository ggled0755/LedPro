package cn.fuego.led.ui.widget;


import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.fuego.led.R;



public class RecAddAndSubView extends LinearLayout 
{

	  private Context context = getContext();//上下文

	    private OnNumChangeListener onNumChangeListener;

	    private ImageView addButton;//加按钮

	    private ImageView subButton;//减按钮

	    private TextView editText;//数量显示

	    int num;          //数量值
	    /** 减 */
	    public static final int TYPE_SUBTRACT = 0;
	    /** 加 */
	    public static final int TYPE_ADD = 1;

	    public boolean isAutoChangeNum=true;//是否自动转变数量
	    /**
	     * 构造方法
	     */
	    public RecAddAndSubView(Context context){
	        super(context);
	        this.context = context;
	        num = 1;
	        control();
	    }

	    /**
	     *构造方法
	     * @param context
	     * @param
	     */
	    public RecAddAndSubView(Context context, int num){
	        super(context);
	        this.context = context;
	        this.num = num;
	        control();
	    }

	    public RecAddAndSubView(Context context, AttributeSet attrs){
	        super(context, attrs);
	        num = 1;
	        control();
	    }

	    /**
	     * 初始化
	     */
	    private void control(){
	        setPadding(1, 1, 1, 1);
	        initView();
	        setViewListener();
	    }


	    /**
	     * 初始化view
	     */
	    private void initView(){
	        View view= LayoutInflater.from(context).inflate(R.layout.misp_add_sub_view, null);
	        addButton =(ImageView)view.findViewById(R.id.add_btn_id);
	        subButton =(ImageView)view.findViewById(R.id.sub_btn_id);
	        editText =(TextView)view.findViewById(R.id.num_text_id);
	        setNum(0);
	        addView(view);
	    }

	    /**
	     *  设置中间的距离
	     * @param distance
	     */
	    public void setMiddleDistance(int distance){
	        editText.setPadding(distance,0,distance,0);
	    }
	    /**
	     *设置默认数量
	     * @param num
	     */
	    public void setNum(int num){
	        this.num = num;
	        if(num>0){
	            setAddBtnBackgroudResource(R.drawable.icon_plus);
	            setSubBtnBackgroudResource(R.drawable.icon_minus);
	            subButton.setVisibility(View.VISIBLE);
	            editText.setVisibility(View.VISIBLE);
	        }else{
/*	            setAddBtnBackgroudResource(R.drawable.goods_add_btn);
	            setSubBtnBackgroudResource(R.drawable.goods_sub_btn);
	            subButton.setVisibility(View.INVISIBLE);
	            editText.setVisibility(View.INVISIBLE);*/
	        	return;

	        }
	        editText.setText(String.valueOf(num));
	    }

	    /**
	     * 获取值
	     * @return
	     */
	    public int getNum(){
	        if (!TextUtils.isEmpty(editText.getText().toString().trim())){
	            return Integer.parseInt(editText.getText().toString());
	        }else {
	            return 0;
	        }
	    }




	    /**
	     *设置加号图片
	     * @param addBtnDrawable
	     */
	    public void setAddBtnBackgroudResource(int addBtnDrawable){
	        addButton.setBackgroundResource(addBtnDrawable);
	    }

	    /**设置减法图片
	     *  @param subBtnDrawable
	     */
	    public void setSubBtnBackgroudResource(int subBtnDrawable){
	        subButton.setBackgroundResource(subBtnDrawable);
	    }

	    /**
	     *  设置是否自动改变数量玩
	     * @param isAutoChangeNum
	     */
	    public void setAutoChangeNumber(boolean isAutoChangeNum){
	        this.isAutoChangeNum=isAutoChangeNum;
	    }
	    /**
	     * 设置加法减法的背景色
	     * @param addBtnColor
	     * @param subBtnColor
	     */
	    public void setButtonBgColor(int addBtnColor, int subBtnColor){
	        addButton.setBackgroundColor(addBtnColor);
	        subButton.setBackgroundColor(subBtnColor);
	    }

	    /**
	     * 设置监听回调
	     * @param onNumChangeListener
	     */
	    public void setOnNumChangeListener(OnNumChangeListener onNumChangeListener){
	        this.onNumChangeListener = onNumChangeListener;
	    }


	    /**
	     * 设置监听器
	     */
	    private void setViewListener()
	    {
	       if(addButton!=null)
	       {
	    	   addButton.setOnClickListener(new OnButtonClickListener());
	       }
	       if(subButton!=null)
	       {
	    	   subButton.setOnClickListener(new OnButtonClickListener());
	       }
	        
	    }


	    /**
	     * 监听器监听事件
	     */
	    private class OnButtonClickListener implements OnClickListener{

	        @Override
	        public void onClick(View v){
	            String numString = editText.getText().toString();
	            if (TextUtils.isEmpty(numString)){
	                num = 0;
	                editText.setText("0");
	            } else{
	                if (v.getId()==R.id.add_btn_id){
	                    if (++num < 0){
	                        //num--;//不知道做什么
/*	                        subButton.setVisibility(View.INVISIBLE);
	                        editText.setVisibility(View.INVISIBLE);*/
 							subButton.setEnabled(false);//如果小于1则禁用点击
	                        setAddBtnBackgroudResource(R.drawable.icon_plus);
	                    } else{
	                        setAddBtnBackgroudResource(R.drawable.icon_plus);
	                        subButton.setVisibility(View.VISIBLE);
							subButton.setEnabled(true);//如果大于0则启用
	                        editText.setVisibility(View.VISIBLE);
	                        if(isAutoChangeNum){
	                            setNum(num);
	                        }else{
	                            setNum( num-1);
	                        }
	                        if (onNumChangeListener != null){
	                            onNumChangeListener.onNumChange(RecAddAndSubView.this, TYPE_ADD, getNum());
	                        }
	                    }
	                } else if (v.getId()==R.id.sub_btn_id){
	                    if ( --num < 1) {
	                    	num++;
/*	                        subButton.setVisibility(View.INVISIBLE);
	                        editText.setVisibility(View.INVISIBLE);*/
							subButton.setEnabled(false);//如果小于1则禁用点击
	                        setAddBtnBackgroudResource(R.drawable.icon_plus);
	                        if (onNumChangeListener != null){
	                            onNumChangeListener.onNumChange(RecAddAndSubView.this, TYPE_SUBTRACT, getNum());
	                        }
	                    } else{
	                        setAddBtnBackgroudResource(R.drawable.icon_plus);
	                        subButton.setVisibility(View.VISIBLE);
	                        editText.setVisibility(View.VISIBLE);
	                        if(isAutoChangeNum){
	                            editText.setText(String.valueOf(num));
	                        }else{
	                            num++;
	                        }
	                        if (onNumChangeListener != null)
	                        {
	                            onNumChangeListener.onNumChange(RecAddAndSubView.this, TYPE_SUBTRACT, getNum());
	                        }
	                    }
	                }
	            }
	        }
	    }


	    public interface OnNumChangeListener
	    {
	        /**
	         * 监听方法
	         * @param view
	         * @param stype 点击按钮的类型
	         * @param num  返回的数值
	         */
	        public void onNumChange(View view, int stype, int num);
	    }
}
