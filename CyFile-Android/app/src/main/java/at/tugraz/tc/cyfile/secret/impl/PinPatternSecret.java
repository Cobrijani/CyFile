package at.tugraz.tc.cyfile.secret.impl;

import com.andrognito.patternlockview.PatternLockView;

import java.util.List;

public class PinPatternSecret extends AbstractSecret {

    private List<PatternLockView.Dot> dotList;

    private String patterns;

    public PinPatternSecret(List<PatternLockView.Dot> pattern) {
        this.dotList = pattern;
    }

    public PinPatternSecret(String patterns) {
        this.patterns = patterns;
    }

    @Override
    public String getSecretValue() {
        if (patterns != null) {
            return patterns;
        }

        if (dotList != null) {
            StringBuilder str = new StringBuilder();

            for (PatternLockView.Dot dot :
                    dotList) {
                str.append(dot.getRow()).append(dot.getColumn());
            }
            return str.toString();
        }

        return null;
    }


}
