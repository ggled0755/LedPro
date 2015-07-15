<?php
// 本类由系统自动生成，仅供测试用途
class ShortMessage
{
	/**
	 * 短信接口 SmartSms
	 * @param unknown $phoneNum
	 * @param unknown $content
	 */
	static function SendMessage($phoneNum,$content) {
		$url = "http://182.92.185.251:8890/mtPort/mt?";
		$url = $url."&phonelist=".$phoneNum;			//短信接口信息
		$url = $url."&content=".$content;
		$url = $url."&pwd="."4112c1bf771e7d140f429eeaf3186823";
		$url = $url."&uid="."249";
		$options = array(
				'http' => array(
						'method' =>'GET',
						'timeout' =>15 * 60 // 超时时间（单位:s）
				)
		);
		$context = stream_context_create($options);
		$result = file_get_contents($url, false, $context);
		return $result;
	}
	/**
	 * 生成固定位数的随机数字
	 * @param unknown $length
	 * @return string
	 */
	static function getRandNum($length) {
		$str = '0123456789';
		$randNum = '';
		$len = strlen($str)-1;
		for($i = 0;$i < $length;$i ++){
			$num = mt_rand(0, $len);
			$randNum .= $str[$num];
		}
		return $randNum ;
	}
	 
}