package interpreter;

public class TestParse {

	public static void main(String[] args) {

		String shitload = (new SetCard("samples/sample.pdf")).toString();
		//		System.out.println((new SetCard("samples/sample.pdf")).toString());

		String[] lines = shitload.split("\\r?\\n");
		for (String o : lines) {
			System.out.println(o);
			System.out.println("xd");
		}
	}

}
