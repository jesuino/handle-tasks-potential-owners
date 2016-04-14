package com.redhat.gss.bpms.listener;

import java.util.List;

import org.jbpm.services.task.commands.TaskContext;
import org.jbpm.services.task.events.DefaultTaskEventListener;
import org.jbpm.services.task.impl.model.TaskImpl;
import org.kie.api.task.TaskEvent;
import org.kie.api.task.model.OrganizationalEntity;
import org.kie.internal.task.api.model.InternalPeopleAssignments;

public class ChangePotentialOwnerListener extends DefaultTaskEventListener {
	
	
	@Override
	public void afterTaskAddedEvent(TaskEvent event) {
		TaskImpl t = (TaskImpl) event.getTask();
		TaskContext taskContext = (TaskContext) event.getTaskContext();
		// will always remove JOHN
		removePotentialOwner(taskContext, t, "john");
	}
	
	public void removePotentialOwner(TaskContext taskContext, TaskImpl task, String userId) {
		List<OrganizationalEntity> excludedOwners = ((InternalPeopleAssignments) task
				.getPeopleAssignments()).getExcludedOwners();
		OrganizationalEntity user = taskContext.getTaskIdentityService().getOrganizationalEntityById(userId);
		excludedOwners.add(user);
		((InternalPeopleAssignments) task.getPeopleAssignments())
				.setExcludedOwners(excludedOwners);
	}
}
