public class FogNode {
    private String ipAddress;
    private String nodeName;

    public FogNode(String ipAddress, String nodeName){
        this.ipAddress = ipAddress;
        this.nodeName = nodeName;
    }
    public String getNodeName(){
        return nodeName;
    }

    @Override
    public int hashCode(){
        int ret = 2;
        if(ipAddress != null) ret = ret + ipAddress.hashCode()*3;
        if(nodeName != null ) ret = ret + nodeName.hashCode()*5;
        return ret;
    }
    @Override
    public boolean equals(Object obj){
        if(! (obj instanceof FogNode) ){
            return false;
        }
        FogNode other = (FogNode) obj;
        boolean ret = (other.ipAddress.equals(this.ipAddress) 
                        && other.nodeName.equals(this.nodeName));

        return ret;
    }
}
