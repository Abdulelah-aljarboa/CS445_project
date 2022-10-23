import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

public class regularExpression {
    
    private String REGEX;
    private String concatREGEX;
    private String postfixREGEX;
    
    //CONSTRUCTOR
    public regularExpression(String REGEX) {
        this.REGEX = REGEX;
    }

    public String getREGEX() {
        return this.REGEX;
    }

    public void setREGEX(String REGEX) {
        this.REGEX = REGEX;
    }

    public String getConcatREGEX() {
        return this.concatREGEX;
    }

    public void setConcatREGEX(String concatREGEX) {
        this.concatREGEX = concatREGEX;
    }

    public String getPostfixREGEX() {
        return this.postfixREGEX;
    }

    public void setPostfixREGEX(String postfixREGEX) {
        this.postfixREGEX = postfixREGEX;
    }

	public int getLength() {
		return this.REGEX.length();
	}

    

    //Add "." in the places that need to have one, so we have right representation. (ab) -> (a.b)
    public void REGEX_Concatenation() {  
        int length = REGEX.length(); //the length of the original regular expression 
        if(length == 1) { // if the length is 1 which means only one letter, return the same thing 
            System.out.println("No Need for alteration -> " + REGEX);
            return;
        }
        int output_String_length = 0;
        char[] output_String = new char[2 * length + 2]; // we multiply by 2 because if all letters are divided by concat, we need to double the number of spaces that we have. and plus 2 for the parantheses
        char first = ' ';
        char second = ' ';
        for (int i = 0; i < length - 1; i++) { 
            first = REGEX.charAt(i);
            second = REGEX.charAt(i + 1);
            output_String[output_String_length++] = first;
            if (first != '(' && first != '|' && is_letter(second)) {
                output_String[output_String_length++] = '.';
            } else if (second == '(' && first != '|' && first != '(') {
                output_String[output_String_length++] = '.';
            }

        }
        output_String[output_String_length++] = second;
        String REGEX_String = new String(output_String, 0, output_String_length);
        System.out.println("After Concatenation: " + REGEX_String);
        this.concatREGEX = REGEX_String;

    }

    public String REGEX_Postfix() {
        String concatREGEX_postHelper = concatREGEX + "#";

		Stack<Character> s = new Stack<>();
		char ch1 = '#', ch2, op;
		s.push(ch1);
		String output_string = "";
		int read_Index = 0;
		ch1 = concatREGEX_postHelper.charAt(read_Index++);
		while (!s.empty()) { 
			if (is_letter(ch1)) {
				output_string = output_string + ch1;
				ch1 = concatREGEX_postHelper.charAt(read_Index++);
			} else {
				ch2 = s.peek(); //[#,(] )
				if (inStack(ch2) < outStack(ch1)) {
					s.push(ch1);
					ch1 = concatREGEX_postHelper.charAt(read_Index++);
				} else if (inStack(ch2) > outStack(ch1)) {
					op = s.pop();
					output_string = output_string + op;
				} else {
					op = s.pop();
					if (op == '(')
						ch1 = concatREGEX_postHelper.charAt(read_Index++);
				}
			}
		}
		System.out.println("postfix: " + output_string);
		System.out.println();
		this.postfixREGEX = output_string;
		return output_string;
	}

    private int inStack(char c) {
		switch (c) {
		case '#':
			return 0;
		case '(':
			return 1;
		case '*':
			return 7;
		case '.':
			return 5;
		case '|':
			return 3; 
		case ')':
			return 8;
		}
		return -1;
	}

    private int outStack(char c) {
		switch (c) {
		case '#':
			return 0;
		case '(':
			return 8;
		case '*':
			return 6;
		case '.':
			return 4;
		case '|':
			return 2;
		case ')':
			return 1;
		}
		return -1;
	}

    private boolean is_letter(char check) {
		{
			if (check >= 'a' && check <= 'z' || check >= 'A' && check <= 'Z')
				return true;
			return false;
		}
	}
	// return the set of the letters used
	public Set<Character> listOfCharacters() {
		Set<Character> temp = new HashSet<>();
		for (int i = 0; i < this.REGEX.length(); i++) { 
			if(this.is_letter(this.REGEX.charAt(i))) {
				temp.add(this.REGEX.charAt(i));
			}
		}
		return temp;
	}

    
}