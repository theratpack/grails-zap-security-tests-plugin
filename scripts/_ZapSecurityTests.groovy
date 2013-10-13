/*
 * ======================================================================
 *
 *    _ \   __|  _ \   __|  _ \ 
 *   (   |\__ \ (   | (    (   |    
 *  \___/ ____/\___/ \___|\___/ 
 *
 * Copyright (c) 2013 OSOCO. http://www.osoco.
 *
 * ======================================================================
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import groovy.util.ConfigSlurper

import org.apache.tools.ant.BuildException
import org.zaproxy.clientapi.core.ClientApi

includeTargets << grailsScript("_GrailsEvents")

def zapConfig
def zapClient
boolean zapTimeoutExceeded 

target('startZap': "Start the OWASP ZAP Proxy") {
    depends(zapConfiguration)
    argsMap['daemon'] ?  startZapDaemon() : startZapUI()
}

target('stopZap': "Stop the OWASP ZAP Proxy") {
    depends(zapConfiguration)
    stopZapProcess()
}

target('startZapUI': "Start the OWASP ZAP Proxy in UI mode") {
    // depends(zapConfiguration)
    event('StatusFinal', ['Starting ZAP Proxy in UI mode...'])
    startZapProcess(false)
}

target('startZapDaemon': "Start the OWASP ZAP Proxy in daemon mode") {
    depends(zapConfiguration)
    event('StatusFinal',['Starting ZAP Proxy in daemon mode...'])
    startZapProcess(true)
}

target('zapConfiguration': 'Parse ZAP configuration') {
    if (!zapConfig) {
	event('StatusFinal', ['Configuring ZAP Security Tests plugin...'])
	def configPath = "${grailsSettings.baseDir}/grails-app/conf/ZapSecurityTestsConfig.groovy"
	zapConfig = new ConfigSlurper().parse(new URL("file://${configPath}"))
    }
}

initClient = {
    if (!zapClient) {
	zapClient = new ClientApi(zapConfig.zap.proxyHost, zapConfig.zap.proxyPort, zapConfig.zap.debug)
    }
}

startZapProcess = { daemonMode ->
    def zapAddress = zapConfig.zap.proxyHost
    def zapPort = zapConfig.zap.proxyPort
    def zapInstallDir = zapConfig.zap.installDir
    def zapTimeout = zapConfig.zap.timeout
    ant.path(id: 'zapClasspath', { pathelement(location: "$zapInstallDir/zap.jar") })
    ant.java(
            classname: 'org.zaproxy.zap.ZAP', 
            classpathRef: 'zapClasspath', 
            fork: true, spawn: true, dir: zapInstallDir) {
	if (daemonMode) {
	    arg(value: '-daemon')
	}
    }
    event('StatusFinal', ['Waiting ZAP Proxy to start...'])
    ant.waitfor(maxwait: zapTimeout, maxwaitunit: 'millisecond', timeoutproperty: 'zapTimeoutExceeded') {
	socket(server: zapAddress, port: zapPort)
    }
    if (zapTimeoutExceeded) {
	def msg = "ZAP proxy dit not start in ${zapTimeout}ms or it is not listening at the proxy port $zapPort"
	event('StatusError', [msg])
	exit 1
    } else {
	event('StatusUpdate', ["ZAP proxy started and listening at proxy port $zapPort"])
    }
}

stopZapProcess = {
    event('StatusFinal', ["Stopping OWASP ZAP Proxy..."])
    try {
	initClient()
	zapClient.stopZap()
    } catch (Exception e) {
	throw new BuildException(e)
    }
}

spiderUrl = { url ->
    event('StatusFinal', ["Spidering [$url]..."])
    try {
	initClient()
	zapClient.spiderUrl(url)
    } catch (Exception e) {
	throw new BuildException(e)
    }
}

activeScanUrl = { url ->
    event('StatusFinal', ["Active scanning [$url]..."])
    try {
	initClient()
	zapClient.activeScanUrl(url)
    } catch (Exception e) {
	throw new BuildException(e)
    }
}

checkAlerts = {
    event('StatusFinal', ["Checking ZAP alerts..."])
    initClient()
    def ignoredAlerts = zapConfig.zap.ignoredAlerts
    def requiredAlerts = zapConfig.zap.requiredAlerts
    zapClient.checkAlerts(ignoredAlerts, requiredAlerts)
}

storeSession = {
    def sessionName = "zapReport-${new Date().format('yyyy-MM-dd-HH-mm-ss')}"
    def zapReportsDir = "${grailsSettings.testReportsDir}/${zapConfig.zap.reportsDir}"
    ant.mkdir(dir: "${zapReportsDir}")
    saveZapSession("${grailsSettings.baseDir}/${zapReportsDir}/${sessionName}")
}

saveZapSession = { name ->
    event('StatusFinal', ["Storing ZAP session at $name..."])
    initClient()
    zapClient.saveSession(name)
}

setZapProxyProperties = {
    zapConfiguration()
    System.setProperty(zapConfig.zap.proxyHostSystemProperty, zapConfig.zap.proxyHost)
    System.setProperty(zapConfig.zap.proxyPortSystemProperty, "${zapConfig.zap.proxyPort}")
}
