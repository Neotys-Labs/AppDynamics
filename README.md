<p align="center"><img src="/screenshots/AppDynamicsLogo.png" width="40%" alt="AppDynamics Logo" /></p>

# AppDynamics Integration for NeoLoad

## Overview

This Advanced Action allows you to integrate [NeoLoad](https://www.neotys.com/neoload/overview) with [AppDynamics](https://www.appdynamics.com/) in order to correlate data from AppDynamics with NeoLoad. 

You can specify which metrics you want to retrieve from NewRelic among all available metrics.


| Property | Value |
| ----------------    | ----------------   |
| Maturity | Stable |
| Author | Neotys |
| License           | [BSD Simplified](https://www.neotys.com/documents/legal/bsd-neotys.txt) |
| NeoLoad         | From version 6.3|
| AppDynamics | Tested with versions 4.4 and 4.5
| Requirements | <ul><li>License FREE edition, or Enterprise edition, or Professional with Integration & Advanced Usage</li><li>AppDynamics account with Infrastructures and Plugins</li></ul>|
| Bundled in NeoLoad | No |
| Download Binaries    | See the [latest release](https://github.com/Neotys-Labs/AppDynamics/releases/latest)|


## Installation

1. Download the [latest release](https://github.com/Neotys-Labs/AppDynamics/releases/latest).
1. Read the NeoLoad documentation to see [How to install a custom Advanced Action](https://www.neotys.com/documents/doc/neoload/latest/en/html/#25928.htm).

<p align="center"><img src="/screenshots/AppDynamicsActions.png" alt="AppDynamics Advanced Action" /></p>

## Set-up

Once installed, how to use in a given NeoLoad project:

1. Create an "appdynamics" User Path .
2. Insert the "AppDynamics Monitoring" Custom Action in the **Actions** container (Custom Action is inside Advanced > APM > AppDynamics).

<p align="center"><img src="/screenshots/AppDynamicsUserPath.png" alt="AppDynamics User Path" /></p>

3. Select the **Actions** container and set a pacing duration of 60 seconds.

<p align="center"><img src="/screenshots/ActionsContainerPacing.png" alt="Action's Pacing" /></p>

4. Select the **Actions** container and set the "Reset user session and emulate new browser between each iteration" runtime parameters  to "No".

<p align="center"><img src="/screenshots/ActionsContainerResetIterationNo.png" alt="Action's Runtime parameters" /></p>

5. Create a Population "PopulationAppDynamics" that contains 100% of "appdynamics" User Path.

<p align="center"><img src="/screenshots/AppDynamicsPopulation.png" alt="AppDynamics Population" /></p>

6. In the **Runtime** section, select your scenario, select the "PopulationAppDynamics" population and define a constant load of 1 user for the full duration of the load test.

<p align="center"><img src="/screenshots/AppDynamicsScenario.png" alt="Scenario" /></p>

7. Do not use multiple Load Generators. Good practice should be to keep only the local one.

8. Verify to have a license with "Integration & Advanced Usage".

<p align="center"><img src="/screenshots/LicenseIntegrationAndAdvancedUsage.png" alt="License with Integration & Advanced Usage" /></p>


## Parameters

| Name                     | Description       | Required/Optional|
| ---------------          | ----------------- |----------------- |
| appDynamicsURL          | The URL of AppDynamics Server like:<br> 'https://\<accountId\>.saas.appdynamics.com' for Saas or<br>'http://\<hostname\>:8090' for OnPremise. |Required|
| appDynamicsApplicationName          | The AppDynamics application name that will be monitored. |Required|
| appDynamicsAPIKey          |  AppDynamics API access token. It is only available from AppDynamics 4.5 or newer. See more at [AppDynamics API Clients](https://docs.appdynamics.com/display/PRO45/API+Clients). |Optional|
| appDynamicsAccountName | AppDynamics Account Name. It appears in the URL when going on AppDynamics SaaS like 'https://**\<accountId\>**.saas.appdynamics.com/' or "customer1" for AppDynamics OnPremise.<br>Required when argument 'appDynamicsAPIKey' is absent or empty. |Optional|
| appDynamicsUserName | AppDynamics User Name. It appears in **My Preference**/**My Account**.<br>Required when argument 'appDynamicsAPIKey' is absent or empty. |Optional|
| appDynamicsPassword | AppDynamics Password.<br>Required when argument 'appDynamicsAPIKey' is absent or empty.|Optional|
| appDynamicsMetricPaths | Specify the list of the AppDynamics metric paths (separated by a line break) to retrieve. See more details at [Metric Paths](#appDynamics-metric-paths)|Optional|
| proxyName | NeoLoad proxy name to access AppDynamics. |Optional|
| dataExchangeApiUrl          | Where the DataExchange server is located. Optional, by default it is: http://${NL-ControllerIp}:7400/DataExchange/v1/Service.svc/ |Optional|
| dataExchangeApiKey            | Identification key specified in NeoLoad. |Optional|

Examples of AppDynamics configuration in NeoLoad:
- For **SaaS** 
    <p align="center"><img src="/screenshots/AppDynamicsConfigurationSaaS.png" alt="AppDynamics Configuration Saas" /></p>
- For **OnPremise**
    <p align="center"><img src="/screenshots/AppDynamicsConfigurationOnPremise.png" alt="AppDynamics Configuration OnPremise" /></p>

## AppDynamics Metric Paths

- You can monitor more than one metric in the same AppDynamics Action. Metric paths are separated by a line break.

- How to get Metric Path from AppDynamics UI<br />
    - Go to the monitored application.
    - Click Metric Browser.
    - Select a metric that you want to monitor.
    - Right click and select 'Copy Full Path'.
    - Paste it in the appDynamicsMetricPaths parameter in NeoLoad.
    <p align="center"><img src="/screenshots/AppDynamicsMetricPaths.png" alt="AppDynamics Metric Paths" /></p>

- You can use the wildcards in Metric Path. The maximum metric number per Metric Path sent from AppDynamics is limited to 200. See more details at [Metric Path Wildcards](https://docs.appdynamics.com/display/PRO45/Metric+and+Snapshot+API#MetricandSnapshotAPI-usingwildcards)

- Default Metric Paths:<br />
    Application Infrastructure Performance|\*|Hardware Resources|CPU|User<br />
	Application Infrastructure Performance|\*|Hardware Resources|CPU|System<br />
	Application Infrastructure Performance|\*|Hardware Resources|CPU|%Idle<br />
	Application Infrastructure Performance|\*|Hardware Resources|Memory|\*<br />
	Application Infrastructure Performance|\*|Hardware Resources|Network|Incoming Errors<br />
	Application Infrastructure Performance|\*|Hardware Resources|Network|Outgoing Errors<br />
	Application Infrastructure Performance|\*|Hardware Resources|Disks|Reads/sec<br />
	Application Infrastructure Performance|\*|Hardware Resources|Disks|Writes/sec<br />
	Application Infrastructure Performance|\*|Agent|Business Transactions|\*<br />

## Analyse results in NeoLoad

- All the metrics retrieved from AppDynamics are available on the NeoLoad Controller (live during the test, and after the test is executed), in the **External Data** tab.
- The most recent metric values sent from AppDynamics are still a few minutes in the past compared to the current time. Some metric values are so missed at the end of NeoLoad tests.
<p align="center"><img src="/screenshots/AppDynamicsNeoLoadExternalDataGraphs.png" alt="NeoLoad Graphs External Data" /></p>

## NeoLoad Error Codes
* NL-APP_DYNAMICS_ACTION-01: Issue while parsing Advanced Action arguments.
* NL-APP_DYNAMICS_ACTION-02: Technical Error. See details for more information.
* NL-APP_DYNAMICS_ACTION-03: Failed to retrieve metrics data from AppDynamics. See details for more information.
* NL-APP_DYNAMICS_ACTION-04: Not enough delay between the two executions of the AppDynamics Advanced Action. Make sure to have at least 60 seconds pacing on the Actions container.  

## ChangeLog
* Version 1.0.0 (September 15, 2018): Initial release.
