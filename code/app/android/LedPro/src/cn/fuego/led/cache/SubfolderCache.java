/**   
* @Title: SubfolderCache.java 
* @Package cn.fuego.led.cache 
* @Description: TODO
* @author Aether
* @date 2015-7-23 上午10:10:30 
* @version V1.0   
*/ 
package cn.fuego.led.cache;

import java.util.ArrayList;
import java.util.List;

import cn.fuego.common.util.validate.ValidatorUtil;
import cn.fuego.led.util.treeview.Node;
import cn.fuego.led.webservice.up.model.base.SubfolderJson;

/** 
 * @ClassName: SubfolderCache 
 * @Description: TODO
 * @author Aether
 * @date 2015-7-23 上午10:10:30 
 *  
 */
public class SubfolderCache
{
	private static SubfolderCache instance;
	private List<Integer> selIDList=new ArrayList<Integer>();
	private List<Node> allNode = new ArrayList<Node>();
	private Integer targetID;
	private List<SubfolderJson> subfolderList = new ArrayList<SubfolderJson>();
	private boolean isChange=false;//标记是否修改信息
	
	public synchronized static SubfolderCache getInstance()
	{

		if(null == instance)
		{
			instance = new SubfolderCache();
		}
		return instance;
		
	}
	public List<Integer> getSelIDList()
	{
		selIDList.clear();
		if(!ValidatorUtil.isEmpty(allNode))
		{
			selIDList.addAll(generateALLChild(targetID,allNode));

		}
		selIDList.add(targetID);
		return selIDList;
	}
	private List<Integer> generateALLChild(Integer pId,List<Node> data)
	{
		List<Integer> result = new ArrayList<Integer>();
		for(Node node:data)
		{
			if(node.getPId()==pId)
			{
				result.add(node.getId());
				if(!node.isLeaf())
				{
					result.addAll(generateALLChild(node.getId(),node.getChildren()));
				}
			}
		}
		return result;
	}
	public void setSelIDList(List<Integer> selIDList)
	{
		this.selIDList = selIDList;
	}
	public List<Node> getAllNode()
	{
		return allNode;
	}
	public void setAllNode(List<Node> allNode)
	{
		this.allNode = allNode;
	}
	public Integer getTargetID()
	{
		return targetID;
	}
	public void setTargetID(Integer targetID)
	{
		this.targetID = targetID;
	}
	public List<SubfolderJson> getSubfolderList()
	{
		return subfolderList;
	}
	public void setSubfolderList(List<SubfolderJson> subfolderList)
	{
		this.subfolderList = subfolderList;
	}
	
	public SubfolderJson getSelSd(int id)
	{
		for(SubfolderJson sd:subfolderList)
		{
			if(sd.getSubfolder_id()==id)
			{
				return sd;
			}
		}
		return new SubfolderJson();
	}
	public boolean isChange()
	{
		return isChange;
	}
	public void setChange(boolean isChange)
	{
		this.isChange = isChange;
	}
	public void updateSf(SubfolderJson detail)
	{
		for(int i=0;i<this.subfolderList.size();i++)
		{
			if(subfolderList.get(i).getSubfolder_id()==detail.getSubfolder_id())
			{
				subfolderList.remove(i);
				subfolderList.add(detail);
				break;
			}
		}
		
	}
	
	
}
