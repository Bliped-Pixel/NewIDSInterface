public class Device {
    private String macAddress;
    private String deviceName;
    private DeviceType type;
    
    public Device(String macAddress, String deviceName, int type ){
        this.deviceName = deviceName;
        this.macAddress = macAddress;
        this.type = DeviceType.getType(type);
    }

    public String getDeviceName(){
        return deviceName;
    }
    
    @Override
    public int hashCode(){
        int ret = 2;

        if(deviceName != null) ret = ret + deviceName.hashCode()*3;
        if(macAddress != null) ret = ret + macAddress.hashCode()*7;
        if(type != null) ret = ret + type.hashCode()*5;

        return ret;
    }
    @Override
    public boolean equals(Object obj){
        if(! (obj instanceof Device) ){
            return false;
        }
        Device other = (Device) obj;
        boolean ret = (other.macAddress.equals(this.macAddress) 
                        && other.deviceName.equals(this.deviceName));

        return ret;
    }
}

enum DeviceType{
    BUTTON, TOGGLE;

    public static DeviceType getType(int type){
        if(type == 0){
            return DeviceType.BUTTON;
        }
        else if (type == 1){
            return DeviceType.TOGGLE;
        }
        return null;
    }
}
