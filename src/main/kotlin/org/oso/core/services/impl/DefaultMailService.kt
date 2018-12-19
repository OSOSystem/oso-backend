package org.oso.core.services.impl

import org.oso.core.services.MailService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.JavaMailSenderImpl
import org.springframework.stereotype.Service

@Service
class DefaultMailService
    /**
    * @constructor
    * @param mailSender Spring creates this bean automatically when it is not defined.
     *                  It takes the mail properties as program argument or from the application.yml.
    */
    @Autowired constructor(private val mailSender: JavaMailSender) : MailService {
    private val FROM = "kontakt@ososystem.de"

    override fun send(participants: List<String>, subject: String, text: String) {
        mailSender as JavaMailSenderImpl
        mailSender.javaMailProperties.put("mail.smtp.ssl.enable", "true")

        participants
            .map { participant ->
                val message = SimpleMailMessage()
                with(message) {
                    setFrom(FROM)
                    setTo(participant)
                    setSubject(subject)
                    setText(text)
                }
                message
            }
            .forEach { mail ->
                mailSender.send(mail)
            }
    }
}