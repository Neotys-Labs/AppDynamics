package com.neotys.appdynamics;

import com.google.common.base.Optional;
import com.neotys.action.argument.Arguments;

import javax.swing.*;
import java.net.URL;

public enum  Constants {
	;
	public static final String CUSTOM_ACTION_TYPE = "AppDynamicsMonitoringAction";
	public static final String APP_DYNAMICS = "AppDynamics";

	public static final ImageIcon CUSTOM_ACTION_ICON;
	public static final String CUSTOM_ACTION_DESCRIPTION;

	static {
		final URL iconURL = AppDynamicsAction.class.getResource("appdynamics.png");
		if (iconURL != null) {CUSTOM_ACTION_ICON = new ImageIcon(iconURL);}
		else {CUSTOM_ACTION_ICON = null;}
		final StringBuilder description = new StringBuilder();
		description.append("AppDynamics Monitoring Action will retrieve all the counters specified in AppDynamics.\n").append(
				Arguments.getArgumentDescriptions(AppDynamicsOption.values()));
		CUSTOM_ACTION_DESCRIPTION = description.toString();
	}

	public static final String CUSTOM_ACTION_DISPLAY_NAME = "AppDynamics Monitoring";
	public static final String CUSTOM_ACTION_DISPLAY_PATH = "APM/AppDynamics";

	public static final Optional<String> CUSTOM_ACTION_MINIMUM_VERSION = Optional.of("6.3");
	public static final Optional<String> CUSTOM_ACTION_MAXIMIM_VERSION = Optional.absent();

	/*** NeoLoad error codes ***/
	public static final String STATUS_CODE_INVALID_PARAMETER = "NL-APP_DYNAMICS_ACTION-01";
	public static final String STATUS_CODE_TECHNICAL_ERROR = "NL-APP_DYNAMICS_ACTION-02";
	public static final String STATUS_CODE_BAD_CONTEXT = "NL-APP_DYNAMICS_ACTION-03";
	public static final String STATUS_CODE_INSUFFICIENT_DELAY = "NL-APP_DYNAMICS_ACTION-04";

	public static final String APP_DYNAMICS_LAST_EXECUTION_TIME = "AppDynamicsLastExecutionTime_%s_%s";

	public static final String APP_DYNAMICS_API_KEY_DESCRIPTION = "AppDynamics API key.";
	public static final String APP_DYNAMICS_APPLICATION_NAME_DESCRIPTION = "AppDynamics application name.";
	public static final String APP_DYNAMICS_ACCOUNT_NAME_DESCRIPTION = "AppDynamics account name.";
	public static final String APP_DYNAMICS_USER_NAME_DESCRIPTION = "AppDynamics user name.";
	public static final String APP_DYNAMICS_PASSWORD_DESCRIPTION = "AppDynamics password.";
	public static final String APP_DYNAMICS_URL_DESCRIPTION = "AppDynamics URL.";
	public static final String APP_DYNAMICS_METRIC_PATHS_DESCRIPTION = "AppDynamics Metric Paths separated by a line break.";
	public static final String APP_DYNAMICS_PROXY_NAME_DESCRIPTION = "The NeoLoad proxy name to access AppDynamics.";
	public static final String NEOLOAD_DATA_EXCHANGE_API_URL_DESCRIPTION = "The URL of the DataExchange server (located on the NeoLoad Controller).";
	public static final String NEOLOAD_DATA_EXCHANGE_API_KEY_DESCRIPTION = "Identification key specified in NeoLoad for DataExchange server.";

	public static final String APP_DYNAMICS_URL_DEFAULT_VALUE = "https://<account_name>.saas.appdynamics.com";
	public static final String NEOLOAD_DATA_EXCHANGE_API_URL_DEFAULT_VALUE = "http://localhost:7400/DataExchange/v1/Service.svc/";
	public static final String APP_DYNAMICS_METRIC_PATHS_DEFAULT_VALUE =
			"Application Infrastructure Performance|*|Hardware Resources|CPU|User" + "\n" +
			"Application Infrastructure Performance|*|Hardware Resources|CPU|System" + "\n" +
			"Application Infrastructure Performance|*|Hardware Resources|CPU|%Idle" + "\n" +
			"Application Infrastructure Performance|*|Hardware Resources|Memory|*" + "\n" +
			"Application Infrastructure Performance|*|Hardware Resources|Network|Incoming Errors" + "\n" +
			"Application Infrastructure Performance|*|Hardware Resources|Network|Outgoing Errors" + "\n" +
			"Application Infrastructure Performance|*|Hardware Resources|Disks|Reads/sec" + "\n" +
			"Application Infrastructure Performance|*|Hardware Resources|Disks|Writes/sec" + "\n" +
			"Application Infrastructure Performance|*|Agent|Business Transactions|*" + "\n";

	public static final String APP_DYNAMICS_REST_CLIENT = "AppDynamicsRestClient_%s_%s";

	/*** NeoLoad context (Data Exchange API) ***/
	public static final String NEOLOAD_CONTEXT_HARDWARE = APP_DYNAMICS;
	public static final String NEOLOAD_CONTEXT_LOCATION = APP_DYNAMICS;
	public static final String NEOLOAD_CONTEXT_SOFTWARE = APP_DYNAMICS;
	public static final String NL_DATA_EXCHANGE_API_CLIENT = "NLDataExchangeAPIClient";
}
