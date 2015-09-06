/**
 * Created by Dagan on 11/14/2014.
 */
public class Complex {
    double real;
    double imaginary;

    Complex() {
        real = 0;
        imaginary = 0;
    }

    Complex(double r, double i) {
        real = r;
        imaginary = i;
    }

    public double getReal() {
        return real;
    }

    public double getImaginary() {
        return imaginary;
    }

    public Complex add(Complex operand) {
        return new Complex(real + operand.getReal(), imaginary + operand.getImaginary());
    }

    public Complex add(double operand) {
        return new Complex(real + operand, imaginary);
    }

    public Complex subtract(Complex operand) {
        return new Complex(real - operand.getReal(), imaginary - operand.getImaginary());
    }

    public Complex subtract(double operand) {
        return new Complex(real - operand, imaginary);
    }

    public Complex multiply(Complex operand) {
        double a = real;
        double b = imaginary;
        double c = operand.real;
        double d = operand.imaginary;
        return new Complex(a * c - b * d, a * d + b * c);
    }

    public Complex multiply(double operand) {
        return new Complex(real * operand, imaginary * operand);
    }

    public Complex divide(double operand) {
        return new Complex(real / operand, imaginary / operand);
    }

    public Complex divide(Complex operand) {//(a+bi)/(c+di)=(a+bi)(a-ci)/(c^2+d^2)
        double a = real;
        double b = imaginary;
        double c = operand.real;
        double d = operand.imaginary;
        return (new Complex(a, b).multiply(new Complex(c, -d))).divide(c * c + d * d);
    }

    public double arg() {
        if(real>0 || imaginary !=0){
            return 2*Math.atan(imaginary/(Math.sqrt(real*real+imaginary*imaginary)+real));

        }
        if(real<0 && imaginary==0){
            return Math.PI;
        }
        return Math.atan(imaginary / real);
    }

    public double abs() {
        return Math.sqrt(real * real + imaginary * imaginary);
    }

    public Complex pow(Complex operand) {
        if(imaginary==0 && real==0){
            if(operand.real==1 && operand.imaginary==0){
                return new Real(1);
            }else{
                return new Real(0);
            }
        }
        if(imaginary==0 && real>0  && operand.imaginary==0){
            return new Real(Math.pow(real,operand.real));
        }
        double a = real;
        double b = imaginary;
        double c = operand.real;
        double d = operand.imaginary;
        double cis_arg = c * new Complex(a, b).arg() + (d / 2.0) * Math.log(a * a + b * b);
        Complex cis_part = new Complex(Math.cos(cis_arg), Math.sin(cis_arg));
        double rpart = Math.pow(a * a + b * b, c / 2) * Math.exp(-d * new Complex(a, b).arg());
        return new Complex(rpart, 0).multiply(cis_part);
    }
    public Complex sqrt(){
        return this.pow(new Real(.5));
    }
    public Complex ln() {
        if(imaginary==0 && real>0){
            return new Real(Math.log(real));
        }
        if (real < 0) {
            return (new Complex(-real, -imaginary).ln()).add(new Complex(0, Math.PI));
        }
        return new Real(Math.log(abs())).add(new Complex(0, 1).multiply(arg()));
    }
    public Complex cis(){
        return (this.cos()).add((this.sin()).multiply(new Imaginary(1)));
    }
    public Complex sinh(){
        return (new Real(Math.E).pow(this).subtract(new Real(Math.E).pow(this.multiply(new Real(-1))))).divide(new Real(2));
    }
    public Complex cosh(){
        return (new Real(Math.E).pow(this).add(new Real(Math.E).pow(this.multiply(new Real(-1))))).divide(new Real(2));
    }
    public Complex sin() {
        if(imaginary==0){
            return new Real(Math.sin(real));
        }
        Complex ar = new Complex(real, imaginary);
        ar = ar.multiply(new Complex(0, -1));
        return ar.sinh().multiply(new Imaginary(1));
    }
    public Complex cos(){
        if(imaginary==0){
            return new Real(Math.cos(real));
        }
        Complex ar=new Complex(real, imaginary);
        ar=ar.multiply(new Complex(0,-1));
        return ar.cosh();
    }
    public Complex tan(){
        if(imaginary==0){
            return new Real(Math.tan(real));
        }
        return this.sin().divide(this.cos());
    }
    public Complex cot(){
        if(imaginary==0){
            return new Real(1/Math.tan(real));
        }
        return this.cos().divide(this.sin());
    }
    public Complex sec(){
        if(imaginary==0){
            return new Real(1/Math.cos(real));
        }
        return new Real(1).divide(this.cos());
    }
    public Complex csc(){
        if(imaginary==0){
            return new Real(1/Math.sin(real));
        }
        return new Real(1).divide(this.sin());
    }
    public Complex arctan(){
        if(imaginary==0){return new Real(Math.atan(real));}
        Complex inner= new Complex();
        inner=(new Imaginary(1).subtract(this)).divide(new Imaginary(1).add(this));
        return (inner.ln()).multiply(new Imaginary(-1/2.0));
    }
    public Complex arccot(){
        Complex inner= new Complex();
        inner=(new Imaginary(1).add(this)).divide(new Imaginary(-1).add(this));
        return (inner.ln()).multiply(new Imaginary(-1/2.0));
    }
    public Complex arcsin(){
        if(imaginary==0 && real>=-1 && real<=1){return new Real(Math.asin(real));}
        Complex denominator=(new Real(1).subtract(this.pow(new Real(2)))).pow(new Real(.5));
        Complex inner=this.divide(denominator);
        return inner.arctan();
    }
   public Complex arccos(){
       if(imaginary==0 && real>=-1 && real<=1){return new Real(Math.acos(real));}
       return new Real(Math.PI/2).subtract(this.arcsin());
    }
    public Complex arcsec(){
        return (new Real(1).divide(this)).arccos();
    }
    public Complex arccsc(){
        return (new Real(1).divide(this)).arcsin();
    }
    public Complex arsinh(){
        Complex in=this.pow(new Real(2)).add(new Real(1));
        in=in.sqrt();
        Complex inner=this.add(in);
        return inner.ln();
    }
    public Complex arcsch(){
        return (new Real(1).divide(this)).arsinh();
    }
    public Complex arsech(){
        return (new Real(1).divide(this)).arcosh();
    }
    public Complex arcosh(){
        Complex in=this.pow(new Real(2)).subtract(new Real(1));
        in=in.sqrt();
        Complex inner=this.add(in);
        return inner.ln();
    }

    //cosh(ln(-e+sqrt(e^2-1)))=-2.718281828

    public void print() {
        String output="";
        double r=real;
        double i=imaginary;
        double tiny=.000000000000001;
        if (r<tiny && r>-tiny){
            r=0;
        }
        if (i<tiny && i>-tiny){
            i=0;
        }
        if(r!=0){
            if(r==Math.round(r)){
                output+=Math.round(r);
            }else {
                output += r;
            }
            if(i>0){
                if(i==1){
                    output+="+i";
                }else {
                    output += "+" + i + "i";
                }
            }
            if(i<0){
                if(i==-1){
                    output+="-i";
                }else {
                    output += i + "i";
                }
            }
        }else{
            if(imaginary==0){
                output="0";
            }else if(imaginary==1){
                output="i";
            }else if(imaginary==-1){
                output="-i";
            }else{
                output=""+imaginary+"i";
            }
        }
        System.out.println(output);
    }
}