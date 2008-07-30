package com.googlecode.activemq.eclipse.content;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.swt.graphics.Image;

import com.googlecode.activemq.eclipse.model.ActiveMQModel;
import com.googlecode.activemq.eclipse.utils.ImageShop;

public class BrokerNode implements Node, IAdaptable {

	private String id;

	private String tooltip;

	private String label;

	private Image image;

	private final ActiveMQModel model;

	public BrokerNode(ActiveMQModel model) {
		this.model = model;
		this.id = "broker";
		this.tooltip = "Apache ActiveMQ Broker";
		this.label = "Broker";
		image = ImageShop.get("broker.gif");
	}

	public String toString() {
		return this.label;
	}

	public Object getAdapter(Class type) {
		/*
		 * if (type == IPropertySource.class) { return new
		 * DestinationPropertySource(this); }
		 */System.out.println("Attempted to convert node: " + this + " to type: " + type);
		return null;
	}

	// Properties

	public String getId() {
		return id;
	}

	public String getTooltip() {
		return tooltip;
	}

	public String getLabel() {
		return label;
	}

	public Image getImage() {
		return image;
	}

}
