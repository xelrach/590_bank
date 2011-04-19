class UnitTest{
	private static boolean testSnapshotParse() {
		boolean good = true;
		String s = "00001.1 4 b 00001.01 12.3 00001.02 14.5 p 00002.22 00001.01 10.1";
		good = good & testSnapshotParseActual(s);
		s = "00002.10 19 b p";
		good = good & testSnapshotParseActual(s);
		s = "00002.10 19 b p";
		good = good & testSnapshotParseActual(s);
		return good;
	}

	private static boolean testSnapshotParseActual(String input) {
		boolean good = true;
		Snapshot result = Snapshot.parseSnap(input);
		if ( !input.equals(result.getMessage()) ) {
			good = false;
		}
		return good;
	}

	public static void main(String[] agrs) {
		if (testSnapshotParse()) {
			System.out.println("Snapshot Parsing Test Result: PASS");
		} else {
			System.out.println("Snapshot Parsing Test Result: FAIL");
		}
	}
}
