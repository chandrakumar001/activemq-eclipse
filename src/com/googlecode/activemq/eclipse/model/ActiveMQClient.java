package com.googlecode.activemq.eclipse.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.advisory.ConsumerEvent;
import org.apache.activemq.advisory.ConsumerEventSource;
import org.apache.activemq.advisory.ConsumerListener;
import org.apache.activemq.advisory.ConsumerStartedEvent;
import org.apache.activemq.advisory.DestinationEvent;
import org.apache.activemq.advisory.DestinationListener;
import org.apache.activemq.advisory.DestinationSource;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;
import org.apache.activemq.command.ConsumerId;
import org.apache.activemq.command.ConsumerInfo;
import org.apache.activemq.util.ServiceStopper;
import org.apache.activemq.util.ServiceSupport;

public class ActiveMQClient extends ServiceSupport implements DestinationListener, ConsumerListener {
	private ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
	private ActiveMQConnection connection;
	private DestinationSource destinationSource;
	private ConsumerEventSource queueConsumerSource;
	private ConsumerEventSource topicConsumerSource;
	private Map<Destination, Map<ConsumerId, ConsumerInfo>> consumers = new ConcurrentHashMap<Destination, Map<ConsumerId, ConsumerInfo>>();
	private List<Runnable> modelChangeListeners = new ArrayList<Runnable>();


	@Override
	protected void doStart() throws Exception {
		connection = (ActiveMQConnection) connectionFactory.createConnection();

		destinationSource = connection.getDestinationSource();
		destinationSource.setDestinationListener(this);
		destinationSource.start();

		queueConsumerSource = new ConsumerEventSource(connection, new ActiveMQQueue(">"));
		queueConsumerSource.setConsumerListener(this);
		queueConsumerSource.start();

		topicConsumerSource = new ConsumerEventSource(connection, new ActiveMQTopic(">"));
		topicConsumerSource.setConsumerListener(this);
		topicConsumerSource.start();

		connection.start();
	}

	@Override
	protected void doStop(ServiceStopper stopper) throws Exception {
		if (connection != null) {
			connection.close();
			connection = null;
		}
	}

	public void addModelChangeListener(Runnable listener) {
		modelChangeListeners.add(listener);
	}

	public void removeModelChangeListener(Runnable listener) {
		modelChangeListeners.remove(listener);
	}

	/**
	 * Returns the consumers for the given destination
	 */
	public Collection<ConsumerInfo> getConsumers(Destination destination) {
		Map<ConsumerId, ConsumerInfo> consumerMap = getDestinationConsumerMap(destination);
		return consumerMap.values();
	}

	public DestinationSource getDestinationSource() {
		return destinationSource;
	}

	public Connection getConnection() {
		return connection;
	}

	public void setConnection(ActiveMQConnection connection) {
		this.connection = connection;
	}

	public ActiveMQConnectionFactory getConnectionFactory() {
		return connectionFactory;
	}

	public void setConnectionFactory(ActiveMQConnectionFactory connectionFactory) {
		this.connectionFactory = connectionFactory;
	}

	public void onDestinationEvent(DestinationEvent destinationEvent) {
		System.out.println("Destination event: " + destinationEvent.getDestination());
		fireModelChangedListeners();
	}

	public synchronized void onConsumerEvent(ConsumerEvent consumerEvent) {
		System.out.println("Consumer event: " + consumerEvent);

		// TODO Auto-generated method stub
		Destination destination = consumerEvent.getDestination();
		Map<ConsumerId, ConsumerInfo> consumerMap = getDestinationConsumerMap(destination);
		if (consumerEvent instanceof ConsumerStartedEvent) {
			ConsumerStartedEvent startedEvent = (ConsumerStartedEvent) consumerEvent;
			ConsumerInfo consumerInfo = startedEvent.getConsumerInfo();
			System.out.println("New consumer: " + consumerInfo);
			consumerMap.put(startedEvent.getConsumerId(), consumerInfo);
		} else {
			consumerMap.remove(consumerEvent.getConsumerId());
		}
		fireModelChangedListeners();
	}

	protected synchronized Map<ConsumerId, ConsumerInfo> getDestinationConsumerMap(Destination destination) {
		Map<ConsumerId, ConsumerInfo> consumerMap = consumers.get(destination);
		if (consumerMap == null) {
			consumerMap = new ConcurrentHashMap<ConsumerId, ConsumerInfo>();
			consumers.put(destination, consumerMap);
		}
		return consumerMap;
	}

	protected synchronized void fireModelChangedListeners() {
		for (Runnable runner : modelChangeListeners) {
			runner.run();
		}
	}

	public String getName() {
		String userName = connectionFactory.getUserName();
		return connectionFactory.getBrokerURL() + (userName != null && userName.length() > 0 ? " user: " + userName : "");
	}

}
