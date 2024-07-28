import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class Login extends HttpServlet{

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)throws ServletException, IOException{
        doPost(req, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)throws ServletException, IOException{

        NetworkManager manager = NetworkManager.getInstance();
        PrintWriter writer = res.getWriter();
        String sender = req.getHeader("sender");
        if(sender.equals("index"))
        {
            String username = req.getHeader("username");
            String password = req.getHeader("password");
            boolean login = manager.login(username, password);

            writer.print("{\"response\" :  \""+ login+ "\"}");

        }
        else if(sender.equals("config")){
            String action  = req.getHeader("action");
            if(action.equals("removeDevice")){
                String deviceName = req.getHeader("deviceName");
                boolean permission = manager.deleteDevice(deviceName);
                writer.print("{\"response\" :  \" "+ permission + "\"}");
            }
            else if(action.equals("removeNode")){
                String nodeName = res.getHeader("nodeName");
                boolean permission = manager.removeFogNode(nodeName);
                writer.print("{\"response\" :  \" "+ permission + "\"}");
            }
            else if(action.equals("addDevice")){
                String deviceName = req.getHeader("deviceName");
                String deviceip = req.getHeader("deviceIp");
                String nodeName = req.getHeader("node");    
                boolean permission = manager.addDevice(deviceName, deviceip, nodeName);
                writer.print("{\"response\" :  \" "+ permission + "\"}");
                
            }
            else if(action.equals("addNode")){
                String nodeName = req.getHeader("nodeName");
                String nodeIp = req.getHeader("nodeIp");
                boolean permission = manager.addFogNode(nodeName, nodeIp);
                writer.print("{\"response\" :  \" "+ permission + "\"}");
            }


        }
        else if(sender.equals("DeviceNodeList")){
            StringBuilder deviceJson = new StringBuilder();
            StringBuilder nodeJson = new StringBuilder();
            StringBuilder json = new StringBuilder();

            Set<Device> devices = manager.getDevices();
            Set<FogNode> nodes = manager.getNodes();


            nodeJson.append("[");
            for(FogNode node : nodes) {
                nodeJson.append("\"");
                nodeJson.append(node.getNodeName());
                nodeJson.append("\"");
                nodeJson.append(",");
            }
            nodeJson.deleteCharAt(nodeJson.length() - 1);
            nodeJson.append("]");
            
            deviceJson.append("[");
            for(Device device : devices) {
                deviceJson.append("\"");
                deviceJson.append(device.getDeviceName());
                deviceJson.append("\"");
                deviceJson.append(",");
            }
            deviceJson.deleteCharAt(deviceJson.length() - 1);
            deviceJson.append("]");

            json.append("{ devices : ");
            json.append(deviceJson.toString());
            json.append(",");
            json.append("nodes :");
            json.append(nodeJson.toString());
            json.append("}");


            writer.print(json.toString());
        }
    }
}
