package org.oso.core.services.external

/**
 * Responsible for sending out emails after an emergency emitted.
 */
interface MailService {
    /**
     * Sends an email to a list of [recipients].
     * @param recipients A list of people who receive an email.
     */
    fun send(recipients: List<String>)
}