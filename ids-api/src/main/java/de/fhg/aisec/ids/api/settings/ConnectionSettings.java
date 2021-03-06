/*-
 * ========================LICENSE_START=================================
 * ids-api
 * %%
 * Copyright (C) 2018 Fraunhofer AISEC
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * =========================LICENSE_END==================================
 */
package de.fhg.aisec.ids.api.settings;

import java.io.Serializable;

public final class ConnectionSettings implements Serializable {
  private static final long serialVersionUID = 1L;

  private final String integrityProtectionAndVerification;
  private final String authentication;
  private final String serviceIsolation;
  private final String integrityProtectionVerificationScope;
  private final String appExecutionResources;
  private final String dataUsageControlSupport;
  private final String auditLogging;
  private final String localDataConfidentiality;

  public ConnectionSettings() {
    integrityProtectionAndVerification = "1";
    authentication = "1";
    serviceIsolation = "1";
    integrityProtectionVerificationScope = "1";
    appExecutionResources = "1";
    dataUsageControlSupport = "1";
    auditLogging = "1";
    localDataConfidentiality = "1";
  }

  public String getIntegrityProtectionAndVerification() {
    return integrityProtectionAndVerification;
  }

  public String getAuthentication() {
    return authentication;
  }

  public String getServiceIsolation() {
    return serviceIsolation;
  }

  public String getIntegrityProtectionVerificationScope() {
    return integrityProtectionVerificationScope;
  }

  public String getAppExecutionResources() {
    return appExecutionResources;
  }

  public String getDataUsageControlSupport() {
    return dataUsageControlSupport;
  }

  public String getAuditLogging() {
    return auditLogging;
  }

  public String getLocalDataConfidentiality() {
    return localDataConfidentiality;
  }
}
