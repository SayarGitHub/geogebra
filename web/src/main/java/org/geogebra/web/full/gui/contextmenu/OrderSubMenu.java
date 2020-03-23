package org.geogebra.web.full.gui.contextmenu;

import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import org.geogebra.common.euclidian.LayerManager;
import org.geogebra.common.kernel.geos.GeoElement;
import org.geogebra.common.main.App;
import org.geogebra.web.html5.gui.util.AriaMenuBar;
import org.geogebra.web.html5.gui.util.AriaMenuItem;

import java.util.ArrayList;

public class OrderSubMenu extends AriaMenuBar {

	private App app;
	private LayerManager layerManager;

	private ArrayList<GeoElement> geos;

	/**
	 * A submenu containing items for changing the drawing order of
	 * GeoElements on the graphics view
	 * @param geos GeoElements to apply the order-changing operations on
	 */
	public OrderSubMenu(App app, ArrayList<GeoElement> geos) {
		this.geos = geos;

		this.app = app;
		this.layerManager = app.getKernel().getConstruction().getLayerManager();

		addItem("ContextMenu.BringToFront", new BringToFrontCommand());
		addItem("ContextMenu.BringForward", new BringForwardCommand());
		addItem("ContextMenu.SendBackward", new SendBackwardCommand());
		addItem("ContextMenu.SendToBack", new SendToBackCommand());
	}

	private void addItem(String key, ScheduledCommand command) {
		addItem(new AriaMenuItem(app.getLocalization().getMenu(key),
				false, wrap(command)));
	}

	private ScheduledCommand wrap(final ScheduledCommand command) {
		return new ScheduledCommand() {
			@Override
			public void execute() {
				app.getActiveEuclidianView().getEuclidianController().splitSelectedStrokes(true);
				command.execute();
				app.getActiveEuclidianView().invalidateDrawableList();
				app.getKernel().storeUndoInfo();
			}
		};
	}

	private class BringToFrontCommand implements ScheduledCommand {
		@Override
		public void execute() {
			layerManager.moveToFront(geos);
		}
	}

	private class BringForwardCommand implements ScheduledCommand {
		@Override
		public void execute() {
			layerManager.moveForward(geos);
		}
	}

	private class SendBackwardCommand implements ScheduledCommand {
		@Override
		public void execute() {
			layerManager.moveBackward(geos);
		}
	}

	private class SendToBackCommand implements ScheduledCommand {
		@Override
		public void execute() {
			layerManager.moveToBack(geos);
		}
	}
}
