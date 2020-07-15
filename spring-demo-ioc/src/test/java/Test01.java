public class Test01 {
    public static void main(String[] args) {
        char[] chars = "AALLL".toCharArray();
        chars[0] += 32;
        System.out.println(String.valueOf(chars));
    }
}
