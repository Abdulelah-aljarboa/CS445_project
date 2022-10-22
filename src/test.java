import java.util.Scanner;

public class test {
    public static void main(String[] args) {
       
        Scanner in = new Scanner(System.in);
		System.out.println("input a regular expression");
		String REGEX = in.nextLine();
		System.out.println("REGEX:" + REGEX);
        regularExpression r1 = new regularExpression(REGEX);
        r1.REGEX_Concatenation();
        r1.REGEX_Postfix(); 
		NFA nfa = new NFA(r1);
		nfa.RegularExpression_To_NFA();
		nfa.print();
		
		DFA dfa = new DFA(nfa.getPair(),nfa.getLetter());
		dfa.createDFA();
		dfa.printDFA();
		
		
		in.close();
}
}