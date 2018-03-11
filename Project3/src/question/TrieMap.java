package question;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class TrieMap<T> extends TrieMapBase<T> {

	Node<T> root;
	static int childNum;

	public TrieMap(int i) {
		// TODO Auto-generated constructor stub
		TrieMap.childNum=i;
		root=new Node<T>("");
	}
	/**
	 * Returns 	true if key appears in text as a substring;
	 * 			false, otherwise
	 * 
	 * Use Trie data structure to solve the problem
	 */
	public static boolean containsSubstr(String text, String key) {
		TrieMap<Integer> tr=new TrieMap<>(26);
		text=text.toLowerCase();
		key=key.toLowerCase();

		if(key.length()>text.length())
			return false;

		for(int i=0;i<=text.length()-key.length();i++){
			String temp=text.substring(i, i+key.length());
			tr.insert(temp,i);
		}
		return (tr.search(key)!=null);
	}

	/**
	 * Returns how many times the word in the parameter appears in the book.
	 * Each word in book is separated by a white space. 
	 * 
	 * Use Trie data structure to solve the problem
	 */
	public static int wordCount(String book, String word) {
		TrieMap<Integer> tr=new TrieMap<>(26);
		Scanner input=new Scanner(book);
		Object res;
		word=word.toLowerCase();
		while(input.hasNext()){
			String temp=input.next();
			temp=temp.toLowerCase();
			if(temp.length()==word.length()){
				res =tr.search(temp);
				if(res==null)
					tr.insert(temp, 1);
				else{
					tr.insert(temp,(int)res+1);
				}
			}
		}

		res=tr.search(word);
		if(res!=null){
			return (int)res;
		}
		return 0;
	}

	/**
	 * Returns the array of unique words in the book given as parameter.
	 * Each word in book is separated by a white space.
	 *  
	 * Use Trie data structure to solve the problem
	 */
	public static String[] uniqueWords(String book) {
		TrieMap<String> tr=new TrieMap<>(26);
		Scanner input=new Scanner(book);

		while(input.hasNext()){
			String temp=input.next();
			temp=temp.toLowerCase();
			if(tr.search(temp)==null)
				tr.insert(temp, temp);
		}

		ArrayList<String> list=tr.returnBFSOrder();
		String[] unique=new String[list.size()];

		for(int i=0;i<unique.length;i++){
			//System.out.println(list.get(i));
			unique[i]=list.get(i);
		}
		return unique;
	}

	/**
	 * Recommends word completions based on the user history.
	 * 
	 * Among all the strings in the user history, the method takes 
	 * those that start with a given incomplete word S, 
	 * sort the words according to their frequencies (how many 
	 * times they are written), and recommend the 3 most frequently written ones.
	 * 
	 * @param userHistory 
	 * 			the words written previously by the user
	 * 
	 * @param incompleteWords 
	 * 			the list of strings to be autocompleted
	 * @return 
	 * 			a Sx3 array that contains the recommendations
	 * 			for each word to be autocompleted.
	 * 
	 * Use Trie data structure to solve the problem
	 */
	public static String[][] autoComplete(String[] userHistory, String[] incompleteWords){
		String[][] array=new String[incompleteWords.length][3];

		for(int i=0;i<incompleteWords.length;i++){
			TrieMap<Integer> tr=new TrieMap<>(26);
			String word=incompleteWords[i];
			word=word.toLowerCase();
			Object res;
			for(int j=0;j<userHistory.length;j++){
				String temp=userHistory[j];
				temp=temp.toLowerCase();
				if(temp.startsWith(word)){
					res=tr.search(temp);
					if(res==null){
						tr.insert(temp, 1);
					}
					else{
						tr.insert(temp, (int)res+1);
					}
				}
			}

			int max;
			String text;

			for(int k=0;k<3;k++){
				max=0;
				text=null;
				for(int l=0;l<userHistory.length;l++){
					res=tr.search(userHistory[l]);
					if(res!=null){
						if(max<(int)res){
							max=(int)res;
							text=userHistory[l];
						}
					}
				}

				array[i][k]=text;
				//System.out.println(text);

				if(text!=null)
					tr.delete(text);
			}
		}
		return array;
	}

	@Override
	public void insert(String key, T value) {

		Node<T> temp=root;
		int count=1;
		key=key.toLowerCase();
		if(TrieMap.childNum>26 || TrieMap.childNum<2)
			return;
		
		if(key.length()==0)
			return;
		
		for(int i=0;i<key.length();i++){
			char c=key.charAt(i);
			if(c>=TrieMap.childNum+97)
				return ;
		}
		while(true){
			String gigi=key.substring(0,count);
			int index=key.charAt(count-1)-97;

			if(temp.child[index]==null){ 
				if(gigi.equals(key)){
					temp.child[key.charAt(count-1)-97]=new Node(key,value);
					break;
				}
				else{
					temp.child[key.charAt(count-1)-97]=new Node(gigi);
					temp=temp.child[key.charAt(count-1)-97];
					count++;
				}
			}
			else{
				if(gigi.equals(key)){
					temp.child[key.charAt(count-1)-97].value=value;
					temp.child[key.charAt(count-1)-97].isLeaf=true;
					break;
				}
				else{
					temp=temp.child[key.charAt(count-1)-97];
					count++;
				}
			}
		}
	}

	@Override
	public boolean delete(String key) {
		// TODO Auto-generated method stub
		Node<T> temp=root;
		int count=1;
		key=key.toLowerCase();
		
		if(key.length()==0)
			return false;
		
		if(TrieMap.childNum>26 || TrieMap.childNum<2)
			return false;

		for(int i=0;i<key.length();i++){
			char c=key.charAt(i);
			if(c>=TrieMap.childNum+97)
				return false;
		}
		while(true){
			if(temp.child[key.charAt(count-1)-97]==null)
				return false;
			else{
				if(temp.child[key.charAt(count-1)-97].key.length()>key.length())
					return false;
				else if(temp.child[key.charAt(count-1)-97].key.equals(key)){
					if(temp.child[key.charAt(count-1)-97].value==null)
						return false;
					else{
						temp.child[key.charAt(count-1)-97].value=null;
						temp.child[key.charAt(count-1)-97].isLeaf=false;
						return true;
					}
				}
				else{
					temp=temp.child[key.charAt(count-1)-97];
					count++;
				}
			}
		}
	}

	@Override
	public T search(String key) {
		// TODO Auto-generated method stub

		int count=1;
		Node<T> temp=root;
		key=key.toLowerCase();
		if(TrieMap.childNum>26 || TrieMap.childNum<2)
			return null;
		
		if(key.length()==0)
			return null;

		for(int i=0;i<key.length();i++){
			char c=key.charAt(i);
			if(c>=TrieMap.childNum+97)
				return null;
		}
		while(true){
			if(temp.child[key.charAt(count-1)-97]==null)
				return null;
			else{
				if(temp.child[key.charAt(count-1)-97].key.length()>key.length())
					return null;
				else if(temp.child[key.charAt(count-1)-97].key.equals(key)){
					if(temp.child[key.charAt(count-1)-97].value==null)
						return null;
					else
						return (T) temp.child[key.charAt(count-1)-97].value;
				}
				else{
					temp=temp.child[key.charAt(count-1)-97];
					count++;
				}
			}
		}
	}

	@Override
	public int nodeCount() {
		// TODO Auto-generated method stub
		Queue<Node> queue=new LinkedList<>();
		int count=0;

		if(TrieMap.childNum>26 || TrieMap.childNum<2)
			return 0;

		if(root!=null){
			queue.add(root);
			count++;
		}
		while(!queue.isEmpty()){
			Node<T> temp=queue.poll();
			int i=0;
			while(i!=TrieMap.childNum){
				if(temp.child[i]!=null){
					queue.add(temp.child[i]);
					count++;
				}
				i++;
			}
		}
		return count;
	}

	@Override
	public ArrayList<T> returnBFSOrder() {
		// TODO Auto-generated method stub
		ArrayList<T> list=new ArrayList<>();
		Queue<Node> queue=new LinkedList<>();

		if(TrieMap.childNum>26 || TrieMap.childNum<2)
			return list;

		if(root!=null)
			queue.add(root);

		while(!queue.isEmpty()){
			Node<T> temp=queue.poll();
			if(temp.isLeaf){
				//System.out.println(temp.value);
				list.add(temp.value);
			}

			int i=0;
			while(i!=TrieMap.childNum){
				if(temp.child[i]!=null){
					queue.add(temp.child[i]);
				}
				i++;
			}
		}
		return list;
	}
}

class Node<T> {
	String key;
	T value;
	Node<T>[] child=new Node[TrieMap.childNum];
	boolean isLeaf;

	public Node(String key,T val){
		this.key=key;
		this.value=val;
		this.isLeaf=true;

	}
	public Node(String key) {
		// TODO Auto-generated constructor stub
		this.key=key;
		this.value=null;
		this.isLeaf=false;
	}
}
