package com.fixtures.test;

public class PrimaryKey {

	private Long key;

	public PrimaryKey(Long key) {
		this.key = key;
	}

	public Long getKey() {
		return key;
	}

	@Override
	public String toString() {
		return key.toString();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof PrimaryKey){
			PrimaryKey pr = (PrimaryKey)obj;
			return pr.key.equals(this.key);
		}
		return false;
	}
}
