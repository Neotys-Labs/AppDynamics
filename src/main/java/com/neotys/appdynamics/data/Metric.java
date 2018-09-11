package com.neotys.appdynamics.data;

import com.google.common.base.MoreObjects;
import com.google.common.base.Splitter;
import com.google.gson.annotations.SerializedName;
import com.neotys.appdynamics.Constants;
import com.neotys.rest.dataexchange.model.Entry;
import com.neotys.rest.dataexchange.model.EntryBuilder;

import java.util.List;
import java.util.stream.Collectors;

import static com.google.common.collect.Lists.newArrayList;

public class Metric {
	@SerializedName("metricName")
	final String metricName;
	@SerializedName("metricId")
	final String metricId;
	@SerializedName("metricPath")
	final String metricPath;
	@SerializedName("frequency")
	final String frequency;

	@SerializedName("metricValues")
	final List<MetricValue> metricValues;

	public Metric(final String metricName,
	              final String metricId,
	              final String metricPath,
	              final String frequency,
	              final List<MetricValue> metricValues) {
		this.metricName = metricName;
		this.metricId = metricId;
		this.metricPath = metricPath;
		this.frequency = frequency;
		this.metricValues = metricValues;
	}

	public String getMetricName() {
		return metricName;
	}

	public String getMetricId() {
		return metricId;
	}

	public String getMetricPath() {
		return metricPath;
	}

	public String getFrequency() {
		return frequency;
	}

	public List<MetricValue> getMetricValues() {
		return metricValues;
	}

	public List<Entry> buildEntries(final String applicationName) {
		return metricValues.stream()
				.map(metricValue -> buildEntry(metricValue, applicationName))
				.collect(Collectors.toList());
	}

	private Entry buildEntry(final MetricValue metricValue, final String applicationName) {
		final List<String> dataEntryPath = newArrayList(Constants.APP_DYNAMICS, applicationName);
		dataEntryPath.addAll(Splitter.on("|").splitToList(metricPath));

		final EntryBuilder entryBuilder = new EntryBuilder(dataEntryPath, metricValue.startTimeInMillis);
		entryBuilder.unit("");
		entryBuilder.value(metricValue.value);
		return entryBuilder.build();
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
				.add("metricName", metricName)
				.add("metricId", metricId)
				.add("metricPath", metricPath)
				.add("frequency", frequency)
				.add("metricValues", metricValues)
				.toString();
	}
}
