package com.googlecode.activemq.eclipse.content;

public class NodeConnection {

	private Node source;
	private Node destination;
	private String connectionString;

	public NodeConnection(Node source, String connectionString, Node destination) {
		this.source = source;
		this.connectionString = connectionString;
		this.destination = destination;
	}

	public Node getSource() {
		return source;
	}

	public void setSource(Node source) {
		this.source = source;
	}

	public Node getDestination() {
		return destination;
	}

	public void setDestination(Node destination) {
		this.destination = destination;
	}

	public String getConnectionString() {
		return connectionString;
	}

	public void setConnectionString(String connectionString) {
		this.connectionString = connectionString;
	}

	public String toString() {
		return "";
	}

}
