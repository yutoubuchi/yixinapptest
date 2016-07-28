package com.netease.hzqa.olive.newAppInterface.utils;

import java.util.List;

public class Http {
	private String connection;
	private List parameters;
	private List headers;
	private List entity;

	public Http(String connection, List parameters, List headers, List entity) {
		this.connection = connection;
		this.parameters = parameters;
		this.headers = headers;
		this.entity = entity;
	}

	public String getConnection() {
		return connection;
	}

	public void setConnection(String connection) {
		this.connection = connection;
	}

	public List getParameters() {
		return parameters;
	}

	public void setParameters(List parameters) {
		this.parameters = parameters;
	}

	public List getHeaders() {
		return headers;
	}

	public void setHeaders(List headers) {
		this.headers = headers;
	}

	public List getEntity() {
		return entity;
	}

	public void setEntity(List entity) {
		this.entity = entity;
	}
}
