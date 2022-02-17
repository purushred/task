package com.unity.task.service;

import com.unity.task.domain.Message;
import com.unity.task.domain.Task;
import com.unity.task.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class TaskService {

	@Autowired
	private TaskRepository taskRepository;

	@Autowired
	private KafkaTemplate<String, Message> kafkaTemplate;

	@Value(value = "${kafka.topic}")
	private String topicName;

	public TaskService() {
	}

	private void sendMessage(Task msg) {
		kafkaTemplate.send(topicName, msg.getMessage());
	}

	public void postData(Task task) {
		taskRepository.save(task);
		sendMessage(task);

	}
}
