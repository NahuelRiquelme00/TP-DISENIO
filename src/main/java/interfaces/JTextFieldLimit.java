package interfaces;
import javax.swing.text.PlainDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;

public class JTextFieldLimit extends PlainDocument {
	  private final int limit;

	  JTextFieldLimit(int limit) {
	   super();
	   this.limit = limit;
	   }

          @Override
	  public void insertString( int offset, String  str, AttributeSet attr ) throws BadLocationException {
	    if (str == null) return;

	    if ((getLength() + str.length()) <= limit) {
	      super.insertString(offset, str.toUpperCase(), attr);
	    }
	  }
	}