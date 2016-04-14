package com.redhat.gss.bpms.base;

import javax.ejb.Singleton;
import javax.ejb.Startup;

import org.kie.api.runtime.manager.RuntimeManager;
import org.kie.internal.runtime.manager.RuntimeManagerRegistry;

@Startup
@Singleton
public class RuntimeService {

	private static final String DEPLOYMENT_ID = "org.kie.example:project1:1.0.0-SNAPSHOT";

	public RuntimeManager getManager() {
		return RuntimeManagerRegistry.get().getManager(DEPLOYMENT_ID);
	}

}
