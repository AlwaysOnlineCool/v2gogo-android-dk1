package com.v2gogo.project.utils.qiniu;

import java.util.UUID;

public class CreateUUid
{

	private CreateUUid()
	{
	}

	public static String creatUUID()
	{
		return UUID.randomUUID().toString().replace("-", "");
	}
}
