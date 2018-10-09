import java.util.Iterator;

public class Myset<Type> implements Iterable<Type> {
	private MyLinkedList<Type> _list;

	Myset() {
		_list = new MyLinkedList<Type>();
	}

	public boolean isEmpty() {
		return _list.isEmpty();
	}

	public boolean isMember(Type o) {
		return _list.isMember(o);
	}

	public void insert(Type o) {
		if (!_list.isMember(o)) {
			_list.insertRear(o);
		}
	}

	public void delete(Type o) throws Exception {
		try {
			_list.delete(o);
		} catch (Exception e) {
			throw new Exception("Error - Element not in set");
		}
	}

	public Myset<Type> union(Myset<Type> a) {
		Myset<Type> b = new Myset<Type>();
		Iterator<Type> it = this.iterator();
		while (it.hasNext()) {
			b.insert(it.next());
		}
		it = a.iterator();
		while (it.hasNext()) {
			b.insert(it.next());
		}
		return b;
	}

	public Myset<Type> intersection(Myset<Type> a) {
		Myset<Type> b = new Myset<Type>();
		Iterator<Type> it = this.iterator();
		while (it.hasNext()) {
			Type t = it.next();
			if (a.isMember(t)) {
				b.insert(t);
			}
		}
		return b;
	}

	@Override
	public Iterator<Type> iterator() {
		return _list.iterator();
	}
}
