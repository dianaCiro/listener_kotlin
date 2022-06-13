package com.attlasian.listener.sqs

import com.amazonaws.services.s3.AmazonS3
import com.attlasian.listener.client.EmailClient
import com.attlasian.listener.model.Task
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener
import org.springframework.stereotype.Component
import java.io.File

@Component
class Listener {

    @Autowired
    @Qualifier("instanceS3")
    private lateinit var amazonS3: AmazonS3

    @Autowired
    private lateinit var emailClient: EmailClient

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @SqsListener(value = ["\${cloud.aws.queue.name}"])
    fun receiveMessage(message: String) {
        var task: Task = objectMapper.readValue(message, Task::class.java)
        println("the message was received:  ${task.description}")
        publicMessageToS3(task)
    }

    fun publicMessageToS3(task: Task) {
        val fileName = "task_${task.id}.txt"
        var file = File(fileName)
        file.writeText(objectMapper.writeValueAsString(task))
        emailClient.sendEmail(task, "kathe9463@gmail.com", "Task report", file)
        amazonS3.putObject("dashboard", fileName, file)
    }
}
