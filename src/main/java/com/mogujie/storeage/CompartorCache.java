package com.mogujie.storeage;

//package com.juangua.search.store;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.LinkedBlockingQueue;
//import java.util.concurrent.ThreadFactory;
//import java.util.concurrent.ThreadPoolExecutor;
//import java.util.concurrent.TimeUnit;
//import java.util.concurrent.atomic.AtomicLong;
//
//import org.apache.log4j.Logger;
//import org.apache.thrift.TDeserializer;
//import org.apache.thrift.protocol.TBinaryProtocol;
//
//import com.google.common.base.Splitter;
//import com.google.protobuf.ByteString;
//import com.juangua.search.core.SimpleSearchSystem;
//import com.juangua.search.service.Doc;
//import com.juangua.search.service.SortType;
//import com.juangua.search.store.bitcask.BitCask;
//import com.juangua.search.store.bitcask.KeyValueIter;
//
//public class CompartorCache
//{
//	public final static String CREATED_FIELD = "created";
//	public final static String FAV_FIELD = "cfav";
//	public final static String FAVCTU_FIELD = "zfavctu";
//	public final static String EDITOR_FIELD = "zeditor";
//	public final static long NEGATIVE_TO_POSITIVE = 10000;
//	private Map<String, Long[]> lowCompartorStoreCache;
//	private Map<String, Long[]> highCompartorStoreCache;
//	private List<String> sortFields;
//	private SimpleSearchSystem system;
//	private static final int CACHE_SIZE = 20000000;// 2000w
//	private static final Logger log = Logger.getLogger(CompartorCache.class);
//	public final static ExecutorService EXECUTOR = new ThreadPoolExecutor(3, 20, 30000L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(), new CompartorThreadFactory(), new ThreadPoolExecutor.DiscardPolicy());
//
//	public CompartorCache(String sortFields, SimpleSearchSystem system)
//	{
//		log.info("初始化CompartorCache....");
//		getSortFields(sortFields);
//		this.system = system;
//		this.lowCompartorStoreCache = new HashMap<String, Long[]>();
//		this.highCompartorStoreCache = new HashMap<String, Long[]>();
//
//		if (this.sortFields != null && !this.sortFields.isEmpty())
//		{
//			// 先把排序要计算的值扔进去
//			lowCompartorStoreCache.put(SimpleSearchSystem.NEW_SORT, new Long[CACHE_SIZE]);
//			lowCompartorStoreCache.put(SimpleSearchSystem.POP_SORT, new Long[CACHE_SIZE]);
//			lowCompartorStoreCache.put(SimpleSearchSystem.HOT7DAY_SORT, new Long[CACHE_SIZE]);
//			lowCompartorStoreCache.put(SimpleSearchSystem.HOT30DAY_SORT, new Long[CACHE_SIZE]);
//
//			highCompartorStoreCache.put(SimpleSearchSystem.NEW_SORT, new Long[CACHE_SIZE]);
//			highCompartorStoreCache.put(SimpleSearchSystem.POP_SORT, new Long[CACHE_SIZE]);
//			highCompartorStoreCache.put(SimpleSearchSystem.HOT7DAY_SORT, new Long[CACHE_SIZE]);
//			highCompartorStoreCache.put(SimpleSearchSystem.HOT30DAY_SORT, new Long[CACHE_SIZE]);
//
//			// 再把排序字段也仍进去
//			for (String sortField : this.sortFields)
//			{
//				lowCompartorStoreCache.put(sortField, new Long[CACHE_SIZE]);
//				highCompartorStoreCache.put(sortField, new Long[CACHE_SIZE]);
//			}
//
//		}
//	}
//
//	/**
//	 * 获取排序字段。没配置则生成默认字段
//	 * 
//	 * @param sortStr
//	 */
//	private void getSortFields(String sortStr)
//	{
//		List<String> sortFieldList = new ArrayList<String>();
//		if (sortStr == null)
//		{
//			sortFieldList.add(CREATED_FIELD);
//			sortFieldList.add(FAV_FIELD);
//			sortFieldList.add(FAVCTU_FIELD);
//			sortFieldList.add(EDITOR_FIELD);
//		} else
//		{
//			for (String str : Splitter.on(",").split(sortStr))
//				sortFieldList.add(str); // 逗号分隔
//		}
//
//		this.sortFields = sortFieldList;
//	}
//
//	/**
//	 * 初始化cache。全部计算
//	 * 
//	 * @param bitCask
//	 */
//	public void initCompartorCache(final BitCask bitCask)
//	{
//
//		log.info("开始对数据进行排序计算");
//		long startTime = System.currentTimeMillis();
//		try
//		{
//			bitCask.fold(new KeyValueIter<BitCask>()
//			{
//				public BitCask each(ByteString key, final ByteString value, BitCask newBitCask)
//				{
//					EXECUTOR.execute(new Runnable()
//					{
//						public void run()
//						{
//							try
//							{
//								TDeserializer dserializer = new TDeserializer(new TBinaryProtocol.Factory());
//								if (value != null)
//								{
//									Doc doc = new Doc();
//									dserializer.deserialize(doc, value.toByteArray());
//									putSortFields(doc);
//									computeCompartarRes(doc);
//								}
//							} catch (Exception e)
//							{
//								log.error(e.getMessage(), e);
//							}
//						}
//					});
//					return newBitCask;
//				}
//			}, bitCask);
//		} catch (Exception e)
//		{
//			log.error(e.getMessage(), e);
//		}
//		long sortTime = System.currentTimeMillis() - startTime;
//		log.info("排序完毕,耗时:" + sortTime + "ms!");
//	}
//
//	/**
//	 * 根据排序字段计算排序值
//	 * 
//	 * @param doc
//	 */
//	public void computeCompartarRes(Doc doc)
//	{
//		if (!checkDocFields(doc))
//		{
//			return;
//		}
//		long uid = doc.getUid();
//		Map<String, Long[]> cache = null;
//		if (uid > 1000000000)
//		{
//			uid = uid - 1000000000;
//			cache = highCompartorStoreCache;
//		} else
//		{
//			cache = lowCompartorStoreCache;
//		}
//
//		Map<String, String> fields = doc.getFields();
//		long zeditor = Long.parseLong(fields.get(EDITOR_FIELD)) - NEGATIVE_TO_POSITIVE;
//		long cfav = Long.parseLong(fields.get(FAVCTU_FIELD)) - NEGATIVE_TO_POSITIVE + Long.parseLong(fields.get(FAV_FIELD));
//		long created = Long.parseLong(fields.get(CREATED_FIELD));
//		Long[] temarray;
//		Map<String, Integer> sortData = null;
//		SortType sortType = null;
//		for (int i = 1; i <= 4; i++)
//		{
//			sortType = SortType.findByValue(i);
//			sortData = system.getSort(sortType);
//			if (sortData != null)
//			{
//				temarray = cache.get(system.getSortString(sortType));
//				long maxFav = sortData.get(SimpleSearchSystem.MAX_FAV);
//				long maxEditor = sortData.get(SimpleSearchSystem.MAX_EDITOR);
//				long timeWeight = sortData.get(SimpleSearchSystem.TIME_WEIGHT);
//				long editorWeight = sortData.get(SimpleSearchSystem.EDITOR_WEIGHT);
//				long favWeight = sortData.get(SimpleSearchSystem.FAV_WEIGHT);
//				cfav = cfav > maxFav ? maxFav : cfav;
//				zeditor = zeditor > maxEditor ? maxEditor : zeditor;
//				long sortWeight = created * timeWeight + (cfav + zeditor * editorWeight) * favWeight;
//				temarray[getArrayIndex(uid)] = sortWeight; // Caution :数组越界
//			}
//		}
//	}
//
//	/**
//	 * 缓存排序字段
//	 * 
//	 * @param doc
//	 */
//	public void putSortFields(Doc doc)
//	{
//		if (!checkDocFields(doc))
//		{
//			return;
//		}
//		Map<String, String> fields = doc.getFields();
//		Long[] fieldArr;
//		long uid = doc.getUid();
//		Map<String, Long[]> cache = null;
//		if (uid > 1000000000)
//		{
//			uid = uid - 1000000000;
//			cache = highCompartorStoreCache;
//		} else
//		{
//			cache = lowCompartorStoreCache;
//		}
//		for (String sortField : this.sortFields)
//		{
//			fieldArr = cache.get(sortField);
//			fieldArr[getArrayIndex(uid)] = Long.parseLong(fields.get(sortField));
//		}
//	}
//
//	/**
//	 * 根据排序字段获取计算出来的值
//	 * 
//	 * @param sortField
//	 * @param uid
//	 * @return
//	 */
//	public long getSortValue(String sortField, long uid)
//	{
//		Long value = 0l;
//		Map<String, Long[]> cache = null;
//		if (uid > 1000000000)
//		{
//			uid = uid - 1000000000;
//			cache = highCompartorStoreCache;
//		} else
//		{
//			cache = lowCompartorStoreCache;
//		}
//
//		Long[] docValudArr = cache.get(sortField);
//		if (docValudArr != null && uid <= CACHE_SIZE)
//		{
//			value = docValudArr[getArrayIndex(uid)]; // Caution：数组越界
//			if (value == null)
//			{
//				value = 0l;
//			}
//		} else
//		{
//			log.warn("CACHE数据已经超过了容量!");
//		}
//		return value;
//	}
//
//	private int getArrayIndex(long uid)
//	{
//		int index = (int) (uid % CACHE_SIZE);
//		return index;
//	}
//
//	/**
//	 * 检查doc是否包含排序字段
//	 * 
//	 * @param doc
//	 */
//	private boolean checkDocFields(Doc doc)
//	{
//		boolean flag = true;
//		Map<String, String> fields = doc.getFields();
//		for (String field : this.sortFields)
//		{
//			if (!fields.containsKey(field))
//			{
//				flag = false;
//				log.warn("文档：" + doc.getUid() + " ; fields : " + fields.toString() + ".没有排序字段：" + field);
//			}
//		}
//		return flag;
//	}
//
//	public static class CompartorThreadFactory implements ThreadFactory
//	{
//		public Thread newThread(Runnable runnable)
//		{
//			Thread thread = new Thread(runnable);
//			thread.setName("CompartorThreadFactory-pool-thread-" + _threaCounter.incrementAndGet());
//			thread.setDaemon(false);
//			return thread;
//		}
//
//		private final static AtomicLong _threaCounter = new AtomicLong(0);
//	}
// }
