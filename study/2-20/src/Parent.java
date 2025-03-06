package study;

public class Parent {

	public void name() {
		System.out.println("親クラスが呼び出されました。");
	}

	public void update() {
		System.out.println("上書きされていません。");
	}

	public void argument() {
		System.out.println("引数が0つのものが呼び出されました。");
	}

	public void argument(int number) {
		System.out.println("引数が" + number + "つのものが呼び出されました。");
	}

	public void argument(int number1, int number2) {
		System.out.println("引数が" + (number1 + number2) + "つのものが呼び出されました。");
	}
}