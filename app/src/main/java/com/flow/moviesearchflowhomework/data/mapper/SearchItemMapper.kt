package com.flow.moviesearchflowhomework.data.mapper

import android.text.Html
import com.flow.moviesearchflowhomework.data.dto.NaverSearchQueryDto
import com.flow.moviesearchflowhomework.data.util.BaseMapper
import com.flow.moviesearchflowhomework.domain.entity.SearchItem

class SearchItemMapper : BaseMapper<NaverSearchQueryDto, List<SearchItem>> {
    override fun map(from: NaverSearchQueryDto): List<SearchItem> =
        from.items.asSequence().map { item ->
            SearchItem(
                Html.fromHtml(item.title, Html.FROM_HTML_MODE_LEGACY).toString(),
                item.actor,
                item.director.removeSuffix("|"),
                item.image,
                item.link,
                item.pubDate,
                item.subtitle,
                item.userRating
            )
        }.toList()
}