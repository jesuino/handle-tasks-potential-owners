Change a task potential owners list
--

This repository contains some examples to show how we can manipulate a task potential owner list.

What you will find here:

- `bpms62-handle-task-listener`: A task listener that reads process variables to manipulate the potential owners list;
- `bpms62-task-owner`: an incomplete web application to be deployed in the same EAP instance where business central is running and allow you to remove and add potential owners using a REST API 
- `bpms_proj_change-task-owner`:  A sample BPM Suite project to be used  along with the task listener;
- `TaskPotentialOwnersTest.java`: a snipet of a client application to retrieve the potential owners of a given task.
- 

### Notice that it makes use of some internal BPM Suite APIs, which means that this is a subject for change in future BPM Suite releases!

