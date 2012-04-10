namespace java com.mogujie.jessica.service.thrift

struct TToken
{
	1:required string value;
	2:required i32 position=255;// must < 256
	3:optional i32 weight;
}

struct TField
{
	1:required string name;
	2:required string tokenCount;
	3:required bool indexable;
	4:required list<TToken> tokens;
	5:optional i32 payload;
}

struct Doc
{
	1:required i64 uid;
	2:required map<string,string> fields;
}

struct TDocument
{
	1:required i32 object_id;
	2:required list<TField> fields;
	3:optional i32 timestamp;
	4:optional string segmentationHost;
	5:optional i32 shardIdx;//索引的标识
	6:optional Doc doc;
}

struct ResultCode
{
	1:required i32 code;
	2:optional string msg;
}

service UpdateService
{
	ResultCode update(1:list<TDocument> documents);
}

service AdminService
{
	string  purgeIndex();
	string  expungeDeletes();
	string  optimize(1:i32 numSegs);
	string  flushDocumentStore();
	string  setBlockSize(1:i64 size);
	string  refreshDiskReader();
	
	string  setBatchSize(1:i64 batchSize);
	string  setMaxBatchSize(1:i64 maxBatchSize);
	string  setSearchCacheSize(1:i64 cacheSize);
	string  setSearchCacheExpireTime(1:i64 ms);
	string  setQueryCacheSize(1:i64 cacheSize);
	string  setStoreCacheSize(1:i64 cacheSize);

	string setSortParams(1:string type, 2:i32 favMax, 3:i32 maxEditor, 4:i32 timeWeight, 5:i32 favWeight, 6:i32 editorWeight   );

	string  enableUpdateOperation(1:bool enable);
	map<i64,i64> qps();
	i64   currentQps();
	i64   numSearches();
	string status();
	string jvmstatus();
}

struct SearchRequest
{
	1:required	string query;//query josn 对象
	2:required  i32    offset;
	3:required  i32    limit;
	4:required  string sortField="default";
	5:required  bool  sortReverse;
	6:optional map<string,i32> sortMap;
	7:optional list<string> fields;
	8:optional  i32  qtime=20;//最大查询时间消耗 20ms
	9:optional  i32  stime=10;//最大排序时间消耗 10ms
}

struct ResultHit
{
	1:required double score;
	2:optional map<string,string> fields;
	3:optional i32 docId;
}

struct SearchResponse
{
	1:required i32 docs;
	2:required i32 hits;
	3:required list<ResultHit> results;
	4:required i64 time; 
}

service SearchService
{
	SearchResponse search(1:SearchRequest searchRequest);
}





