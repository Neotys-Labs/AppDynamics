package com.neotys.appdynamics.rest;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.neotys.appdynamics.AppDynamicsException;
import com.neotys.appdynamics.data.Metric;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

public enum AppDynamicsAPI {
	;
	private static final String API_APPLICATIONS_URL = "/controller/rest/applications/";
	private final static String METRIC_DATA = "/metric-data";
	private final static String METRIC_PATH = "metric-path=";
	private final static String TIME_RANGE = "time-range-type=";
	private final static String DURATION = "duration-in-mins=";
	private final static String ROLLUP = "rollup=";
	private final static String OUPUT = "output=";

	public static List<Metric> listMetric(final AppDynamicsRestClient client, final String url,
	                                      final String application, final String path, final int durationMinutes) throws Exception {
		HttpClient newHttpClient = client.getNewHttpClient();
		StringBuilder urlBuilder = new StringBuilder();
		try {
			urlBuilder.append(url).append(API_APPLICATIONS_URL).append(application).append(METRIC_DATA)
					.append("?").append(METRIC_PATH).append(URLEncoder.encode(path, StandardCharsets.UTF_8.toString()))
					.append("&").append(TIME_RANGE).append("BEFORE_NOW")
					.append("&").append(DURATION).append(durationMinutes)
					.append("&").append(ROLLUP).append(false)
					.append("&").append(OUPUT).append("JSON");

			HttpGet request = new HttpGet(urlBuilder.toString());
			client.getHeaders().forEach(request::addHeader);

			HttpResponse response = newHttpClient.execute(request);

			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				String jsonResponse = EntityUtils.toString(response.getEntity(), "UTF-8");
				Type collectionType = new TypeToken<List<Metric>>() {}.getType();
				return new Gson().fromJson(jsonResponse, collectionType);
			} else {
				throw new AppDynamicsException("AppDynamics Rest error: " + response.getStatusLine().getStatusCode() + "-" + response.getStatusLine().getReasonPhrase());
			}
		} catch (IOException e) {
			throw new AppDynamicsException(e.getMessage());
		} finally {
			newHttpClient.getConnectionManager().shutdown();
		}
	}
}
