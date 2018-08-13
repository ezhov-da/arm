package ru.ezhov.remote.hints;

import java.util.Arrays;
import java.util.List;

public class DataInfo {
	private String lastUpdate;
	private List<Knowledge> knowledges;

	public String getLastUpdate() {
		return lastUpdate;
	}

	public List<Knowledge> getKnowledges() {
		return knowledges;
	}

	@Override
	public String toString() {
		return "DataInfo{" +
			"lastUpdate=" + lastUpdate +
			", knowledges=" + Arrays.toString(knowledges.toArray()) +
			'}';
	}
}
