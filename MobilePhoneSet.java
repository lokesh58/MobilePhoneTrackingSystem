import java.util.Iterator;

public class MobilePhoneSet implements Iterable<MobilePhone> {
	private Myset<MobilePhone> _set;
	
	MobilePhoneSet() {
		_set = new Myset<MobilePhone>();
	}

	MobilePhoneSet(Myset<MobilePhone> set) {
		_set = set;
	}

	public boolean isEmpty() {
		return _set.isEmpty();
	}

	public boolean isMember(MobilePhone m) {
		return _set.isMember(m);
	}

	public void insert(MobilePhone m) {
		_set.insert(m);
	}

	public void delete(MobilePhone m) throws Exception {
		_set.delete(m);
	}

	public MobilePhoneSet union(MobilePhoneSet a) {
		return new MobilePhoneSet(this._set.union(a._set));
	}

	public MobilePhoneSet intersection(MobilePhoneSet a) {
		return new MobilePhoneSet(this._set.intersection(a._set));
	}

	@Override
	public Iterator<MobilePhone> iterator() {
		return _set.iterator();
	}
}
