import java.util.Iterator;

public class MyLinkedList<Type> implements Iterable<Type> {
	class Node {
		private Type _data;
		private Node _next, _prev;
		Node(Type d) {
			_data = d;
			_next = null;
			_prev = null;
		}

		public Node next() {
			return _next;
		}

		public Node prev() {
			return _prev;
		}
		
		public Type data() {
			return _data;
		}

		public void setData(Type o) {
			_data = o;
		}

		public void setNext(Node n) {
			_next = n;
		}

		public void setPrev(Node n) {
			_prev = n;
		}
	}
	
	private Node _start, _end;
	
	MyLinkedList() {
		_start = null;
		_end = null;
	}

	public boolean isEmpty() {
		return _start == null;
	}

	public boolean isMember(Type o) {
		Node curr = _start;
		while (curr != null) {
			if (curr.data().equals(o)) {
				return true;
			}
			curr = curr.next();
		}
		return false;
	}

	public Type get(int i) throws Exception {
		Node curr = _start;
		while (--i >= 0 && curr != null) {
			curr = curr.next();
		}
		if (curr == null) {
			throw new Exception("Error - " + i + "th element not in list");
		} else {
			return curr.data();
		}
	}

	public void insertFront(Type o) {
		Node n = new Node(o);
		if (_start == null) {
			_start = _end = n;
		} else {
			n.setNext(_start);
			_start.setPrev(n);
			_start = n;
		}
	}

	public void insertRear(Type o) {
		Node n = new Node(o);
		if (_end == null) {
			_start = _end = n;
		} else {
			n.setPrev(_end);
			_end.setNext(n);
			_end = n;
		}
	}

	public Type deleteFront() throws Exception {
		if (_start == null) {
			throw new Exception("Error - List is empty");
		} else if (_start == _end) {
			Type ret = _start.data();
			_start = _end = null;
			return ret;
		} else {
			Type ret = _start.data();
			_start = _start.next();
			_start.setPrev(null);
			return ret;
		}
	}

	public Type deleteRear() throws Exception {
		if (_end == null) {
			throw new Exception("Error - List is empty");
		} else if (_start == _end) {
			Type ret = _end.data();
			_start = _end = null;
			return ret;
		} else {
			Type ret = _end.data();
			_end = _end.prev();
			_end.setNext(null);
			return ret;
		}
	}

	public void delete(Type o) throws Exception {
		Node curr = _start;
		while (curr != null) {
			if (curr.data().equals(o)) {
				break;
			}
			curr = curr.next();
		}
		if (curr == null) {
			throw new Exception("Error - Element not in list");
		} else if (_start == _end) {
			_start = _end = null;
		} else if (_start == curr) {
			_start = _start.next();
			_start.setPrev(null);
		} else if (_end == curr) {
			_end = _end.prev();
			_end.setNext(null);
		} else {
			curr.prev().setNext(curr.next());
			curr.next().setPrev(curr.prev());
		}
	}

	@Override
	public Iterator<Type> iterator() {
		return new Iterator<Type>() {
			private Node curr = _start;

			@Override
			public boolean hasNext() {
				return curr != null;
			}

			@Override
			public Type next() {
				Type o = curr.data();
				curr = curr.next();
				return o;
			}
			
			@Override
			public void remove() {
				throw new UnsupportedOperationException("Error - Remove operation not supported");
			}
		};
	}
}
