public class test {
    public static void main(String[] args) {
        // Scanner scan = new Scanner(System.in);
        // System.out.println("regex :");
        // String reg = scan.nextLine();
        String re = "(a|bc)(ab)";
        regularExpression r1 = new regularExpression(re);
        r1.REGEX_Concatenation();
        r1.REGEX_Postfix(); 
}
}