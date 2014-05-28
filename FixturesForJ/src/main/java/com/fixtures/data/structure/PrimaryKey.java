package com.fixtures.data.structure;

public class PrimaryKey {

	private String key;

	public PrimaryKey(String key) {
		this.key = key;
	}

	public String getKey() {
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
