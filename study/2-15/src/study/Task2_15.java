package study;

public class Task2_15 {
		// TODO 自動生成されたメソッド・スタブ
		 // ① redとblueにstatic修飾子とアクセス修飾子を追加
		 // ① redとblueにstatic修飾子とアクセス修飾子を追加
		public static String red = "赤";
		public static String blue = "青";

	    public static void main(String[] args) {
	        // ② Bikeクラスをインスタンス化
	        // 引数なしのコンストラクタを使用してbike1とbike2をインスタンス化する
	       Bike bike1 = new Bike();
	       Bike bike2 = new Bike();
	       // ③ bike1とbike2にcheckを設定する(出力結果通りに)
	        bike1.run();
	        bike2.stop();
	        // ④ bike1とbike2のtypeを変更する(出力結果通りに)
	        bike1.check();
			System.out.println("car1は" + red + "色で" + bike1.type + "タイプです。 現在" + bike1.check());
			bike2.check();
			System.out.println("car2は" + blue + "色で" + bike2.type + "タイプです。 現在" + bike2.check());
	    }
	}

	 class Bike {
	    String color;
	    String type;
	    Boolean state;

	    // 引数なしのコンストラクタ
	    public Bike() {
	        this.color = "未設定";
	        this.type = "250cc"; // デフォルト値として250ccを設定
	    }

	    public void run() {
	        this.state = true;
	    }

	    public void stop() {
	        this.state = false;
	    }

	    public String check() {
	        if (this.state) {
	            return "走行中です。";
	        } else {
	            return "停止中です。";
	        }
	    }
	}
