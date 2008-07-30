package com.googlecode.activemq.eclipse.content;

import org.apache.activemq.command.ConsumerInfo;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.views.properties.IPropertySource;

import com.googlecode.activemq.eclipse.utils.ImageShop;

public class ConsumerNode implements Node, IAdaptable {

	private String id;

	private String tooltip;

	private String label;

	private Image image;

	private final ConsumerInfo consumerInfo;

	public ConsumerNode(ConsumerInfo consumerInfo) {
		this.consumerInfo = consumerInfo;
		this.id = consumerInfo.getConsumerId().toString();
		String selector = consumerInfo.getSelector();
		this.tooltip = "Consumer" + (selector != null ? " selector: " + selector : "");
		this.label = id; // TODO better label?
		image = ImageShop.get("consumer.gif");
	}

	public String toString() {
		return this.label;
	}

	public Object getAdapter(Class type) {
		if (type == IPropertySource.class) {
			return new ConsumerInfoPropertySource(consumerInfo);
		}
		System.out.println("Attempted to convert node: " + this + " to type: " + type);
		return null;
	}

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

	public ConsumerInfo getConsumerInfo() {
		return consumerInfo;
	}

}
