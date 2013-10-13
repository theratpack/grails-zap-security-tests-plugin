import org.zaproxy.clientapi.core.Alert

zap {
    installDir = '/opt/zaproxy'
    proxyHost = 'localhost'
    proxyPort = 8090
    proxyHostSystemProperty = 'ZAP_PROXY_HOST'
    proxyPortSystemProperty = 'ZAP_PROXY_PORT'
    reportsDir = 'zap'

    // ignoredAlerts alerts are ignored if reported
    // requiredAlerts will fail if not present
    //
    // Missing elements match everything, so the following collection of ignoredAlerts
    // will ignore all alerts of risk Low or Informational:
    //
    // ignoredAlerts = [
    // 	new Alert(null, null, Alert.Risk.Low, null),
    // 	new Alert(null, null, Alert.Risk.Informational, null)
    // ]
    //
    // Another example to ignore three specific security alerts:
    //
    // ignoredAlerts = [
    // 	new Alert('X-Content-Type-Options header missing', null),
    // 	new Alert('X-Frame-Options header not set', null),
    // 	new Alert('Content-type header missing', null)
    // ]
    requiredAlerts = []
    ignoredAlerts = []

    timeout = 10000
    debug = false
}