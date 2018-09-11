package com.neotys.appdynamics;

import com.google.common.base.Optional;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.neotys.appdynamics.data.Metric;
import com.neotys.appdynamics.rest.AppDynamicsRestClient;
import junit.framework.TestCase;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class AppDynamicsActionEngineTest extends TestCase {

	private static final String API_APPLICATIONS_URL = "https://wharf201809041959504.saas.appdynamics.com:443/controller/rest/applications/";
	private final String METRIC_DATA = "/metric-data";
	private final String OUPUT = "output=";
	private String TIME_RANGE = "time-range-type=";
	private String DURATION = "duration-in-mins=";
	private String ROLLUP = "rollup=";
	private String applicationId = "MyApp";
	//	private String path = "metric-path=Application%20Infrastructure%20Performance%7CMyTier%7CJVM%7C*";
	private String METRIC_PATH = "metric-path=";
	private String path = "Application Infrastructure Performance|MyTier|JVM|Memory|Heap|*";


	public void testGetMetric() {
		StringBuilder urlBuilder = new StringBuilder();
		try {
			urlBuilder.append(API_APPLICATIONS_URL).append(applicationId).append(METRIC_DATA)
					.append("?").append(METRIC_PATH).append(URLEncoder.encode(path, StandardCharsets.UTF_8.toString()))
					.append("&").append(TIME_RANGE).append("BEFORE_NOW")
					.append("&").append(DURATION).append(5)
					.append("&").append(ROLLUP).append(false)
					.append("&").append(OUPUT).append("JSON");


			CredentialsProvider provider = new BasicCredentialsProvider();
			UsernamePasswordCredentials credentials
					= new UsernamePasswordCredentials("wharf201809041959504@wharf201809041959504", "admin");
			provider.setCredentials(AuthScope.ANY, credentials);

			HttpClient client = HttpClientBuilder.create()
					.setDefaultCredentialsProvider(provider)
					.build();

			HttpResponse response = client.execute(
					new HttpGet(urlBuilder.toString()));

			String jsonResponse = EntityUtils.toString(response.getEntity(), "UTF-8");

			Type collectionType = new TypeToken<List<Metric>>(){}.getType();
			List<Metric> enums = new Gson().fromJson(jsonResponse, collectionType);

			System.out.println(jsonResponse);
			System.out.println(enums);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}