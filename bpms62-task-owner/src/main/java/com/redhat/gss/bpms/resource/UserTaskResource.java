package com.redhat.gss.bpms.resource;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import org.kie.api.runtime.manager.Context;
import org.kie.api.runtime.manager.RuntimeEngine;
import org.kie.api.runtime.manager.RuntimeManager;
import org.kie.api.task.TaskService;
import org.kie.api.task.model.OrganizationalEntity;
import org.kie.api.task.model.PeopleAssignments;
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
			@QueryParam("userId") String id) {
		RuntimeManager manager = service.getManager();
		Context<String> context = EmptyContext.get();
		RuntimeEngine runtime = manager.getRuntimeEngine(context);
		TaskService taskService = runtime.getTaskService();
		Task task = taskService.getTaskById(taskId);
		PeopleAssignments pplAssignments = task.getPeopleAssignments();
		InternalPeopleAssignments assignments = (InternalPeopleAssignments) pplAssignments;
		List<OrganizationalEntity> owners = assignments.getPotentialOwners();
		InternalTaskService iTaskService = (InternalTaskService) taskService;
		OrganizationalEntity usr = iTaskService.getOrganizationalEntityById(id);
		owners.add(usr);
		assignments.setPotentialOwners(owners);
		manager.disposeRuntimeEngine(runtime);
	}

	@POST
	@Path("/remove")
	public void remove(@QueryParam("taskId") Long taskId,
			@QueryParam("userId") String id) {
		RuntimeManager manager = service.getManager();
		Context<String> ctx = EmptyContext.get();
		RuntimeEngine runtime = manager.getRuntimeEngine(ctx);
		TaskService taskService = runtime.getTaskService();
		Task task = taskService.getTaskById(taskId);
		System.out.println(task);
		PeopleAssignments peopleAssignments = task.getPeopleAssignments();
		InternalPeopleAssignments assignments = (InternalPeopleAssignments) peopleAssignments;
		List<OrganizationalEntity> excluded = assignments.getExcludedOwners();
		InternalTaskService iTaskService = (InternalTaskService) taskService;
		OrganizationalEntity usr = iTaskService.getOrganizationalEntityById(id);
		excluded.add(usr);
		assignments.setExcludedOwners(excluded);
		assignments.getPotentialOwners().remove(usr);
		manager.disposeRuntimeEngine(runtime);
	}
}