package ru.ezhov.arm.core.plugin.infrastructure;

import ru.ezhov.arm.core.plugin.dao.PluginDao;
import ru.ezhov.arm.core.plugin.domain.Plugin;
import ru.ezhov.arm.plugin.gui.panel.PanelPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class PluginService {
	private static final Logger LOG = Logger.getLogger(PluginService.class.getName());
	private Map<String, AlreadyUploadedPlugin> loaderPlugin;
	private List<PluginListener> pluginListeners = new ArrayList<>();
	private PluginDao pluginDao;

	public PluginService(PluginDao pluginDao) {
		this.pluginDao = pluginDao;
		loaderPlugin = new HashMap<>();
	}

	public List<Plugin> getAll() {
		return pluginDao.getAll();
	}

	public PanelPlugin loadPlugin(Plugin plugin) throws Exception {
		PanelPlugin panelPlugin = null;
		AlreadyUploadedPlugin alreadyUploadedPlugin = loaderPlugin.get(plugin.getName());
		if (alreadyUploadedPlugin != null) {
			panelPlugin = alreadyUploadedPlugin.getPanelPlugin();
		} else {
			String clazz = plugin.getClazz();
			panelPlugin = (PanelPlugin) Class.forName(clazz).newInstance();
			//Подумать на какой стороне кешировать панель при первом запуске
			loaderPlugin.put(plugin.getName(), new AlreadyUploadedPlugin(plugin, panelPlugin));
			firePluginLoad(plugin);
		}
		return panelPlugin;
	}

	protected void firePluginLoad(Plugin plugin) {
		for (PluginListener pluginListener : pluginListeners) {
			pluginListener.pluginLoad(plugin);
		}
	}

	public void addPluginListener(PluginListener pluginListener) {
		if (!pluginListeners.contains(pluginListener)) {
			pluginListeners.add(pluginListener);
		}
	}

	public void removePluginListener(PluginListener pluginListener) {
		pluginListeners.remove(pluginListener);
	}

	class AlreadyUploadedPlugin {
		private Plugin plugin;
		private PanelPlugin panelPlugin;

		public AlreadyUploadedPlugin(Plugin plugin, PanelPlugin panelPlugin) {
			this.plugin = plugin;
			this.panelPlugin = panelPlugin;
		}

		public Plugin getPlugin() {
			return plugin;
		}

		public PanelPlugin getPanelPlugin() {
			return panelPlugin;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;
			AlreadyUploadedPlugin that = (AlreadyUploadedPlugin) o;
			return plugin != null ? plugin.getName().equals(that.plugin.getName()) : that.plugin == null;
		}

		@Override
		public int hashCode() {
			return plugin != null ? plugin.getName().hashCode() : 0;
		}
	}
}
