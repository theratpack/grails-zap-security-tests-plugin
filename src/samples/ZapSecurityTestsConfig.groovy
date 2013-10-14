import org.zaproxy.clientapi.core.Alert

zap {
    // Absolute path where ZAP is installed
    installDir = '/opt/zaproxy'

    // Address the proxy will bind
    proxyHost = 'localhost'

    // Port the proxy will listen
    proxyPort = 8090

    // System properties the plugin will set with proxy host and port values
    // to allow you to configure the functional tests to use the ZAP proxy
    proxyHostSystemProperty = 'ZAP_PROXY_HOST'
    proxyPortSystemProperty = 'ZAP_PROXY_PORT'

    // Subdirectory of test reports dir where ZAP sessions will be stored
    reportsDir = 'zap'

    // ignoredAlerts specify a collection of alerts (instances of
    // org.zaproxy.clientapi.core.Alert) that will be ignored if reported
    // by ZAP.
    //
    // Missing elements match everything, so the following collection of
    // ignoredAlerts will ignore all alerts of risk Low or Informational:
    //
    //   ignoredAlerts = [
    //       new Alert(null, null, Alert.Risk.Low, null),
    //       new Alert(null, null, Alert.Risk.Informational, null)
    //   ]
    //
    // Another example to ignore three specific security alerts:
    //
    //   ignoredAlerts = [
    //       new Alert('X-Content-Type-Options header missing', null),
    //       new Alert('X-Frame-Options header not set', null),
    //       new Alert('Content-type header missing', null)
    //   ]
    ignoredAlerts = []

    // requiredAlerts specify a collection of alerts (instances of
    // org.zaproxy.clientapi.core.Alert) that will fail if not present.
    requiredAlerts = []

    // Timeout in millisecond the proxy will wait for ZAP to start
    timeout = 10000

    // Enable debug in the REST based API interactions with ZAP
    debug = false
}