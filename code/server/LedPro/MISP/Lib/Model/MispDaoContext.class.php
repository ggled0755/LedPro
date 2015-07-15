<?php

class MispDaoContext
{
	const MISPDB = 'mysql://root:root@localhost:3306/ledpro';
	const PREFIX = 'misp_';
	static function SystemUser()
	{
		return M('user',MispDaoContext::PREFIX,MispDaoContext::MISPDB);
	}
	static function Menu()
	{
		return M('menu',MispDaoContext::PREFIX,MispDaoContext::MISPDB);
	}
	static function Company()
	{
		return M('company',MispDaoContext::PREFIX,MispDaoContext::MISPDB);
	}
	static function ClientVersion()
	{
		return M('client_version',MispDaoContext::PREFIX,MispDaoContext::MISPDB);
	}
	static function Privilege()
	{
		return M('privilege',MispDaoContext::PREFIX,MispDaoContext::MISPDB);
	}
	static function SystemRole()
	{
		return M('role',MispDaoContext::PREFIX,MispDaoContext::MISPDB);
	}
	static function Token()
	{
		return M('token',MispDaoContext::PREFIX,MispDaoContext::MISPDB);
	}
	static function Enum()
	{
		return M('enum',MispDaoContext::PREFIX,MispDaoContext::MISPDB);
	}
}

?>