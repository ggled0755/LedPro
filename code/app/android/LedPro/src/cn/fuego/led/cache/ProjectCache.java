/**   
* @Title: ProjectCache.java 
* @Package cn.fuego.led.cache 
* @Description: TODO
* @author Aether
* @date 2015-7-29 下午6:25:58 
* @version V1.0   
*/ 
package cn.fuego.led.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.fuego.common.util.validate.ValidatorUtil;
import cn.fuego.led.webservice.up.model.base.ProjectJson;

/** 
 * @ClassName: ProjectCache 
 * @Description: TODO
 * @author Aether
 * @date 2015-7-29 下午6:25:58 
 *  
 */
public class ProjectCache
{
	private static ProjectCache instance;
	private boolean isChanged =false;
	
	public synchronized static ProjectCache getInstance()
	{

		if(null == instance)
		{
			instance = new ProjectCache();
		}
		return instance;
		
	}
	private List<ProjectJson> projectList = new ArrayList<ProjectJson>();
	private Map<Integer,ProjectJson> projectMap = new HashMap<Integer, ProjectJson>();

	public void clear()
	{
		if(!ValidatorUtil.isEmpty(projectList))
		{
			projectList.clear();
			projectMap.clear();
		}

	}

	//更新产品信息
	public void updatePro(ProjectJson project)
	{
		if(null!=projectMap.get(project.getProject_id()))
		{
			projectMap.put(project.getProject_id(), project);
		}
		
	} 
	public List<ProjectJson> getProjectList()
	{
		projectList.clear();
		if(projectMap.size()>0)
		{
			for(Integer e:projectMap.keySet())
			{
				projectList.add(projectMap.get(e));
			}
		}

		return projectList;
	}
	public void setProjectList(List<ProjectJson> projectList)
	{
		clear();
		this.projectList = projectList;
		if(!ValidatorUtil.isEmpty(projectList))
		{
			for(int i=0;i<projectList.size();i++)
			{
				projectMap.put(projectList.get(i).getProject_id(), projectList.get(i));
			}
		}
		
	}
	public Map<Integer, ProjectJson> getProjectMap()
	{
		return projectMap;
	}
	public void setProjectMap(Map<Integer, ProjectJson> projectMap)
	{
		this.projectMap = projectMap;
	}
	public boolean isChanged()
	{
		return isChanged;
	}
	public void setChanged(boolean isChanged)
	{
		this.isChanged = isChanged;
	}

			
			
			
}
