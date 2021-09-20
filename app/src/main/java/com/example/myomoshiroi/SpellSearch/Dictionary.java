package com.example.myomoshiroi.SpellSearch;
import java.util.*;
import com.example.myomoshiroi.SpellSearch.Node;

public class Dictionary {
	private HashMap<Character,Node> roots = new HashMap<Character,Node>();
	int flag;
	public String tagger = null;
	public boolean search(String string) {
		flag = 0;
		if (roots.containsKey(string.charAt(0))) {
			if ( string.length()== 1 && roots.get(string.charAt(0)).endOfWord) {
				tagger = roots.get(string.charAt(0)).wordProperty;
				return true;
			}
			return searchFor(string.substring(1),roots.get(string.charAt(0)));
		}
		else {
			return false;
		}
	}

	//Recursive method that searches through the Trie Tree to find the value.
	private boolean searchFor(String string, Node node) {
		flag++;
		if (string.length()==0) {
			if (node.endOfWord) {
				tagger = node.wordProperty;
				return true;
			} else {
				return false;
			}
		}

		if (node.children.containsKey(string.charAt(0))) {
			return searchFor(string.substring(1),node.children.get(string.charAt(0)));
		} else {
			return false;
		}
	}

	/**
	 * Insert a word into the dictionary.
	 */
	public void insert(String string, String tagger) {
		string = string.toLowerCase();
		if (!roots.containsKey(string.charAt(0))) {
			roots.put(string.charAt(0), new Node());
		}
		insertWord(string.substring(1), roots.get(string.charAt(0)), tagger);
	}

	//Recursive method that inserts a new word into the trie tree.
	private void insertWord(String string, Node node, String tagger) {
		final Node nextChild;
		if(string.length() == 0){
			node.endOfWord = true;
			node.wordProperty = tagger;
			//insertTag(string.substring(1), node);
			return;
		}
		if (node.children.containsKey(string.charAt(0))) {
			nextChild = node.children.get(string.charAt(0));
		} else {
			nextChild = new Node();
			node.children.put(string.charAt(0), nextChild);
		}

		if (string.length() == 1) {
			nextChild.endOfWord = true;
			nextChild.wordProperty = tagger;
			return;
		} else {
			insertWord(string.substring(1),nextChild, tagger);
		}
	}

	private String getTag(String string, Node node){
		return node.wordProperty;
	}

	void show(){
		System.out.println(roots);
	}
}