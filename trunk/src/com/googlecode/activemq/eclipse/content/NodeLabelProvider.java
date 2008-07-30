package com.googlecode.activemq.eclipse.content;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

public class NodeLabelProvider extends LabelProvider {

	public Image getImage(Object element) {
		if (element instanceof Node) {
			Node routeNode = (Node) element;
			return routeNode.getImage();
		}
		return null;
	}

	public String getText(Object element) {
		if (element instanceof Node) {
			Node routeNode = (Node) element;
			return routeNode.getLabel();
		}
		return element.toString();
	}

}