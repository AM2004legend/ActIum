package com.actum.ui.gate

import com.actum.model.Envelope
import com.actum.model.ProposedAction
import com.actum.model.Routed
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

/**
 * One held-out result as a JSON line, the same for the rules (on the computer or the
 * phone) and for Gemma, so one script scores both. Proposals only: nothing here
 * validates or runs anything. Cases are synthetic, so their fields may be logged.
 */
object HeldOut {

    /** A rule's reading: null [actions] when no rule read the sentence. */
    fun line(id: String, path: String, actions: List<ProposedAction>?, millis: Long): String =
        JsonObject(
            mapOf(
                "id" to JsonPrimitive(id),
                "path" to JsonPrimitive(path),
                "answered" to JsonPrimitive(actions != null),
                "actions" to actions(actions.orEmpty()),
                "ms" to JsonPrimitive(millis),
            )
        ).toString()

    /** Gemma's reading, with whether its answer had to be repaired or asked again. */
    fun line(id: String, path: String, routed: Routed): String {
        val envelope = routed.envelope
        val found = (envelope as? Envelope.Actions)?.actions.orEmpty()
        return JsonObject(
            mapOf(
                "id" to JsonPrimitive(id),
                "path" to JsonPrimitive(path),
                "answered" to JsonPrimitive(envelope is Envelope.Actions),
                "invalid" to JsonPrimitive((envelope as? Envelope.Invalid)?.reason),
                "repaired" to JsonPrimitive((envelope as? Envelope.Actions)?.repaired == true),
                "retried" to JsonPrimitive(routed.answers.size > 1),
                "actions" to actions(found),
                "ms" to JsonPrimitive(routed.millis),
            )
        ).toString()
    }

    private fun actions(actions: List<ProposedAction>) = JsonArray(
        actions.map { JsonObject(mapOf("intent" to JsonPrimitive(it.intent.name), "fields" to it.fields)) }
    )
}
