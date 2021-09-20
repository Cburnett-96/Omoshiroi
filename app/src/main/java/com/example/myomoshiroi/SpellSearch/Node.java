package com.example.myomoshiroi.SpellSearch;

import java.util.HashMap;

public class Node {
	public Node parent;
	public Boolean endOfWord = false; //Does this Node mark the end of a particular word?

	public String wordProperty = null;
	public HashMap<Character,Node> children = new HashMap<Character,Node>();
}
