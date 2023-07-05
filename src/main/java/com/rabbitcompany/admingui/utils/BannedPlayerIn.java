package com.rabbitcompany.admingui.utils;

import java.util.Date;

public class BannedPlayerIn {

	public String uuid_from;
	public String username_from;
	public String uuid_to;
	public String username_to;
	public String reason;
	public Date until;
	public String server;
	public Date created;

	public BannedPlayerIn(String uuid_from, String username_from, String uuid_to, String username_to, String reason, Date until, String server, Date created) {
		this.uuid_from = uuid_from;
		this.username_from = username_from;
		this.uuid_to = uuid_to;
		this.username_to = username_to;
		this.reason = reason;
		this.until = until;
		this.server = server;
		this.created = created;
	}

}
