package com.evilking.indexlist;

public class BoolIndexArrayList {
	
	private EntryNode[] indexArray;
	
	private int BLOCK_SIZE = 5000;		//数组块的大小
	private int BUFFER_SIZE = 50;	//一级索引能索引的数组块数
	
	private int block_num;

	
	public BoolIndexArrayList(){
		indexArray = new EntryNode[1];
		indexArray[0] = new EntryNode(new BoolNode());
		block_num++;
	}
	
	public void put(int index,boolean value){
		int size = indexArray.length - 1;	//数组长度索引
		int buffer_index = index / (BLOCK_SIZE * BUFFER_SIZE);
		if(buffer_index <= size){
			setValue(indexArray[buffer_index].getNode(),index,value);
		}else{
			BoolNode currentNode = indexArray[size].getNode();
			
			setValue(currentNode,index,value);
		}
		
	}
	
	private void setValue(BoolNode curNode,int index,boolean value){
		do{
			if(index >= curNode.getLeft() && index <= curNode.getRight()){
				curNode.putValue(index, value);
				break;
			}else if(curNode.getNext() == null && index > curNode.getRight()) {
				BoolNode nextNode = new BoolNode(curNode.getRight() + 1);
				curNode.setNext(nextNode);	//自动创建下一个数组块
				
				//更新一级索引
				block_num++;
				if(block_num % BUFFER_SIZE == 1){	//一级索引增加元素
					//复制一级索引
					indexArray = extendsEntryNode(indexArray,1);
					indexArray[indexArray.length - 1] = new EntryNode(nextNode);
//					block_num = 1;
				}else{
					indexArray[indexArray.length - 1].updateRight(BLOCK_SIZE);
				}
				
				curNode = nextNode;
			}else{
				curNode = curNode.getNext();
			}				
			
		}while(true);
	}
	
	public boolean get(int index){
		int size = indexArray.length - 1;	//数组长度索引
		for(int i = 0;i <= size;i++){
			if(indexArray[i].inSection(index)){
				return indexArray[i].getNode().findValue(index);
			}
		}
		
		return false;
	}
	
	public int getBlockSize(){
		return block_num;
	}
	
	
	private EntryNode[] extendsEntryNode(EntryNode[] indexArray,int num){
		EntryNode[] newArray = new EntryNode[indexArray.length + num];
		System.arraycopy(indexArray, 0, newArray, 0, indexArray.length);
		return newArray;
	}
	
	class EntryNode{
		private BoolNode intNode;
		
		private int left;
		private int right;
		
		public EntryNode(BoolNode node){
			this.intNode = node;
			left = node.getLeft();
			right = node.getRight();
		}
		
		public boolean inSection(int index){
			return index >= left && index <= right;
		}

		public BoolNode getNode() {
			return intNode;
		}
		
		public void updateRight(int num){
			this.right += num;
		}
	}
	
	class BoolNode{
		private boolean[] value;	//保存的数组实际内容		[0,BLOCK_SIZE - 1]
		private int left;		//数组块的左边界		[left,right]
		private int right;		//数组块的右边界
		private BoolNode next;	//数组块的下一个指针
		
		
		public BoolNode(){
			this.value = new boolean[BLOCK_SIZE];
			this.left = 0;
			this.right = left + BLOCK_SIZE - 1;
		}
		
		public BoolNode(int left){
			this.value = new boolean[BLOCK_SIZE];
			this.left = left;
			this.right = left + BLOCK_SIZE - 1;
		}
		
		/**
		 * 返回index指向的元素
		 * @param index
		 * @return
		 */
		public boolean findValue(int index){
			if(index >= left && index <= right){
				return value[index - left];
			}else if(next == null && index > right){
				return false;	//代表还未被创建的节点，对应的值当然为0了
			}else{
				return next.findValue(index);
			}
		}

		public BoolNode getNext() {
			return next;
		}

		public void setNext(BoolNode next) {
			this.next = next;
		}

		public int getLeft() {
			return left;
		}

		public int getRight() {
			return right;
		}

		public boolean[] getValue() {
			return value;
		}
		
		public void putValue(int index,boolean val){
			value[index - left] = val;
		}
		
	}
	
}
