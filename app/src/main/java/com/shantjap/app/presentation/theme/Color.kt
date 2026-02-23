package com.shantjap.app.presentation.theme

import androidx.compose.ui.graphics.Color
import com.shantjap.app.domain.model.Mode

val RadhaPrimary = Color(0xFFFF8A3D)
val RadhaGlow = Color(0xFFFFC089)
val RadhaBackground = Color(0xFFFFF7F0)

val RamPrimary = Color(0xFF1F8A70)
val RamGlow = Color(0xFF7BDCB5)
val RamBackground = Color(0xFFF2FBF6)

val NeutralPrimary = Color(0xFF2A2A2A)
val NeutralSurface = Color(0xFFFFFFFF)
val NeutralBackground = Color(0xFFF7F5F2)
val NeutralOnSurface = Color(0xFF1B1B1B)
val NeutralOnBackground = Color(0xFF1B1B1B)

data class ModeColors(
    val primary: Color,
    val glow: Color,
    val background: Color
)

object ModeThemes {
    fun forMode(mode: Mode): ModeColors {
        return when (mode) {
            Mode.RADHA -> ModeColors(
                primary = RadhaPrimary,
                glow = RadhaGlow,
                background = RadhaBackground
            )
            Mode.RAM -> ModeColors(
                primary = RamPrimary,
                glow = RamGlow,
                background = RamBackground
            )
        }
    }
}
