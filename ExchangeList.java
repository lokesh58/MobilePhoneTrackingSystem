import java.util.Iterator;

public class ExchangeList implements Iterable<Exchange> {
	private MyLinkedList<Exchange> _list;

	ExchangeList() {
		_list = new MyLinkedList<Exchange>();
	}

	public boolean isEmpty() {
		return _list.isEmpty();
	}

	public boolean isMember(Exchange e) {
		return _list.isMember(e);
	}
	
	public void insertFront(Exchange e) {
		_list.insertFront(e);
	}

	public void insertRear(Exchange e) {
		_list.insertRear(e);
	}

	public Exchange deleteFront() throws Exception {
		return _list.deleteFront();
	}

	public Exchange deleteRear() throws Exception {
		return _list.deleteRear();
	}
	
	public void delete(Exchange e) throws Exception {
		_list.delete(e);
	}

	public Exchange get(int i) throws Exception {
		return _list.get(i);
	}

	@Override
	public Iterator<Exchange> iterator() {
		return _list.iterator();
	}
}
