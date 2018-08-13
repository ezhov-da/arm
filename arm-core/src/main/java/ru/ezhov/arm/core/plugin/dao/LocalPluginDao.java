package ru.ezhov.arm.core.plugin.dao;

import ru.ezhov.arm.core.plugin.domain.Plugin;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

public class LocalPluginDao implements PluginDao {
	private static final Logger LOG = Logger.getLogger(LocalPluginDao.class.getName());

	@Override
	public List<Plugin> getAll() {
		return Arrays.asList(
			new Plugin("Общие знания", "ru.ezhov.remote.hints.CommonKnowledgesPanel"),
			new Plugin("Swing панель", "ru.ezhov.swing.panel.SwingPanel")
		);
	}
}
