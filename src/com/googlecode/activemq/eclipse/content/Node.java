package com.googlecode.activemq.eclipse.content;

import org.eclipse.swt.graphics.Image;

/**
 * Represents a graphical node
 */
public interface Node {
	String getId();

	String getLabel();

	Image getImage();

	String getTooltip();
}
