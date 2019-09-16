//Celine Cui
//4.15.2019
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.security.MessageDigest;


public class RsaSign {
    public static void main(String[] args) {
        LargeInteger one = new LargeInteger(new byte[]{(byte) 1});
        LargeInteger c = one;

        // lazily catch all exceptions...
        try {
            // read in the file to hash
            Path path = Paths.get(args[1]);
            byte[] data = Files.readAllBytes(path);

            // create class instance to create SHA-256 hash
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            // process the file
            md.update(data);

            // generate a hash of the file
            byte[] digest = md.digest();
            c = new LargeInteger(digest);

        } catch (Exception e) {
            System.out.println("\tError: " + e.toString());
            System.exit(0);
        }

        if (args[0].equals("s")) {
            try {
                FileInputStream privkey = new FileInputStream("privkey.rsa");

                byte[] keybyte_d = new byte[65];
                byte[] keybyte_n = new byte[65];
                privkey.read(keybyte_d, 1, 64);
                privkey.read(keybyte_n, 1, 64);
                LargeInteger d = new LargeInteger(keybyte_d);
                LargeInteger n = new LargeInteger(keybyte_n);
                privkey.close();

                FileOutputStream sig = new FileOutputStream(args[1] + ".sig");
                //Recover m as: m = c^d(mod n)
                sig.write(c.modularExp(d, n).getVal());
                sig.close();
            } catch (FileNotFoundException e) {
                System.out.println("\tError: privkey.rsa not found");
                System.exit(0);
            } catch (IOException e) {
                System.out.println("\tError: reading file failed");
                System.exit(0);
            } catch (Exception e) {
                System.out.println("\tError: " + e.toString());
                System.exit(0);
            }
        }
        else if (args[0].equals("v")) {
            LargeInteger m = one;
            try {
                Path sig = Paths.get(args[1] + ".sig");
                m = new LargeInteger(Files.readAllBytes(sig));
            }catch (FileNotFoundException e) {
                System.out.println("\tError: .sig file not found");
                System.exit(0);
            }
            catch (Exception e) {
                System.out.println("\tError: " + e.toString());
                System.exit(0);
            }

            try{
                FileInputStream pubkey = new FileInputStream("pubkey.rsa");

                byte[] keybyte_e = new byte[65];
                byte[] keybyte_n = new byte[65];
                pubkey.read(keybyte_e, 1, 64);
                pubkey.read(keybyte_n, 1, 64);
                LargeInteger e = new LargeInteger(keybyte_e);
                LargeInteger n = new LargeInteger(keybyte_n);
                pubkey.close();
                
                //Compute the ciphertext c as: c = m^e(mod n)
                if (c.equals(m.modularExp(e, n))) System.out.println("\tSignature is valid");
                else System.out.println("\tSignature is not valid");

            }catch (FileNotFoundException e) {
                System.out.println("\tError: pubkey.rsa not found");
                System.exit(0);
            } catch (Exception e) {
                System.out.println("\tError: " + e.toString());
                System.exit(0);
            }

        }
    }
}