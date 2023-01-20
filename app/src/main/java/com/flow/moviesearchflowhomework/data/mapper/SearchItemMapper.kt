package com.flow.moviesearchflowhomework.data.mapper

import android.text.Html
import com.flow.moviesearchflowhomework.data.dto.NaverSearchQueryDto
import com.flow.moviesearchflowhomework.data.util.BaseMapper
import com.flow.moviesearchflowhomework.domain.entity.SearchItem

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