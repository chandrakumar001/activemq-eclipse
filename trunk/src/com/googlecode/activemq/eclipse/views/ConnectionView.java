package com.googlecode.activemq.eclipse.views;

import java.util.ArrayList;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.*;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.jface.viewers.*;
import org.eclipse.swt.graphics.Image;
import org.eclipse.jface.action.*;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.*;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.SWT;
import org.eclipse.core.runtime.IAdaptable;

import com.googlecode.activemq.eclipse.action.ConnectToActiveMQAction;
import com.googlecode.activemq.eclipse.content.ActiveMQClientPropertySource;
import com.googlecode.activemq.eclipse.model.ActiveMQClient;

/**
 * Displays all of the ActiveMQ connections along with their status
 */

public class ConnectionView extends ViewPart {
	private TreeViewer viewer;
	private DrillDownAdapter drillDownAdapter;
	private Action action1;
	private Action action2;
	private Action action3;
	private Action action4;
	private Action doubleClickAction;
	private TreeParent invisibleRoot;
	private TreeParent visibleRoot;

	/*
	 * The content provider class is responsible for providing objects to the
	 * view. It can wrap existing objects in adapters or simply return objects
	 * as-is. These objects may be sensitive to the current input of the view,
	 * or ignore it and always show the same content (like Task List, for
	 * example).
	 */

	class TreeObject implements IAdaptable {
		private String name;
		private TreeParent parent;

		public TreeObject() {
		}

		public TreeObject(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public void setParent(TreeParent parent) {
			this.parent = parent;
		}

		public TreeParent getParent() {
			return parent;
		}

		public String toString() {
			return getName();
		}

		public Object getAdapter(Class key) {
			System.out.println("Attempt to convert " + this + " into class: " + key);
			return null;
		}

		public void setName(String name) {
			this.name = name;
		}
	}

	class TreeParent extends TreeObject {
		private ArrayList children = new ArrayList();;

		public TreeParent() {
		}

		public TreeParent(String name) {
			super(name);
		}

		public void addChild(TreeObject child) {
			children.add(child);
			child.setParent(this);
		}

		public void removeChild(TreeObject child) {
			children.remove(child);
			child.setParent(null);
		}

		public TreeObject[] getChildren() {
			return (TreeObject[]) children.toArray(new TreeObject[children.size()]);
		}

		public boolean hasChildren() {
			return children.size() > 0;
		}
	}

	class ActiveMQConnectionTree extends TreeParent {
		private ActiveMQClient client = new ActiveMQClient();
		
		public ActiveMQConnectionTree() {
			setName(client.getName());
		}

		public ActiveMQClient getClient() {
			return client;
		}

		@Override
		public Object getAdapter(Class key) {
			if (key == IPropertySource.class) {
				return new ActiveMQClientPropertySource(client);
			}
			return super.getAdapter(key);
		}
		
		

	}

	class ViewContentProvider implements IStructuredContentProvider, ITreeContentProvider {

		public void inputChanged(Viewer v, Object oldInput, Object newInput) {
		}

		public void dispose() {
		}

		public Object[] getElements(Object parent) {
			if (parent.equals(getViewSite())) {
				if (invisibleRoot == null)
					initialize();
				return getChildren(invisibleRoot);
			}
			return getChildren(parent);
		}

		public Object getParent(Object child) {
			if (child instanceof TreeObject) {
				return ((TreeObject) child).getParent();
			}
			return null;
		}

		public Object[] getChildren(Object parent) {
			if (parent instanceof TreeParent) {
				return ((TreeParent) parent).getChildren();
			}
			return new Object[0];
		}

		public boolean hasChildren(Object parent) {
			if (parent instanceof TreeParent)
				return ((TreeParent) parent).hasChildren();
			return false;
		}

		private void initialize() {
			invisibleRoot = new TreeParent("");
			visibleRoot = new TreeParent("ActiveMQ Connections");
			invisibleRoot.addChild(visibleRoot);
			createNewConnection();
		}

	}

	class ViewLabelProvider extends LabelProvider {

		public String getText(Object obj) {
			return obj.toString();
		}

		public Image getImage(Object obj) {
			String imageKey = ISharedImages.IMG_OBJ_ELEMENT;
			if (obj instanceof TreeParent)
				imageKey = ISharedImages.IMG_OBJ_FOLDER;
			return PlatformUI.getWorkbench().getSharedImages().getImage(imageKey);
		}
	}

	class NameSorter extends ViewerSorter {
	}

	/**
	 * The constructor.
	 */
	public ConnectionView() {
	}

	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	public void createPartControl(Composite parent) {
		viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		drillDownAdapter = new DrillDownAdapter(viewer);
		viewer.setContentProvider(new ViewContentProvider());
		viewer.setLabelProvider(new ViewLabelProvider());
		viewer.setSorter(new NameSorter());
		viewer.setInput(getViewSite());

		// lets expose the selection to other viewers
		getViewSite().setSelectionProvider(viewer);

		// Create the help context id for the viewer's control
		PlatformUI.getWorkbench().getHelpSystem().setHelp(viewer.getControl(), "com.googlecode.activemq.eclipse.viewer");
		makeActions();
		hookContextMenu();
		hookDoubleClickAction();
		contributeToActionBars();
	}

	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				ConnectionView.this.fillContextMenu(manager);
			}
		});
		Menu menu = menuMgr.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, viewer);
	}

	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		fillLocalPullDown(bars.getMenuManager());
		fillLocalToolBar(bars.getToolBarManager());
	}

	private void fillLocalPullDown(IMenuManager manager) {
		manager.add(action1);
		manager.add(new Separator());
		manager.add(action2);
		manager.add(action3);
		manager.add(action4);
	}

	private void fillContextMenu(IMenuManager manager) {
		manager.add(action1);
		manager.add(action2);
		manager.add(action3);
		manager.add(action4);
		manager.add(new Separator());
		drillDownAdapter.addNavigationActions(manager);
		// Other plug-ins can contribute there actions here
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}

	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(action1);
		manager.add(action2);
		manager.add(action3);
		manager.add(action4);
		manager.add(new Separator());
		drillDownAdapter.addNavigationActions(manager);
	}

	private void makeActions() {
		action1 = new Action() {
			public void run() {
				connectConnection();
			}
		};
		action1.setText("Connect");
		action1.setToolTipText("Connect to the ActiveMQ broker using the current connection details");
		action1.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));


		action2 = new Action() {
			public void run() {
				createNewConnection();
				viewer.refresh();
			}
		};
		action2.setText("New Connection");
		action2.setToolTipText("Create a new connection");
		action2.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));

		action3 = new Action() {
			public void run() {
				// TODO
			}
		};
		action3.setText("Edit");
		action3.setToolTipText("Edit the connection details");
		action3.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));

		action4 = new Action() {
			public void run() {
				ISelection selection = viewer.getSelection();
				Object obj = ((IStructuredSelection) selection).getFirstElement();
				if (obj instanceof ActiveMQConnectionTree) {
					ActiveMQConnectionTree connection = (ActiveMQConnectionTree) obj;
					ActiveMQClient client = connection.getClient();
					if (client.isStarted()) {
						try {
							client.stop();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							System.out.println("Failed to connect! " + e);
							e.printStackTrace();
						}
					}
					visibleRoot.removeChild(connection);
					viewer.refresh();
				}
			}
		};
		action4.setText("Delete");
		action4.setToolTipText("Delete this connection");
		action4.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));

		doubleClickAction = new Action() {
			public void run() {
				//connectConnection();
			}
		};
	}

	private void hookDoubleClickAction() {
		viewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				doubleClickAction.run();
			}
		});
	}

	private void showMessage(String message) {
		MessageDialog.openInformation(viewer.getControl().getShell(), "ActiveMQ Connections", message);
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		viewer.getControl().setFocus();
	}

	protected void connectConnection() {
		ISelection selection = viewer.getSelection();
		Object obj = ((IStructuredSelection) selection).getFirstElement();
		if (obj instanceof ActiveMQConnectionTree) {
			ActiveMQConnectionTree connection = (ActiveMQConnectionTree) obj;
			ActiveMQClient client = connection.getClient();
			if (!client.isStarted()) {
				try {
					client.start();
					ConnectToActiveMQAction.onConnectionCreated(client);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					System.out.println("Failed to connect! " + e);
					e.printStackTrace();
				}
			}
		}
		else {
			showMessage("Double-click detected on " + obj.toString() + " with type: " + obj.getClass().getName());
		}
	}
	
	protected void createNewConnection() {
		visibleRoot.addChild(new ActiveMQConnectionTree());
	}

}