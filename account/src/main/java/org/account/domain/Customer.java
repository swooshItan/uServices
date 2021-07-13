package org.account.domain;

public class Customer {

	private String id;
	
	private String name;


	public String getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}

    public String toString() {
        return new StringBuilder().append("Customer[id=").append(id).append(", name=").append(name).append("]").toString();
    }
}
