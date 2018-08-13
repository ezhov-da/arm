package ru.ezhov.arm.gui;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import ru.ezhov.arm.core.plugin.dao.LocalPluginDao;
import ru.ezhov.arm.core.plugin.domain.Plugin;
import ru.ezhov.arm.core.plugin.infrastructure.PluginListener;
import ru.ezhov.arm.core.plugin.infrastructure.PluginService;
import ru.ezhov.arm.plugin.gui.panel.PanelPlugin;

import java.util.logging.Logger;

public class ApplicationPanel extends BorderPane implements PluginListener {
	private static final Logger LOG = Logger.getLogger(ApplicationPanel.class.getName());
	private Menu menu;
	private TabPane tabPane;
	private PluginService pluginService;

	public ApplicationPanel() {
		menu = new Menu("Список инструментов");
		PluginService pluginService = new PluginService(new LocalPluginDao());
		pluginService.addPluginListener(this);
		for (Plugin plugin : pluginService.getAll()) {
			MenuItem menuItem = new MenuItem(plugin.getName());
			menuItem.setUserData(plugin);
			menu.getItems().add(menuItem);
			menuItem.setOnAction(e -> {
				try {
					ObservableList<Tab> tabs = tabPane.getTabs();
					Tab findTab = null;
					for (Tab tab : tabs) {
						if (plugin.getName().equals(tab.getText())) {
							findTab = tab;
							break;
						}
					}
					if (findTab == null) {
						PanelPlugin panelPlugin = pluginService.loadPlugin(plugin);
						Pane pane = panelPlugin.getPane();
						Tab tab = new Tab(plugin.getName());
						tab.setContent(pane);
						tabPane.getTabs().addAll(tab);
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			});
		}

		MenuBar menuBar = new MenuBar();
		menuBar.setUseSystemMenuBar(true);
		menuBar.getMenus().addAll(
			menu
		);

		setTop(menuBar);
		tabPane = new TabPane();
		setCenter(tabPane);
	}

	@Override
	public void pluginLoad(Plugin plugin) {
		Platform.runLater(() -> {
			ObservableList<MenuItem> menuItems = menu.getItems();
			for (MenuItem menuItem : menuItems) {
				Plugin pluginFrom = (Plugin) menuItem.getUserData();
				if (plugin.getName().equals(pluginFrom.getName())) {
					menuItem.setText(pluginFrom.getName() + " (загружен)");
				}
			}
		});
	}
}
