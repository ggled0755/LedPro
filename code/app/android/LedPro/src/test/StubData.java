/**   
* @Title: Stub.java 
* @Package test 
* @Description: TODO
* @author Aether
* @date 2015-6-10 下午4:44:53 
* @version V1.0   
*/ 
package test;

import java.util.ArrayList;
import java.util.List;

import cn.fuego.led.webservice.up.model.base.ProductJson;
import cn.fuego.led.webservice.up.model.base.ProjectJson;
import cn.fuego.led.webservice.up.model.base.ProjectSubfolderJson;

/** 
 * @ClassName: Stub 
 * @Description: TODO
 * @author Aether
 * @date 2015-6-10 下午4:44:53 
 *  
 */
public class StubData
{

	public static List<ProductJson> getProductList()
	{
		List<ProductJson> proList = new ArrayList<ProductJson>();
		
		ProductJson p1 = new ProductJson();
		p1.setProduct_id(1);
		p1.setProdcut_catg("tc11");
		p1.setManufacture("IKEA FACTORY");
		p1.setProduct_name("LED METAL pendant");
		p1.setProduct_desp("LED light in colored stones with orchids");
		p1.setCertification("product factory cert");
		p1.setWarranty("3 months");
		p1.setProduct_score((float) 4.0);
		proList.add(p1);
		
		ProductJson p2 = new ProductJson();
		p2.setProduct_id(2);
		p2.setProdcut_catg("LEDD22");
		p2.setManufacture("IKEA FACTORY");
		p2.setProduct_name("LED METAL pendant");
		p2.setProduct_desp("LED light in colored stones with orchids");
		p2.setCertification("product factory cert");
		p2.setWarranty("6 months");
		p2.setProduct_score((float) 4.5);
		proList.add(p2);
		
		ProductJson p3 = new ProductJson();
		p3.setProduct_id(1);
		p3.setProdcut_catg("ii90");
		p3.setManufacture("IKEA FACTORY");
		p3.setProduct_name("LED METAL pendant");
		p3.setProduct_desp("LED light in colored stones with orchids");
		p3.setCertification("product factory cert");
		p3.setWarranty("1 year");
		p3.setProduct_score((float) 5.0);
		proList.add(p3);
		
		return proList;
	}
	
	public static List<ProjectJson> getProjectList()
	{
		List<ProjectJson> proList = new ArrayList<ProjectJson>();
		ProjectJson p1= new ProjectJson();
		p1.setProject_id(1);
		p1.setProject_name("Project 1");
		p1.setProject_note("Free notes");
		p1.setTotal_catg(5);
		p1.setTotal_num(14);
		p1.setTotal_watt(1000);
		p1.setTotal_cost(200);
		proList.add(p1);
		
		ProjectJson p2= new ProjectJson();
		p2.setProject_id(2);
		p2.setProject_name("Project 2");
		p2.setProject_note("Free notes");
		p2.setTotal_catg(5);
		p2.setTotal_num(14);
		p2.setTotal_watt(1000);
		p2.setTotal_cost(200);
		proList.add(p2);
		
		return proList;
		
	}

	public static List<ProjectSubfolderJson> getSubfolderList()
	{
		List<ProjectSubfolderJson> fList = new ArrayList<ProjectSubfolderJson>();
		ProjectSubfolderJson f1 = new ProjectSubfolderJson();
		f1.setSubfolder_id(1);
		f1.setSubfolder_parent_id(0);
		f1.setSubfolder_name("subfolder1");
		f1.setProject_id(1);
		fList.add(f1);

		ProjectSubfolderJson f2 = new ProjectSubfolderJson();
		f2.setSubfolder_id(2);
		f2.setSubfolder_parent_id(1);
		f2.setSubfolder_name("subfolder1.1");
		f2.setProject_id(1);
		fList.add(f2);
		
		ProjectSubfolderJson f3 = new ProjectSubfolderJson();
		f3.setSubfolder_id(3);
		f3.setSubfolder_parent_id(0);
		f3.setSubfolder_name("subfolder2");
		f3.setProject_id(1);
		fList.add(f3);
		
		return fList;
	}
	
	
	
	
	
}
