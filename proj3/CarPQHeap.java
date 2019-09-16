//Celine Cui
//3.2.2019
public class CarPQHeap{
    private static CarPQ prices;
    private static CarPQ mileages;
    private static CarPQ sprices;
    private static CarPQ smileages;
    private static PQDLB specialPrices;
    private static PQDLB specialMileages;

    public CarPQHeap(){
        prices = new CarPQ('p', false);
        mileages = new CarPQ('m', false);
        specialPrices = new PQDLB();
        specialMileages = new PQDLB();
    }

    public void insert(Car car){
        String make = car.getMake();
        String model = car.getModel();
        String s = make + "@" + model;

        prices.insert(car);
        mileages.insert(car);
        sprices = specialPrices.getPQ(s);
        smileages = specialMileages.getPQ(s);
        if(sprices == null){
            CarPQ newPQ = new CarPQ('p', true);
            specialPrices.insert(s, newPQ);
            sprices = newPQ;
        }
        if(smileages == null){
            CarPQ newPQ = new CarPQ('m', true);
            specialMileages.insert(s, newPQ);
            smileages = newPQ;
        }
        sprices.insert(car);
        smileages.insert(car);
    }

    public Car getLowestPrice(){
        return prices.getMin();
    }

    public Car getLowestMileage(){
        return mileages.getMin();
    }

    public Car getLowestSpecialPrice(String make, String model){
        String s = make + "@" + model;
        sprices = specialPrices.getPQ(s);
        if(sprices == null){
            return null;
        }else{
            return sprices.getMin();
        }
    }

    public Car getLowestSpecialMileage(String make, String model){
        String s = make + "@" + model;
        smileages = specialMileages.getPQ(s);
        if(smileages == null){
            return null;
        }else{
            return smileages.getMin();
        }
    }

    public void update(Car car, boolean updatePrice){
        String make = car.getMake();
        String model = car.getModel();
        String s = make + "@" + model;

        if(updatePrice){
            int pIndex = car.getPricesIndex();
            int psIndex = car.getSpecialPrice();
            prices.update(pIndex);

            sprices = specialPrices.getPQ(s);
            if(sprices != null) sprices.update(psIndex);
        } else{
            int mIndex = car.getMileageIndex();
            int msIndex = car.getSpecialMileage();
            mileages.update(mIndex);

            smileages = specialMileages.getPQ(s);
            if(smileages != null) smileages.update(msIndex);
        }
    }

    public void remove(Car car){
        int pIndex = car.getPricesIndex();
        int mIndex = car.getMileageIndex();
        int psIndex = car.getSpecialPrice();
        int msIndex = car.getSpecialMileage();
        String make = car.getMake();
        String model = car.getModel();

        prices.remove(pIndex);
        mileages.remove(mIndex);

        String s = make + "@" + model;
        sprices = specialPrices.getPQ(s);
        smileages = specialMileages.getPQ(s);
        if(sprices != null) sprices.remove(psIndex);
        if(smileages != null) smileages.remove(msIndex);
    }

}
