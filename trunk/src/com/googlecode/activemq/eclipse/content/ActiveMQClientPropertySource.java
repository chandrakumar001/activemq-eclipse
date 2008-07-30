package com.googlecode.activemq.eclipse.content;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

import com.googlecode.activemq.eclipse.model.ActiveMQClient;

// TODO is there a bean generator for these things?
public class ActiveMQClientPropertySource implements IPropertySource {

	private static final String BROKER_URL = "activemq.client.brokerUrl";
	private static final String USER_NAME = "activemq.client.userName";
	private static final String PASSWORD = "activemq.client.password";

	private IPropertyDescriptor[] propertyDescriptors = { 
			new TextPropertyDescriptor(BROKER_URL, "Broker URL"), 
			new TextPropertyDescriptor(USER_NAME, "User name"), 
			new TextPropertyDescriptor(PASSWORD, "Password"), 
			};

	private final ActiveMQClient client;

	public ActiveMQClientPropertySource(ActiveMQClient client) {
		this.client = client;
	}

	/**
	 * @see org.eclipse.ui.views.properties.IPropertySource#getPropertyDescriptors()
	 */
	public IPropertyDescriptor[] getPropertyDescriptors() {
		return propertyDescriptors;
	}

	public Object getEditableValue() {
		return client;
	}

	public Object getPropertyValue(Object name) {
		ActiveMQConnectionFactory factory = client.getConnectionFactory();
		if (name.equals(BROKER_URL)) {
			return factory.getBrokerURL();
		} else if (name.equals(USER_NAME)) {
			return factory.getUserName();
		} else if (name.equals(PASSWORD)) {
			return factory.getPassword();
		}
		return null;
	}

	public boolean isPropertySet(Object name) {
		return false;
	}

	public void resetPropertyValue(Object name) {
	}

	public void setPropertyValue(Object name, Object value) {
		ActiveMQConnectionFactory factory = client.getConnectionFactory();
		if (name.equals(BROKER_URL)) {
			factory.setBrokerURL(toString(value));
		} else if (name.equals(USER_NAME)) {
			factory.setUserName(toString(value));
		} else if (name.equals(PASSWORD)) {
			factory.setPassword(toString(value));
		}
	}

	protected String toString(Object value) {
		if (value != null) {
			return value.toString();
		}
		return null;
	}
}
