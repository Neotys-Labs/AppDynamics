package com.neotys.appdynamics;

import com.google.common.base.Optional;
import com.neotys.action.result.ResultFactory;
import com.neotys.appdynamics.data.Metric;
import com.neotys.appdynamics.rest.AppDynamicsAPI;
import com.neotys.appdynamics.rest.AppDynamicsRestClient;
import com.neotys.extensions.action.ActionParameter;
import com.neotys.extensions.action.engine.ActionEngine;
import com.neotys.extensions.action.engine.Context;
import com.neotys.extensions.action.engine.Proxy;
import com.neotys.extensions.action.engine.SampleResult;
import com.neotys.rest.dataexchange.client.DataExchangeAPIClient;
import com.neotys.rest.dataexchange.client.DataExchangeAPIClientFactory;
import com.neotys.rest.dataexchange.model.ContextBuilder;
import com.neotys.rest.dataexchange.model.Entry;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Strings.isNullOrEmpty;
import static com.google.common.collect.Lists.newArrayList;
import static com.neotys.action.argument.Arguments.getArgumentLogString;
import static com.neotys.action.argument.Arguments.parseArguments;
import static com.neotys.appdynamics.Constants.APP_DYNAMICS_LAST_EXECUTION_TIME;
import static com.neotys.appdynamics.Constants.STATUS_CODE_BAD_CONTEXT;
import static com.neotys.appdynamics.Constants.STATUS_CODE_INVALID_PARAMETER;
import static com.neotys.appdynamics.Constants.STATUS_CODE_TECHNICAL_ERROR;
import static java.util.stream.Collectors.toList;

public class AppDynamicsActionEngine implements ActionEngine {
	public SampleResult execute(final Context context, final List<ActionParameter> parameters) {
		final SampleResult sampleResult = new SampleResult();
		final StringBuilder requestContentBuilder = new StringBuilder();

		// Parse arguments
		final AppDynamicsActionArguments arguments;
		try {
			final Map<String, Optional<String>> parsedArgs = parseArguments(parameters, AppDynamicsOption.values());
			requestContentBuilder.append("Parameters: ")
					.append(getArgumentLogString(parsedArgs, AppDynamicsOption.values()))
					.append("\n");
			arguments = new AppDynamicsActionArguments(parsedArgs);
		} catch (final IllegalArgumentException e) {
			return newErrorResult(requestContentBuilder, context, STATUS_CODE_INVALID_PARAMETER, "Issue while parsing advanced action arguments: ", Optional.of(e));
		}

		// Check parameters consistency
		if (isNullOrEmpty(arguments.getAppDynamicURL())) {
			return newErrorResult(requestContentBuilder, context, STATUS_CODE_INVALID_PARAMETER, "The AppDynamics URL must be not empty.", Optional.absent());
		}
		if (isNullOrEmpty(arguments.getDataExchangeApiUrl())) {
			return newErrorResult(requestContentBuilder, context, STATUS_CODE_INVALID_PARAMETER, "The NeoLoad data exchange URL must be not empty.", Optional.absent());
		}
		if (isNullOrEmpty(arguments.getAppDynamicsApplicationName())) {
			return newErrorResult(requestContentBuilder, context, STATUS_CODE_INVALID_PARAMETER, "The AppDynamics application name must be not empty.", Optional.absent());
		}
		if (!hasAuthenticationInfo(arguments)) {
			return newErrorResult(requestContentBuilder, context, STATUS_CODE_INVALID_PARAMETER, "There is no authentication information. Either API key or (account name, user name, password) must be specified.", Optional.absent());
		}
		final Optional<List<String>> metricPaths = arguments.getAppDynamicMetricPaths();
		if (!metricPaths.isPresent() || metricPaths.get().isEmpty()) {
			return newErrorResult(requestContentBuilder, context, STATUS_CODE_INVALID_PARAMETER, "The metric paths of AppDynamics application must be not empty.", Optional.absent());
		}

		// Check last execution time (and fail if called less than 45 seconds ago).
		final String appDynamicsLastExecutionKey = String.format(APP_DYNAMICS_LAST_EXECUTION_TIME,
				arguments.getAppDynamicURL(), arguments.getAppDynamicsApplicationName());
		final Object appDynamicsLastExecutionTime = context.getCurrentVirtualUser().get(appDynamicsLastExecutionKey);
		final Long appDynamicsCurrentExecution = System.currentTimeMillis();
		context.getCurrentVirtualUser().put(appDynamicsLastExecutionKey, appDynamicsCurrentExecution);
		if (!(appDynamicsLastExecutionTime instanceof Long)) {
			requestContentBuilder.append("(first execution).\n");
		} else if ((Long) appDynamicsLastExecutionTime + 45 * 1000 > appDynamicsCurrentExecution) {
			return newErrorResult(requestContentBuilder, context, Constants.STATUS_CODE_INSUFFICIENT_DELAY,
					"Not enough delay between the two AppDynamics advanced action execution. Make sure to have at least 60 seconds pacing on the Actions container.", Optional.absent());
		} else {
			requestContentBuilder.append("(last execution was " + ((appDynamicsCurrentExecution - (Long) appDynamicsLastExecutionTime) / 1000) + " seconds ago)\n");
		}

		//Init or get rest client
		final String appDynamicsRestClientKey = String.format(Constants.APP_DYNAMICS_REST_CLIENT,
				arguments.getAppDynamicURL(), arguments.getAppDynamicsApplicationName());
		AppDynamicsRestClient appDynamicsRestClient = (AppDynamicsRestClient) context.getCurrentVirtualUser().get(appDynamicsRestClientKey);
		if (appDynamicsRestClient == null) {
			try {
				appDynamicsRestClient = new AppDynamicsRestClient(arguments,
						getProxy(context, arguments.getProxyName(), arguments.getAppDynamicURL()),
						context.getLogger());
				context.getCurrentVirtualUser().put(appDynamicsRestClientKey, appDynamicsRestClient);
			} catch (Exception e) {
				return newErrorResult(requestContentBuilder, context, STATUS_CODE_TECHNICAL_ERROR,
						"Technical Error while creating AppDynamics Rest Client: ", Optional.of(e));
			}
		} else {
			requestContentBuilder.append("AppDynamicsRestClient retrieved from User Path Context.\n");
		}

		// Retrieve DataExchangeAPIClient from Context, or instantiate new one
		DataExchangeAPIClient dataExchangeAPIClient = (DataExchangeAPIClient) context.getCurrentVirtualUser().get(Constants.NL_DATA_EXCHANGE_API_CLIENT);
		if (dataExchangeAPIClient == null) {
			try {
				final ContextBuilder contextBuilder = new ContextBuilder();
				contextBuilder.hardware(Constants.NEOLOAD_CONTEXT_HARDWARE).location(Constants.NEOLOAD_CONTEXT_LOCATION).software(
						Constants.NEOLOAD_CONTEXT_SOFTWARE).script("NewRelicInfrasfructureMonitoring" + System.currentTimeMillis());
				dataExchangeAPIClient = DataExchangeAPIClientFactory.newClient(arguments.getDataExchangeApiUrl(),
						contextBuilder.build(),
						arguments.getDataExchangeApiKey().orNull());
				context.getCurrentVirtualUser().put(Constants.NL_DATA_EXCHANGE_API_CLIENT, dataExchangeAPIClient);
				requestContentBuilder.append("DataExchangeAPIClient created.\n");
			} catch (final Exception e) {
				return newErrorResult(requestContentBuilder, context, STATUS_CODE_TECHNICAL_ERROR,
						"Technical Error while creating DataExchangeAPI Client: ", Optional.of(e));
			}
		} else {
			requestContentBuilder.append("DataExchangeAPIClient retrieved from User Path Context.\n");
		}

		sampleResult.sampleStart();

		// AppDynamics -> NeoLoad DataExchangeAPI
		final String applicationName = arguments.getAppDynamicsApplicationName();
		final String appDynamicURL = arguments.getAppDynamicURL();

		int duration = 1;
		if (appDynamicsLastExecutionTime instanceof Long) {
			duration = (int) Math.ceil((appDynamicsCurrentExecution - (Long) appDynamicsLastExecutionTime) / 60000D);
		}

		final List<Entry> entries = newArrayList();
		for (final String path : metricPaths.get()) {
			try {
				List<Metric> metrics = AppDynamicsAPI.listMetric(appDynamicsRestClient, appDynamicURL, applicationName, path, duration + 1);
				if (!metrics.isEmpty()) {
					entries.addAll(metrics.stream()
							.map(metric -> metric.buildEntries(applicationName))
							.flatMap(List::stream)
							.collect(toList()));
				}
			} catch (final Exception e) {
				return newErrorResult(requestContentBuilder, context, STATUS_CODE_BAD_CONTEXT, "Failed to retrieve data metric: ", Optional.of(e));
			}
		}

		try {
			if (!entries.isEmpty()) {
				dataExchangeAPIClient.addEntries(entries);
			}
		} catch (final Exception e) {
			return newErrorResult(requestContentBuilder, context, STATUS_CODE_TECHNICAL_ERROR,
					"Technical Error while sending AppDynamics metric Data and inject them to NeoLoad through DataExchange API: ", Optional.of(e));
		} finally {
			sampleResult.sampleEnd();
		}

		return sampleResult;
	}

	private boolean hasAuthenticationInfo(final AppDynamicsActionArguments arguments) {
		final Optional<String> apiKey = arguments.getAppDynamicAPIKey();
		if (apiKey.isPresent() && !isNullOrEmpty(apiKey.get())) {
			return true;
		}
		final Optional<String> accountName = arguments.getAppDynamicAccountName();
		final Optional<String> userName = arguments.getAppDynamicUserName();
		final Optional<String> password = arguments.getAppDynamicPassword();
		return accountName.isPresent() && !isNullOrEmpty(accountName.get())
				&& userName.isPresent() && !isNullOrEmpty(userName.get())
				&& password.isPresent() && !isNullOrEmpty(password.get());

	}

	private static Optional<Proxy> getProxy(final Context context, final Optional<String> proxyName, final String url) throws MalformedURLException, AppDynamicsException {
		if (proxyName.isPresent()) {
			Proxy proxyByName = context.getProxyByName(proxyName.get(), new URL(url));
			if(proxyByName == null){
				throw new AppDynamicsException("Not able to retrieve proxy");
			}
			return Optional.of(proxyByName);
		}
		return Optional.absent();
	}

	@Override
	public void stopExecute() {
		//Nothing to do
	}

	private static SampleResult newErrorResult(final StringBuilder requestContentBuilder, final Context context, final String statusCode, final String statusMessage, final Optional<Exception> exception) {
		final SampleResult sampleResult;
		if (exception.isPresent()) {
			sampleResult = ResultFactory.newErrorResult(context, statusCode, statusMessage, exception.get());
		} else {
			sampleResult = ResultFactory.newErrorResult(context, statusCode, statusMessage);
		}
		sampleResult.setRequestContent(requestContentBuilder.toString());
		return sampleResult;
	}
}
