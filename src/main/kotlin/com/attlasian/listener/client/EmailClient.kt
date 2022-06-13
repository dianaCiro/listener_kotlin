package com.attlasian.listener.client

import com.amazonaws.util.IOUtils
import com.attlasian.listener.model.Task
import freemarker.template.Configuration
import freemarker.template.TemplateException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.core.io.ByteArrayResource
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.messaging.MessagingException
import org.springframework.stereotype.Component
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.io.StringWriter
import javax.mail.internet.MimeMessage

@Component
class EmailClient {

    @Autowired
    private lateinit var javaMailSender: JavaMailSender

    @Autowired
    @Qualifier("freeMarker")
    private lateinit var configuration: Configuration

    @Throws(MessagingException::class, IOException::class, TemplateException::class)
    fun sendEmail(task: Task, to: String, subject: String, file: File) {
        val mimeMessage: MimeMessage = javaMailSender.createMimeMessage()
        val helper = MimeMessageHelper(mimeMessage, true)
        helper.setSubject(subject)
        helper.setTo(to)

        helper.addAttachment(
            file.name,
            ByteArrayResource(IOUtils.toByteArray(FileInputStream(file)))
        )
        val emailContent = getEmailContent(task)
        helper.setText(emailContent, true)
        javaMailSender.send(mimeMessage)
    }

    @Throws(IOException::class, TemplateException::class)
    fun getEmailContent(task: Task): String {
        val stringWriter = StringWriter()
        val model: MutableMap<String, Any> = mutableMapOf()

        model["description"] = task.description
        model["endDate"] = task.endDate

        configuration.getTemplate("email.ftlh").process(model, stringWriter)
        return stringWriter.getBuffer().toString()
    }
}
