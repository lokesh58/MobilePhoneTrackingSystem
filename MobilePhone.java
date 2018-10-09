public class MobilePhone {
	private int _id;
	private boolean _switchedOn;
	private Exchange _location;

	MobilePhone(int number) {
		_id = number;
		_switchedOn = false;
		_location = null;
	}

	public int number() {
		return _id;
	}

	public boolean status() {//returns true if switched on and false if switched off
		return _switchedOn;
	}

	public void setLocation(Exchange location) throws Exception {
		if (_switchedOn) {
			if (_location != null) {
				_location.removeResident(this);
			}
			_location = location;
			_location.addResident(this);
		} else {
			throw new Exception("Error - Mobile phone with identifier " + _id + " is currently switched off");
		}
	}

	public void switchOn() throws Exception {
		if (_switchedOn) {
			throw new Exception("Error - Mobile phone with identifier " + _id + " is already switched on");
		} else {
			_switchedOn = true;
		}
	}

	public void switchOff() throws Exception {
		if (_switchedOn) {
			_location.removeResident(this);
			_location = null;
			_switchedOn = false;
		} else {
			throw new Exception("Error - Mobile phone with identifier " + _id + " is already switched off");
		}
	}

	public Exchange location() throws Exception {
		if (!_switchedOn) {
			throw new Exception("Error - Mobile phone with identifier " + _id + " is currently switched off");
		} else {
			return _location;
		}
	}

	public boolean equals(MobilePhone other) {
		return this._id == other._id;
	}
}
