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
    fontSize = fontSizeNormal,
    fontWeight = FontWeight.Medium,
    fontFamily = customTitleFont
)

//Buttons text
val buttonsTextStyle = TextStyle(
    fontSize = fontSizeMedium,
    fontWeight = FontWeight.Normal,
    fontFamily = customTextFont
)

//Answers text
//TODO::

//Score text
//TODO::

//YourGames elements
//TODO::

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
        //TODO::
        fontFamily = customTextFont,
        fontWeight = FontWeight.Normal,
        fontSize = fontSizeNormal
    ),
    displayMedium = TextStyle(
        //TODO::
        fontFamily = customTextFont,
        fontWeight = FontWeight.Medium,
        fontSize = fontSizeMedium
    )
)

//TODO:: da rimuovere
//// Set of Material typography styles to start with
//val Typography = Typography(
//    bodyLarge = TextStyle(
//        fontFamily = FontFamily.Default,
//        fontWeight = FontWeight.Normal,
//        fontSize = 16.sp,
//        lineHeight = 24.sp,
//        letterSpacing = 0.5.sp
//    )
//    /* Other default text styles to override
//    titleLarge = TextStyle(
//        fontFamily = FontFamily.Default,
//        fontWeight = FontWeight.Normal,
//        fontSize = 22.sp,
//        lineHeight = 28.sp,
//        letterSpacing = 0.sp
//    ),
//    labelSmall = TextStyle(
//        fontFamily = FontFamily.Default,
//        fontWeight = FontWeight.Medium,
//        fontSize = 11.sp,
//        lineHeight = 16.sp,
//        letterSpacing = 0.5.sp
//    )
//    */
//)