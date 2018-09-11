package com.neotys.appdynamics;

import com.google.common.base.Optional;
import com.neotys.action.argument.Option;
import com.neotys.extensions.action.Action;
import com.neotys.extensions.action.ActionParameter;
import com.neotys.extensions.action.engine.ActionEngine;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class AppDynamicsAction implements Action {
	@Override
	public String getType() {
		return Constants.CUSTOM_ACTION_TYPE;
	}

	@Override
	public List<ActionParameter> getDefaultActionParameters() {
		final List<ActionParameter> parameters = new ArrayList<>();
		for (final AppDynamicsOption option : AppDynamicsOption.values()) {
			if (Option.AppearsByDefault.True.equals(option.getAppearsByDefault())) {
				parameters.add(new ActionParameter(option.getName(), option.getDefaultValue(), option.getType()));
			}
		}
		return parameters;
	}

	@Override
	public Class<? extends ActionEngine> getEngineClass() {
		return AppDynamicsActionEngine.class;
	}

	@Override
	public Icon getIcon() {
		return Constants.CUSTOM_ACTION_ICON;
	}

	@Override
	public String getDescription() {
		return Constants.CUSTOM_ACTION_DESCRIPTION;
	}

	@Override
	public String getDisplayName() {
		return Constants.CUSTOM_ACTION_DISPLAY_NAME;
	}

	@Override
	public String getDisplayPath() {
		return Constants.CUSTOM_ACTION_DISPLAY_PATH;
	}

	@Override
	public Optional<String> getMinimumNeoLoadVersion() {
		return Constants.CUSTOM_ACTION_MINIMUM_VERSION;
	}

	@Override
	public Optional<String> getMaximumNeoLoadVersion() {
		return Constants.CUSTOM_ACTION_MAXIMIM_VERSION;
	}

	@Override
	public boolean getDefaultIsHit() {
		return false;
	}
}
