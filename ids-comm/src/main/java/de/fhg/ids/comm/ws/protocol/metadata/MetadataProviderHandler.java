/*-
 * ========================LICENSE_START=================================
 * ids-comm
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
package de.fhg.ids.comm.ws.protocol.metadata;

import com.google.protobuf.MessageLite;
import de.fhg.aisec.ids.messages.Idscp.ConnectorMessage;
import de.fhg.aisec.ids.messages.Idscp.MedadataExchange;
import de.fhg.ids.comm.ws.protocol.fsm.Event;

public class MetadataProviderHandler extends MetadataHandler {

  public MessageLite response(Event e) {
    this.sessionID = e.getMessage().getId();

    return ConnectorMessage.newBuilder()
        .setId(++this.sessionID)
        .setType(ConnectorMessage.Type.META_RESPONSE)
        .setMetadataExchange(
            MedadataExchange.newBuilder().setRdfdescription(generateMetaDataRDF()).build())
        .build();
  }
}
