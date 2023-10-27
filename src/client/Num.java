package client;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Num extends JTextField {
    public Num(String str) {
        super(str);
    }

    @Override
    protected Document createDefaultModel() {
        return new CustomPlainDocument();
    }
}

class CustomPlainDocument extends PlainDocument {
   @Override
    public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
        if (str == null) {
            return;
        }
        if (isNumber(str)) {
            int number = Integer.parseInt(getText(0, getLength()) + str);
            if (number < 10000) {
                super.insertString(offs, str, a);
            }
        }

    }

    private boolean isNumber(String string) {
        Pattern pattern = Pattern.compile("[0-9]+$");
        Matcher matcher = pattern.matcher(string);
        return matcher.matches();
    }
}