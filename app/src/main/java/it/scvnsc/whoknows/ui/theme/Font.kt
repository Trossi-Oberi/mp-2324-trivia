package it.scvnsc.whoknows.ui.theme

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import it.scvnsc.whoknows.R

// Definisce la famiglia di font personalizzato per i titoli
val customTitleFont = FontFamily(
    Font(R.font.nabla_regular_title, FontWeight.Normal)
)

// Definisce la famiglia di font personalizzato per i testi generali
val customTextFont = FontFamily(
    Font(R.font.chakra_regular, FontWeight.Normal),
    Font(R.font.chakra_bold, FontWeight.Bold),
    Font(R.font.chakra_light, FontWeight.Light),
    Font(R.font.chakra_italic, FontWeight.Normal)
)