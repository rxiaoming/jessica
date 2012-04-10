package com.mogujie.jessica.service;

import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.log4j.Logger;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.server.THsHaServer;
import org.apache.thrift.server.TServer;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TNonblockingServerSocket;

import com.mogujie.jessica.MSearcher;
import com.mogujie.jessica.index.MIndexWriter;
import com.mogujie.jessica.service.UpdateServiceImpl.UpdateServiceThreadFactory;
import com.mogujie.jessica.service.thrift.AdminService.Iface;
import com.mogujie.jessica.service.thrift.AdminService.Processor;
import com.mogujie.storeage.SimpleBitcaskStore;

public class AdminServiceImpl implements Iface
{
    private static final Logger log = Logger.getLogger(AdminServiceImpl.class);
    private TServer server;
    private String ip;
    private ExecutorService executor;
    private int port;
    private MSearcher msearch;
    private SimpleBitcaskStore bitcaskStore;

    public AdminServiceImpl(MSearcher msearch, SimpleBitcaskStore bitcaskStore, String ip, int port)
    {
        this.msearch = msearch;
        this.bitcaskStore = bitcaskStore;
        this.ip = ip;
        this.port = port;
        executor = new ThreadPoolExecutor(1, 4, 30000L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(), new UpdateServiceThreadFactory(), new ThreadPoolExecutor.DiscardPolicy());
    }

    public void start()
    {
        log.info("启动更新索引服务!");
        try
        {
            Inet4Address inet4Address = (Inet4Address) Inet4Address.getByName(ip);
            InetSocketAddress socketAddress = new InetSocketAddress(inet4Address, port);
            TNonblockingServerSocket socket = new TNonblockingServerSocket(socketAddress, 30000);
            Processor<AdminServiceImpl> processor = new Processor<AdminServiceImpl>(this);

            THsHaServer.Args args = new THsHaServer.Args(socket);
            args.processor(processor);
            args.protocolFactory(new TBinaryProtocol.Factory());
            args.transportFactory(new TFramedTransport.Factory());

            server = new THsHaServer(args);

            executor.execute(new Runnable()
            {
                @Override
                public void run()
                {
                    server.serve();
                }
            });

        } catch (Exception e)
        {
            log.error(e.getMessage(), e);
        }
    }

    public void shutdown()
    {
        server.stop();
    }

    /**
     * 手动调用索引compact
     */
    @Override
    public String purgeIndex() throws TException
    {
        msearch.getIndexWriter().triggerCompact();
        return "ok";
    }

    @Override
    public String expungeDeletes() throws TException
    {
        return null;
    }

    @Override
    public String optimize(int numSegs) throws TException
    {
        return null;
    }

    @Override
    public String flushDocumentStore() throws TException
    {
        return null;
    }

    @Override
    public String setBlockSize(long size) throws TException
    {
        return null;
    }

    @Override
    public String refreshDiskReader() throws TException
    {
        return null;
    }

    @Override
    public String setBatchSize(long batchSize) throws TException
    {
        return null;
    }

    @Override
    public String setMaxBatchSize(long maxBatchSize) throws TException
    {
        return null;
    }

    @Override
    public String setSearchCacheSize(long cacheSize) throws TException
    {
        return null;
    }

    @Override
    public String setSearchCacheExpireTime(long ms) throws TException
    {
        return null;
    }

    @Override
    public String setQueryCacheSize(long cacheSize) throws TException
    {
        return null;
    }

    @Override
    public String setStoreCacheSize(long cacheSize) throws TException
    {
        return null;
    }

    @Override
    public String setSortParams(String type, int favMax, int maxEditor, int timeWeight, int favWeight, int editorWeight) throws TException
    {
        return null;
    }

    @Override
    public String enableUpdateOperation(boolean enable) throws TException
    {
        return null;
    }

    @Override
    public Map<Long, Long> qps() throws TException
    {
        return null;
    }

    @Override
    public long currentQps() throws TException
    {
        return 0;
    }

    @Override
    public long numSearches() throws TException
    {
        return 0;
    }

    @Override
    public String status() throws TException
    {
        return null;
    }

    @Override
    public String jvmstatus() throws TException
    {
        return null;
    }

    public static class AdminServiceThreadFactory implements ThreadFactory
    {
        public Thread newThread(Runnable runnable)
        {
            Thread thread = new Thread(runnable);
            thread.setName("AdminService-pool-thread-" + threaCounter.incrementAndGet());
            thread.setDaemon(false);
            return thread;
        }

        private final static AtomicLong threaCounter = new AtomicLong(0);
    }

}
