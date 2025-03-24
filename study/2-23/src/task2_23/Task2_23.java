package task2_23;

import java.util.ArrayList;

import constants.Constants;
import language.Student;
import person.Person;


public class Task2_23 {

	public static void main(String[] args) {
		//Personクラスのインスタンスを格納するArrayListクラス型の変数persons
		ArrayList<Person> persons = new ArrayList<Person>(); 

		//①Personクラスの変数名「yamada」というインスタンスを作成して下さい。
		//引数には1:山田太郎 2:Java を入れて下さい。
		//また「Java」は、Constants.javaのものを扱って下さい。

		//ここに解答
		Student yamada = new Student("山田太郎",Constants.LANGUAGE_JAVA);
		persons.add(yamada);
		//②作成した変数「yamada」を利用し名前を表示して下さい。
		System.out.println(yamada.getName());
		//ここに解答


		//③Personクラスの変数名「ishitani」というインスタンスを作成して下さい。
		//引数には1:石谷花子 2:HTML を入れて下さい。
		//また「HTML」は、Constants.javaのものを扱って下さい。

		//ここに解答
		Student ishitani = new Student("石谷花子",Constants.LANGUAGE_HTML);
		persons.add(ishitani);
		//④作成した変数「ishitani」を利用し学んでいる言語を表示して下さい。

		//ここに解答
		System.out.println(ishitani.getLanguage());
		//⑤「〇〇が△△を学んでいます」という表示で山田さん、石谷さんが何を学んでいるか出力して下さい。

		//ここに解答
		yamada.studyLanguage();
		ishitani.studyLanguage();
	}

}