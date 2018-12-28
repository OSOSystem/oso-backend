package org.oso.core.services.impl

import org.oso.core.services.MailService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.MailException
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.JavaMailSenderImpl
import org.springframework.stereotype.Service

@Service
class DefaultMailService
    /**
    * @constructor      Creates a default mail service.
    * @param mailSender Spring creates this bean automatically when it is not defined.
     *                  It takes the mail properties as program argument or from the application.yml.
     *                  Therefore it is annotated with @Suppress.
    */
    @Autowired constructor(
        @Value("\${email.from}")
        private val FROM: String,
        @Suppress
        private val mailSender: JavaMailSender
    ) : MailService {

    override fun send(participants: List<String>, subject: String, text: String) {
        require(FROM.isNotEmpty()) { "The email.from key should be set for an outgoing email." }

        if (mailSender !is JavaMailSenderImpl) {
            return
        }

        mailSender.javaMailProperties["mail.smtp.ssl.enable"] = "true"

        participants
            .map { participant ->
                SimpleMailMessage().apply {
                    setFrom(FROM)
                    setTo(participant)
                    setSubject(subject)
                    setText(text)
                }
            }
            .forEach { mail ->
                try {
                    mailSender.send(mail)
                    LOGGER.info("Mail was sent to {}.", mail)
                } catch (e: MailException) {
                    LOGGER.error("{}", e.localizedMessage, e)
                }
            }
    }

    companion object {
        private val LOGGER: Logger = LoggerFactory.getLogger(DefaultHelpProviderService::class.java)
    }
}