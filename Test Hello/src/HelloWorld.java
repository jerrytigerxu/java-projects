public class HelloWorld {
    int sales;

    public HelloWorld(int profit){
        sales = profit;
    }

    public static void main(String[] args) {
        System.out.println("Hello world! Testing!");

        HelloWorld test = new HelloWorld(55);

        System.out.println(test.sales);

    }



}
