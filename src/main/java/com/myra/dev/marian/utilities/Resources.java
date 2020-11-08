package com.myra.dev.marian.utilities;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;

public class Resources {
    public String getCpuLoad() throws Exception {
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        ObjectName name = ObjectName.getInstance("java.lang:type=OperatingSystem");
        AttributeList list = mbs.getAttributes(name, new String[]{"ProcessCpuLoad"});

        if (list.isEmpty()) return String.valueOf(Double.NaN);

        Attribute att = (Attribute) list.get(0);
        Double value = (Double) att.getValue();

        // usually takes a couple of seconds before we get real values
        if (value == -1.0) return String.valueOf(Double.NaN);
        // returns a percentage value with 1 decimal point precision
        return (value * 1000) / 10.0 + "%";
    }

    public String getRAMUsage() {
        try {
            Runtime runtime = Runtime.getRuntime();
            long allocatedMemory = runtime.totalMemory();
            long freeMemory = runtime.freeMemory();
            return (allocatedMemory - freeMemory) / 1000 / 1000 + "%";
        } catch (Exception e) {
            return "There was an error while attempting to collect ram usage!";
        }
    }


}
