
/* YOU CAN TRY THIS TOO */

import java.io.File;
import java.lang.management.ManagementFactory;
// import java.lang.management.OperatingSystemMXBean;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.management.RuntimeMXBean;
import java.io.*;
import java.net.*;
import java.util.*;
import java.io.LineNumberReader;
import com.sun.management.OperatingSystemMXBean;
import java.util.Random;

public class CPU {

	static int mb = 1024 * 1024;
	static int gb = mb * 1024;
	
	public static void printUsage(Runtime runtime) {
		long total, free, used;
		int mb = 1024 * 1024;

		total = runtime.totalMemory();
		free = runtime.freeMemory();
		used = total - free;
		System.out.println("\nTotal Memory: " + total / mb + "MB");
		System.out.println(" Memory Used: " + used / mb + "MB");
		System.out.println(" Memory Free: " + free / mb + "MB");
		System.out.println("Percent Used: " + ((double) used / (double) total) * 100 + "%");
		System.out.println("Percent Free: " + ((double) free / (double) total) * 100 + "%");
	}

	public static void log(Object message) {
		System.out.println(message);
	}

	public static int calcCPU(long cpuStartTime, long elapsedStartTime, int cpuCount) {
		long end = System.nanoTime();
		long totalAvailCPUTime = cpuCount * (end - elapsedStartTime);
		long totalUsedCPUTime = ManagementFactory.getThreadMXBean().getCurrentThreadCpuTime() - cpuStartTime;
		// log("Total CPU Time:" + totalUsedCPUTime + " ns.");
		// log("Total Avail CPU Time:" + totalAvailCPUTime + " ns.");
		float per = ((float) totalUsedCPUTime * 100) / (float) totalAvailCPUTime;
		log(per);
		return (int) per;
	}

	static boolean isPrime(int n) {
		// 2 is the smallest prime
		if (n <= 2) {
			return n == 2;
		}
		// even numbers other than 2 are not prime
		if (n % 2 == 0) {
			return false;
		}
		// check odd divisors from 3
		// to the square root of n
		for (int i = 3, end = (int) Math.sqrt(n); i <= end; i += 2) {
			if (n % i == 0) {
				return false;
			}
		}
		return true;
	}
	
	public static void main(String[] args) {
		
		/* PHYSICAL MEMORY USAGE */
		System.out.println(getPhysicalUsage());
		
		
		/* DISC SPACE DETAILS */
		int system = getSystem();

		System.out.println(getDiskSpace());
		Runtime runtime;
		byte[] bytes;
		System.out.println("\n \n**MEMORY DETAILS  ** \n");
		// Print initial memory usage.
		runtime = Runtime.getRuntime();
		printUsage(runtime);

		// Allocate a 1 Megabyte and print memory usage
		bytes = new byte[1024 * 1024];
		printUsage(runtime);

		bytes = null;
		// Invoke garbage collector to reclaim the allocated memory.
		runtime.gc();

		// Wait 5 seconds to give garbage collector a chance to run
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
			return;
		}

		// Total memory will probably be the same as the second printUsage call,
		// but the free memory should be about 1 Megabyte larger if garbage
		// collection kicked in.
		printUsage(runtime);
		for (int i = 0; i < 30; i++) {
			long start = System.nanoTime();
			// log(start);
			// number of available processors;
			int cpuCount = ManagementFactory.getOperatingSystemMXBean().getAvailableProcessors();
			Random random = new Random(start);
			int seed = Math.abs(random.nextInt());
			log("\n \n CPU USAGE DETAILS \n\n");
			log("Starting Test with " + cpuCount + " CPUs and random number:" + seed);
			int primes = 10000;
			//
			long startCPUTime = ManagementFactory.getThreadMXBean().getCurrentThreadCpuTime();
			start = System.nanoTime();
			while (primes != 0) {
				if (isPrime(seed)) {
					primes--;
				}
				seed++;

			}
			float cpuPercent = calcCPU(startCPUTime, start, cpuCount);
			log("CPU USAGE : " + cpuPercent + " % ");

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
		}

		try {
			Thread.sleep(500);
		} catch (Exception ignored) {
		}
	}

	private static String getDiskSpace() {
		StringBuilder builder = new StringBuilder();
		File diskPartition = new File("C:");
		File diskPartition1 = new File("D:");
		File diskPartition2 = new File("E:");
		builder.append("\n**** Sizes in Giga Bytes ****\n");
		builder.append("DISC SPACE DETAILS \n");
		double freePartitionSpace = diskPartition.getFreeSpace() / gb;
		builder.append("Free Space in drive C: : " + freePartitionSpace + "GB\n");
		double freePartitionSpace1 = diskPartition1.getFreeSpace() / gb;
		builder.append("Free Space in drive D:  : " + freePartitionSpace1 + "GB\n");
		double freePartitionSpace2 = diskPartition2.getFreeSpace() / gb;
		builder.append("Free Space in drive E: " + freePartitionSpace2 + "GB\n");
		double usablePatitionSpace = diskPartition.getUsableSpace() / gb;

		long totalCapacity = diskPartition.getTotalSpace() / gb;
		builder.append("Total C partition size : " + totalCapacity + "GB\n");
		builder.append("Usable Space : " + usablePatitionSpace + "GB\n");
		
		long totalCapacity1 = diskPartition1.getTotalSpace() / gb;
		if (freePartitionSpace <= totalCapacity % 10 || freePartitionSpace1 <= totalCapacity1 % 10)
			builder.append(" !!!ALERT!!!!");

		return builder.toString();
	}

	private static String getPhysicalUsage() {
		StringBuilder builder = new StringBuilder();
		builder.append("\n**** Sizes in Mega Bytes ****\n");
		OperatingSystemMXBean operatingSystemMXBean = (OperatingSystemMXBean) ManagementFactory
				.getOperatingSystemMXBean();
		// RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
		// operatingSystemMXBean = (com.sun.management.OperatingSystemMXBean)
		// ManagementFactory.getOperatingSystemMXBean();
		OperatingSystemMXBean os = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
		long physicalMemorySize = os.getTotalPhysicalMemorySize();
		builder.append("PHYSICAL MEMORY DETAILS \n");
		builder.append("total physical memory : " + physicalMemorySize / mb + "MB\n");
		long physicalfreeMemorySize = os.getFreePhysicalMemorySize();
		builder.append("total free physical memory : " + physicalfreeMemorySize / mb + "MB");
		
		return builder.toString();
	}

	private static int getSystem() {
		String os = System.getProperty("os.name");
		boolean windows = os.contains("Windows");
		boolean mac = os.contains("Mac");
		boolean linux = os.contains("Linux");
		if (windows) 
			return 1;
		else if (mac) 
			return 2;
		else if (linux) 
			return 3;
		else if (!os.isEmpty()) 
			return 0;
		else
			return -1;
	}
}
