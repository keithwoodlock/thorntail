/**
 * Copyright 2015-2016 Red Hat, Inc, and individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.wildfly.swarm.arquillian.adapter;

import org.jboss.arquillian.container.spi.client.container.DeployableContainer;
import org.jboss.arquillian.container.test.impl.enricher.resource.URIResourceProvider;
import org.jboss.arquillian.container.test.impl.enricher.resource.URLResourceProvider;
import org.jboss.arquillian.container.test.spi.client.deployment.AuxiliaryArchiveAppender;
import org.jboss.arquillian.container.test.spi.client.protocol.Protocol;
import org.jboss.arquillian.core.spi.LoadableExtension;
import org.jboss.arquillian.test.spi.enricher.resource.ResourceProvider;
import org.wildfly.swarm.arquillian.daemon.protocol.DaemonProtocol;
import org.wildfly.swarm.arquillian.resources.SwarmURIResourceProvider;
import org.wildfly.swarm.arquillian.resources.SwarmURLResourceProvider;

/**
 * @author Bob McWhirter
 */
public class WildFlySwarmExtension implements LoadableExtension {
    @Override
    public void register(ExtensionBuilder builder) {
        builder.service(Protocol.class, DaemonProtocol.class)
                .service(AuxiliaryArchiveAppender.class, WildFlySwarmDeploymentAppender.class)
                .service(DeployableContainer.class, WildFlySwarmContainer.class)
                .override(ResourceProvider.class, URLResourceProvider.class, SwarmURLResourceProvider.class)
                .override(ResourceProvider.class, URIResourceProvider.class, SwarmURIResourceProvider.class)
                .observer(WildFlySwarmObserver.class);
    }
}