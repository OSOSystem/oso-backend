package org.oso.core.services

/**
 * Everything related with mail is handled by this service.
 */
interface MailService {
    /**
     * Sends a mail to the given [participants].
     *
     * @param participants The people to send the email to
     * @param subject The subject of the mail
     * @param text The text of the mail
     */
    fun send(participants: List<String>, subject: String, text: String)
}