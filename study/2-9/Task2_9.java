/**
* Task2-9: 課題内容
*
* ログイン機能をイメージして処理を書いてみましょう。
* ・アカウント情報としてデータベースに登録されている情報
* 　名前：alice
* 　パスワード：alice123
* 本課題では、すべての定数(合計６種類)を使いつつ、if文の使い方に慣れていきましょう。
* 問①〜問④まで回答お願いします。
*
*/
public class Task2_9 {

  // 定数（アカウント情報）
  private static final String USER_NAME = "alice";
  private static final String USER_PASSWORD = "alice123";

  // 定数（メッセージ）
  private static final String CONST_MSG_SUCCESS = "ログイン成功です。";
  private static final String CONST_MSG_ERROR_NAME = "名前に誤りがあります。";
  private static final String CONST_MSG_ERROR_PASS = "パスワードに誤りがあります。";
  private static final String CONST_MSG_ERROR_INPUT = "入力情報に誤りがあります。";

  // 補足:
  // 定数は①〜④で使い回すために定義しておきます。
  // 定数とは、プログラム内で一度値が代入されると、その値を変更できない変数です。
  // つまり、一度定義された値はプログラムの実行中に変更されることはありません。
  // これにより、プログラムの可読性や安全性を向上させることができます。
  // 定数は変数のように定義したものを後から呼び出すことが可能です。
  // 登録されている名前（USER_NAME）とパスワード（USER_PASSWORD）を定数で定義。
  //private: この修飾子は、変数やメソッドが同じクラス内からのみアクセス可能であることを示します。つまり、他のクラスからは直接アクセスできません。
  //static: この修飾子は、クラス変数やクラスメソッドを定義する際に使用されます。つまり、インスタンス化せずにアクセスできるようになります。
  // final: この修飾子は、一度初期化された後に値が変更できないことを示します。定数を表す変数やフィールドに使用され、再代入を防ぎます。


  // 「ここへ記述」部分へ適当な値を記述しましょう。

  public static void main(String[] args) {
    // 以下の変数「name」「pass」を使用して、①〜④の条件を満たす処理を記述して下さい。
    // nameとpassはログイン画面からの入力値だと想定してみましょう。

    String name = "alice";
    String pass = "alice123";

    // ① 「name」の値が「USER_NAME」と等しく、「 pass 」の値が「USER_PASSWORD」と等しい場合。
    // 定数を使用して「 ログイン成功です。 」と出力して下さい。
    if(name.equals(USER_NAME) && pass.equals(USER_PASSWORD)){
        System.out.println(CONST_MSG_SUCCESS);
    }

    // ② 「USER_NAME」の値のみ等しい場合。
    // 定数を使用して「 パスワードに誤りがあります。 」 と出力して下さい。
    else if (name.equals(USER_NAME)){
        System.out.println(CONST_MSG_ERROR_PASS);
    }

    // ③ 「USER_PASSWORD」の値のみ等しい場合。
    //定数を使用して「 名前に誤りがあります。 」と出力して下さい。
    else if (pass.equals(USER_PASSWORD)){
        System.out.println(CONST_MSG_ERROR_NAME);
    }
    // ④ 「USER_NAME」も「USER_PASSWORD」の値も間違っていた場合。
    //定数を使用して「 入力情報に誤りがあります。 」と出力して下さい。
    else {
  System.out.println(CONST_MSG_ERROR_INPUT);
}
  }
}