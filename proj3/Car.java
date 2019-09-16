//Celine Cui
//3.2.2019
public class Car{
    private String VIN;
    private String make;
    private String model;
    private String color;
    private int mileage;
    private int price;
    private int pIndex = -1;
    private int mIndex = -1;
    private int psIndex = -1;
    private int msIndex = -1;

    public Car(String id, String make, String model, String color, int mileage, int price){
        VIN = id;
        this.make = make;
        this.model = model;
        this.color = color;
        this.mileage = mileage;
        this.price = price;
    }

    public String getVIN(){ return VIN; }
    public void setVIN(String id){
        VIN = id;
    }

    public String getMake(){
        return make;
    }
    public void setMake(String make){ this.make = make; }

    public String getModel(){
        return model;
    }
    public void setModel(String model){ this.model = model; }

    public String getColor(){
        return color;
        }
    public void setColor(String color){
        this.color = color;
    }

    public int getMileage(){
        return mileage;
    }
    public void setMileage(int mileage){
        this.mileage = mileage;
    }

    public int getPrice(){
        return price;
    }
    public void setPrice(int price){
        this.price = price;
    }

    public int getPricesIndex(){
        return pIndex;
    }
    public void setPriceIndex(int index){ pIndex = index; }

    public int getMileageIndex(){
        return mIndex;
    }
    public void setMileageIndex(int index){
        mIndex = index;
    }

    public int getSpecialPrice(){
        return psIndex;
    }
    public void setSpecialPrice(int index){
        psIndex = index;
    }

    public int getSpecialMileage(){
        return msIndex;
    }
    public void setSpecialMileage(int index){
        msIndex = index;
    }

    public String toString(){
        String output = "\t------------------------- " + "\n\tVIN: " + VIN + "\n\tMake: "
                + make + "\n\tModel: " + model + "\n\tPrice: $" + price + "\n\tMileage: "
                + mileage + "\n\tColor: " + color +  "\n\t-------------------------";
        return output;
    }
}