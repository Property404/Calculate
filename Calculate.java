import java.util.ArrayList;

public class Calculate {

    //Call constructor everytime an error occurs
    //This is subject to change depending on context
    //E.g, if you're not doing a CLI app, maybe force an error
    //on screen
    static class Error {
        Error(String msg) {
            System.out.println("ERROR: " + msg);
        }
    }
    static class Warning {
        Warning(String msg) {
            System.out.println("ERROR: " + msg);
        }
    }

    //This will be used for all variables
    static class Variable {
        String name;
        Complex value;

        Variable(String nom, int val) {
            name = nom;
            value.real=val;
            value.imaginary=0;
        }

        Variable(String nom, double val) {
            name = nom;
            value.real=val;
            value.imaginary=0;
        }

        Variable(String nom, String val) {
            name = nom;
            value.real = Double.valueOf(val);
            value.imaginary=0;
        }
        Variable(String nom, Complex val) {
            name = nom;
            value=val;
        }
    }


    //This represents the "state" of interpret
    //allows for recursive interpretation
    static class State {
        ArrayList<Variable> variables = new ArrayList<Variable>();
        //String source;//This is unnecessary

        void addVariable(Variable var) {
            variables.add(var);
        }

        void addVariable(String nom, int val) {
            variables.add(new Variable(nom, val));
        }

        void addVariable(String nom, double val) {
            variables.add(new Variable(nom, val));
        }

        void addVariable(String nom, String val) {
            variables.add(new Variable(nom, val));
        }

        void addVariable(String nom, Complex val) {
            variables.add(new Variable(nom, val));
        }
        void changeVariable(String nom, Complex val) {
            for (Variable i : variables) {
                if (i.name.equals(nom)) {
                    i.value = val;
                        return;
                    }
                }
                new Warning("changeVariable: No variable named " + nom);
                addVariable(nom, val);
            }

            Variable getVariable(String nom) {
                for (Variable i: variables) {
                    if (i.name.equals(nom)) {
                        return i;
                    }
                }
                new Warning("getVariable: No variable named " + nom);
                return new Variable(nom, "0");
            }
        }

        //Create static function Interpret
        //Calculates based on input
    static Complex interpret(String source, State state){
        String original_source=source;
        int oplevel=0;//operator level
        int it=0;
        int plevel=0;//parenthesis level
        //Operation order (in reverse of PEMDAS)
        final String[] oplist={"addition","multiplication","expo"};

        //Queue for operations
        ArrayList<String>operation_queue=new ArrayList<String>();

        //Main loop
        while(oplevel<oplist.length){
            if(it>=source.length()){
                if(operation_queue.size()==0){
                    oplevel++;
                    it=0;
                }else{
                    break;
                }
            }
            if(source.charAt(it)=='('){
                plevel++;it++;continue;
            }
            if(source.charAt(it)==')'){
                plevel--;

                if(plevel<0){new Error("Parenthesis level is negative: "+original_source);}
                it++;
                continue;
            }
            if(plevel==0 && (source.charAt(it)=='^' || source.charAt(it)=='+' || source.charAt(it)=='%' || source.charAt(it)=='-' || source.charAt(it)=='/' ||source.charAt(it)=='*')){
                if(oplist[oplevel].equals("addition") && ((source.charAt(it)=='-') || source.charAt(it)=='+')){
                    if(!source.substring(0,it).equals("")) {
                        operation_queue.add(source.substring(0, it));
                    }
                    if(source.charAt(it)=='+'){
                        operation_queue.add("+");
                    }else if(source.charAt(it)=='-'){
                        operation_queue.add("-");
                    }
                    source=source.substring(it+1,source.length());
                    it=0;continue;
                }
                if(oplist[oplevel].equals("multiplication") && ((source.charAt(it)=='*') || source.charAt(it)=='/' || source.charAt(it)=='%')){
                    operation_queue.add(source.substring(0,it));
                    if(source.charAt(it)=='*'){
                        operation_queue.add("*");
                    }else if(source.charAt(it)=='/'){
                        operation_queue.add("/");
                    } else if(source.charAt(it)=='%'){
                        operation_queue.add("%");
                    }
                    source=source.substring(it+1,source.length());
                    it=0;continue;
                }
                if(oplist[oplevel].equals("expo") && source.charAt(it)=='^'){
                    operation_queue.add(source.substring(0,it));
                    if(source.charAt(it)=='^'){
                        operation_queue.add("^");
                    }
                    source=source.substring(it+1,source.length());
                    it=0;continue;
                }
            }
            it++;//continue iteration
            //if loop doesn't find criteria for one level of OoO,
            //Go to the next level
            //else evaluate this level. break is positive

        }
        //End main loop


        //Part 2: Evaluation begins

        //Run through operations
        if(operation_queue.size()!=0){
            operation_queue.add(source);
            it=1;
            Complex value=new Complex();
            if(operation_queue.get(0).equals("-")){
                it++;
                value=interpret(operation_queue.get(1),state).multiply(new Real(-1));
            }else{
                value=interpret(operation_queue.get(0),state);
            }

            while(it<operation_queue.size()){
                if(operation_queue.get(it).equals("+")){value=value.add(interpret(operation_queue.get(it+1),state)); }
                if(operation_queue.get(it).equals("-")){value=value.subtract(interpret(operation_queue.get(it+1),state)); }
                if(operation_queue.get(it).equals("*")){value=value.multiply(interpret(operation_queue.get(it+1),state)); }
                if(operation_queue.get(it).equals("/")){value=value.divide(interpret(operation_queue.get(it+1),state)); }
                if(operation_queue.get(it).equals("^")){value=value.pow(interpret(operation_queue.get(it+1),state)); }
                it+=2;//continue;
            }
            return value;
        }


        //Check is source is parenthetical
        if(source.charAt(0)=='('){
            if(source.charAt(source.length()-1)==')'){
                return interpret(source.substring(1,source.length()-1),state);
            }else{new Error("Incomplete parenthesis");}
        }

        //Check if variable or function
        if((source.charAt(0)>=65 && source.charAt(0)<=90) || (source.charAt(0)>=97 && source.charAt(0)<=122) || source.charAt(0)=='_') {//i.e check for alphabetic or underscore
            it=0;
            boolean is_var=true;
            boolean is_func=false;
            while(it<source.length()){
                if(!Character.isLetterOrDigit(source.charAt(it)) && source.charAt(it)!='_'){
                    is_var=false;
                    if(source.charAt(it)=='('){
                        is_func=true;
                        break;
                    }else{
                        new Error("ALPHANUMERIC ENTITY IS NOT VARIABLE OR FUNCTION");
                    }
                }
                it++;
            }
            if(is_var){
                if(source.equals("pi")){return new Real(Math.PI);}
                if(source.equals("tau")){return new Real(Math.PI*2);}
                if(source.equals("phi")){return new Real((Math.sqrt(5)+1.0)/2.0);}
                if(source.equals("e")){return new Real(Math.E);}
                if(source.equals("i")){return new Imaginary(1);}
                if(source.equals("j")){return new Imaginary(1);}
                if(source.equals("googol")){return new Real(Math.pow(10,100));}
                return interpret(state.getVariable(source).value.real+"+("+state.getVariable(source).value.imaginary+")*i",state);
            }
            if(is_func){
                String func_name=source.substring(0,it);
                String func_args=source.substring(it+1,source.length()-1);
                //Logs and exps
                if(func_name.equals("ln")){return interpret(func_args,state).ln(); }
                if(func_name.equals("log")){return interpret(func_args,state).ln().divide(new Real(Math.log(10))); }
                if(func_name.equals("exp")){return new Real(Math.E).pow(interpret(func_args,state));}
                if(func_name.equals("cis")){return interpret(func_args,state).cis();}
                if(func_name.equals("sqrt")){return interpret(func_args,state).sqrt();}

                //Abs and sign
                if(func_name.equals("abs")){return new Real(interpret(func_args,state).abs());}
                if(func_name.equals("sgn")){
                    if(interpret(func_args,state).real==interpret(func_args,state).imaginary && interpret(func_args,state).real==0){
                        return new Real(0);
                    }
                    return interpret(func_args,state).divide(new Real(interpret(func_args,state).abs()));}


                //Complex Specific
                if(func_name.equals("cis")){return interpret(func_args,state).cis();}
                if(func_name.equals("arg")){return new Real(interpret(func_args,state).arg());}
                if(func_name.equals("real")){return new Real(interpret(func_args,state).real);}
                if(func_name.equals("imaginary")){return new Real(interpret(func_args,state).imaginary);}


                //Hyperbolic functions
                if(func_name.equals("sinh")){return interpret(func_args,state).sinh(); }
                if(func_name.equals("cosh")){return interpret(func_args,state).cosh(); }
                if(func_name.equals("tanh")){return interpret(func_args,state).sinh().divide(interpret(func_args,state).cosh()); }
                if(func_name.equals("coth")){return interpret(func_args,state).cosh().divide(interpret(func_args,state).sinh()); }
                if(func_name.equals("csch")){return new Real(1).divide(interpret(func_args,state).sinh()); }
                if(func_name.equals("sech")){return new Real(1).divide(interpret(func_args,state).cosh()); }

                //Trig functiosns
                if(func_name.equals("sin")){return interpret(func_args,state).sin(); }
                if(func_name.equals("cos")){return interpret(func_args,state).cos(); }
                if(func_name.equals("tan")){return interpret(func_args,state).tan(); }
                if(func_name.equals("cot")){return interpret(func_args,state).cot(); }
                if(func_name.equals("csc")){return interpret(func_args,state).csc(); }
                if(func_name.equals("sec")){return interpret(func_args,state).sec(); }

                //Inverse Trig functions
                if(func_name.equals("arctan")){return interpret(func_args,state).arctan();}
                if(func_name.equals("arccot")){return interpret(func_args,state).arccot();}
                if(func_name.equals("arcsin")){return interpret(func_args,state).arcsin();}
                if(func_name.equals("arccos")){return interpret(func_args,state).arccos();}
                if(func_name.equals("arccsc")){return interpret(func_args,state).arccsc();}
                if(func_name.equals("arcsec")){return interpret(func_args,state).arcsec();}

                //Inverse Hyperbolic functions
                if(func_name.equals("arsinh")){return interpret(func_args,state).arsinh();}
                if(func_name.equals("arcsch")){return interpret(func_args,state).arcsch();}
                if(func_name.equals("arcosh")){return interpret(func_args,state).arcosh();}
                if(func_name.equals("arsech")){return interpret(func_args,state).arsech();}

                //converting
                if(func_name.equals("rad")){return interpret(func_args,state).multiply(new Real(Math.PI/180));}
                if(func_name.equals("deg")){return interpret(func_args,state).divide(new Real(Math.PI/180));}
                //Misc
                if(func_name.equals("dgn")){return interpret(func_args,state).pow(interpret(func_args,state));}
                if(func_name.equals("mar")){return interpret(func_args,state).pow(interpret(func_args,state).pow(new Real(-1)));}
                //Behold! Relics of a distance past!
                //if(func_name.equals("log")){return Math.log10(interpret(func_args,state)); }
                //if(func_name.equals("exp")){return Math.exp(interpret(func_args,state)); }
                /*
                //Inverse trig functions
                if(func_name.equals("arcsin")){return Math.asin(interpret(func_args,state)); }
                if(func_name.equals("arccos")){return Math.acos(interpret(func_args,state)); }
                if(func_name.equals("arctan")){return Math.atan(interpret(func_args,state)); }
                if(func_name.equals("arcsec")){return Math.acos(1.0/interpret(func_args,state)); }
                if(func_name.equals("arcsin")){return Math.asin(1.0/interpret(func_args,state)); }
                if(func_name.equals("arccot")){return Math.atan(1.0/interpret(func_args,state)); }

                //Powers and roots
                if(func_name.equals("sqrt")){return Math.sqrt(interpret(func_args,state)); }

                //Abs and sgn
                if(func_name.equals("abs")){return Math.abs(interpret(func_args,state));}
                if(func_name.equals("sgn")){return Math.signum(interpret(func_args,state));}

                //Radians/Degrees
                if(func_name.equals("rad")){return Math.toRadians(interpret(func_args,state));}
                if(func_name.equals("deg")){return Math.toDegrees(interpret(func_args,state));}

                //Rounding
                if(func_name.equals("floor")){return Math.floor(interpret(func_args,state));}
                if(func_name.equals("ceil")){return Math.ceil(interpret(func_args,state));}
                if(func_name.equals("round")){return Math.round(interpret(func_args,state));}
                */
                new Error("NO SUCH FUNCTION \""+func_name+"\"");

            }
            //IF function
        }

        //Return Number. Don't recurse
        if((source.charAt(0)>48 && source.charAt(0)<58) || source.charAt(0)=='0' || source.charAt(0)=='.'){
            return new Real(Double.valueOf(source));
        }
        new Error("didn't catch source");
        return new Real(0);
    }

    //static double interpret(String source){
    //     return interpret(source, new State());
    // }
}
