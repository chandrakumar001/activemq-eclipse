package com.googlecode.activemq.eclipse.content;

import org.apache.activemq.command.ActiveMQDestination;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.views.properties.IPropertySource;

import com.googlecode.activemq.eclipse.utils.ImageShop;

public class DestinationNode implements Node, IAdaptable {

	private String id;

	private String tooltip;

	private String label;

	private Image image;

	private final ActiveMQDestination destination;

	public DestinationNode(ActiveMQDestination destination) {
		this.destination = destination;
		this.id = destination.getQualifiedName();
		this.tooltip = "Queue " + destination.getPhysicalName();
		this.label = destination.toString();
		image = ImageShop.get("queue.gif");
	}

	public String toString() {
		return this.label;
	}

	public Object getAdapter(Class type) {
		if (type == IPropertySource.class) {
			return new DestinationPropertySource(this);
		}
		System.out.println("Attempted to convert node: " + this + " to type: " + type);
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

	public ActiveMQDestination getDestination() {
		return destination;
	}

}
