package uff.ic.swlab.ckan2void.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public abstract class Config {

    public static final SWLabHost HOST = SWLabHost.DEFAULT_HOST;

    public static String FUSEKI_DATASET;
    public static String FUSEKI_TEMP_DATASET;
    public static String CKAN_CATALOGS;

    public static Integer TASK_INSTANCES;
    public static Integer TASK_TIMEOUT;
    public static Integer PARALLELISM;
    public static Integer POOL_SHUTDOWN_TIMEOUT;
    public static TimeUnit POOL_SHUTDOWN_TIMEOUT_UNIT;

    public static Long SPARQL_TIMEOUT;
    public static Long MODEL_READ_TIMEOUT;
    public static Long MODEL_WRITE_TIMEOUT;
    public static Integer HTTP_CONNECT_TIMEOUT;
    public static Integer HTTP_READ_TIMEOUT;
    public static Integer HTTP_ACCESS_TIMEOUT;

    public static Long MAX_VOID_FILE_SIZE;

    public static void configure(String filename) throws IOException {
        try (InputStream input = new FileInputStream(filename);) {
            Properties prop = new Properties();
            prop.load(input);

            FUSEKI_DATASET = prop.getProperty("fusekiDataset");
            FUSEKI_TEMP_DATASET = prop.getProperty("fusekiTempDataset");
            CKAN_CATALOGS = prop.getProperty("ckanCatalog");

            TASK_INSTANCES = Integer.valueOf(prop.getProperty("taskInstances"));
            TASK_TIMEOUT = Integer.valueOf(prop.getProperty("taskTimeout"));
            PARALLELISM = Integer.valueOf(prop.getProperty("parallelism"));
            POOL_SHUTDOWN_TIMEOUT = Integer.valueOf(prop.getProperty("poolShutdownTimeout"));
            POOL_SHUTDOWN_TIMEOUT_UNIT = TimeUnit.valueOf(prop.getProperty("poolShutdownTimeoutUnit"));

            MODEL_READ_TIMEOUT = Long.valueOf(prop.getProperty("modelReadTimeout"));
            MODEL_WRITE_TIMEOUT = Long.valueOf(prop.getProperty("modelWriteTimeout"));
            SPARQL_TIMEOUT = Long.valueOf(prop.getProperty("sparqlTimeout"));
            HTTP_CONNECT_TIMEOUT = Integer.valueOf(prop.getProperty("httpConnectTimeout"));
            HTTP_READ_TIMEOUT = Integer.valueOf(prop.getProperty("httpReadTimeout"));
            HTTP_ACCESS_TIMEOUT = Integer.valueOf(prop.getProperty("httpAccessTimeout"));

            MAX_VOID_FILE_SIZE = Long.valueOf(prop.getProperty("maxVoidFileSize"));
        }
    }
}