//Celine Cui
//2.16.2019
/*************************************************************************
 *  Compilation:  javac LZW.java
 *  Execution:    java LZW - < input.txt   (compress)
 *  Execution:    java LZW + < input.txt   (expand)
 *  Dependencies: BinaryIn.java BinaryOut.java
 *
 *  Compress or expand binary input from standard input using LZW.
 *
 *  WARNING: STARTING WITH ORACLE JAVA 6, UPDATE 7 the SUBSTRING
 *  METHOD TAKES TIME AND SPACE LINEAR IN THE SIZE OF THE EXTRACTED
 *  SUBSTRING (INSTEAD OF CONSTANT SPACE AND TIME AS IN EARLIER
 *  IMPLEMENTATIONS).
 *
 *  See <a href = "http://java-performance.info/changes-to-string-java-1-7-0_06/">this article</a>
 *  for more details.
 *
 *************************************************************************/

public class MyLZW {
    private static final int R = 256;        // number of input chars
    private static final int MAX_LENGTH = 16;         // codeword width
    private static final int MIN_LENGTH = 9;    
    private static int W = 9;
    private static int L = (int)Math.pow(2,W);  
    private static char mode = 'n';
    private static int uncompressed_data_size = 0;
    private static int compressed_data_size = 0;
    private static double old_ratio = 0.0;
    private static double new_ratio = 0.0;
    private static final double ratio_threshold = 1.1;
    private static boolean reset = true;

    public static void compress() { 
        String input = BinaryStdIn.readString();
        TST<Integer> st = reset_compress();

        int code = R+1;  // R is codeword for EOF
        BinaryStdOut.write(mode,8);
        while (input.length() > 0) {
            String s = st.longestPrefixOf(input);  // Find max prefix match s.
            
            BinaryStdOut.write(st.get(s), W);   // Print s's encoding. 

            int t = s.length();
            uncompressed_data_size += t*8; //length of the String times size of char
            compressed_data_size += W; //w-bits codeword adding to the file (compressed)
  
            if (t < input.length() && code < L){ // Add s to symbol table.
                //System.out.println("codebook: "+input.substring(0, t + 1) + " code: "+ code);
                st.put(input.substring(0, t + 1), code++);
            	old_ratio = (double)uncompressed_data_size/compressed_data_size;
            }else if(t < input.length() && code >= L){
                if(W < MAX_LENGTH){
                    W++;
                    L = (int) Math.pow(2, W);
                    st.put(input.substring(0, t + 1), code++);
                }else{
                    if(mode == 'r'){
                        st = reset_compress();
                        code = R+1;
                        W = MIN_LENGTH; 
                        L = (int) Math.pow(2, W);
                        st.put(input.substring(0, t + 1), code++);
                    }
                    else if(mode == 'm'){
                        new_ratio = (double)uncompressed_data_size/compressed_data_size;
                        if(old_ratio/new_ratio > ratio_threshold){
                            st = reset_compress();
//                             System.out.println("reset: " + old_ratio/new_ratio);
                            old_ratio = 0.0;
                            code = R+1;
                            W = MIN_LENGTH;
                            L = (int) Math.pow(2, W);
                            st.put(input.substring(0, t + 1), code++);
                        }
                    }  
                }
                
            }   

            input = input.substring(t);            // Scan past s in input.
        }
        BinaryStdOut.write(R, W);
        BinaryStdOut.close();
    } 


    public static TST<Integer> reset_compress(){
        TST<Integer> temp = new TST<Integer>();
        //initialize dictionary
        for (int i = 0; i < R; i++)
            temp.put("" + (char) i, i);
        return temp;
    }

    public static String[] reset_expand(){
        String[] st = new String[(int)Math.pow(2,MAX_LENGTH)];
        int i;  // next available codeword value
        // initialize symbol table with all 1-character strings
        for (i = 0; i < R; i++){
            st[i] = "" + (char) i;
        }
        st[i++] = "";   // (unused) lookahead for EOF
        return st;
    }

    public static void expand() {
        mode=BinaryStdIn.readChar(8);
        String[] st = reset_expand();
        int i = R + 1;

        int codeword = BinaryStdIn.readInt(W);
        if (codeword == R) return;           // expanded message is empty string      
        String val = st[codeword];	//should be a single character at the first time

        while (true) {

            BinaryStdOut.write(val);
            
            uncompressed_data_size += val.length()*8; 
            compressed_data_size += W;         
            
            if(i >= L){
                if(W < MAX_LENGTH){
                    W++;
                    L = (int)Math.pow(2, W);
                }else {
                    if(mode == 'r'){
                        st = reset_expand();
                        W = MIN_LENGTH;
                        L = (int) Math.pow(2,W);
                        i = R+1;
                }
                else if(mode == 'm'){
                    new_ratio = (double)uncompressed_data_size/compressed_data_size;
                    if(old_ratio/new_ratio>ratio_threshold){
                        st = reset_expand();
                        old_ratio = 0.0;
                        i = R+1;
                        W = MIN_LENGTH;
                        L = (int)Math.pow(2,W);
                    }
                       
                }
                }
            }

            codeword = BinaryStdIn.readInt(W);    
            if (codeword == R) break;
            String s = st[codeword];
			if (i == codeword) s = val + val.charAt(0);   // special case hack //only happens when the codeword is just added, so it must be the previous codeword adding the first character of itself.
            if (i < L){
				st[i++] = val + s.charAt(0); //val is the previous codeword and add the first character of the current codeword
            	old_ratio = (double)uncompressed_data_size/compressed_data_size;
        	}

            val = s;
        }
        BinaryStdOut.close();
    }

    public static void main(String[] args) {
        if(args.length >= 2){ 
            if(args[1].length() == 1 && (args[1].equals("m") || args[1].equals("n") || args[1].equals("r"))){
                mode = args[1].charAt(0);
            }else{
                throw new IllegalArgumentException("Invalid compression mode");
            }
        }
        if (args[0].equals("-")) compress();   
        else if (args[0].equals("+")) expand();
        else throw new IllegalArgumentException("Illegal command line argument");
    }


}
