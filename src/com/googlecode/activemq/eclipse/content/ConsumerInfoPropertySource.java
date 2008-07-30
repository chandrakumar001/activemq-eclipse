package com.googlecode.activemq.eclipse.content;

import org.apache.activemq.command.ConsumerInfo;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

public class ConsumerInfoPropertySource implements IPropertySource {

	public static final String ID = "activemq.consumer.id";
	public static final String SELECTOR = "activemq.consumer.selector";
	public static final String CURRENT_PREFETCH_SIZE = "activemq.consumer.currentPrefetchSize";
	public static final String DESTINATION = "activemq.consumer.destination";
	public static final String SUBSCRIPTION_NAME = "activemq.consumer.subscriptionName";
	public static final String MAXIMUM_PENDING_MESSAGE_LIMIT = "activemq.consumer.maximumPendingMessageLimit";
	public static final String PREFETCH_SIZE = "activemq.consumer.prefetchSize";
	public static final String PRIORITY = "activemq.consumer.priority";

	private IPropertyDescriptor[] propertyDescriptors = { new TextPropertyDescriptor(ID, "ID"), new TextPropertyDescriptor(SELECTOR, "Selector"),
			new PropertyDescriptor(CURRENT_PREFETCH_SIZE, "Current Prefetch Size"), new PropertyDescriptor(DESTINATION, "Destination"),
			new PropertyDescriptor(SUBSCRIPTION_NAME, "Subscription Name"),
			new PropertyDescriptor(MAXIMUM_PENDING_MESSAGE_LIMIT, "Maximum Pending Message Limit"), new PropertyDescriptor(PREFETCH_SIZE, "Prefetch Size"),
			new PropertyDescriptor(PRIORITY, "Priority"), };

	private final ConsumerInfo consumerInfo;

	public ConsumerInfoPropertySource(ConsumerInfo exchange) {
		this.consumerInfo = exchange;
	}

	/**
	 * @see org.eclipse.ui.views.properties.IPropertySource#getPropertyDescriptors()
	 */
	public IPropertyDescriptor[] getPropertyDescriptors() {
		return propertyDescriptors;
	}

	public Object getEditableValue() {
		return consumerInfo;
	}

	public Object getPropertyValue(Object name) {
		if (name.equals(ID)) {
			return consumerInfo.getConsumerId();
		} else if (name.equals(SELECTOR)) {
			return consumerInfo.getSelector();
		} else if (name.equals(CURRENT_PREFETCH_SIZE)) {
			return consumerInfo.getCurrentPrefetchSize();
		} else if (name.equals(DESTINATION)) {
			return consumerInfo.getDestination();
		} else if (name.equals(SUBSCRIPTION_NAME)) {
			return consumerInfo.getSubscriptionName();
		} else if (name.equals(MAXIMUM_PENDING_MESSAGE_LIMIT)) {
			return consumerInfo.getMaximumPendingMessageLimit();
		} else if (name.equals(PREFETCH_SIZE)) {
			return consumerInfo.getPrefetchSize();
		} else if (name.equals(PRIORITY)) {
			return consumerInfo.getPriority();
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
