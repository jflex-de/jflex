package edu.tum.cup2.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;


/**
 * This class is some kind of {@link Map} implementation based on a tree. For each dimension of the key, there might be
 * a branch with a leaf storing a value. Its special ability is that it matches keys which are shorter then stored ones
 * with the same beginning - but still unambigoius - to the value of the full key. <code>null</code> as key or value is
 * omitted.<br/>
 * <br/>
 * The structure of the tree is as follows:
 * <ul>
 * <li>each node has between 0 and {@link #maxDimension} children</li>
 * <li>each node represents a dimension of a stored key</li>
 * <li>a node may have a own value; then it is a leaf (even if it has children!)</li>
 * <li>nodes that are no leafs and only have one leaf in the branch under themselves store a reference to this leaf's
 * value</li>
 * <li>nodes that are no leafs and have more then on leaf in the branch(es) under themselves store <code>null</code> as
 * value<br/>
 * </li>
 * </ul>
 * <br/>
 * When {@link #get(Iterable)} is called is traverses the tree by navigating with the values of its key-parts in the
 * order its iterator presents them. When reaching the end of the key, the value of the current node is returned. This
 * value is either:
 * <ul>
 * <li><code>null</code> if there is no branch for this key</li>
 * <li>The value which has been stored with this exact key</li>
 * <li>The value which has been stored for some key longer then this - in the case this is the only leaf of the branch
 * the given key has lead to</li>
 * <li><code>null</code> in the case that the branch the given key has lead to is ambigious (has more then one leaf)</li>
 * </ul>
 * <br/>
 * This structure makes get and containsKey operations quite fast while putting longer keys or especially removing keys
 * is more expensive.<br/>
 * The major disadvantage of this implementation: Because of obvious performance reasons this class uses arrays in each
 * node to store references to its children instead of {@link HashMap}s. Therefore the single values of each key
 * (keyparts) need to have integer representations which are bijective! (like enumerations for instance) This projection
 * is delegated by using the {@link IEnumerator} interface.
 * 
 * @author Gero
 * 
 * @param <K> Key (must be an instance of {@link Iterable}
 *           <P>
 * @param <P> The parts the key consists of
 * @param <V> Value
 */
public class KTreeMap<K extends Iterable<P>, P, V>
{
	private final int maxDimension;
	private final IEnumerator<P> enumerator;
	
	private Node<P, V> root;
	private int size = 0;
	
	
	/**
	 * @param maxSize The maximal size of dimensions
	 * @throws IllegalArgumentException If maxSize > {@value #MAXIMUM_DIMENSION}
	 */
	public KTreeMap(int maxDimension, IEnumerator<P> enumerator)
	{
		this.maxDimension = maxDimension;
		this.enumerator = enumerator;
		
		this.root = new Node<P, V>(enumerator, maxDimension, null, null);
	}
	
	
	/**
	 * Puts a the given value under a branch defined by the given key
	 * 
	 * @param key
	 * @param value
	 * @return Returns <code>false</code> if there has been a value assigned to this exact key before (and it !equals(value))
	 * @throws NullPointerException If key or value are <code>null</code>!
	 * @throws IllegalArgumentException If the key iterator is emtpy
	 */
	public boolean put(K key, V value)
	{
		if (key == null || value == null)
		{
			throw new NullPointerException("Key and Value must not be null!!!");
		}
		
		final Iterator<P> it = key.iterator();
//		if (!it.hasNext())
//		{
//			throw new IllegalArgumentException("Key must not be empty!!!");
//		}
		
		
		// Try it with a loop...
		final Stack<Node<P, V>> stack = new Stack<Node<P, V>>();
		Node<P, V> node = this.root;
		P lastKeyPart = null;
		
		// Descent
		while (it.hasNext())
		{
			lastKeyPart = it.next();
			node = node.getChildOrCreate(lastKeyPart, value);
			stack.push(node);
		}
		if (!stack.isEmpty())
		{
			stack.pop(); // Ensures that not <node> is on top but its parent!
		}
		
		
		// At the bottom we are!
		final boolean result;	// Whether the value has been properly inserted (or it has been there before)
		final boolean addedNewLeaf;
		if (node.isLeaf)
		{
			// Sanity: Hash-collision or normal double-put???
			// // Only relevant if node is not root
			// if (lastKeyPart != null)
			// {
			// if (!lastKeyPart.equals(node.keyPart))
			// {
			// // keyPart failed the equals vs. hashCode-test, collision!
			
			// This node already is a leaf. Now we have to handle the isNode-special case
			if (isRoot(node)) {
				if (node.value == null) {
					addedNewLeaf = true;
					result = true;
				} else {
					addedNewLeaf = false;
					result = value.equals(node.value);
				}
			} else {
				// No root node: As it already isLeaf, result depends whether node.value equals value
				addedNewLeaf = false;
				result = value.equals(node.value);
			}
			
			// }
			// }
		} else
		{
			result = true;
			addedNewLeaf = true;
		}
		
		node.isLeaf = true;
		node.value = value;
		
		
		// Ascend back to root
		V propagatedValue = value;
		
		while (!stack.isEmpty())
		{
			node = stack.pop();
			
			node.leafCount += 1;
			
			// Updates of nodes value
			if (node.isLeaf)
			{
				// Okay, a leafs value is fix. We just check whether we have to propagate value or null
				if (propagatedValue != null && node.value.equals(propagatedValue))
				{
					// Just keep propagatedValue
				} else
				{
					// There seems to be an ambiguity from this point, set null!
					propagatedValue = null;
				}
			} else
			{
				if (propagatedValue != null && node.value != null && node.value.equals(propagatedValue))
				{
					// Just keep propagatedValue
				} else
				{
					// We dont' know whether the ambiguity here is old (node.value == null) or new (propagatedValue == null
					// || node.value.equals(propagatedValue), we just set null!
					propagatedValue = null;
					node.value = null;
				}
			}
		}
		
		if (addedNewLeaf)
		{
			this.size += 1;
		}
		return result;
	}
	
	
	private boolean isRoot(Node<P, V> node)
	{
		return node.keyPart == null;
	}
	
	
	/**
	 * @param key
	 * @return <code>true</code> if the key matches an unambigious branch in the tree
	 */
	public boolean containsKey(K key)
	{
		return get(key) != null;
	}
	
	
	/**
	 * @param key
	 * @return <code>true</code> if this exact key is present in the tree
	 */
	public boolean containsKeyExact(K key)
	{
		return getExact(key) != null;
	}
	
	
	/**
	 * @param key
	 * @return <code>true</code> if the key matches an unambigious value-branch in the tree
	 */
	public boolean containsValueFor(K key)
	{
		return getValue(key) != null;
	}
	
	
	/**
	 * Gets a value associated with the given key - or the value which has been put for a unambigious longer key with
	 * the same beginning
	 * 
	 * @param key
	 * @return See above
	 * @see #getNode(Iterable, boolean)
	 * @throws NullPointerException If the given key is <code>null</code>
	 */
	public V get(K key)
	{
		Node<P, V> node = getNode(key, false);
		if (node == null)
		{
			return null;
		} else
		{
			return node.value;
		}
	}
	
	
	/**
	 * Gets exactlly value associated with the given key - or <code>null</code> if there is none.
	 * 
	 * @param key
	 * @return See above
	 * @see #getNode(Iterable, boolean)
	 * @throws NullPointerException If the given key is <code>null</code>
	 */
	public V getExact(K key)
	{
		Node<P, V> node = getNode(key, true);
		if (node == null)
		{
			return null;
		} else
		{
			if (!node.isLeaf)
			{
				return null;
			}
			return node.value;
		}
	}
	
	
	/**
	 * Gets a value associated with the given key, the value which has been put for a unambigious longer key with
	 * the same beginning, or even the value associated with a shorter key if values are unambigious.
	 * 
	 * @param key
	 * @return See above
	 * @see #getNodeLazy(Iterable)
	 * @throws NullPointerException If the given key is <code>null</code>
	 */
	public V getValue(K key)
	{
		Node<P, V> node = getNodeLazy(key);
		if (node == null)
		{
			return null;
		} else
		{
			return node.value;
		}
	}
	
	
	private Node<P, V> getNodeLazy(K key)
	{
		if (key == null)
		{
			throw new NullPointerException("Key must not be null!!!");
		}
		
		// Descent in the tree
		Node<P, V> node = this.root;
		final Iterator<P> it = key.iterator();
		while (it.hasNext())
		{
			final P keyPart = it.next();
			node = node.getChildWithNull(keyPart);
			if (node == null)
			{
				return null;
			}
			
			// Check for unambiguity of branch...
			if ((node.leafCount == 1) && (!node.isLeaf) || ((node.leafCount == 0) && node.isLeaf))
			{
				// Okay, narrow branch || real leaf!
				return node;
			}
		}
		
		// Check for unambiguity of value... (possible even if branch has more then one leaf!)
		if (node.value != null)
		{
			return node;
		}
		
		// Ambigious!
		return null;
	}
	
	
	private Node<P, V> getNode(K key, boolean exact)
	{
		if (key == null)
		{
			throw new NullPointerException("Key must not be null!!!");
		}
		
		// Descent in the tree
		Node<P, V> node = this.root;
		final Iterator<P> it = key.iterator();
		while (it.hasNext())
		{
			final P keyPart = it.next();
			node = node.getChildWithNull(keyPart);
			if (node == null)
			{
				return null;
			}
			
			if (!exact)
			{
				// Check for unambiguity of branch...
				if ((node.leafCount == 1) && (!node.isLeaf) || ((node.leafCount == 0) && node.isLeaf))
				{
					// Okay, narrow branch || real leaf!
					return node;
				}
			}
		}
		
		// Check for leaf...
		// if (exact)
		// {
		// if (node.isLeaf)
		// {
		// // Okay, real leaf!
		// return node;
		// }
		// } else
		// {
		// // Check for unambiguity of branch...
		// if ()
		// {
		// // Okay, real leaf || narrow branch
		// return node;
		// }
		// }
		
		// Ambigious!
		return node;
	}
	
	
	/**
	 * Removes the given value from the tree
	 * 
	 * @param key
	 * @return The value stored for the given key
	 * @throws NullPointerException If the given key is <code>null</code>
	 */
	public V remove(K key)
	{
		if (key == null)
		{
			throw new NullPointerException("Key must not be null!!!");
		}
		
		final Iterator<P> it = key.iterator();
//		if (!it.hasNext())
//		{
//			return null;
//		}
		
		
		// Try it with a loop...
		final Stack<Node<P, V>> stack = new Stack<Node<P, V>>();
		Node<P, V> parent = null;
		Node<P, V> node = this.root;
		
		// Descent
		while (it.hasNext())
		{
			final P keyPart = it.next();
			final Node<P, V> child = node.getChildWithNull(keyPart);
			if (child == null)
			{
				// Easy: Key not found => jump off
				return null;
			} else
			{
				stack.push(node);
				parent = node;
				node = child;
			}
		}
		if (!stack.isEmpty())
		{
			stack.pop(); // Ensure that <parent> is at the top of the stack!
		}
		
		
		// Reached wanted node
		final V result = node.value;
		V propagatedValue;
		if (node.isLeaf)
		{
			node.isLeaf = false; // So it will be deleted
			propagatedValue = updateNode(parent, node, true, null);
		} else
		{
			// It is not possible to remove not-put keys!
			return null;
		}
		
		
		// Ascend
		Node<P, V> child = null;
		while (!stack.isEmpty())
		{
			child = node;
			node = parent;
			parent = stack.pop();
			
			// Remove was successful, update values-references
			node.leafCount -= 1;
			
			final boolean updateAfterRemove = (node.getChildWithNull(child.keyPart) == null);
			propagatedValue = updateNode(parent, node, updateAfterRemove, propagatedValue);
		}
		
		this.size -= 1;
		
		return result;
	}
	
	
	/**
	 * Updates the nodes value after its own value or one of its leafs' values has been removed. Used when ascending the
	 * branch.
	 * 
	 * @param parent
	 * @param node
	 * @param completeUpdate
	 * @param newValue The value which is propagated from the lower part of the branch if it is narrow (leafCount == 1)
	 *           or all leafes have the same value
	 * @return The new value to propagate up the tree
	 */
	private static <P, V> V updateNode(Node<P, V> parent, Node<P, V> node, boolean completeUpdate, V newValue)
	{
		// 1. Check for remove
		if (!node.isLeaf && node.leafCount == 0)
		{
			// Delete this!
			if (parent != null) // Special case for root node!!!
			{
				parent.remove(node.keyPart);
			}
			return null;
		}
		// 2. Check for new value to set and/or propagate up the path
		else
		{
			// Special case for root node!
			if (parent == null)
			{
				return null;
			}
			
			// At this point we got a problem: We have to decide which value we want to assign to the current node, but
			// therefore we need to know the values of each children.
			if (node.isLeaf)
			{
				// Gather values held by all direct children and compare them with the one of node
				final Iterator<Node<P, V>> it = node.childrenIterator();
				V valueToCompare = node.value;
				boolean valuesAreEqual = true;
				while (it.hasNext())
				{
					final Node<P, V> oneChild = it.next();
					if (oneChild.value == null || !oneChild.value.equals(valueToCompare))
					{
						valuesAreEqual = false;
						break;
					}
				}
				
				if (valuesAreEqual)
				{
					// Values are equal, so it should be propagated up the branch!
					return valueToCompare;
				} else
				{
					return null;
				}
			} else
			{
				// Gather values held by all direct children and compare them
				final Iterator<Node<P, V>> it = node.childrenIterator();
				V valueToCompare = null;
				if (completeUpdate)
				{
					// In case we just removed a direct child: Don't care about newValue, but simply take the first of the
					// other children
					if (it.hasNext())
					{
						valueToCompare = it.next().value;
					} else
					{
						throw new IllegalStateException("Something went horribly wrong here... :-(");
					}
				} else
				{
					// If the remove operation is already some time ago, consider the propagated value!
					valueToCompare = newValue;
				}
				while (valueToCompare != null && it.hasNext())
				{
					final Node<P, V> oneChild = it.next();
					if (oneChild.value == null || !oneChild.value.equals(valueToCompare))
					{
						valueToCompare = null;
						break;
					}
				}
				
				node.value = valueToCompare;
				return valueToCompare;
			}
		}
	}
	
	
	/**
	 * Represents a dimension of a key in the {@link KTreeMap}.
	 * 
	 * @author Gero
	 * 
	 * @param <P> A part of the key representing on dimensions
	 * @param <V> The value stored for a key
	 */
	static class Node<P, V>
	{
		private final IEnumerator<P> enumerator;
		private final Node<?, ?>[] table;
		private final P keyPart;
		private V value;
		private int leafCount;
		private boolean isLeaf;
		
		
		/**
		 * Constructor for the root-node
		 * 
		 * @param maxSize
		 * @param keyPart
		 * @param newValue
		 */
		Node(IEnumerator<P> enumerator, int maxSize, P keyPart, V newValue)
		{
			this.enumerator = enumerator;
			this.table = new Node[maxSize];
			this.keyPart = keyPart;
			
			this.value = newValue;
			this.isLeaf = true;	// only root isLeaf from the start!
			this.leafCount = 0;
		}
		
		
		private Node(IEnumerator<P> enumerator, int maxSize, P keyPart)
		{
			this.enumerator = enumerator;
			this.table = new Node[maxSize];
			this.keyPart = keyPart;
			
			this.value = null;
			this.isLeaf = false;
			this.leafCount = 0;
		}
		
		
		@SuppressWarnings("unchecked")
		private Node<P, V> getChildOrCreate(P keyPart, V value)
		{
			final int index = enumerator.ordinal(keyPart, this.table.length - 1);
			Node<P, V> child = (Node<P, V>) this.table[index];
			if (child == null)
			{
				child = new Node<P, V>(enumerator, this.table.length, keyPart);
				child.value = value;
				this.table[index] = child;
			}
			return child;
		}
		
		
		@SuppressWarnings("unchecked")
		private Node<P, V> getChildWithNull(P keyPart)
		{
			final int index = enumerator.ordinal(keyPart, this.table.length - 1);
			return (Node<P, V>) this.table[index];
		}
		
		
		private void remove(P keyPart)
		{
			final int index = enumerator.ordinal(keyPart, this.table.length - 1);
			this.table[index] = null;
		}
		
		
		public Iterator<Node<P, V>> childrenIterator()
		{
			return new Iterator<Node<P, V>>() {
				private int curIndex = -1;
				private Node<?, ?> curNode = null;
				private boolean hasToMove = true;
				
				
				public boolean hasNext()
				{
					if (hasToMove)
					{
						return moveToNext();
					}
					return curNode == null;
				}
				
				
				private boolean moveToNext()
				{
					hasToMove = false;
					
					final Node<?, ?>[] table = Node.this.table;
					for (curIndex++; curIndex < table.length; curIndex++)
					{
						curNode = table[curIndex];
						if (curNode != null)
						{
							return true;
						}
					}
					curNode = null;
					return false;
				}
				
				
				@SuppressWarnings("unchecked")
				public Node<P, V> next()
				{
					if (hasToMove)
					{
						moveToNext();
					}
					hasToMove = true;
					return (Node<P, V>) curNode;
				}
				
				
				public void remove()
				{ /* Not implemented */
				}
			};
		}
		
		
		@Override
		public String toString()
		{
			return "[Node \n  key: " + keyPart + "\n  leafCount: " + leafCount + "\n  isLeaf: " + Boolean.toString(isLeaf)
					+ "\n  value: " + value + "\n]";
		}
	}
	
	
	public int getMaxDimensions()
	{
		return maxDimension;
	}
	
	
	public IEnumerator<P> getEnumerator()
	{
		return enumerator;
	}
	
	
	/**
	 * @return The number of stored values
	 */
	public int size()
	{
		return size;
	}
	
	
	public boolean isEmpty()
	{
		return size == 0;
	}
	
	
	public void clear()
	{
		this.root = new Node<P, V>(enumerator, maxDimension, null, null);
		this.size = 0;
	}
	
	
	/**
	 * Used in {@link KTreeMap} to delegate the generation of a bijective "hash" to the user of {@link KTreeMap}
	 * 
	 * @author Gero
	 * 
	 * @param <P>
	 */
	public interface IEnumerator<P>
	{
		/**
		 * @param keypart
		 * @param maxOrdinal
		 * @return A integer value 0 <= ordinal <= maxOrdinal which represents the given keypart
		 */
		public int ordinal(P keypart, int maxOrdinal);
	}
}
