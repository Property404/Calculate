//Dagan Martinez 2014
//Edited in 2015 - added GPL license
//Calculator - Calculates complex expression from user


import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        //Set up objects
        Complex ans = new Complex(0, 0);
        Calculate.State state = new Calculate.State();
        Calculate.Variable initial_value = new Calculate.Variable("ans", new Real(0));
        state.variables.add(initial_value);

        //Get input and calculate expression
        while (true) {
            System.out.print(">");
            try {
                ans = Calculate.interpret(new Scanner(System.in).nextLine(), state);
            } catch (IndexOutOfBoundsException e) {
                new Calculate.Error("SYNTAX ERROR (" + e + ")");
            }
            state.changeVariable("ans", ans);
            ans.print();

        }
    }
}