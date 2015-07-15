<?php
import("MISP.Util.FuegoLog");
import("MISP.Constant.MispErrorCode");
import("MISP.Util.FuegoException");
class MispMapService
{
	/**
	 * @param lat 纬度 lon 经度 raidus 单位米
	 * return minLat,minLng,maxLat,maxLng
	 */
	static public function getAround($loc_ns,$loc_we,$raidus){
		
		$condition = array();
		
		$PI = 3.14159265;
		$degree = (24901*1609)/360.0;
		$dpmLat = 1/$degree;
		$raidusLat = $dpmLat*$raidus;
		
		$condition['min_loc_ns'] = $loc_ns - $raidusLat;
		$condition['max_loc_ns'] = $loc_ns + $raidusLat;
		
		$mpdLng = $degree*cos($loc_ns*($PI/180));
		$dpmLng = 1/$mpdLng;
		$raidusLng = $dpmLng*$raidus;
		
		$condition['max_loc_we'] = $loc_we - $raidusLng;
		$condition['min_loc_we'] = $loc_we + $raidusLng;
		
		return $condition;
	}
	/**
	 * @desc 根据两点间的经纬度计算距离
	 * @param float $lat 纬度值
	 * @param float $lng 经度值
	 */
	static function getDistance($lat1, $lng1, $lat2, $lng2)
	{
		$earthRadius = 6367000; //approximate radius of earth in meters
	
		/*
		 Convert these degrees to radians
		 to work with the formula
		 */
	
		$lat1 = ($lat1 * pi() ) / 180;
		$lng1 = ($lng1 * pi() ) / 180;
	
		$lat2 = ($lat2 * pi() ) / 180;
		$lng2 = ($lng2 * pi() ) / 180;
	
		/*
		 Using the
		 Haversine formula
	
		 http://en.wikipedia.org/wiki/Haversine_formula
	
		 calculate the distance
		 */
	
		$calcLongitude = $lng2 - $lng1;
		$calcLatitude = $lat2 - $lat1;
		$stepOne = pow(sin($calcLatitude / 2), 2) + cos($lat1) * cos($lat2) * pow(sin($calcLongitude / 2), 2);
		$stepTwo = 2 * asin(min(1, sqrt($stepOne)));
		$calculatedDistance = $earthRadius * $stepTwo;
	
		return round($calculatedDistance);
	}
	
}

?>