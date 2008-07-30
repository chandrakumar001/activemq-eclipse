package com.googlecode.activemq.eclipse.content;

import org.apache.activemq.command.ActiveMQDestination;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

public class DestinationPropertySource implements IPropertySource {

	private static final String PROPERTIES = "activemq.destination.properties";
	private static final String NAME = "cactivemq.destination.name";

	private IPropertyDescriptor[] propertyDescriptors = { new TextPropertyDescriptor(NAME, "Pattern Name"), new PropertyDescriptor(PROPERTIES, "Properties"), };

	private final ActiveMQDestination destination;

	public DestinationPropertySource(DestinationNode node) {
		this(node.getDestination());
	}

	public DestinationPropertySource(ActiveMQDestination destination) {
		this.destination = destination;
	}

	/**
	 * @see org.eclipse.ui.views.properties.IPropertySource#getPropertyDescriptors()
	 */
	public IPropertyDescriptor[] getPropertyDescriptors() {
		return propertyDescriptors;
	}

	public Object getEditableValue() {
		return destination;
	}

	public Object getPropertyValue(Object name) {
		if (name.equals(NAME)) {
			return destination.getPhysicalName();
		} else if (name.equals(PROPERTIES)) {
			return destination.getProperties();
		}
		return null;
	}

	public boolean isPropertySet(Object name) {
		return false;
	}

	public void resetPropertyValue(Object name) {
	}

	public void setPropertyValue(Object name, Object value) {
		// TODO
	}
}
