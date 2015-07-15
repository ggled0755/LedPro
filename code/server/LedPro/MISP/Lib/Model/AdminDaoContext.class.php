<?php
class AdminDaoContext
{
	const ADMINDB = 'mysql://root:root@localhost:3306/ledpro';
	const PREFIX = 'lp_';
	static function Customer()
	{
		return M('customer',AdminDaoContext::PREFIX,AdminDaoContext::ADMINDB);
	}
	static function SubFolder()
	{
		return M('subfolder',AdminDaoContext::PREFIX,AdminDaoContext::ADMINDB);
	}
	static function SubFolderDetail()
	{
		return M('subfolder_detail',AdminDaoContext::PREFIX,AdminDaoContext::ADMINDB);
	}
	static function Product()
	{
		return M('product',AdminDaoContext::PREFIX,AdminDaoContext::ADMINDB);
	}
	static function Project()
	{
		return M('project',AdminDaoContext::PREFIX,AdminDaoContext::ADMINDB);
	}
}
?>