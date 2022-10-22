import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

public class NFA {
    private regularExpression regex;
    private String[] letter;
    private Pair pair;
    private ConsoleTable table;
    private int restate = 0;

    public NFA(regularExpression regex) {
        this.regex = regex;
        Set<Character> temp = new HashSet<>();
        temp = this.regex.listOfCharacters();
        letter = new String[temp.size() + 2];
        Object[] tempObj = temp.toArray();
        int i = 0;
        letter[i] = "";
        for (; i < tempObj.length; i++) {
            letter[i+1] = (char)tempObj[i] + "";
        }
        letter[i+1] = "Epsilon";
        table = new ConsoleTable(letter.length, true);
		table.appendRow();
		for (String string : letter) {
			table.appendColum(string);
        }
        
    }

    public Pair getPair() {
		return pair;
	}
	
	public String[] getLetter() {
		return letter;
	}

	public void setPair(Pair pair) {
		this.pair = pair;
	}

    public void RegularExpression_To_NFA() {
		pair = new Pair();
		Pair temp = new Pair();
		Pair right, left;
		NfaBuilder builder = new NfaBuilder();
		char ch[] = this.regex.getPostfixREGEX().toCharArray();
		Stack<Pair> stack = new Stack<>();
		for (char c : ch) {
			switch (c) {
			case '|':
				right = stack.pop();
				left = stack.pop();
				pair = builder.constructNfaForOR(left, right);
				stack.push(pair);
				break;
			case '*':
				temp = stack.pop();
				pair = builder.constructStarClosure(temp);
				stack.push(pair);
				break;
			case '+':
				temp = stack.pop();
				pair = builder.constructPlusClosure(temp);
				stack.push(pair);
				break;
			case '.':
				right = stack.pop();
				left = stack.pop();
				pair = builder.constructNfaForConnector(left, right);
				stack.push(pair);
				break;
			default:
				pair = builder.constructNfaForSingleCharacter(c);
				stack.push(pair);
				break;
			}
		}
	}

    public void print() {
		restate(this.pair.startNode);
		revisit(this.pair.startNode);
		System.out.println("--------NFA--------");
		table.appendRow();
		printNfa(this.pair.startNode);
		System.out.print(table);
		revisit(this.pair.startNode);
		
		System.out.println("start state: " + (this.pair.startNode.getState()));
		System.out.println("end state: " + (this.pair.endNode.getState()));
	}

    private void restate(Node startNfa) {
		if (startNfa == null || startNfa.isVisited()) {
			return;
		}
		startNfa.setVisited();
		startNfa.setState(restate++);
		restate(startNfa.next);
		restate(startNfa.next2);
	}
	private void revisit(Node startNfa) {
		if (startNfa == null || !startNfa.isVisited()) {
			return;
		}
		startNfa.setUnVisited();
		revisit(startNfa.next);
		revisit(startNfa.next2);
	}

    private void printNfa(Node startNfa) {
		if (startNfa == null || startNfa.isVisited()) {
			return;
		}

		startNfa.setVisited();

		printNode_NFA(startNfa);
		if (startNfa.next != null) {
			table.appendRow();
		}
		printNfa(startNfa.next);
		printNfa(startNfa.next2);
	}

    private void printNode_NFA(Node node) {
		if (node.next != null) {
			table.appendColum(node.getState());
			if(node.getEdge()==-1) {
				for(int i=0;i<letter.length-2;i++) {
					table.appendColum(" ");
				}
				if (node.next2 != null)
					table.appendColum("{"+node.next.getState()+","+node.next2.getState()+"}");
				else
					table.appendColum("{"+node.next.getState()+"}");
				}
			else {
				int index = getindex(""+(char)node.getEdge());
				for(int i=0;i<letter.length-1;i++) {
					if(i!=index)
						table.appendColum(" ");
					else {
						if (node.next2 != null)
							table.appendColum("{"+node.next.getState()+","+node.next2.getState()+"}");
						else
							table.appendColum("{"+node.next.getState()+"}");
					}
				}
			}
		}
		else {
			table.appendColum(node.getState());
			table.appendColum(" ");
			table.appendColum(" ");
			table.appendRow();
		}
	}

    private int getindex(String ch) {
		for (int i=0;i<letter.length;i++) {
			if(letter[i].equals(ch))
				return i-1;
		}
		return -1;
	}

}

 class NfaBuilder {
	private NfaHelper nfaManager = null;

	public NfaBuilder() {
		nfaManager = new NfaHelper();
	}

	public Pair constructStarClosure(Pair pairIn) {
		Pair pairOut = new Pair();
		pairOut.startNode = nfaManager.newNfa();
		pairOut.endNode = nfaManager.newNfa();

		pairOut.startNode.next = pairIn.startNode;
		pairIn.endNode.next = pairOut.endNode;

		pairOut.startNode.next2 = pairOut.endNode;
		pairIn.endNode.next2 = pairIn.startNode;

		pairIn.startNode = pairOut.startNode;
		pairIn.endNode = pairOut.endNode;

		return pairOut;
	}

	public Pair constructPlusClosure(Pair pairIn) {
		Pair pairOut = new Pair();

		pairOut.startNode = nfaManager.newNfa();
		pairOut.endNode = nfaManager.newNfa();

		pairOut.startNode.next = pairIn.startNode;
		pairIn.endNode.next = pairOut.endNode;

		pairIn.endNode.next2 = pairOut.startNode;

		pairIn.startNode = pairOut.startNode;
		pairIn.endNode = pairOut.endNode;

		return pairOut;
	}

	public Pair constructNfaForSingleCharacter(char c) {

		Pair pairOut = new Pair();
		pairOut.startNode = nfaManager.newNfa();
		pairOut.endNode = nfaManager.newNfa();
		pairOut.startNode.next = pairOut.endNode;
		pairOut.startNode.setEdge(c);

		return pairOut;
	}

	public Pair constructNfaForOR(Pair left, Pair right) {
		Pair pair = new Pair();
		pair.startNode = nfaManager.newNfa();
		pair.endNode = nfaManager.newNfa();

		pair.startNode.next = left.startNode;
		pair.startNode.next2 = right.startNode;

		left.endNode.next = pair.endNode;
		right.endNode.next = pair.endNode;

		return pair;
	}

	public Pair constructNfaForConnector(Pair left, Pair right) {
		Pair pairOut = new Pair();
		pairOut.startNode = left.startNode;
		pairOut.endNode = right.endNode;

		left.endNode.next = right.startNode;

		return pairOut;
	}
}

 class NfaHelper {
    private final int NFA_MAX = 256; 
    private Node[] nfaStatesArr = null;
    private Stack<Node> nfaStack = null;
    private int nextAlloc = 0; 
    private int nfaStates = 0; 
    
    public NfaHelper()  {
    	nfaStatesArr = new Node[NFA_MAX];
    	for (int i = 0; i < NFA_MAX; i++) {
    		nfaStatesArr[i] = new Node();
    	}
    	
    	nfaStack = new Stack<Node>();
    	
    }
    
    public Node newNfa()  {
    	Node nfa = null;
    	if (nfaStack.size() > 0) {
    		nfa = nfaStack.pop();
    	}
    	else {
    		nfa = nfaStatesArr[nextAlloc];
    		nextAlloc++;
    	}
    	
    	nfa.clearState();
    	nfa.setState(nfaStates++);
    	nfa.setEdge(Node.EPSILON);
    	
    	return nfa;
    }
    
}

 class Node {
	public static final int EPSILON = -1;
	public static final int EMPTY = -2; 
    public Node next; 
	public Node next2; 
	private int state;
	private boolean visited = false;
	private int edge;

	public int getEdge() {
		return edge;
	}

	public void setEdge(int type) {
		edge = type;
	}
	
	public void setVisited() {
		visited = true;
	}

	public void setUnVisited() {
		visited = false;
	}
	
	public boolean isVisited() {
		return visited;
	}

	public void setState(int num) {
		state = num;
	}

	public int getState() {
		return state;
	}


	public void clearState() {
    	next = next2 = null;
    	state = -1;		
	}
	
	@Override
	public String toString() {
		return (char)edge+" "+state+""+isVisited();
	}
}

 class Pair {
    public Node startNode;
    public Node endNode;
}
