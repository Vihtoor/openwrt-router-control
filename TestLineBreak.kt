import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.text.TextStyle

fun check() {
    val lb = LineBreak(
        strategy = LineBreak.Strategy.Simple,
        strictness = LineBreak.Strictness.Normal,
        wordBreak = LineBreak.WordBreak.BreakAll
    )
    val ts = TextStyle(lineBreak = lb)
}
