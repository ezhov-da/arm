package ru.ezhov.remote.hints;

public class Knowledge {
	private String name;
	private String rawUrl;
	private String description;
	private String url;

	public String getName() {
		return name;
	}

	public String getRawUrl() {
		return rawUrl;
	}

	public String getDescription() {
		return description;
	}

	public String getUrl() {
		return url;
	}

	@Override
	public String toString() {
		return "Knowledge{" +
			"name='" + name + '\'' +
			", rawUrl='" + rawUrl + '\'' +
			", description='" + description + '\'' +
			", url='" + url + '\'' +
			'}';
	}
}
