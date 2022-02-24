package com.service.model;

public class RedisMessageModel {

	private String key;

	private String validatorContents;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValidatorContents() {
		return validatorContents;
	}

	public void setValidatorContents(String validatorContents) {
		this.validatorContents = validatorContents;
	}

	public RedisMessageModel() {
		super();
	}

	public RedisMessageModel(String key, String validatorContents) {
		super();
		this.key = key;
		this.validatorContents = validatorContents;
	}

	@Override
	public String toString() {
		return "RedisMessageModel [key=" + key + ", validatorContents=" + validatorContents + "]";
	}

}
