package com.neotys.appdynamics;

import com.neotys.action.argument.ArgumentValidator;
import com.neotys.action.argument.Option;
import com.neotys.extensions.action.ActionParameter;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.neotys.action.argument.DefaultArgumentValidator.ALWAYS_VALID;
import static com.neotys.action.argument.DefaultArgumentValidator.BOOLEAN_VALIDATOR;
import static com.neotys.action.argument.DefaultArgumentValidator.NON_EMPTY;
import static com.neotys.action.argument.Option.AppearsByDefault.False;
import static com.neotys.action.argument.Option.AppearsByDefault.True;
import static com.neotys.action.argument.Option.OptionalRequired.Optional;
import static com.neotys.action.argument.Option.OptionalRequired.Required;
import static com.neotys.appdynamics.Constants.APP_DYNAMICS_ACCOUNT_NAME_DESCRIPTION;
import static com.neotys.appdynamics.Constants.APP_DYNAMICS_API_KEY_DESCRIPTION;
import static com.neotys.appdynamics.Constants.APP_DYNAMICS_APPLICATION_NAME_DESCRIPTION;
import static com.neotys.appdynamics.Constants.APP_DYNAMICS_METRIC_PATHS_DEFAULT_VALUE;
import static com.neotys.appdynamics.Constants.APP_DYNAMICS_METRIC_PATHS_DESCRIPTION;
import static com.neotys.appdynamics.Constants.APP_DYNAMICS_PASSWORD_DESCRIPTION;
import static com.neotys.appdynamics.Constants.APP_DYNAMICS_PROXY_NAME_DESCRIPTION;
import static com.neotys.appdynamics.Constants.APP_DYNAMICS_URL_DEFAULT_VALUE;
import static com.neotys.appdynamics.Constants.APP_DYNAMICS_URL_DESCRIPTION;
import static com.neotys.appdynamics.Constants.APP_DYNAMICS_USER_NAME_DESCRIPTION;
import static com.neotys.appdynamics.Constants.NEOLOAD_DATA_EXCHANGE_API_KEY_DESCRIPTION;
import static com.neotys.appdynamics.Constants.NEOLOAD_DATA_EXCHANGE_API_URL_DESCRIPTION;
import static com.neotys.appdynamics.Constants.TLS_INSECURE_KEY_DESCRIPTION;
import static com.neotys.extensions.action.ActionParameter.Type.PASSWORD;
import static com.neotys.extensions.action.ActionParameter.Type.TEXT;

public enum AppDynamicsOption implements Option {
	AppDynamicsURL("appDynamicsURL", 							Required, True, TEXT, APP_DYNAMICS_URL_DEFAULT_VALUE, APP_DYNAMICS_URL_DESCRIPTION, NON_EMPTY),
	AppDynamicsApplicationName("appDynamicsApplicationName",	Required, True, TEXT, "", APP_DYNAMICS_APPLICATION_NAME_DESCRIPTION, NON_EMPTY),
	AppDynamicsAPIKey("appDynamicsAPIKey", 						Optional, True, TEXT, "", APP_DYNAMICS_API_KEY_DESCRIPTION, ALWAYS_VALID),
	AppDynamicsAccountName("appDynamicsAccountName", 			Optional, True, TEXT, "", APP_DYNAMICS_ACCOUNT_NAME_DESCRIPTION, ALWAYS_VALID),
	AppDynamicsUserName("appDynamicsUserName", 					Optional, True, TEXT, "", APP_DYNAMICS_USER_NAME_DESCRIPTION, ALWAYS_VALID),
	AppDynamicsPassword("appDynamicsPassword", 					Optional, True, PASSWORD, "", APP_DYNAMICS_PASSWORD_DESCRIPTION, ALWAYS_VALID),
	AppDynamicsMetricPaths("appDynamicsMetricPaths", 			Required, True, TEXT, APP_DYNAMICS_METRIC_PATHS_DEFAULT_VALUE, APP_DYNAMICS_METRIC_PATHS_DESCRIPTION, NON_EMPTY),
	AppDynamicsProxyName("proxyName", 							Optional, False, TEXT, "", APP_DYNAMICS_PROXY_NAME_DESCRIPTION, ALWAYS_VALID),
	NeoLoadDataExchangeApiUrl("dataExchangeApiUrl", 			Optional, False, TEXT, "", NEOLOAD_DATA_EXCHANGE_API_URL_DESCRIPTION, ALWAYS_VALID),
	NeoLoadDataExchangeApiKey("dataExchangeApiKey", 			Optional, False, TEXT, "", NEOLOAD_DATA_EXCHANGE_API_KEY_DESCRIPTION, ALWAYS_VALID),
	TLSInsecure("tls.insecure", 			           			Optional, False, TEXT, "", TLS_INSECURE_KEY_DESCRIPTION, BOOLEAN_VALIDATOR);

	private final String name;
	private final Option.OptionalRequired optionalRequired;
	private final Option.AppearsByDefault appearsByDefault;
	private final ActionParameter.Type type;
	private final String defaultValue;
	private final String description;
	private final ArgumentValidator argumentValidator;

	AppDynamicsOption(final String name,
					  final Option.OptionalRequired optionalRequired,
					  final Option.AppearsByDefault appearsByDefault,
					  final ActionParameter.Type type,
					  final String defaultValue,
					  final String description,
					  final ArgumentValidator argumentValidator) {
		this.name = checkNotNull(name);
		this.optionalRequired = checkNotNull(optionalRequired);
		this.appearsByDefault = checkNotNull(appearsByDefault);
		this.type = checkNotNull(type);
		this.defaultValue = checkNotNull(defaultValue);
		this.description = checkNotNull(description);
		this.argumentValidator = checkNotNull(argumentValidator);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Option.OptionalRequired getOptionalRequired() {
		return optionalRequired;
	}

	@Override
	public Option.AppearsByDefault getAppearsByDefault() {
		return appearsByDefault;
	}

	@Override
	public ActionParameter.Type getType() {
		return type;
	}

	@Override
	public String getDefaultValue() {
		return defaultValue;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public ArgumentValidator getArgumentValidator() {
		return argumentValidator;
	}
}
