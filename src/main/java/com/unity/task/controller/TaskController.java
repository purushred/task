package com.unity.task.controller;

import com.unity.task.domain.Task;
import com.unity.task.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class TaskController {

	@Autowired
	private TaskService taskService;

	@RequestMapping("/data")
	private ResponseEntity<?> postData(@Valid @RequestBody Task task) {

		taskService.postData(task);
		return ResponseEntity.ok(task);
	}
}
