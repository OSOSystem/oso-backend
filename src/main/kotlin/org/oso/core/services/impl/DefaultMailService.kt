package org.oso.core.services.impl

import org.oso.core.services.MailService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
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
     *                   It takes the mail properties as program argument or from the application.yml.
     *                   Therefore it is annotated with @Suppress.
     */
    @Autowired constructor(
        @Suppress
        private val mailSender: JavaMailSender
    ) : MailService {

    override fun send(from: String, participants: List<String>, subject: String, text: String): List<String> {
        // workaround to enable ssl because the configuration in the application.yml does not work
        if (mailSender is JavaMailSenderImpl) {
            mailSender.javaMailProperties["mail.smtp.ssl.enable"] = "true"
        }

        val failedToDeliverParticipants = mutableListOf<String>()

        participants
            .map { participant ->
                SimpleMailMessage().apply {
                    setFrom(from)
                    setTo(participant)
                    setSubject(subject)
                    setText(text)
                }
            }
            .forEach { mail ->
                try {
                    mailSender.send(mail)
                    LOGGER.info("Mail was sent to {}", mail)
                } catch (e: MailException) {
                    mail.to?.let {
                        failedToDeliverParticipants += it
                    }

                    LOGGER.error("{}", e.localizedMessage, e)
                }
            }

        return failedToDeliverParticipants
    }

    companion object {
        private val LOGGER: Logger = LoggerFactory.getLogger(DefaultMailService::class.java)
    }
}