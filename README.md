# 안드로이드 채용 과제

## Preview

https://user-images.githubusercontent.com/15981307/213863525-65712dc0-1655-4fe3-ad18-fd75abf77ad5.mov


## Prototyping - Figma

https://www.figma.com/file/076wV4fDXbj99PDuaPOELg/Flow%EA%B3%BC%EC%A0%9C?node-id=1%3A154&t=RCWuQT19TQVh2UXb-1

## 사용 기술스택

`Hilt, Coroutine(Flow), Room, Retrofit(naver), Okhttp, Coil, Paing3`

# UseCase 1 : 네이버 영화 검색 API https://openapi.naver.com/v1/search/movie.json?

- 페이징 사용
- 이미지, 제목, 출시일, 평점, 감독 표시 (동일 제목이 있어 혼동 우려해 감독까지 보여주었음)

## Data Layer

### DTO

```kotlin
data class NaverSearchQueryDto(
    val display: Int,
    val items: List<Item>,
    val lastBuildDate: String,
    val start: Int,
    val total: Int
)
data class Item(
    val actor: String,
    val director: String,
    val image: String,
    val link: String,
    val pubDate: String,
    val subtitle: String,
    val title: String,
    val userRating: String
)
```

### Service

```kotlin
interface NaverService {
    @GET("v1/search/movie.json")
    suspend fun fetchSearchMovies(
        @Query("query") query: String,
        @Query("display") display: Int,
        @Query("start") start: Int
    ): Response<NaverSearchQueryDto>
}
```

- Paging을 위하여 display(page 단위), start(page) query로 넘겨주었음

### SearchMapper

```kotlin
class SearchItemMapper : BaseMapper<NaverSearchQueryDto, List<SearchItem>> {
    override fun map(from: NaverSearchQueryDto): List<SearchItem> =
        from.items.asSequence()
            .map { item ->             //iterator 방식이 아닌 sequence 방식으로 사용, 해당 케이스는 퍼포먼스적으로 크게 변화는 없음
                SearchItem(
                    Html.fromHtml(item.title, Html.FROM_HTML_MODE_LEGACY).toString(),   //<b>태그 제거
                    item.actor,
                    item.director.removeSuffix("|"),     //기존 naver movie 에서는 감독|출연진 순서라 '|'가 같이 왔었음, 제거
                    item.image,
                    item.link,
                    item.pubDate,
                    item.subtitle,
                    item.userRating
                )
            }.toList()
}
```

- Issue
    - title에 검색 키워드에 일치하는 부분에 <b> 태그가 감싸져오기 때문에 제거해주었음.
    - director 부분이 기존 네이버 Movie란에서 “감독 | 출연진” 순으로 보여주어 ‘ | ’ 특수문자가 같이 오기 때문에 해당 Suffix remove 해주었음
    - Mapping 과정이 iterator 방식이기 때문에 Sequence 방식으로 처리해주었음
        - 크게 미비한 퍼모먼스의 차이는 없지만 두 가지 방식의 차이는 존재

### SearchPaging 처리 (`PagingSource`) - Load

```kotlin
override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SearchItem> {
        val key = params.key ?: NAVER_STARTING_PAGE_INDEX

        var total = Int.MAX_VALUE
        val response =
            catchingApiCall {
                naverService.fetchSearchMovies(
                    query = query,
                    display = params.loadSize,
                    start = 1 + (key - 1) * params.loadSize         //1, 31, 61, 91 ... 순서로 호출하기 위함 start value
                )
            }?.let { dto ->
                total = dto.total
                mapper.map(dto)
            } ?: listOf()    // 해당 Case가 catchingApiCall에서 잡힌 Exception, 현재는 따로 처리할 것이 없으므로 빈 배열 반환
        return try {
            LoadResult.Page(
                data = response,
                nextKey = if (response.isEmpty() || total < 1 + key * params.loadSize) null else key + (params.loadSize / 30),  //초기에 loadSize * 3으로 호출 되기 때문에 key를 그만큼 업데이트 해주었음
                prevKey = if (key == NAVER_STARTING_PAGE_INDEX) null else key, // Only paging forward.
            )
        } catch (e: IllegalArgumentException) {   //LoadResult.Page 객체 생성시에 require 부분에서 해당 exception throw 하기 때문에 처리
            LoadResult.Error(e)
        }
    }
```

- start가 display 배수에 따라, 1, 31, 61 순으로 설정 되어야함. 따라서 해당 `(1 + (key - 1) * params.loadSize)` 방식으로 next start 설정, key 자체는 +1 씩 증가
- total이 15일 때 next start가 31이면 start가 11 등으로 call 이 넘어가는 오류가 있음(post man 테스트시에도 동일) 따라서 next key 설정 해줄 때 다음 키의 start가 total을 넘는지 확인
- dto to entity 처리 (Mapper)
- catchingApiCall 사용
- 따라서 return에서 Page 생성하는 로직에만 발생하는 IllegalArgumentException만 캐치

### CatchingApiCall

```kotlin
suspend inline fun <T> catchingApiCall(crossinline apiCall: suspend () -> Response<T>): T? {
    return runCatching {
        apiCall().body()
    }.getOrElse { throwable ->
        when (throwable) {
            is IOException -> {         //SocketConnection, Timeout Error Catch, 보통 핸드쉐이크 관련
                Log.e("IOException", throwable.message.toString())
                null
            }
            is HttpException -> {       //error 코드까지는 내려옴, end point 까지는 접근
                Log.e("HttpException", throwable.message.toString())
                null
            }
            else -> {
                null
            }
        }
    }
}
```

- runCatching으로 api 호출 safe call
- 현재는 해당 에러에 대해서 재시도 등의 처리는 없고 null로만 보내줌(null을 통해서 에러 확인)

### Fetch Movie(HomeRepositoryImpl.kt)

```kotlin
override fun fetchMovie(
        keyword: String
    ): Flow<PagingData<SearchItem>> =
        Pager(
            config = PagingConfig(
                pageSize = 30
            ), pagingSourceFactory = { SearchPagingSource(naverService, keyword, searchItemMapper) }
        ).flow
```

- flow로 반환, pageSize로 loadSize Set
- PagingSourceFactory로 만들어둔 SearchPagingSource 전달
- 의존성 역전으로 domain을 통해 Presentation에게 전달

## Domain Layer

### Entity ( Mapper로 매핑 된 후 )

```kotlin
data class SearchItem(
    val title: String,
    val actor: String,
    val director: String,
    val image: String,
    val link: String,
    val pubDate: String,
    val subtitle: String,
    val userRating: String
)
```

### HomeRepository

```kotlin
interface HomeRepository {
    fun fetchMovie(keyword: String): Flow<PagingData<SearchItem>>
}
```

- interface로써 전달, dependecy inject로 presentation에게 data layer 참조받지 않게함, 해당 기능만을 사용할 수 있음.

## Presentation Layer

### ViewModel(SearchViewModel.kt)

```kotlin
fun initSearchCollect(keyword: String): Flow<PagingData<SearchItem>> {
    if (keyword.isNotBlank()) {
        viewModelScope.launch {
            homeRepository.insertRecentSearch(
                RecentSearchKeywordEntity(
                    keyword,
                    LocalDateTime.now()
                )
            )
        }
    }
    return homeRepository.fetchMovie(keyword = keyword).cachedIn(viewModelScope)
}
```

- if문은 Room을 위한 것이므로 여기선 생략
- homeRepositoy를 주입 받은 해당 interface 함수를 사용하여 activity에게 flow 스트림 반환

### SearchListPagerAdapter.kt(Movie List)

- 기존 ListAdapter와 코드 동일
- LayoutInflater 재생성 오버헤드 줄였음 lateinit var, reflection 사용

```kotlin
private lateinit var inflater: LayoutInflater
override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
    if (!::inflater.isInitialized) {
        inflater = LayoutInflater.from(parent.context)
    }
    return SearchViewHolder(ItemSearchResultBinding.inflate(inflater, parent, false))
}
```

- 생성자로 click 이벤트 람다를 받아 viewholder를 inner class로 만들지 않게 하였음(메모리 릭 방지)

```kotlin
class SearchListPagerAdapter(private val movieClickListener: (SearchItem) -> Unit))

override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
    val data = getItem(position)
    if (data != null) {  //Paging Adapter 사용시 nullable로 바뀜
        holder.binding.setVariable(BR.searchList, data)
        holder.binding.root.setOnClickListener {
            movieClickListener(data)
        }
    }
}
```

### Activity(MovieSearchActivity.kt) - 검색 시

```kotlin
private fun callSearch(keyword: String) {      //Progress, Collect Paging List
    job = lifecycleScope.launch {
        searchViewModel.initSearchCollect(keyword).flowWithLifecycle(lifecycle)
            .collectLatest { paging ->
                searchAdapter.submitData(paging)
            }
    }
    collectFlowWhenStarted(searchAdapter.loadStateFlow) { state ->
        binding.pbProgress.isVisible = state.refresh is LoadState.Loading
    }
}
```
- 검색 리스트 collect 하는 코루틴 job에 보관
- load 여부 어댑터의 loadStateFlow로 progress로 초기 상태만 처리하였음.
- ViewModel상에서 initSearchCollect():Flow<PagingData<SearchItem>> Stream Collect → submitData()처리
- onStop()에서 수집 정지(다음 검색어 입력으로 인한 새로운 수집이 이뤄질 수 있기 때문)

```kotlin
override fun onStop() {
    super.onStop()
    job?.cancel()
}
```

### null image Date 처리
- image가 없는 데이터가 올 때가 있음
- remote image 로드는 BindingAdapter 상에서 coil을 사용하여 로드중
```kotlin
@BindingAdapter("app:loadRemoteRoundedImage")
fun ImageView.loadRemoteRoundedImage(url: String) {
    if (url.isBlank()) {            //Image가 없는 것이 올 때는 placeholder 띄움
        load(R.drawable.placeholder) {
            transformations(RoundedCornersTransformation(12F))
        }
        return
    }
    load(url) {  //Use Coil
        crossfade(true)
        transformations(RoundedCornersTransformation(12F))
        placeholder(R.drawable.placeholder)
    }
}
```
- isBlank()에 걸리는 경우에 placeholder를 default 이미지로 로드

### Activity - 어댑터 아이템 클릭 시

```kotlin
private fun setAdapter() {
    searchAdapter = SearchListPagerAdapter { item ->     // Move to WebView
        Intent(this, SearchDetailActivity::class.java).apply {
            searchViewModel.inputSearchText.value = ""
            putExtra(LINK_NAME, item.link)
            startActivity(this)
            finish()
        }
    }
    binding.rvSearchList.adapter = searchAdapter
}
```

- Entity상의 Link를 intent로 보내주었음.
- SearchDetailActivity에서 해당 링크 처리
- 검색어 초기화

## WebView (MovieDetailActivity.kt)

```kotlin
override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setListener()

        with(binding.webview) {
            webViewClient = WebViewClient()
            webChromeClient = WebChromeClient() // 안정성을 위해서 크롬 클라이언트도 적용
            with(settings) {
                loadsImagesAutomatically = true // 이미지 자동 로드
                javaScriptEnabled = true
            }
            val link = intent.getStringExtra("link")
            clearCache(true)
            link?.let { loadUrl(it) }
        }
    }

override fun onDestroy() {
    super.onDestroy()
    with(binding.webview) {
        clearHistory()
        clearCache(true)
        loadUrl("about:blank")
        destroy()
    }
}
```

- 아이템 선택 시에 넘어오는 링크로 웹뷰로 띄워주었음
- onDestroy()시에 webView Destroy()처리

### reference

- [https://proandroiddev.com/infinite-scrolling-with-android-paging-library-and-flow-api-e017f47517d6](https://proandroiddev.com/infinite-scrolling-with-android-paging-library-and-flow-api-e017f47517d6)
- [https://developer.android.com/topic/libraries/architecture/paging/load-state?hl=ko](https://developer.android.com/topic/libraries/architecture/paging/load-state?hl=ko)
- [https://leveloper.tistory.com/208](https://leveloper.tistory.com/208)
- [https://leveloper.tistory.com/202](https://leveloper.tistory.com/202)
- [https://developer.android.com/guide/webapps/webview?hl=ko](https://developer.android.com/guide/webapps/webview?hl=ko)

# UseCase 2 : 최근 검색어 저장 및 해당 키워드로 검색

## Domain Layer

### Entity

```kotlin
@Entity(tableName = "recent_search")
data class RecentSearchKeywordEntity(
    @PrimaryKey
    val keyword: String,   //동일 키워드로 검색시 업데이트 되기 위함
    val createAt: LocalDateTime   // TypeConverter 필요
)
```

- 최근 검색어는 중복 될 수 없으므로 keyword를 primary key로 설정
- 동일 검색어 서치 시에 createAt만 최신화 된 채로 업데이트 (DAO- replace)

### Repository

```kotlin
interface HomeRepository {
    suspend fun fetchRecentSearch(): List<RecentSearchKeywordEntity>
    suspend fun insertRecentSearch(recentSearchKeywordEntity: RecentSearchKeywordEntity)
    suspend fun deleteRecentSearch(recentSearchKeywordEntity: RecentSearchKeywordEntity)
}
```

## Data Layer - Room DB 사용

### DAO(RecentSearchKeywordEntity는 Domain Layer에 있음)

```kotlin
@Dao
interface SearchDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE) //LRU -> 넣으려는 데이터가 이미 있는 데이터면 가장 최신으로 업데이트
    suspend fun insertRecentSearch(data: RecentSearchKeywordEntity)

    @Update
    suspend fun updateRecentSearch(vararg recentSearchKeywordEntity: RecentSearchKeywordEntity)  //여러개를 넣을 수 있게 해야함, List로 받아서 처리하는 것보다 vararg로 처리하는게 훨씬 빠름

    @Delete
    suspend fun deleteRecentSearch(vararg recentSearchKeywordEntity: RecentSearchKeywordEntity)

    @Query("select * from recent_search order by createAt desc ")   //최신순으로 뽑아줌
    suspend fun getAllRecentList(): List<RecentSearchKeywordEntity>
}
```

- getAllRecentList는 createAt의 desc 순으로 get해올 수 있음, 자동적으로 맨 마지막 데이터가 가장 예전에 저장한 데이터가 됨

### vararg로 전달한 이유

- 기본적으로 Room은 하나의 SQL 명령을 내렸을 때 다음과 같은 동작을 함(ex : delete)
    1. beginTransaction()
    2. delete(recentSearchKeywordEntity)
    3. setTransactionSuccessful()
    4. endTransaction()
- 만약 @Delete가 다음과 같이 있었다고 해보자.
    
    ```kotlin
    @Delete
    suspend fun deleteRecentSearch(recentSearchKeywordEntity: RecentSearchKeywordEntity)
    ```
    
- Datasource상에서 List<recentSearchKeywordEntity>로 받아 foreach 등으로 해당 deleteRecentSearch를 호출하게 되면 앞서 말한 1~4번의 동작을 foreach의 횟수만큼 해야한다.
- vararg로 전달한다면 내부적으로 delete(T[] entities){}라는 함수를 통해서 한 방에 처리할 수 있다. 따라서 1~4의 과정에서 1, 3, 4는 한 번만 실행한다는 것이다.
- 많은 데이터를 처리해야한다면 오버헤드의 차이가 몇 배 차이가 날 것으로 해당 방법을 통해서 처리할 수 있다.
- 하지만 해당 예제에서는 하나씩 처리하기 때문에 큰 차이는 없음

### TypeConverter(LocalDateTime)

```kotlin
@ProvidedTypeConverter
class DateTypeConverter {
    @TypeConverter
    fun fromString(value: String): LocalDateTime =
        LocalDateTime.parse(value, DateTimeFormatter.ISO_LOCAL_DATE_TIME)

    @TypeConverter
    fun fromLocalDate(value: LocalDateTime): String = value.toString()
}
```

- LocalDateTime < - > String 간의 Mapping을 자동으로 처리해주는 Converter 생성

### DataBase (SearchDB)

```kotlin
@Database(entities = [RecentSearchKeywordEntity::class], version = 1)
@TypeConverters(DateTypeConverter::class)   //LocalDateTime Convert 용
abstract class SearchDatabase : RoomDatabase() {
    abstract fun searchDAO(): SearchDAO
}
```

- Entity, DAO, TypeConverter주입
- 해당 로직은 Dependency Injection으로 DB Class, DAO Interface 주입 받음

### HomeRepositoryImpl.kt

```kotlin
override suspend fun fetchRecentSearch(): List<RecentSearchKeywordEntity> =
        localRecentSearchDataSource.getAllRecentKeyword()

override suspend fun insertRecentSearch(recentSearchKeywordEntity: RecentSearchKeywordEntity) {
    localRecentSearchDataSource.insertRecentKeyword(recentSearchKeywordEntity)
    val currentList = localRecentSearchDataSource.getAllRecentKeyword()
    if (currentList.size > 10) {
        localRecentSearchDataSource.deleteRecentSearch(currentList[10])   // LRU 기법처럼 가장 뒤에 있는 데이터부터 제거
    }
}

override suspend fun deleteRecentSearch(recentSearchKeywordEntity: RecentSearchKeywordEntity) {  //presentation adapter에서 데이터를 하나씩 지울 수 있음
    localRecentSearchDataSource.deleteRecentSearch(recentSearchKeywordEntity)
}
```

- 10개 이하로만 보여주어야 하기 때문에 insert한 후의 현재 데이터에 대해서 size가 10을 넘을 시에 가장 마지막 데이터를 제거

## Presentation Layer

- **검색이 될 때 마다 해당 키워드 저장**

```kotlin
fun initSearchCollect(keyword: String): Flow<PagingData<SearchItem>> {
    if (keyword.isNotBlank()) {
        viewModelScope.launch {
            homeRepository.insertRecentSearch(
                RecentSearchKeywordEntity(
                    keyword,
                    LocalDateTime.now()
                )
            )
        }
    }
    return homeRepository.fetchMovie(keyword = keyword).cachedIn(viewModelScope)
}
```

- homeRepository.fetchMovie()가 네이버 API Call, 현재는 if문 안에서 일어나는 일을 focus
- repository를 통해서 DAO에게 insert 요청을 보내주었음.

### RecentSearchKeywordViewModel

```kotlin
private val _recentSearchList =
    MutableStateFlow<UiState<List<RecentSearchKeywordEntity>>>(UiState.Loading)  //시작과 동시에 로드하기 떄문에(init block) 로드를 초기값으로 setting
val recentSearchList get() = _recentSearchList.asStateFlow()   //Immutable StateFlow

init {    //상호 작용 이후에 불러오는 것이 아니기 때문에 생성과 동시에 데이터 query
    viewModelScope.launch {
        _recentSearchList.value = UiState.Success(homeRepository.fetchRecentSearch())
    }
}

fun deleteRecentKeyword(keywordEntity: RecentSearchKeywordEntity) {
    viewModelScope.launch {
        homeRepository.deleteRecentSearch(keywordEntity)
        _recentSearchList.value = UiState.Success(homeRepository.fetchRecentSearch())
    }
}
```

- 뷰모델 생성과 동시에 데이터 query
- UiState로 Progress 띄워주었음
- Adapter에서 아이템 삭제 클릭 시 deleteRecentKeyword() 함수를 통해 아이템 제거

### 키워드 클릭 시 해당 검색어로 검색

- 홈에서 최근 검색어 창으로 이동후에 키워드 선택시 홈으로 돌아와 해당 검색어로 검색이 되어야 함.
- MovieSearchActivity(Caller)에서 RecentSearchKeywordActivity(Callee) 호출 시에 `ActivityResultLauncher` 사용
- 아래와 같이 넘어오는 result 값에 따라서 callSearch를 호출. (empty는 callSearch에서 체크)
    
    ```kotlin
    private val getResultText: ActivityResultLauncher<Intent> =    //Get Activity Result
    registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val data = result.data?.getStringExtra(SEARCH_KEYWORD).toString()
            searchViewModel.inputSearchText.value = data  //edit text
            callSearch(data)
        }
    }
    //When Click
    getResultText.launch(Intent(this, RecentSearchKeywordActivity::class.java))
    ```
    
- Caller에서는 Result에 따라서 동작할 수 있는 콜백 함수를 만들어두었음.
- Callee 액티비티에서 키워드 클릭 시 setResult 함수를 호출하여 콜백을 날려주었음.
    
    ```kotlin
    private fun setAdapter() {
        recentSearchAdapter =
            RecentSearchListAdapter(
                recentKeywordClickListener = { keywordEntity ->     //Move to SearchActivity
                    backToSearch(keywordEntity.keyword)
                }, deleteKeywordClickListener = { keywordEntity ->  //delete 1 Item
                    deleteKeyword(keywordEntity)
                })
        binding.rvRecentList.adapter = recentSearchAdapter
    }
    
    private fun backToSearch(keyword: String) {         //Return to getResultText(Search Activity)
        val intent = Intent(this, MovieSearchActivity::class.java).apply {
            putExtra("searchKeyword", keyword)    //Search Keyword
        }
        setResult(RESULT_OK, intent)
        finish()
    }
    ```
    
- recentKeywordClickListener에서 호출하는 backToSearch()함수를 통해서 홈으로 복귀 (setResult로 result 넘겨주었음)
- 홈으로 복귀 후 해당 키워드로 재 검색(Room Entity에서도 최신 데이터로 변경)
