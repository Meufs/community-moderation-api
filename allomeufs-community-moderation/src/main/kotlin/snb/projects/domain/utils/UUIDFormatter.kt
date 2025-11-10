package snb.projects.domain.utils

import jakarta.validation.constraints.NotNull
import io.quarkus.logging.Log;
import java.util.*

object UUIDFormatter {
    fun formatUUIDSequence(uuidToFormat: UUID?, joiner: @NotNull String?): String {
        requireNotNull(uuidToFormat) { "UUID can not be null" }
        Log.info("Formatting UUIDSequence.")
        return java.lang.String.join(
            joiner,
            *uuidToFormat.toString().split("-".toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray())
    }
}