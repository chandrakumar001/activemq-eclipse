package com.googlecode.activemq.eclipse.content;

import java.io.IOException;

import org.apache.activemq.command.ActiveMQMessage;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

public class MessagePropertySource implements IPropertySource {

	private static final String ID = "activemq.message.id";
	private static final String BODY = "activemq.message.body";
	private static final String PROPERTIES = "activemq.message.properties";

	private IPropertyDescriptor[] propertyDescriptors = { new TextPropertyDescriptor(ID, "ID"), new PropertyDescriptor(BODY, "Body"),
			new PropertyDescriptor(PROPERTIES, "Properties"), };

	private final ActiveMQMessage message;

	public MessagePropertySource(ActiveMQMessage message) {
		this.message = message;
	}

	/**
	 * @see org.eclipse.ui.views.properties.IPropertySource#getPropertyDescriptors()
	 */
	public IPropertyDescriptor[] getPropertyDescriptors() {
		return propertyDescriptors;
	}

	public Object getEditableValue() {
		return message;
	}

	public Object getPropertyValue(Object name) {
		if (name.equals(ID)) {
			return message.getMessageId();
		} else if (name.equals(BODY)) {
			return message.getContent();
		} else if (name.equals(PROPERTIES)) {
			try {
				return message.getProperties();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
