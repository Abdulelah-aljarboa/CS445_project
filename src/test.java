import java.util.Scanner;

public class test {
    public static void main(String[] args) {
        // Scanner scan = new Scanner(System.in);
        // System.out.println("regex :");
        // String reg = scan.nextLine();
         //String re = "(a|b)*a";
        //  regularExpression r1 = new regularExpression(re);
        // r1.REGEX_Concatenation();
        // r1.REGEX_Postfix(); 


        Scanner in = new Scanner(System.in);
		System.out.println("input a regular expression");
		String re = in.nextLine();
		System.out.println("re:" + re);
        regularExpression r1 = new regularExpression(re);
        r1.REGEX_Concatenation();
        r1.REGEX_Postfix(); 
		NFA nfa = new NFA(r1);
		nfa.RegularExpression_To_NFA();
		nfa.print();
		
		
		
		
		in.close();
}
}