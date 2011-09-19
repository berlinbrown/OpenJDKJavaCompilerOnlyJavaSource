public class Test {

    public void execute() {
       final int x = 1 + 2;
       System.out.println(x);
    } 
    public static void main(final String [ ] args) {
      System.out.println("Run");
      new Test().execute();
      System.out.println("Done");
        
    }
    
} // End of the Class //
