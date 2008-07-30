package com.googlecode.activemq.eclipse.content;

import java.util.Collection;
import java.util.Set;

import org.apache.activemq.advisory.DestinationSource;
import org.apache.activemq.command.ActiveMQDestination;
import org.apache.activemq.command.ConsumerInfo;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.zest.core.viewers.IGraphContentProvider;

import com.googlecode.activemq.eclipse.model.ActiveMQModel;

/**
 * Can handle either a {@link List<RouteType>} or a {@link RouteSource} as the
 * input which is then converted into the content for the viewer
 */
public class NodeContentProvider implements IGraphContentProvider, Runnable {

	protected NodeStore nodeStore = new NodeStore();
	private ActiveMQModel model;

	public NodeContentProvider() {
	}

	public void run() {
		if (model != null) {
			clear();
			try {
				buildRoute(model);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void setModel(ActiveMQModel model) {
		if (this.model != null) {
			this.model.removeModelChangeListener(this);
		}
		this.model = model;
		if (model != null) {
			model.addModelChangeListener(this);
			run();
		}
	}

	@SuppressWarnings("unchecked")
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		if (newInput instanceof ActiveMQModel) {
			setModel((ActiveMQModel) newInput);
		}
	}

	public Object getSource(Object rel) {
		NodeConnection compareConnection = (NodeConnection) rel;
		return nodeStore.getSource(compareConnection);
	}

	public Object[] getElements(Object input) {
		return nodeStore.getConnectionsAsArray();
	}

	public Object getDestination(Object rel) {
		NodeConnection compareConnection = (NodeConnection) rel;
		return nodeStore.getDestination(compareConnection);
	}

	public NodeConnection connect(NodeStore routesTempStore, Node source, Node target) {
		// ensure:connectionString is unique in NodeStore.
		String connectionString = source.getId() + "->" + target.getId();

		NodeConnection routeConnection = new NodeConnection(source, connectionString, target);
		routesTempStore.getConnections().add(routeConnection);

		return routeConnection;
	}

	public double getWeight(Object connection) {
		return 0;
	}

	public void dispose() {
	}

	protected void buildRoute(ActiveMQModel model) {

		BrokerNode brokerNode = nodeStore.getBrokerNode(model);

		DestinationSource destinationSource = model.getDestinationSource();
		System.out.println("Queues: " + destinationSource.getQueues());
		System.out.println("Topics: " + destinationSource.getTopics());
		buildDestinations(model, brokerNode, destinationSource.getQueues());
		buildDestinations(model, brokerNode, destinationSource.getTopics());
		buildDestinations(model, brokerNode, destinationSource.getTemporaryQueues());
		buildDestinations(model, brokerNode, destinationSource.getTemporaryTopics());
	}

	protected void buildDestinations(ActiveMQModel model, BrokerNode brokerNode, Set<? extends ActiveMQDestination> destinations) {
		for (ActiveMQDestination queue : destinations) {
			DestinationNode destinationNode = nodeStore.getDestinationNode(queue);

			connect(nodeStore, brokerNode, destinationNode);

			Collection<ConsumerInfo> consumers = model.getConsumers(queue);
			for (ConsumerInfo consumerInfo : consumers) {
				ConsumerNode consumerNode = nodeStore.getConsumerNode(consumerInfo);
				connect(nodeStore, consumerNode, destinationNode);
			}

			// TODO add producers!
		}
	}

	public void clear() {
		nodeStore.clear();
	}

}