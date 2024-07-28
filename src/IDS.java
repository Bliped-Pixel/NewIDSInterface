import org.jnetpcap.Pcap;
import org.jnetpcap.PcapIf;
import org.jnetpcap.packet.JPacket;
import org.jnetpcap.protocol.network.Ip4;
import org.jnetpcap.protocol.tcpip.Tcp;

import java.util.ArrayList;
import java.util.List;

public class IDS {
    public static void main(String[] args) {
        // List of network interfaces
        List<PcapIf> interfaces = new ArrayList<>();
        StringBuilder errorBuffer = new StringBuilder();

        // Find available network interfaces
        int status = Pcap.findAllDevs(interfaces, errorBuffer);
        if (status != Pcap.OK || interfaces.isEmpty()) {
            System.err.printf("Error: %s", errorBuffer.toString());
            return;
        }

        // Select the first network interface
        PcapIf device = interfaces.get(0);

        // Open the network interface
        int snaplen = 64 * 1024; // Capture all packets, no truncation
        int flags = Pcap.MODE_PROMISCUOUS; // Capture in promiscuous mode
        int timeout = 10 * 1000; // 10 seconds timeout

        Pcap pcap = Pcap.openLive(device.getName(), snaplen, flags, timeout, errorBuffer);
        if (pcap == null) {
            System.err.printf("Error opening device: %s", errorBuffer.toString());
            return;
        }

        // Monitor network traffic
        pcap.loop(10, (JPacket packet, Object user) -> {
            Tcp tcp = new Tcp();
            Ip4 ip = new Ip4();

            // Check if the packet is a TCP packet
            if (packet.hasHeader(tcp) && packet.hasHeader(ip)) {
                int srcPort = tcp.source();
                int dstPort = tcp.destination();
                String srcIP = org.jnetpcap.packet.format.FormatUtils.ip(ip.source());
                String dstIP = org.jnetpcap.packet.format.FormatUtils.ip(ip.destination());

                // Simple rule: flag connections to port 22 (SSH)
                if (dstPort == 22) {
                    System.out.printf("Suspicious activity detected: %s:%d to %s:%d\n",
                            srcIP, srcPort, dstIP, dstPort);
                }
            }
        }, null);

        pcap.close();
    }
}
