import java.util.Iterator;

public class Exchange {
	private int _id;
	private Exchange _parent;
	private ExchangeList _children;
	private int _numChildren;
	private MobilePhoneSet _residentSet;

	Exchange(int number) {
		_id = number;
		_parent = null;
		_children = new ExchangeList();
		_numChildren = 0;
		_residentSet = new MobilePhoneSet();
	}

	public int id() {
		return _id;
	}

	public Exchange parent() {
		return _parent;
	}

	public void setParent(Exchange parent) {
		_parent = parent;
	}
	
	public int numChildren() {
		return _numChildren;
	}

	public Iterator<Exchange> children() {
		return _children.iterator();
	}

	public Exchange child(int i) throws Exception {
		if (i < 0 || i >= _numChildren) {
			throw new Exception("Error - " + i + "th child of exchange with identifier " + _id + " does not exist");
		} else {
			return _children.get(i);
		}
	}

	public void addChild(Exchange child) {
		_children.insertRear(child);
		++_numChildren;
	}

	public boolean isRoot() {
		return _parent == null;
	}

	public RoutingMapTree subtree(int i) throws Exception {
		try {
			return new RoutingMapTree(this.child(i));
		} catch (Exception e) {
			throw new Exception("Error - " + i + "th subtree of exchange with identifier " + _id + " does not exist");
		}
	}

	public MobilePhoneSet residentSet() {
		return _residentSet;
	}

	public void addResident(MobilePhone m) {
		Exchange curr = this;
		while (curr != null) {
			curr._residentSet.insert(m);
			curr = curr.parent();
		}
	}

	public void removeResident(MobilePhone m) throws Exception {
		try {
			Exchange curr = this;
			while (curr != null) {
				curr._residentSet.delete(m);
				curr = curr.parent();
			}
		} catch (Exception e) {
			throw new Exception("Error - Mobile phone with identifier " + m.number() + " not in resident set of exchange with identifier " + _id);
		}
	}

	public boolean equals(Exchange other) {
		return this._id == other._id;
	}
}
