package main;

import java.util.ArrayList;
import java.util.List;

/**
 * Nodes werden für den Aufbau des Trees verwendet.
 * 
 * @author Benjamin huber
 * @author Julia Mueller
 *
 */
public class Node {

	public List<Node> children;
	public String name;
	public String nodeType;
	
	public Node(String name, String nodeType) {
		children = new ArrayList<Node>();
		this.name = name;
		this.nodeType = nodeType;
	}
	
	public Node(Node node) {
		children = node.children;
		this.name = node.name;
		this.nodeType = node.nodeType;
	}
	
	
	public void addChildren(Node child) {
		children.add(child);
	}
	

	public void addChildren(Node child1, Node child2) {
		children.add(child1);
		children.add(child2);
	}
	
	

}
