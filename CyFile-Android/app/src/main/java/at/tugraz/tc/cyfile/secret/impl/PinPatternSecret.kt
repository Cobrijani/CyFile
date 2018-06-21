package at.tugraz.tc.cyfile.secret.impl

import com.andrognito.patternlockview.PatternLockView

class PinPatternSecret : AbstractSecret {

    private val patterns: String

    override val secretValue: String
        get() {
            return patterns
        }

    constructor(pattern: List<PatternLockView.Dot>) {
        this.patterns = convert(pattern)
    }

    constructor(patterns: String) {
        this.patterns = patterns
    }

    private fun convert(pattern: List<PatternLockView.Dot>): String {
        val str = StringBuilder()
        pattern
                .forEach { x -> str.append(x.row).append(x.column) }
        return str.toString()
    }


}
