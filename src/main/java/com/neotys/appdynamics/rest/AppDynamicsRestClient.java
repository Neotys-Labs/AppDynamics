package com.neotys.appdynamics.rest;

import com.google.common.base.Optional;
import com.google.common.base.Strings;
import com.neotys.appdynamics.AppDynamicsActionArguments;
import com.neotys.appdynamics.AppDynamicsException;
import com.neotys.extensions.action.engine.Logger;
import com.neotys.extensions.action.engine.Proxy;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.message.BasicHeader;

import javax.net.ssl.HttpsURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class AppDynamicsRestClient {
	private final String url;
	private final Optional<String> apiKey;
	private final Optional<String> userName;
	private final Optional<String> accountName;
	private final Optional<String> password;
	private final Optional<Proxy> proxy;
	private final Logger logger;
	private List<Header> headers;

	public AppDynamicsRestClient(final AppDynamicsActionArguments appDynamicsActionArguments, final Optional<Proxy> proxy, final Logger logger) {
		this.apiKey = appDynamicsActionArguments.getAppDynamicAPIKey();
		this.userName = appDynamicsActionArguments.getAppDynamicUserName();
		this.accountName = appDynamicsActionArguments.getAppDynamicAccountName();
		this.password = appDynamicsActionArguments.getAppDynamicPassword();
		this.url = appDynamicsActionArguments.getAppDynamicURL();
		this.proxy = proxy;
		this.logger = logger;
		initHeaders();
	}

	private void initHeaders() {
		headers = new ArrayList<>();
		if (this.apiKey.isPresent()) {
			headers.add(new BasicHeader("Authorization", "Bearer " + this.apiKey.get()));
		}
	}

	public HttpClient getNewHttpClient() throws Exception {
		DefaultHttpClient defaultHttpClient;

		//Manage https
		if (this.url.contains("https")) {
			defaultHttpClient = new DefaultHttpClient(initHttpsConnManager());
		} else {
			defaultHttpClient = new DefaultHttpClient();
		}

		BasicCredentialsProvider basicCredentialsProvider = new BasicCredentialsProvider();

		//Api Key credentials
		if (this.apiKey.isPresent()) {
			//Nothing to do
		}
		//Login Password credentials
		else if (this.userName.isPresent() && this.accountName.isPresent() && this.password.isPresent()) {
			String login = this.userName.get() + "@" + this.accountName.get();
			UsernamePasswordCredentials credentials
					= new UsernamePasswordCredentials(login, this.password.get());

			URL decodeUrl = new URL(url);
			basicCredentialsProvider.setCredentials(new AuthScope(decodeUrl.getHost(), decodeUrl.getPort()), credentials);

			defaultHttpClient.setCredentialsProvider(basicCredentialsProvider);
		} else {
			throw new AppDynamicsException("Not able to create AppDynamics rest api client");
		}

		//Proxy init
		if (proxy.isPresent()) {
			final HttpHost proxyHttpHost = new HttpHost(proxy.get().getHost(), proxy.get().getPort(), "http");
			defaultHttpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxyHttpHost);
			if (!Strings.isNullOrEmpty(proxy.get().getLogin())) {
				basicCredentialsProvider.setCredentials(
						new AuthScope(proxy.get().getHost(), proxy.get().getPort()),
						new UsernamePasswordCredentials(proxy.get().getLogin(), proxy.get().getPassword()));
			}
		}

		return defaultHttpClient;
	}

	@SuppressWarnings("deprecation")
	private static SingleClientConnManager initHttpsConnManager() throws Exception {
		TrustStrategy acceptingTrustStrategy = (cert, authType) -> true;
		X509HostnameVerifier allowAllHostnameVerifier = SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;
		SSLSocketFactory sslSocketFactory = new SSLSocketFactory(acceptingTrustStrategy, allowAllHostnameVerifier);
		final SchemeRegistry registry = new SchemeRegistry();
		registry.register(new Scheme("http", 80, PlainSocketFactory.getSocketFactory()));
		registry.register(new Scheme("https", 443, sslSocketFactory));
		HttpsURLConnection.setDefaultHostnameVerifier(allowAllHostnameVerifier);
		return new SingleClientConnManager(registry);
	}

	public Logger getLogger() {
		return logger;
	}

	public List<Header> getHeaders() {
		return headers;
	}
}
