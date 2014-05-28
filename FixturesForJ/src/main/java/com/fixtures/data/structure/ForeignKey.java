package com.fixtures.data.structure;

public abstract class ForeignKey {

	private String value;
	
	public ForeignKey(String value) {
		this.value = value;
	}

	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof ForeignKey){
			ForeignKey fr = (ForeignKey)obj;
			return fr.value.equals(value);
		}
		return false;
	}
	
	@Override
	public String toString() {
		return value;
	}
}
