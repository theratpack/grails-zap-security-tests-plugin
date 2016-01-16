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
class ZapSecurityTestsGrailsPlugin {

    def version = "0.1.3-SNAPSHOT"
    def grailsVersion = "2.0 > *"
    def author = 'The Rat Pack'
    def authorEmail = 'info@osoco.es'
    def title = 'OWASP Zap Security Tests Plugin'
    def description = 'Security tests for your Grails app using OWASP ZAP proxy'
    def documentation = 'http://grails.org/plugin/zap-security-tests'

    def license = 'APACHE'
    def organization = [ name: "The Rat Pack", url: "http://github.com/theratpack" ]
    def developers = [ [ name: "Rafael Luque" ], [ name: "Jose San Leandro"]]
    def scm = [url: "https://github.com/theratpack/grails-zap-security-tests-plugin/"]
    def issueManagement = [
        system: "GitHub", 
        url: "https://github.com/theratpack/grails-zap-security-tests-plugin/issues"
    ]
}
