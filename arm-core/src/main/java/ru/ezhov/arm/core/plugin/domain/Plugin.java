package ru.ezhov.arm.core.plugin.domain;

import java.util.logging.Logger;

public class Plugin {
	private static final Logger LOG = Logger.getLogger(Plugin.class.getName());
	private String name;
	private String clazz;
	private String description;
	private String version;

	public Plugin(String name, String clazz) {
		this.name = name;
		this.clazz = clazz;
	}

	public String getName() {
		return name;
	}

	public String getClazz() {
		return clazz;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Plugin plugin = (Plugin) o;
		return name.equals(plugin.name);
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}
}
