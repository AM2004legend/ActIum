package com.actum.ui.gate

import com.actum.model.Rules
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.File

/**
 * The rules alone over the held-out set, written to `build/heldout/rules.jsonl` for
 * `tools/heldout_score.py`. It reports; it does not grade, so a miss here is a number,
 * not a failure. The set must not be tuned against.
 */
class HeldOutRulesTest {

    @Test
    fun rulesOverTheHeldOutSet() {
        val cases = File("src/main/assets/heldout.jsonl").readLines().filter { it.isNotBlank() }
        val out = File("build/heldout/rules.jsonl").apply { parentFile?.mkdirs() }
        val lines = cases.map { line ->
            val case = Json.parseToJsonElement(line).jsonObject
            val text = case.getValue("text").jsonPrimitive.content
            val start = System.nanoTime()
            val rule = Rules.route(text)
            HeldOut.line(case.getValue("id").jsonPrimitive.content, "rules", rule?.let { listOf(it) }, (System.nanoTime() - start) / 1_000_000)
        }
        out.writeText(lines.joinToString("\n", postfix = "\n"))
        assertEquals(cases.size, lines.size)
    }
}
