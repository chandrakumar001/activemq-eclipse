package com.googlecode.activemq.eclipse.content;

import java.util.Collection;
import java.util.Set;

import org.apache.activemq.advisory.DestinationSource;
import org.apache.activemq.command.ActiveMQDestination;
import org.apache.activemq.command.ConsumerInfo;
import org.eclipse.core.runtime.ISafeRunnable;
import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.zest.core.viewers.IGraphContentProvider;

import com.googlecode.activemq.eclipse.model.ActiveMQClient;

/**
 * Can handle either a {@link List<RouteType>} or a {@link RouteSource} as the
 * input which is then converted into the content for the viewer
 */
public class NodeContentProvider implements IGraphContentProvider, Runnable {

	protected NodeStore nodeStore = new NodeStore();
	private ActiveMQClient model;
	private Viewer viewer;

	public NodeContentProvider() {
	}

	public void run() {
		System.out.println("Rebuild ActiveMQ model event for model: " + model);
		if (model != null) {
			rebuildModel();
			PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
				public void run() {
					// TODO avoid refresh if we've just done recently!
					// viewer.setInput(new Object());
					viewer.setInput(model);
					viewer.refresh();
				}
			});
		}
	}

	protected void rebuildModel() {
		clear();
		try {
			buildRoute(model);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setModel(ActiveMQClient model) {
		if (this.model != model) {
			if (this.model != null) {
				this.model.removeModelChangeListener(this);
			}
			this.model = model;
			if (model != null) {
				model.addModelChangeListener(this);
				rebuildModel();
			}
		}
	}

	@SuppressWarnings("unchecked")
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		this.viewer = viewer;
		if (newInput instanceof ActiveMQClient) {
			setModel((ActiveMQClient) newInput);
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

	protected void buildRoute(ActiveMQClient model) {

		BrokerNode brokerNode = nodeStore.getBrokerNode(model);

		DestinationSource destinationSource = model.getDestinationSource();
		System.out.println("Queues: " + destinationSource.getQueues());
		System.out.println("Topics: " + destinationSource.getTopics());
		buildDestinations(model, brokerNode, destinationSource.getQueues());
		buildDestinations(model, brokerNode, destinationSource.getTopics());
		buildDestinations(model, brokerNode, destinationSource.getTemporaryQueues());
		buildDestinations(model, brokerNode, destinationSource.getTemporaryTopics());
	}

	protected void buildDestinations(ActiveMQClient model, BrokerNode brokerNode, Set<? extends ActiveMQDestination> destinations) {
		for (ActiveMQDestination queue : destinations) {
			DestinationNode destinationNode = nodeStore.getDestinationNode(queue);

			connect(nodeStore, brokerNode, destinationNode);

			Collection<ConsumerInfo> consumers = model.getConsumers(queue);
			for (ConsumerInfo consumerInfo : consumers) {
				ConsumerNode consumerNode = nodeStore.getConsumerNode(consumerInfo);
				NodeConnection connect = connect(nodeStore, destinationNode, consumerNode);
				System.out.println("Added consumer connection: " + connect.getConnectionString());
			}

			// TODO add producers!
		}
	}

	public void clear() {
		nodeStore.clear();
	}

}