package com.redhat.gss.bpms.remote.api.tests;

import java.util.List;

import org.junit.Test;
import org.kie.api.task.TaskService;
import org.kie.api.task.model.OrganizationalEntity;
import org.kie.api.task.model.Task;

import com.redhat.gss.bpms.remote.RemoteAPITestBase;

public class TaskPotentialOwnersTest extends RemoteAPITestBase {

	private static final long TASK_ID = 4;

	@Test
	public void doTest() throws Exception {
		TaskService taskService = getEngine(APP_URL, USER, PASSWORD, "").getTaskService();
		Task task = taskService.getTaskById(TASK_ID);
		List<OrganizationalEntity> org = task.getPeopleAssignments()
				.getPotentialOwners();
		for (OrganizationalEntity ent : org) {
			System.out.println("org: " + ent.getId());
		}
	}
}
