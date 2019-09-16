//Celine Cui
//3.2.2019
import java.util.*;
import java.io.File;

public class CarTracker{
    private static VINDLB Cars;
    private static CarPQHeap heap; //Manages all of the heaps for all prices, all mileages, and the same for all makes and models
    private static Scanner sc;

    public static void main(String[] args){
        Cars = new VINDLB();
        heap = new CarPQHeap();
        readSourceFile();
        sc = new Scanner(System.in);
        String input = "";

        do{
            System.out.println("\n\t1. Add a car");
            System.out.println("\t2. Update a car");
            System.out.println("\t3. Remove a car");
            System.out.println("\t4. Retrieve the lowest priced car");
            System.out.println("\t5. Retrieve the lowest mileage car");
            System.out.println("\t6. Retrieve the lowest priced car by make/model");
            System.out.println("\t7. Retrieve the lowest mileage car by make/model");
            System.out.println("\t8. Quit");
            System.out.print("Enter your choice(one number): ");
            input = sc.nextLine();

            if(input.equals("1")){
                add_car();
            } else if(input.equals("2")){
                update_car();
            } else if(input.equals("3")){
                remove_car();
            } else if(input.equals("4")){
                get_lowest_price_car();
            } else if(input.equals("5")){
                get_lowest_mileage_car();
            } else if(input.equals("6")){
                get_lowest_price_car_by_make_and_model();
            } else if(input.equals("7")){
                get_lowest_mileage_car_by_make_and_model();
            } else if(input.equals("8")){
                System.exit(0);
            } else{
                System.out.println("\tInvalid option. ");
            }

        }while(input != "8");
    }
    public static void readSourceFile(){
        Scanner fsc;
        String[] info;
        int n = 0;

        try{
            File f = new File("cars.txt");
            fsc = new Scanner(f);

        }catch(Exception e) {
            System.out.println("This file doesn't exist. ");
            return;
        }
        fsc.nextLine();

        while(fsc.hasNext()){
            info = fsc.nextLine().split(":");
            int price = Integer.parseInt(info[3]);
            int mileage = Integer.parseInt(info[4]);
            Car newCar = new Car(info[0], info[1], info[2], info[5], mileage, price);
            Cars.insert(newCar);
            heap.insert(newCar);

        }
    }
    //A user adds a car with its necessary information
    public static void add_car(){
        String id;
        String make;
        String model;
        String color;
        int mileage = 0;
        int price = 0;

        System.out.print("\nEnter a VIN: ");
        id = sc.nextLine();

        if(Cars.exists(id)){
            System.out.println("\nA car with VIN " + id + " already exists.");
            return;
        }

        System.out.print("Enter a make: ");
        make = sc.nextLine();

        System.out.print("Enter a model: ");
        model = sc.nextLine();

		System.out.print("Enter an integer price: $");
        price = valid_price_or_mileage();
        
        System.out.print("Enter an integer mileage: ");
        mileage = valid_price_or_mileage();
        
        System.out.print("Enter a color: ");
        color = sc.nextLine();
        
        Car newCar = new Car(id, make, model, color, mileage, price);
        Cars.insert(newCar);
        heap.insert(newCar);

    }

    public static int valid_price_or_mileage(){
        int integer;
        try{
            integer = Integer.parseInt(sc.nextLine());
            while(integer < 0){
                System.out.print("Enter a valid integer: ");
                integer = Integer.parseInt(sc.nextLine());
            }
        } catch(NumberFormatException e){ //parseInt() will throw NumberFormatException if the user doesn't enter a number
            System.out.print("Enter a valid integer: ");
            integer = Integer.parseInt(sc.nextLine());
            while(integer < 0){
                System.out.print("Enter a valid integer: ");
                integer = Integer.parseInt(sc.nextLine());
            }
        }
        return integer;
    }


    public static void update_car(){
        System.out.print("\nEnter the VIN number of a car to remove('q' to Quit): ");
        String id = sc.nextLine();
        if(id.equals("q")) return;
        Car car = getValidVIN(id);
        if(car == null){
            return;
        }
        System.out.println("\nEnter the property that you want to update:\n\t1. Price\n\t2. Mileage\n\t3. Color");
        System.out.print("Enter your choice(one number) 'q' to Quit: ");
        String choice = sc.nextLine();
        while(!choice.equals("1") && !choice.equals("2") && !choice.equals("3") && !choice.equals("q")){
            System.out.println("\n\tInvalid option. ");
            System.out.println("\nEnter the property that you want to update:\n\t1. Price\n\t2. Mileage\n\t3. Color");
            System.out.print("Enter your choice(one number) 'q' to Quit: ");
            choice = sc.nextLine();
        }

        if(choice.equals("1")){
            int price;
            System.out.print("Enter the integer price: $");
            price = valid_price_or_mileage();

            car.setPrice(price);
            heap.update(car, true);
            System.out.println("\nThe price of car VIN "+ id + " has been updated.");
        } else if(choice.equals("2")){ //Update the mileage of the car
            int mileage;
            System.out.print("Enter the integer mileage: ");
            mileage = valid_price_or_mileage();

            car.setMileage(mileage);
            heap.update(car, false);
            System.out.println("\nThe mileage of car VIN "+ id + " has been updated.");
        } else if(choice.equals("3")){
            System.out.print("Enter a color: ");
            String color = sc.nextLine();
            car.setColor(color);
            System.out.println("\nThe color of car VIN "+ id + " has been updated.");
        } else if(choice.equals("q")){
            return;
        }
    }
    public static Car getValidVIN(String id){
        Car car = null;
        Boolean isExist = Cars.exists(id);
        if(isExist) car = Cars.theCar;
        while(car == null){
            System.out.println("\nThis VIN is invalid. ");
            System.out.print("Enter the VIN number to update ('q' to Quit): ");
            id = sc.nextLine();
            if(id.equals("q")) return null;
            isExist = Cars.exists(id);
            if(isExist) car = Cars.theCar;
        }
        return car;
    }
    public static void remove_car(){
        System.out.print("\nEnter the VIN number of a car to remove('q' to Quit): ");
        String id = sc.nextLine();
        if(id.equals("q")) return;
        Car car = getValidVIN(id);
        if(car == null) {
            return;
        }
        Cars.remove(id);
        heap.remove(car);
        System.out.println("\nThe car with VIN "+ id + " has been removed.");
    }

    public static void get_lowest_price_car(){
        Car lowest = heap.getLowestPrice();
        if(lowest == null) System.out.println("\n There is no car available.");
        else System.out.println("\n" + lowest.toString());
    }

    public static void get_lowest_mileage_car(){
        Car lowest = heap.getLowestMileage();

        if(lowest == null) System.out.println("\n There is no car available.");
        else System.out.println("\n" + lowest.toString());
    }

    public static void get_lowest_price_car_by_make_and_model(){
        System.out.print("\nEnter a make(e.g., Ford, Toyota, Honda): ");
        String make = sc.nextLine();

        System.out.print("Enter a model(e.g., Fiesta, Camry, Civic): ");
        String model = sc.nextLine();

        Car lowest = heap.getLowestSpecialPrice(make, model);

        if(lowest == null) System.out.println("\n No car is available for this make and model. ");
        else System.out.println("\n" + lowest.toString());
    }

    public static void get_lowest_mileage_car_by_make_and_model(){
        System.out.print("\nEnter a make(e.g., Ford, Toyota, Honda): ");
        String make = sc.nextLine();

        System.out.print("Enter a model(e.g., Fiesta, Camry, Civic): ");
        String model = sc.nextLine();

        Car lowest = heap.getLowestSpecialMileage(make, model);

        if(lowest == null) System.out.println("\n No car is available for this make and model. ");
        else System.out.println("\n" + lowest.toString());
    }
}

