package com.copart.figs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.couchbase.cache.CouchbaseCacheManager;
import org.springframework.data.couchbase.config.AbstractCouchbaseConfiguration;
import org.springframework.data.couchbase.core.CouchbaseTemplate;
import org.springframework.data.couchbase.repository.config.EnableCouchbaseRepositories;

import com.couchbase.client.CouchbaseClient;

/**
 * This is the configuration class for couchbase Lot_Details bucket
 *
 */
@Configuration
@EnableConfigurationProperties
@EnableCouchbaseRepositories(basePackages="com.copart.figs",couchbaseTemplateRef="lotDetailsCouchbaseTemplate")
@EnableCaching
@ConfigurationProperties(prefix = "couchbase")
public class LotDetailsBucketCouchbaseConfig extends AbstractCouchbaseConfiguration
{

    private List<String> hosts = new ArrayList<String>();
    
    @Value("${couchbase.hosts}")
    private String hostsString;
    
    @Value("${couchbase.bucketName}")
    private String bucket;
    
    @Value("${couchbase.bucketPassword}")
    private String password;

    public List<String> getHosts()
    {
        String[] hostStrings = hostsString.split(",");
        this.hosts = Arrays.asList(hostStrings);
        return this.hosts;
    }

    @Override
    protected List<String> bootstrapHosts()
    {
        return getHosts();
    }

    @Override
    protected String getBucketName()
    {
        return bucket;
    }

    @Override
    protected String getBucketPassword()
    {
        return password;
    }
    
    
    /**
     * @return the hostsString
     */
    public String getHostsString()
    {
        return hostsString;
    }

    @Bean
    public CouchbaseCacheManager cacheManager() throws Exception {
        HashMap<String, CouchbaseClient> instances = new HashMap<String, CouchbaseClient>();
        instances.put(bucket, couchbaseClient());
        return new CouchbaseCacheManager(instances);
    }
    
    // Override so we can name it as the "main" bucket client
    @Override
    @Bean(destroyMethod = "shutdown", name = "lotDetailsCouchbaseClient")
    public CouchbaseClient couchbaseClient() throws Exception {
        return super.couchbaseClient();
    }

    // Override so we can name it as the "main" bucket template
    @Bean(name = "lotDetailsCouchbaseTemplate")
    public CouchbaseTemplate couchbaseTemplate() throws Exception {
        return new CouchbaseTemplate(couchbaseClient(), mappingCouchbaseConverter(), translationService());
    }
    
}
