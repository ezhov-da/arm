package ru.ezhov.arm.core.plugin.infrastructure;

import ru.ezhov.arm.core.plugin.domain.Plugin;

public interface PluginListener {
	public void pluginLoad(Plugin plugin);
}
