package com.redhat.gss.bpms.listener;

import java.util.List;
import java.util.logging.Logger;

import org.jbpm.ruleflow.instance.RuleFlowProcessInstance;
import org.jbpm.services.task.commands.TaskContext;
import org.jbpm.services.task.events.DefaultTaskEventListener;
import org.jbpm.services.task.impl.model.TaskImpl;
import org.jbpm.services.task.impl.model.UserImpl;
import org.kie.api.runtime.manager.RuntimeEngine;
import org.kie.api.runtime.manager.RuntimeManager;
import org.kie.api.runtime.process.ProcessInstance;
import org.kie.api.task.TaskEvent;
import org.kie.api.task.model.OrganizationalEntity;
import org.kie.api.task.model.PeopleAssignments;
import org.kie.api.task.model.User;
import org.kie.internal.runtime.manager.RuntimeManagerRegistry;
import org.kie.internal.runtime.manager.context.ProcessInstanceIdContext;
import org.kie.internal.task.api.TaskIdentityService;
import org.kie.internal.task.api.model.InternalPeopleAssignments;

public class ChangePotentialOwnerListener extends DefaultTaskEventListener {

	private static final String P_USR_TO_REMOVE = "ownerToRemove";

	private static final String P_USR_TO_ADD = "ownerToAdd";

	Logger logger = Logger.getLogger(this.getClass().getName());

	private RuntimeManagerRegistry registry = RuntimeManagerRegistry.get();

	@Override
	public void beforeTaskAddedEvent(TaskEvent event) {
		TaskImpl t = (TaskImpl) event.getTask();
		TaskContext taskContext = (TaskContext) event.getTaskContext();
		String ownerToRemove = readProcessVariable(t, P_USR_TO_REMOVE);
		String ownerToAdd = readProcessVariable(t, P_USR_TO_ADD);
		removePotentialOwner(taskContext, t, ownerToRemove);
		addPotentialOwner(taskContext, t, ownerToAdd);
	
	}

	public void removePotentialOwner(TaskContext ctx, TaskImpl t, String usrId) {
		PeopleAssignments pplAssignments = t.getPeopleAssignments();
		InternalPeopleAssignments assignments = (InternalPeopleAssignments) pplAssignments;
		List<OrganizationalEntity> excluded = assignments.getExcludedOwners();
		TaskIdentityService identity = ctx.getTaskIdentityService();
		OrganizationalEntity usr = identity.getOrganizationalEntityById(usrId);
		if (usr == null) {
			logger.warning(usrId + " not a potential owner of task "
					+ t.getName());
			return;
		}
		logger.warning("Removing " + usrId + " from task " + t.getName());
		System.out.println(usr.getClass());
		excluded.add(usr);
		assignments.setExcludedOwners(excluded);
		assignments.getPotentialOwners().remove(usr);
	}

	public void addPotentialOwner(TaskContext ctx, TaskImpl t, String usrId) {
		logger.warning("Adding " + usrId + " to task id " + t.getName());
		PeopleAssignments pplAssignments = t.getPeopleAssignments();
		TaskIdentityService identity = ctx.getTaskIdentityService();
		OrganizationalEntity usr = identity.getOrganizationalEntityById(usrId);
		if (usr == null) {
			logger.warning("User " + usrId + " not created. Creating it...");
			usr = new UserImpl(usrId);
			identity.addUser((User) usr);
		}
		pplAssignments.getPotentialOwners().add(usr);
	}

	private String readProcessVariable(TaskImpl t, String var) {
		String deploymentId = t.getTaskData().getDeploymentId();
		RuntimeManager manager = registry.getManager(deploymentId);
		Long piid = t.getTaskData().getProcessInstanceId();
		ProcessInstanceIdContext ctx = ProcessInstanceIdContext.get(piid);
		RuntimeEngine engine = manager.getRuntimeEngine(ctx);
		ProcessInstance pi = engine.getKieSession().getProcessInstance(piid);
		RuleFlowProcessInstance rfpi = (RuleFlowProcessInstance) pi;
		return (String) rfpi.getVariable(var);
	}
}
