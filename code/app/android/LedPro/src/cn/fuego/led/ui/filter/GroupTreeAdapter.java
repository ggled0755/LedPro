/**   
* @Title: GroupTreeAdapter.java 
* @Package cn.fuego.led.ui.filter 
* @Description: TODO
* @author Aether
* @date 2015-8-7 上午9:46:34 
* @version V1.0   
*/ 
package cn.fuego.led.ui.filter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import cn.fuego.led.R;
import cn.fuego.led.util.treeview.Node;
import cn.fuego.led.util.treeview.TreeListViewAdapter;

/** 
 * @ClassName: GroupTreeAdapter 
 * @Description: TODO
 * @author Aether
 * @date 2015-8-7 上午9:46:34 
 *  
 */
public class GroupTreeAdapter<T> extends TreeListViewAdapter<T>
{

	public GroupTreeAdapter(ListView mTree, Context context, List<T> datas,
			int defaultExpandLevel) throws IllegalArgumentException,
			IllegalAccessException
	{
		super(mTree, context, datas, defaultExpandLevel);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View getConvertView(Node node, int position, View convertView,
			ViewGroup parent)
	{
		ViewHolder viewHolder = null;
		if (convertView == null)
		{
			convertView = mInflater.inflate(R.layout.tree_list_item, parent, false);
			viewHolder = new ViewHolder();
			viewHolder.node_icon = (ImageView) convertView.findViewById(R.id.treenode_node);

			viewHolder.node_label = (TextView) convertView.findViewById(R.id.treenode_label);

			
			convertView.setTag(viewHolder);

		} 
		else
		{
			viewHolder = (ViewHolder) convertView.getTag();
		}

		if (node.getIcon() == -1)
		{
			viewHolder.node_icon.setVisibility(View.INVISIBLE);
		} 
		else
		{
			viewHolder.node_icon.setVisibility(View.VISIBLE);
			viewHolder.node_icon.setImageResource(node.getIcon());
		}

		viewHolder.node_label.setText(node.getName());
		
		return convertView;
	}

	static class ViewHolder
	{
		ImageView node_icon;
		CheckBox node_chk;
		TextView node_label;

	}
}
