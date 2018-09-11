package com.neotys.appdynamics.data;

import com.google.gson.annotations.SerializedName;

public class MetricValue {
	@SerializedName("occurrences")
	final int occurrences;
	@SerializedName("current")
	final int current;
	@SerializedName("min")
	final int min;
	@SerializedName("max")
	final int max;
	@SerializedName("startTimeInMillis")
	final long startTimeInMillis;
	@SerializedName("useRange")
	final boolean useRange;
	@SerializedName("count")
	final int count;
	@SerializedName("sum")
	final int sum;
	@SerializedName("value")
	final Double value;
	@SerializedName("standardDeviation")
	final int standardDeviation;

	public MetricValue(final int occurrences, final int current, final int min, final int max,
	                   final long startTimeInMillis, final boolean useRange, final int count,
	                   final int sum, final double value, final int standardDeviation) {
		this.occurrences = occurrences;
		this.current = current;
		this.min = min;
		this.max = max;
		this.startTimeInMillis = startTimeInMillis;
		this.useRange = useRange;
		this.count = count;
		this.sum = sum;
		this.value = value;
		this.standardDeviation = standardDeviation;
	}

	public int getOccurrences() {
		return occurrences;
	}

	public int getCurrent() {
		return current;
	}

	public int getMin() {
		return min;
	}

	public int getMax() {
		return max;
	}

	public long getStartTimeInMillis() {
		return startTimeInMillis;
	}

	public boolean isUseRange() {
		return useRange;
	}

	public int getCount() {
		return count;
	}

	public int getSum() {
		return sum;
	}

	public double getValue() {
		return value;
	}

	public int getStandardDeviation() {
		return standardDeviation;
	}

	@Override
	public String toString() {
		return "MetricValue{" +
				"occurrences=" + occurrences +
				", current=" + current +
				", min=" + min +
				", max=" + max +
				", startTimeInMillis=" + startTimeInMillis +
				", useRange=" + useRange +
				", count=" + count +
				", sum=" + sum +
				", value=" + value +
				", standardDeviation=" + standardDeviation +
				'}';
	}
}