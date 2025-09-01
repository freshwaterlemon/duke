public class Pakipaki {
    private static final String BOTNAME = "PakiPaki";
    private static final String HORIZONTAL_LINE = "____________________________________________________________";

    public static void startMsg() {
        System.out.println("Hello I am " + BOTNAME + ", your personal assistant ChatBot.");
        System.out.println("How can I help you today?");
    }

    public static void endMsg() {
        System.out.println("Thank you for chatting with me.\nGoodbye, and I look forward to our next conversation!");
    }

    public static void printHorzLine() {
        System.out.println(HORIZONTAL_LINE);
    }

    public static void main(String[] args) {
        printHorzLine();
        startMsg();
        printHorzLine();
        endMsg();
        printHorzLine();
    }
}
