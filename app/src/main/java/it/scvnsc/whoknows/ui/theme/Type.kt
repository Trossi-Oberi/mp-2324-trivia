package it.scvnsc.whoknows.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight

//Main titles
val titleTextStyle = TextStyle(
    fontSize = fontSizeBig,
    fontWeight = FontWeight.Normal,
    fontFamily = customTitleFont
)

val topBarTextStyle = TextStyle(
    fontSize = fontSizeUpperMedium,
    fontWeight = FontWeight.Medium,
    fontFamily = customTitleFont
)

//Buttons text
val buttonsTextStyle = TextStyle(
    fontSize = fontSizeMedium,
    fontWeight = FontWeight.Normal,
    fontFamily = customTextFont
)

val gameQuestionTextStyle = TextStyle(
    fontSize = fontSizeNormal,
    fontWeight = FontWeight.Normal,
    fontFamily = customTextFont
)

val gameButtonsTextStyle = TextStyle(
    fontSize = fontSizeNormal,
    fontWeight = FontWeight.Bold,
    fontFamily = customTextFont
)

val gameTimerTextStyle = TextStyle(
    fontSize = fontSizeUpperNormal,
    fontWeight = FontWeight.SemiBold,
    fontFamily = customTextFont
)

val gameScoreTextStyle = TextStyle(
    fontSize = fontSizeUpperNormal,
    fontWeight = FontWeight.SemiBold,
    fontFamily = customTextFont
)

//Answers text
//TODO::

//Score text
//TODO::

//YourGames elements
val rowButtonTextStyle = TextStyle(
    fontSize = fontSizeUpperNormal,
    fontWeight = FontWeight.Normal,
    fontFamily = customTextFont
)

//Custom typography
val customTypography = Typography(
    bodyLarge = TextStyle(
        fontFamily = customTextFont,
        fontWeight = FontWeight.Normal,
        fontSize = fontSizeNormal
    ),
    titleLarge = TextStyle(
        fontFamily = customTitleFont,
        fontWeight = FontWeight.Normal,
        fontSize = fontSizeMedium
    ),
    displayLarge = TextStyle(
        fontFamily = customTextFont,
        fontWeight = FontWeight.Normal,
        fontSize = fontSizeBig
    ),
    displaySmall = TextStyle(
        fontFamily = customTextFont,
        fontWeight = FontWeight.Normal,
        fontSize = fontSizeNormal
    ),
    displayMedium = TextStyle(
        fontFamily = customTextFont,
        fontWeight = FontWeight.Medium,
        fontSize = fontSizeMedium
    )
)
