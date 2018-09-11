package com.neotys.appdynamics;

import com.google.common.base.Optional;
import com.google.common.base.Splitter;

import java.util.List;
import java.util.Map;

import static com.google.common.base.Strings.isNullOrEmpty;

public class AppDynamicsActionArguments {
	private final String appDynamicURL;
	private final String dataExchangeApiUrl;
	private final String appDynamicsApplicationName;

	private final Optional<String> appDynamicAPIKey;
	private final Optional<String> appDynamicAccountName;
	private final Optional<String> appDynamicUserName;
	private final Optional<String> appDynamicPassword;
	private final Optional<List<String>> appDynamicMetricPaths;
	private final Optional<String> proxyName;
	private final Optional<String> dataExchangeApiKey;


	public AppDynamicsActionArguments(final Map<String, Optional<String>> parsedArgs) throws IllegalArgumentException {
		// Required
		this.appDynamicURL = parsedArgs.get(AppDynamicsOption.AppDynamicsURL.getName()).or("");
		this.dataExchangeApiUrl = parsedArgs.get(AppDynamicsOption.NeoLoadDataExchangeApiUrl.getName()).or("");
		this.appDynamicsApplicationName = parsedArgs.get(AppDynamicsOption.AppDynamicsApplicationName.getName()).or("");

		// Optional
		this.appDynamicAPIKey = getArgumentValue(parsedArgs, AppDynamicsOption.AppDynamicsAPIKey);
		this.appDynamicAccountName = getArgumentValue(parsedArgs, AppDynamicsOption.AppDynamicsAccountName);
		this.appDynamicUserName = getArgumentValue(parsedArgs, AppDynamicsOption.AppDynamicsUserName);
		this.appDynamicPassword = getArgumentValue(parsedArgs, AppDynamicsOption.AppDynamicsPassword);
		this.appDynamicMetricPaths = Optional.of(Splitter.on("\n").splitToList(parsedArgs.get(AppDynamicsOption.AppDynamicsMetricPaths.getName()).or("")));
		this.proxyName = getArgumentValue(parsedArgs, AppDynamicsOption.AppDynamicsProxyName);
		this.dataExchangeApiKey = getArgumentValue(parsedArgs, AppDynamicsOption.NeoLoadDataExchangeApiKey);
	}

	private Optional<String> getArgumentValue(final Map<String, Optional<String>> parsedArgs, final AppDynamicsOption appDynamicsOption) {
		final Optional<String> value = parsedArgs.getOrDefault(appDynamicsOption.getName(), Optional.absent());
		if (value.isPresent() && isNullOrEmpty(value.get())) {
			return Optional.absent();
		}
		return value;
	}

	public String getAppDynamicURL() {
		return appDynamicURL;
	}

	public String getDataExchangeApiUrl() {
		return dataExchangeApiUrl;
	}

	public String getAppDynamicsApplicationName() {
		return appDynamicsApplicationName;
	}

	public Optional<String> getAppDynamicAPIKey() {
		return appDynamicAPIKey;
	}

	public Optional<String> getAppDynamicAccountName() {
		return appDynamicAccountName;
	}

	public Optional<String> getAppDynamicUserName() {
		return appDynamicUserName;
	}

	public Optional<String> getAppDynamicPassword() {
		return appDynamicPassword;
	}

	public Optional<List<String>> getAppDynamicMetricPaths() {
		return appDynamicMetricPaths;
	}

	public Optional<String> getProxyName() {
		return proxyName;
	}

	public Optional<String> getDataExchangeApiKey() {
		return dataExchangeApiKey;
	}
}