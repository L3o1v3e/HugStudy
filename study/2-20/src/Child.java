package study;

	//①Parent（親クラス）を継承して下さい
	public class Child  extends Parent{

	//②親クラスのnameメソッドを呼び出すcallNameメソッドを作成して下さい。
	public void callname() {
		super.name();
	}

	//③親クラスのupdateメソッドをオーバーライドして、updateメソッドが呼び出された際に、
	//「上書きされました。」と表示されるようにして下さい。
	public void update() {
		System.out.println("上書きされました。");
	}
}