import java.util.Iterator;

public class RoutingMapTree {
	private Exchange _root;
	private MobilePhoneSet _offMobilePhoneSet;

	RoutingMapTree() {
		_root = new Exchange(0);
		_offMobilePhoneSet = new MobilePhoneSet();
	}

	RoutingMapTree(Exchange root) {
		_root = root;
		_offMobilePhoneSet = new MobilePhoneSet();
	}
	
	public Exchange root() {
		return _root;
	}
	
	public boolean isEmpty() {
		return _root == null;
	}
	
	public int height() {
		if (_root.numChildren() == 0) {
			return 0;
		} else {
			int h = 0;
			for (int i = 0; i < _root.numChildren(); ++i) {
				int h2;
				try {
					h2 = _root.subtree(i).height();
				} catch (Exception e) {
					h2 = 0;
				}
				if (1+h2 > h) {
					h = 1+h2;
				}
			}
			return h;
		}
	}
	
	public Exchange findNodeById(int id) throws Exception {
		Exchange node;
		if (_root == null) {
			node = null;
		} else {
			node = findNodeById(id, _root);
		}
		if (node == null) {
			throw new Exception("Error - No exchange with identifier "+id+" found in the network");
		}
		return node;
	}

	private Exchange findNodeById(int id, Exchange curr) {
		if (curr.id() == id) {
			return curr;
		} else {
			Iterator<Exchange> it = curr.children();
			while (it.hasNext()) {
				Exchange e = findNodeById(id, it.next());
				if (e != null) {
					return e;
				}
			}
			return null;
		}
	}
	
	public MobilePhone findPhoneById(int id) throws Exception {
		MobilePhoneSet searchSet = _root.residentSet().union(_offMobilePhoneSet);
		Iterator<MobilePhone> it = searchSet.iterator();
		while (it.hasNext()) {
			MobilePhone m = it.next();
			if (m.number() == id) {
				return m;
			}
		}
		throw new Exception("Error - No mobile phone with identifier "+id+" found in the network");
	}

	public boolean containsNode(Exchange a) {
		return findNodeById(a.id(), _root) != null;
	}

	public void addNode(Exchange a, Exchange b) throws Exception {
		if (this.containsNode(b)) {
			throw new Exception("Error - Exchange with identifier "+b.id()+" already exists in the network");
		}
		a.addChild(b);
		b.setParent(a);
	}

	public void switchOn(MobilePhone a, Exchange b) throws Exception {
		a.switchOn();
		a.setLocation(b);
		try {
			_offMobilePhoneSet.delete(a);
		} catch (Exception e) {
			//DO NOTHING as MobilePhone a not in set
		}
	}

	public void switchOff(MobilePhone a) throws Exception {
		a.switchOff();
		_offMobilePhoneSet.insert(a);
	}

	public Exchange queryNthChild(Exchange a, int b) throws Exception {
		return a.child(b);
	}

	public MobilePhoneSet queryMobilePhoneSet(Exchange a) {
		return a.residentSet();
	}

	public Exchange findPhone(MobilePhone m) throws Exception {
		return m.location();
	}
	
	public Exchange lowestRouter(Exchange a, Exchange b) {
		Myset<Exchange> set = new Myset<Exchange>();
		while (!a.equals(_root)) {
			set.insert(a);
			a = a.parent();
		}
		while (!b.equals(_root)) {
			if (set.isMember(b)) {
				return b;
			}
			b = b.parent();
		}
		return _root;
	}

	public ExchangeList routeCall(MobilePhone m1, MobilePhone m2) throws Exception {
		Exchange a = findPhone(m1), b = findPhone(m2), lca = lowestRouter(a, b);
		ExchangeList path = new ExchangeList(), b_path = new ExchangeList();
		while (!a.equals(lca)) {
			path.insertRear(a);
			a = a.parent();
		}
		path.insertRear(lca);
		while (!b.equals(lca)) {
			b_path.insertFront(b);
			b = b.parent();
		}
		while (!b_path.isEmpty()) {
			path.insertRear(b_path.deleteFront());
		}
		return path;
	}

	public void movePhone(MobilePhone a, Exchange b) throws Exception {
		a.setLocation(b);
	}

	public String performAction(String actMsg) {
		String[] tokens = actMsg.trim().split("\\s+");
		String ret = "";
		int a, b;
		if (tokens[0].equals("findPhone")) {
		    actMsg = "queryF"+actMsg.substring(1);
		    tokens[0] = "queryFindPhone";
		} else if (tokens[0].equals("lowestRouter")) {
		    actMsg = "queryL"+actMsg.substring(1);
		    tokens[0] = "queryLowestRouter";
		} else if (tokens[0].equals("findCallPath")) {
		    actMsg = "queryF"+actMsg.substring(1);
		    tokens[0] = "queryFindCallPath";
		}
		try {
			if (tokens[0].equals("addExchange")) {
				if (tokens.length < 3) {
					throw new Exception("Error - Insufficient arguments");
				} else if (tokens.length > 3) {
					throw new Exception("Error - Too many arguments");
				}
				try {
					a = Integer.parseInt(tokens[1]);
				} catch (Exception e) {
					throw new Exception("Error - Argument #1 must be integer");
				}
				try {
					b = Integer.parseInt(tokens[2]);
				} catch (Exception e) {
					throw new Exception("Error - Argument #2 must be integer");
				}
				addNode(findNodeById(a), new Exchange(b));
			} else if (tokens[0].equals("switchOnMobile")) {
				if (tokens.length < 3) {
					throw new Exception("Error - Insufficient arguments");
				} else if (tokens.length > 3) {
					throw new Exception("Error - Too many arguments");
				}
				try {
					a = Integer.parseInt(tokens[1]);
				} catch (Exception e) {
					throw new Exception("Error - Argument #1 must be integer");
				}
				try {
					b = Integer.parseInt(tokens[2]);
				} catch (Exception e) {
					throw new Exception("Error - Argument #2 must be integer");
				}
				MobilePhone m;
				try {
					m = findPhoneById(a);
				} catch (Exception e) {
					m = new MobilePhone(a);
				}
				switchOn(m, findNodeById(b));
			} else if (tokens[0].equals("switchOffMobile")) {
				if (tokens.length < 2) {
					throw new Exception("Error - Insufficient arguments");
				} else if (tokens.length > 2) {
					throw new Exception("Error - Too many arguments");
				}
				try {
					a = Integer.parseInt(tokens[1]);
				} catch (Exception e) {
					throw new Exception("Error - Argument #1 must be integer");
				}
				switchOff(findPhoneById(a));
			} else if (tokens[0].equals("queryNthChild")) {
				if (tokens.length < 3) {
					throw new Exception("Error - Insufficient arguments");
				} else if (tokens.length > 3) {
					throw new Exception("Error - Too many arguments");
				}
				try {
					a = Integer.parseInt(tokens[1]);
				} catch (Exception e) {
					throw new Exception("Error - Argument #1 must be integer");
				}
				try {
					b = Integer.parseInt(tokens[2]);
				} catch (Exception e) {
					throw new Exception("Error - Argument #2 must be integer");
				}
				ret = actMsg+": "+queryNthChild(findNodeById(a), b).id();
			} else if (tokens[0].equals("queryMobilePhoneSet")) {
				if (tokens.length < 2) {
					throw new Exception("Error - Insufficient arguments");
				} else if (tokens.length > 2) {
					throw new Exception("Error - Too many arguments");
				}
				try {
					a = Integer.parseInt(tokens[1]);
				} catch (Exception e) {
					throw new Exception("Error - Argument #1 must be integer");
				}
				Iterator<MobilePhone> it = queryMobilePhoneSet(findNodeById(a)).iterator();
				ret = actMsg+": ";
				if (!it.hasNext()) {
					ret += "<EMPTY>";
				}
				while (it.hasNext()) {
					ret += it.next().number();
					if (it.hasNext()) {
						ret += ", ";
					}
				}
			} else if (tokens[0].equals("queryFindPhone")) {
				if (tokens.length < 2) {
					throw new Exception("Error - Insufficient arguments");
				} else if (tokens.length > 2) {
					throw new Exception("Error - Too many arguments");
				}
				try {
					a = Integer.parseInt(tokens[1]);
				} catch (Exception e) {
					throw new Exception("Error - Argument #1 must be integer");
				}
				ret = actMsg+": "+findPhone(findPhoneById(a)).id();
			} else if (tokens[0].equals("queryLowestRouter")) {
				if (tokens.length < 3) {
					throw new Exception("Error - Insufficient arguments");
				} else if (tokens.length > 3) {
					throw new Exception("Error - Too many arguments");
				}
				try {
					a = Integer.parseInt(tokens[1]);
				} catch (Exception e) {
					throw new Exception("Error - Argument #1 must be integer");
				}
				try {
					b = Integer.parseInt(tokens[2]);
				} catch (Exception e) {
					throw new Exception("Error - Argument #2 must be integer");
				}
				ret = actMsg+": "+lowestRouter(findNodeById(a), findNodeById(b)).id();
			} else if (tokens[0].equals("queryFindCallPath")) {
				if (tokens.length < 3) {
					throw new Exception("Error - Insufficient arguments");
				} else if (tokens.length > 3) {
					throw new Exception("Error - Too many arguments");
				}
				try {
					a = Integer.parseInt(tokens[1]);
				} catch (Exception e) {
					throw new Exception("Error - Argument #1 must be integer");
				}
				try {
					b = Integer.parseInt(tokens[2]);
				} catch (Exception e) {
					throw new Exception("Error - Argument #2 must be integer");
				}
				ExchangeList path = routeCall(findPhoneById(a), findPhoneById(b));
				Iterator<Exchange> it = path.iterator();
				ret = actMsg+": ";
				if (!it.hasNext()) {
					ret += "<EMPTY>";
				}
				while (it.hasNext()) {
					ret += it.next().id();
					if (it.hasNext()) {
						ret += ", ";
					}
				}
			} else if (tokens[0].equals("movePhone")) {
				if (tokens.length < 3) {
					throw new Exception("Error - Insufficient arguments");
				} else if (tokens.length > 3) {
					throw new Exception("Error - Too many arguments");
				}
				try {
					a = Integer.parseInt(tokens[1]);
				} catch (Exception e) {
					throw new Exception("Error - Argument #1 must be integer");
				}
				try {
					b = Integer.parseInt(tokens[2]);
				} catch (Exception e) {
					throw new Exception("Error - Argument #2 must be integer");
				}
				movePhone(findPhoneById(a), findNodeById(b));
			} else {
				throw new Exception("Error - Invalid query");
			}
		} catch (Exception e) {
			ret = actMsg+": "+e.getMessage();
		}
		return ret;
	}
}
