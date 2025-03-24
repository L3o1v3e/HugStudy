package person;

public class Person {

	private String name;
    private String language;

    public Person(String name, String language) {
        this.name = name;
        this.language = language;
    }

//    protected String getName() {
    public String getName() {
        return name;
    }

//    protected String getLanguage() {
    public String getLanguage() {
        return language;
    }

    public void introduce() {
        System.out.println("僕の名前は" + name + "です。");
    }
    
    public void studyLanguage() {
        System.out.println("不正な値です。");
    }
}