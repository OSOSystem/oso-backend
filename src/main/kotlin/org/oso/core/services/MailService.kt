package org.oso.core.services

/**
 * Everything related with mail is handled by this service.
 */
interface MailService {
    /**
     * Sends a mail to the given [participants].
     * @param from The address to send the email from
     * @param participants The people to send the email to
     * @param subject The subject of the mail
     * @param text The text of the mail
     * @return A list containing all [participants] where the mail could not be delivered to
     */
    fun send(from: String, participants: List<String>, subject: String, text: String): List<String>
}