package cn.fuego.misp.constant;

import java.util.ArrayList;
import java.util.List;

public class MispCommonDataSource
{
	public static List<String> getSexDataSource()
	{
		return SexTypeEnum.getAllStr();
	}
	
	public static List<String> getPayDataSource()
	{
		return PayOptionEnum.getAllStr();
	}
	public static List<String> getTypeDataSouce()
	{
		List<String> typeList = new ArrayList<String>();
		
		
		return typeList;
		
	}
	

}
