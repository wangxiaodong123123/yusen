package com.ysdata.blender.element;

public class TreeNode {
	private int id;
	private String name ;
	private boolean mhasParent; 
	private boolean mhasChild ;
	private int parentId;
	private int level;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isMhasParent() {
		return mhasParent;
	}

	public void setHasParent(boolean mhasParent) {
		this.mhasParent = mhasParent;
	}

	public boolean isMhasChild() {
		return mhasChild;
	}

	public void setHasChild(boolean mhasChild) {
		this.mhasChild = mhasChild;
	}

	public int getParentId() {
		return parentId;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public boolean isExpanded() {
		return expanded;
	}

	public void setExpanded(boolean expanded) {
		this.expanded = expanded;
	}


	//private OutlineElement outlineElement;
	private boolean expanded;
	
	public TreeNode(int id, String name,
			boolean mhasParent, boolean mhasChild, int parentId, int level,
			boolean expanded) {
		super();
		this.id = id;
		this.name = name;
		this.mhasParent = mhasParent;
		this.mhasChild = mhasChild;
		this.parentId = parentId;
		this.level = level;
		this.expanded = expanded;
	}
}
