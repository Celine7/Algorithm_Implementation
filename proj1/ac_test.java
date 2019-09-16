//Celine Cui
//1.30.2019
import java.util.Scanner;
import java.util.ArrayList;
import java.io.File;
import java.io.BufferedWriter;
import java.io.FileWriter;
public class ac_test {
    public static void main(String[] args) {
        boolean firstTime = true;
        DLB dictionary_trie = new DLB();
        DLB user_trie = new DLB();
       dictionary_trie.addDictionary("dictionary.txt");
       System.out.print("Enter your first character: ");
       char my_char = ask_input();
       if(my_char == '!'){
         System.out.println("You haven't typed any word.\nBye!");
         System.exit(0);
       }
       StringBuilder prefix = new StringBuilder();
       ArrayList<String> total_prediction= new ArrayList<>(5);
       ArrayList<String> user_prediction= new ArrayList<>(5);
       ArrayList<String> dic_prediction = new ArrayList<>(5);
       int time = 0;
       float totalTime = 0;
       boolean isdigit = true;

       File file = new File("user_history.txt");
       if(file.exists()){
         file.delete();
       }
     while(true){
           while(Character.isDigit(my_char)){
             int a = Character.getNumericValue(my_char);
               while(a <= 0 || a > 5 ){
                 System.out.print("\nPlease enter a number between 0 and 5 or a next character: ");
                 my_char = ask_input();
                 exit_program(my_char, totalTime, time);
                 if(my_char == '$'){
                   my_char = encounter_dollar_sign(prefix.toString());
                   user_trie.addWord(prefix.toString());
                   firstTime = false;
                   dictionary_trie.current = dictionary_trie.root;
                   user_trie.current = user_trie.root;
                   user_trie.prefix = "";
                 dictionary_trie.prefix = "";
                 prefix = new StringBuilder();
                 }
                 a = Character.getNumericValue(my_char);
                 if(!Character.isDigit(my_char)){
                   isdigit = false;
                   break;
                 }
               }
               if(!isdigit){
                 break;
               }

                 System.out.println("\n\nWORD COMPLETED: " + total_prediction.get(a-1));
                 writing_file(total_prediction.get(a-1));
                 user_trie.addWord(total_prediction.get(a-1));
                 System.out.print("\nEnter first character of the next word(or enter number to choose from the previous list): ");
                 firstTime = false;
                 my_char = ask_input();
                 exit_program(my_char, totalTime, time);
                 while(my_char == '$'){
                   System.out.println("\nYou haven't typed any character! \n");
                   System.out.print("\nEnter first character of the next word(or enter number to choose from the previous list): ");
                   firstTime = false;
                   my_char = ask_input();
                   exit_program(my_char, totalTime, time);
                 }

                 dictionary_trie.current = dictionary_trie.root;
                 dictionary_trie.prefix = "";
                 user_trie.current = user_trie.root;
                 user_trie.prefix = "";
                 prefix = new StringBuilder();
           }


           if(firstTime) {
              user_trie.no_user_history = true;
           }else{
               user_trie.user_search_for_word(my_char);
           }
           dictionary_trie.search_for_word(my_char);
            long startTime = System.nanoTime();
           if(dictionary_trie.no_prediction&&user_trie.no_user_history){
               System.out.println("There is no prediction. ");
             dictionary_trie.no_prediction = false;
             prefix.append(my_char);
             System.out.print("Please enter the next character of the word or finish by entering '$': ");
             my_char = ask_input();
             exit_program(my_char, totalTime, time);
             while(my_char != '$'){
                 prefix.append(my_char);
                 System.out.print("Please enter the next character of the word or finish by entering '$': ");
                 my_char = ask_input();
                 exit_program(my_char, totalTime, time);
             }
             my_char = encounter_dollar_sign(prefix.toString());
             user_trie.addWord(prefix.toString());
             firstTime = false;
             exit_program(my_char, totalTime, time);
             if(my_char == '$'){
                 System.out.println("\nYou haven't typed any character! \n");
                 System.out.print("\nEnter first character of the next word(or enter number to choose from the previous list):");
                 firstTime = false;
                 my_char = ask_input();
                 exit_program(my_char, totalTime, time);
             }

             dictionary_trie.current = dictionary_trie.root;
             dictionary_trie.prefix = "";
             user_trie.current = user_trie.root;
             user_trie.prefix = "";
             prefix = new StringBuilder();
             startTime = System.nanoTime();
             dictionary_trie.search_for_word(my_char);
             user_trie.user_search_for_word(my_char);
           }
           dic_prediction = new ArrayList<>(5);
           user_prediction = new ArrayList<>(5);
           user_prediction = user_trie.usr_prediction_array(user_trie.current);

           dic_prediction = dictionary_trie.prediction_array(dictionary_trie.current);
           total_prediction = new ArrayList<>(5);
           int numbers = user_prediction.size() + dic_prediction.size();
           for(int i = 0; i<user_prediction.size(); i++){
             for(int j = 0; j<dic_prediction.size(); j++){
               if(dic_prediction.get(j).equals(user_prediction.get(i))){
                 numbers--;
               }
             }
           }
           int length = 5;
           if(numbers < 5){
             length = numbers;
           }
           if(!firstTime&&!user_trie.no_user_history){
               if(user_prediction.size()<=5){
                   for(int i = 0; i < user_prediction.size(); i++){
                       total_prediction.add(user_prediction.get(i));
                   }
               }else{
                   for(int i = 0; i < 5; i++){
                       total_prediction.add(user_prediction.get(i));
                   }
               }

             int k = 0;
             while(total_prediction.size() < length){
               if(!total_prediction.contains(dic_prediction.get(k))){
                   total_prediction.add(dic_prediction.get(k));
                   k++;
               } else{
                 k++;
               }
             }
           }else{
             for(int i = 0; i<length; i++){
               total_prediction.add(dic_prediction.get(i));
             }
           }

           long endTime = System.nanoTime();
           float estimatedTime = (float)((float)(endTime - startTime)/1000000000);
           System.out.print("\n(");
           System.out.printf("%.6f", estimatedTime);
           System.out.print(" s)");
           totalTime += estimatedTime;
           time ++;
           System.out.println("\nPredictions: ");
           int i = 1;
           for(String elem : total_prediction){
             System.out.print("(" + i+ ") " + elem + "   ");
             i++;
           }
           prefix.append(my_char);
           dictionary_trie.prefix = prefix.toString();
           user_trie.prefix = prefix.toString();

           System.out.print("\n\nEnter the next character: ");
           my_char = ask_input();
           isdigit = true;
           exit_program(my_char, totalTime, time);
           if(my_char == '$'){
               my_char = encounter_dollar_sign(prefix.toString());
               user_trie.addWord(prefix.toString());
               firstTime = false;
               dictionary_trie.current = dictionary_trie.root;
               dictionary_trie.prefix = "";
               user_trie.current = user_trie.root;
               user_trie.prefix = "";
               prefix = new StringBuilder();
           }

           }
         }

   public static void exit_program(char my_char, float totalTime, int time){
     if(my_char == '!'){
             float averageTime = (float)totalTime/time;
             System.out.print("\nAverage Time: ");
             System.out.printf("%.6f", averageTime);
             System.out.print(" s\nBye!\n");
             System.exit(0);
     }
   }

   public static char encounter_dollar_sign(String prefix){

         System.out.println("\n\nWORD COMPLETED: " + prefix);
         writing_file(prefix);
         System.out.print("\nEnter first character of the next word(or enter number to choose from the previous list): ");
         return ask_input();


   }
   public static char ask_input(){
       Scanner keyboard = new Scanner(System.in);
       String t = keyboard.nextLine();
       if (t.length() > 1){
         System.out.println("You can type one character once. The first character is recorded.\n");
       }
       char my_char = t.charAt(0);
       return my_char;
  }

  public static void writing_file(String str){

     File file = new File("user_history.txt");
     try{
         BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
         writer.write(str+"\n");
         writer.close();
     }
     catch(Exception e){
         System.out.println("\n'user_history.txt' has not been created. ");
     }
  }
 }
