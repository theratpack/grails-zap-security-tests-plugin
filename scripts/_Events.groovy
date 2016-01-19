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

includeTargets << new File("${zapSecurityTestsPluginDir}/scripts/_ZapSecurityTests.groovy")

boolean runningSecurityTests = false
boolean securityTestsFailed
def originalFunctionalTestPhaseCleanUp

eventTestPhaseStart = { phaseName ->
    if ((phaseName == 'functional') && argsMap['zap'] && !runningSecurityTests) {
        event('StatusFinal', ['Running Security Tests using OWASP ZAP Proxy...'])
        runningSecurityTests = true
        customFunctionalTestPhaseCleanUp()
        setZapProxyProperties()
        argsMap.daemon ? startZapDaemon() : startZapUI()
    }
}

eventTestPhaseEnd = { phaseName ->
    if ((phaseName == 'functional') && runningSecurityTests) {
        def baseUrl = getTargetUrl()
        spiderUrl(baseUrl)
        activeScanUrl(baseUrl)
        storeSession()

      	try {
            checkAlerts()
        } catch (e) {
            securityTestsFailed = true
            throw e
        } finally {
            zapTestPhaseCleanUp()

            String label = securityTestsFailed ? "Security Tests FAILED" : "Security Tests PASSED"
            String msg = ""
            if (createTestReports) {
                event("TestProduceReports", [])
                msg += " - ZAP session stored"
            }
            if (securityTestsFailed) {
                grailsConsole.error(label, msg)
            } else {
                grailsConsole.addStatus("$label$msg")
            }
        }
    }
}

zapTestPhaseCleanUp = {
    stopZapProcess()
    // originalFunctionalTestPhaseCleanUp()
    runningSecurityTests = false
}

voidFunctionalTestPhaseCleanUp = {}

customFunctionalTestPhaseCleanUp = {
    originalFunctionalTestPhaseCleanUp = owner.functionalTestPhaseCleanUp
    owner.functionalTestPhaseCleanUp = voidFunctionalTestPhaseCleanUp
}
