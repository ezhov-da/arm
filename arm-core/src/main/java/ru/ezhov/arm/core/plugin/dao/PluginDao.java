package ru.ezhov.arm.core.plugin.dao;

import ru.ezhov.arm.core.plugin.domain.Plugin;

import java.util.List;

public interface PluginDao {
	List<Plugin> getAll();
}
