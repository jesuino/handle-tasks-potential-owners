package com.redhat.gss.bpms.resource;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import org.kie.api.runtime.manager.RuntimeEngine;
import org.kie.api.runtime.manager.RuntimeManager;
import org.kie.api.task.TaskService;
import org.kie.api.task.model.OrganizationalEntity;
import org.kie.api.task.model.Task;
import org.kie.internal.runtime.manager.context.EmptyContext;
import org.kie.internal.task.api.InternalTaskService;
import org.kie.internal.task.api.model.InternalPeopleAssignments;

import com.redhat.gss.bpms.base.RuntimeService;

@Path("")
@Stateless
public class UserTaskResource {
	
	@EJB
	RuntimeService service;

	@POST
	@Path("/add")
	public void add(@QueryParam("taskId") Long taskId,
			@QueryParam("userId") String userId) {
		RuntimeManager manager = service.getManager();
		RuntimeEngine runtime = manager.getRuntimeEngine(EmptyContext
				.get());
		TaskService taskService = runtime.getTaskService();
        Task task = taskService.getTaskById(taskId);
        List<OrganizationalEntity> potentialOwners = ((InternalPeopleAssignments)task.getPeopleAssignments()).getPotentialOwners();
        OrganizationalEntity user = ((InternalTaskService) taskService).getOrganizationalEntityById(userId);
        potentialOwners.add(user);
        ((InternalPeopleAssignments)task.getPeopleAssignments()).setPotentialOwners(potentialOwners);
        manager.disposeRuntimeEngine(runtime);
	}

	@POST
	@Path("/remove")
	public void remove(@QueryParam("taskId") Long taskId,
			@QueryParam("userId") String userId) {
		RuntimeManager manager = service.getManager();
		RuntimeEngine runtime = manager.getRuntimeEngine(EmptyContext
				.get());
		TaskService taskService = runtime.getTaskService();
		Task task = taskService.getTaskById(taskId);
		List<OrganizationalEntity> excludedOwners = ((InternalPeopleAssignments) task.getPeopleAssignments()).getExcludedOwners();
		OrganizationalEntity user = ((InternalTaskService) taskService).getOrganizationalEntityById(userId);
		excludedOwners.add(user);
		((InternalPeopleAssignments) task.getPeopleAssignments()).setExcludedOwners(excludedOwners);
		manager.disposeRuntimeEngine(runtime);
	}
}