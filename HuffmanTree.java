import java.util.*;

public class HuffmanTree {
	TreeNode root;
	HashMap<String, String> codes;

	/**
	 * Constructor for HuffmanTree class. Initializes root to null and codes to a new HashMap<String, String>.
	 */
	HuffmanTree() {
		root = null;
		codes = new HashMap<String, String>();
	}

	/**
	 * Function to construct the Huffman Tree. Uses recursive helper function to construct tree
	 * once min heap has been created and initialized with TreeNodes of characters.
	 * @param characters List containing unique characters in human message
	 * @param freq List containing each unique character's frequency
	 */
	public void constructHuffmanTree(ArrayList<String> characters, ArrayList<Integer> freq) {
		int size = characters.size();
		PriorityQueue<TreeNode> minHeap = new PriorityQueue<TreeNode>(size, new Comparator<TreeNode>() {
			@Override
			public int compare(TreeNode o1, TreeNode o2) {
				return o1.getFrequency() - o2.getFrequency();	// change made here
			}
		});

		for(int i = 0; i < size; i++) {		// creating all characters into nodes, adding all TreeNodes to min heap
			TreeNode node = new TreeNode();
			node.setValue(characters.get(i));
			node.setFrequency(freq.get(i));
			minHeap.add(node);
		}

		if(minHeap.size() == 0) {	// no characters
			return;
		}
		else {	// 2 or more characters
			recursiveConstruct(minHeap);
		}
	}

	/**
	 * Recursive helper function for constructing Huffman Tree from the min heap that was constructed prior.
	 * The tree will be accessed from the 'root' field.
	 * @param minHeap Min heap of all characters and their values/frequencies
	 */
	public void recursiveConstruct(PriorityQueue<TreeNode> minHeap) {
		if(minHeap.size() == 1) {
			root = minHeap.poll();
			return;
		}
		else {	// minHeap.size() > 1
			TreeNode one = minHeap.poll();
			TreeNode two = minHeap.poll();
			String comboVal = one.getValue() + two.getValue();
			int comboFreq = one.getFrequency() + two.getFrequency();
			TreeNode comboNode = new TreeNode();
			comboNode.setLeftChild(one);
			comboNode.setRightChild(two);
			comboNode.setValue(comboVal);
			comboNode.setFrequency(comboFreq);
			minHeap.add(comboNode);
			recursiveConstruct(minHeap);
		}
	}

	/**
	 * Function to encode human message using the codes HashMap that was developed from the Huffman Tree.
	 * @param humanMessage String of message to be encoded
	 * @return String encoded message with Huffman codes
	 */
	public String encode(String humanMessage) {
		if(root.getRightChild() == null && root.getLeftChild() == null)
			codes.put(root.getValue(), "0");	// if only one character to encode, default to "0"
		else {
			traverseTree(root, "");
		}

		String encodedMessage = "";
		for(int i = 0; i < humanMessage.length(); i++) {
			String character = humanMessage.substring(i, i+1);	// character from human message
			String encoding = codes.get(character);	// encoding to substitute human character
			encodedMessage += encoding;	// concatenating to final encoded message
		}

	    return encodedMessage;
	}

	/**
	 * Function to decode an encoded message into a human-readable message.
	 * @param encodedMessage String encoded message using Huffman codes.
	 * @return String of the decoded, human-readable message
	 */
	public String decode(String encodedMessage) {
		if(root.getLeftChild() == null && root.getRightChild() == null) {
			encodedMessage = encodedMessage.replaceAll("0", root.getValue());
			return encodedMessage;
		}
		else {
			traverseTree(root, "");
		}

		String decodedString = "";
		TreeNode node = root;
		for(int i = 0; i < encodedMessage.length(); i++) {
			if(encodedMessage.substring(i, i+1).equals("0"))
				node = node.getLeftChild();
			else
				node = node.getRightChild();

			if(node.getRightChild() == null && node.getLeftChild() == null) {
				decodedString += node.getValue();
				node = root;
			}
		}
		return decodedString;
	}

	/**
	 * Recursive function to traverse the Huffman Tree and put characters and their unique codes into the HashMap.
	 * @param node Current node in the traversal. If this node is a leaf, add it to the HashMap with its code.
	 * @param runningCode String code for the current node. Add "0" if traversing to left, "1" if to the right.
	 */
	public void traverseTree(TreeNode node, String runningCode) {
		if (node.getLeftChild() == null && node.getRightChild() == null) {
			codes.put(node.getValue(), runningCode);
			return;
		}
		if(node.getRightChild() != null) {	// if right child exists (add "1")
			String updatedCode = runningCode + "1";
			traverseTree(node.getRightChild(), updatedCode);
		}
		if(node.getLeftChild() != null) {	// if left child exists (add "0")
			String updatedCode = runningCode + "0";
			traverseTree(node.getLeftChild(), updatedCode);
		}
	}
}
