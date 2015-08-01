package cn.fuego.led.ui.project;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import cn.fuego.led.R;
import cn.fuego.led.cache.SubfolderCache;
import cn.fuego.led.util.treeview.Node;
import cn.fuego.led.util.treeview.TreeListViewAdapter;

public class SimpleTreeAdapter<T> extends TreeListViewAdapter<T>
{

	public SimpleTreeAdapter(ListView mTree, Context context, List<T> datas,
			int defaultExpandLevel) throws IllegalArgumentException,
			IllegalAccessException
	{
		super(mTree, context, datas, defaultExpandLevel);
	}

	@SuppressWarnings("unchecked")
	@Override
	public View getConvertView(Node node , int position, View convertView, ViewGroup parent)
	{
		
		ViewHolder viewHolder = null;
		if (convertView == null)
		{
			convertView = mInflater.inflate(R.layout.tree_list_item, parent, false);
			viewHolder = new ViewHolder();
			viewHolder.node_icon = (ImageView) convertView.findViewById(R.id.treenode_node);
			viewHolder.folder_icon = (ImageView) convertView.findViewById(R.id.treenode_folder);
			viewHolder.label = (TextView) convertView.findViewById(R.id.treenode_label);
			
			viewHolder.num=(TextView) convertView.findViewById(R.id.treenode_num);
			
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

		//viewHolder.folder_icon.setImageResource((R.drawable.tree_icon_folder));
		if(node.isSelected())
		{
			viewHolder.folder_icon.setImageResource(R.drawable.icon_folder_full);
		}
		else
		{
			viewHolder.folder_icon.setImageResource(R.drawable.icon_folder_empty);
		}
		if(!node.isLeaf())
		{
			int folderNum=node.getChildren().size();
			
			int sum=SubfolderCache.getInstance().getAllNum(node.getId());
		}
		viewHolder.label.setText(node.getName());
		
		return convertView;
	}

	private final class ViewHolder
	{
		ImageView node_icon;
		ImageView folder_icon;
		TextView label;
		TextView num;
	}

}
