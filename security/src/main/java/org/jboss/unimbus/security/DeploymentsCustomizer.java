package org.jboss.unimbus.security;

import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.unimbus.condition.IfClassPresent;
import org.jboss.unimbus.events.LifecycleEvent;
import org.jboss.unimbus.servlet.DeploymentMetaData;
import org.jboss.unimbus.servlet.Deployments;
import org.jboss.unimbus.servlet.HttpConstraintMetaData;
import org.jboss.unimbus.servlet.ServletMetaData;
import org.jboss.unimbus.servlet.ServletSecurityMetaData;

/**
 * Created by bob on 1/18/18.
 */
@ApplicationScoped
@IfClassPresent("org.jboss.unimbus.servlet.Deployments")
public class DeploymentsCustomizer {

    void customize(@Observes LifecycleEvent.Initialize event) {
        System.err.println("firing customizer");
        this.deployments.stream().forEach(this::customize);
    }

    void customize(DeploymentMetaData deployment) {
        if (deployment.isManagement() && this.managementSecurity.isPresent()) {
            deployment.addAuthMethod(managementSecurity.get().toUpperCase());
            for (ServletMetaData each : deployment.getServlets()) {
                each.setSecurity(
                        new ServletSecurityMetaData()
                        .setHttpConstraint(new HttpConstraintMetaData()
                                                   .setEmptyRoleSemantic(ServletSecurityMetaData.EmptyRoleSemantic.DENY)
                        )
                );
            }

        }
    }

    @Inject
    Deployments deployments;

    @Inject
    @ConfigProperty(name="web.management.security")
    Optional<String> managementSecurity;

    @Inject
    @Any
    Instance<Security> securities;


}