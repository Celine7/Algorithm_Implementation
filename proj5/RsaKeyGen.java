//Celine Cui
//4.15.2019
import java.io.*;
import java.util.Random;

public class RsaKeyGen {
    public static void main(String[] args) {

        Random rand = new Random();
        LargeInteger p, q, n, phi, e, d, one;

        one = new LargeInteger(new byte[]{(byte) 1});
		
        do {
        	p = new LargeInteger(256, rand);
        	q = new LargeInteger(256, rand);
        	n = p.multiply(q);
        	phi = (p.subtract(one)).multiply(q.subtract(one));
            e = new LargeInteger(512, rand);
        } while(phi.subtract(e).subtract(one).isNegative() || !phi.XGCD(e)[0].equals(one)||e.subtract(one).subtract(one).isNegative());

        d = e.modularExp(one.negate(), phi);

        try {
            FileOutputStream pubkey = new FileOutputStream("pubkey.rsa");
            pubkey.write(e.resize().getVal());
            pubkey.write(n.resize().getVal());
            pubkey.close();
            FileOutputStream privkey = new FileOutputStream("privkey.rsa");
            privkey.write(d.resize().getVal());
            privkey.write(n.resize().getVal());
            privkey.close();
        } catch (Exception ee) {
            System.out.println("\tError: " + e.toString());
            System.exit(0);
        }
    }
}