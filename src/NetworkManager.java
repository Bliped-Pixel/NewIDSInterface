import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.Set;

public class NetworkManager{

    private NetworkManager(){}
    
    private static NetworkManager obj = new NetworkManager();
    private static Set<Device> devices = new HashSet<>();
    private static Set<FogNode> nodes = new HashSet<>();
    private String username = "admin";
    private String hash = generateHash("admin");

    public static NetworkManager getInstance(){
        return obj;
    }

    public Set<Device> getDevices(){
        return devices;
    }
    public Set<FogNode> getNodes(){
        return nodes;
    }
    private boolean recievePermissionForId(String name, String message){
        // send message to admin
        return true;
    }
    private String generateHash(String password){
        try 
        {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.update(password.getBytes());
            byte[] hash = digest.digest();
            StringBuilder hexString = new StringBuilder();

            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } 
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
    private boolean removeDevice(String name){

        for(Device device : devices){
            if(device.getDeviceName().equals(name))
                return devices.remove(device);
        }
        return false;
    }
    private boolean removeNode(String name){

        for (FogNode node : nodes) {
            if (node.getNodeName().equals(name)) {
                return nodes.remove(node);
            }
        }
        return false;
    }

    public boolean login(String username, String password){

        String passHash = generateHash(password);
        if(this.username.equals(username) && hash.equals(passHash)){
            return true;
        }
        return false;
    }

    public boolean addDevice(String deviceName, String  mac, String nodeName){
        
        boolean permission = false;
        String message = "add Device";
        // send message to admin and get the response
        permission  = recievePermissionForId(   deviceName, message);
        if(!permission){
            return false;
        }
        else{
            for (Device otherDevice : devices) {
                if(otherDevice.getDeviceName().equals(deviceName)) // device name or ip already exists
                    return false;
            }
            FogNode node = null;
            if(nodeName != null){
                for(FogNode onode : nodes){
                    if(onode.getNodeName().equals(nodeName)){
                        node = onode;
                        break;
                    }
                }
            }
            return devices.add(new Device(mac, deviceName, 1));
        }
    }

    public boolean deleteDevice(String deviceName){
        boolean permission = false;
        String message = "remove device";

        // send message to admin and get the response
        permission  = recievePermissionForId( deviceName,  message);
        if (!permission) {
            return false; 
        }
        return removeDevice(deviceName);
    }
    public boolean addFogNode(String name, String ip){
        FogNode node = new FogNode(ip, name);
        boolean permission = recievePermissionForId(name, "add node");
        
        if(!permission) return false;
        return nodes.add(node);
    }
    public boolean removeFogNode(String name){

        boolean permission = recievePermissionForId(name, "remove node");
        if(!permission){
            return false;
        }
        return removeNode(name);
    }

}  