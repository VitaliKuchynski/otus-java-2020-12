import com.sun.management.GarbageCollectionNotificationInfo;
import ru.otus.Generator;

import javax.management.MBeanServer;
import javax.management.NotificationEmitter;
import javax.management.NotificationListener;
import javax.management.ObjectName;
import javax.management.openmbean.CompositeData;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.util.List;

public class App {
    public static void main(String[] args) throws Exception {
        Generator generator = new Generator();
        switchOnMonitoring();
        long beginTime = System.currentTimeMillis();

        MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
        ObjectName objName = new ObjectName("ru.otus:type=Generator");
        mBeanServer.registerMBean(generator, objName);

        //generator.generateOOM();
        System.out.println("time:" + (System.currentTimeMillis() - beginTime) / 1000);
    }

    private static void switchOnMonitoring() {
        List<GarbageCollectorMXBean> gcbeans = java.lang.management.ManagementFactory.getGarbageCollectorMXBeans();
        for (GarbageCollectorMXBean gcbean : gcbeans) {
            System.out.println("GC name:" + gcbean.getName());
            NotificationEmitter emitter = (NotificationEmitter) gcbean;
            NotificationListener listener = (notification, handback) -> {
                if (notification.getType().equals(GarbageCollectionNotificationInfo.GARBAGE_COLLECTION_NOTIFICATION)) {
                    GarbageCollectionNotificationInfo info = GarbageCollectionNotificationInfo.from((CompositeData) notification.getUserData());
                    String gcName = info.getGcName();
                    String gcAction = info.getGcAction();
                    String gcCause = info.getGcCause();

                    long startTime = info.getGcInfo().getStartTime();
                    long duration = info.getGcInfo().getDuration();

                    long count = gcbean.getCollectionCount();
                    long time = gcbean.getCollectionTime();


                    System.out.println("start:" + startTime + " Name:" + gcName + ", action:" + gcAction + ", gcCause:" + gcCause + "(" + duration + " ms)");
                    System.out.println("Collection count:" + count + " Collection time:" +  time + " ms");
                }
            };
            emitter.addNotificationListener(listener, null, null);
        }
    }
}
