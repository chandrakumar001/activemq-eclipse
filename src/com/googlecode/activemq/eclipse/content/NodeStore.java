package com.googlecode.activemq.eclipse.content;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.activemq.command.ActiveMQDestination;
import org.apache.activemq.command.ConsumerId;
import org.apache.activemq.command.ConsumerInfo;
import org.eclipse.core.runtime.IAdaptable;

import com.googlecode.activemq.eclipse.model.ActiveMQClient;

public class NodeStore {
	public List<NodeConnection> connections = new ArrayList<NodeConnection>();
	private Map<Object, IAdaptable> nodeDataMap = new HashMap<Object, IAdaptable>();

	public Map<Object, IAdaptable> getNodeDataMap() {
		return nodeDataMap;
	}

	public List<NodeConnection> getConnections() {
		return connections;
	}

	public Object[] getConnectionsAsArray() {
		return connections.toArray();
	}

	public Object[] getNodesAsArray() {
		return nodeDataMap.values().toArray();
	}

	public BrokerNode getBrokerNode(ActiveMQClient model) {
		Object o = nodeDataMap.get(model);
		BrokerNode nodeData = null;
		if (o == null) {
			nodeData = new BrokerNode(model);
			nodeDataMap.put(model, nodeData);
		} else {
			nodeData = (BrokerNode) o;
		}
		return nodeData;
	}

	public DestinationNode getDestinationNode(ActiveMQDestination queue) {
		Object o = nodeDataMap.get(queue);
		DestinationNode nodeData = null;
		if (o == null) {
			nodeData = new DestinationNode(queue);
			nodeDataMap.put(queue, nodeData);
		} else {
			nodeData = (DestinationNode) o;
		}
		return nodeData;
	}

	public ConsumerNode getConsumerNode(ConsumerInfo consumerInfo) {
		ConsumerId consumerId = consumerInfo.getConsumerId();
		Object o = nodeDataMap.get(consumerId);
		ConsumerNode nodeData = null;
		if (o == null) {
			nodeData = new ConsumerNode(consumerInfo);
			nodeDataMap.put(consumerId, nodeData);
		} else {
			nodeData = (ConsumerNode) o;
		}
		return nodeData;
	}

	public Object getSource(NodeConnection compareConnection) {

		Iterator<NodeConnection> it = connections.iterator();
		while (it.hasNext()) {
			NodeConnection routeConnection = it.next();
			if (routeConnection.getConnectionString().equals(compareConnection.getConnectionString())) {
				return routeConnection.getSource();
			}
		}
		return null;

	}

	public Object getDestination(NodeConnection compareConnection) {
		Iterator<NodeConnection> it = connections.iterator();
		while (it.hasNext()) {
			NodeConnection routeConnection = it.next();
			if (routeConnection.getConnectionString().equals(compareConnection.getConnectionString())) {
				return routeConnection.getDestination();
			}
		}
		return null;
	}

	public void clear() {
		connections.clear();
		nodeDataMap.clear();
	}
}
