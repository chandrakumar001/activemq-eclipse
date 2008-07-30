package com.googlecode.activemq.eclipse.action;

import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import com.googlecode.activemq.eclipse.model.ActiveMQModel;
import com.googlecode.activemq.eclipse.views.RouteView;

public class ConnectToActiveMQAction implements IObjectActionDelegate, IJavaLaunchConfigurationConstants {
	protected static final String LAUNCHER_NAME = "Connect to ActiveMQ";

	ISelection selection;
	
	protected static ActiveMQModel instance;

	public void run(IAction action) {
		try {
			ActiveMQModel model = new ActiveMQModel();
			// TODO hack - lets keep it around :)
			instance = model;
			model.start();
			onConnectionCreated(model);
			System.out.println("connected!!!");

		} catch (Exception e) {
			handleException(e);
		}
	}

	protected void handleException(Exception e) {
		// TODO: handle exception
		System.out.println("Caught: " + e);
		e.printStackTrace();
	}

	protected void onConnectionCreated(ActiveMQModel model) {
		try {
			IWorkbenchWindow activeWorkbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
			IWorkbenchPage activePage = activeWorkbenchWindow.getActivePage();
			RouteView routeView = (RouteView) activePage.findView(RouteView.VIEW_ID);
			if (routeView == null) {
				// TODO can we auto-open??
			} else {
				activePage.bringToTop(routeView);
				routeView.setActiveMQModel(model);
			}
		} catch (Exception e) {
			handleException(e);
		}

	}

	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		// TODO Auto-generated method stub

	}

	public void selectionChanged(IAction arg0, ISelection selection) {
		this.selection = selection;

	}

}
